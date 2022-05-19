/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.handlers;

import io.gdcc.xoai.dataprovider.exceptions.BadArgumentException;
import io.gdcc.xoai.dataprovider.exceptions.DoesNotSupportSetsException;
import io.gdcc.xoai.dataprovider.exceptions.HandlerException;
import io.gdcc.xoai.dataprovider.exceptions.InternalOAIException;
import io.gdcc.xoai.dataprovider.exceptions.NoMatchesException;
import io.gdcc.xoai.dataprovider.exceptions.NoMetadataFormatsException;
import io.gdcc.xoai.dataprovider.exceptions.OAIException;
import io.gdcc.xoai.dataprovider.handlers.helpers.ItemRepositoryHelper;
import io.gdcc.xoai.dataprovider.handlers.helpers.PreconditionHelper;
import io.gdcc.xoai.dataprovider.handlers.helpers.ResumptionTokenHelper;
import io.gdcc.xoai.dataprovider.handlers.results.ListItemIdentifiersResult;
import io.gdcc.xoai.dataprovider.model.Context;
import io.gdcc.xoai.dataprovider.model.ItemIdentifier;
import io.gdcc.xoai.dataprovider.model.MetadataFormat;
import io.gdcc.xoai.dataprovider.model.Set;
import io.gdcc.xoai.dataprovider.parameters.OAICompiledRequest;
import io.gdcc.xoai.dataprovider.repository.Repository;
import io.gdcc.xoai.model.oaipmh.Header;
import io.gdcc.xoai.model.oaipmh.ListIdentifiers;
import io.gdcc.xoai.model.oaipmh.ResumptionToken;

import java.util.List;


public class ListIdentifiersHandler extends VerbHandler<ListIdentifiers> {
    private final ItemRepositoryHelper itemRepositoryHelper;

    public ListIdentifiersHandler(Context context, Repository repository) {
        super(context, repository);
        this.itemRepositoryHelper = new ItemRepositoryHelper(repository.getItemRepository());
    }


    @Override
    public ListIdentifiers handle(OAICompiledRequest parameters) throws OAIException, HandlerException {
        ListIdentifiers result = new ListIdentifiers();

        if (parameters.hasSet() && !getRepository().getSetRepository().supportSets())
            throw new DoesNotSupportSetsException();

        PreconditionHelper.checkMetadataFormat(getContext(), parameters.getMetadataPrefix());

        int length = getRepository().getConfiguration().getMaxListIdentifiers();
        int offset = getOffset(parameters);
        ListItemIdentifiersResult listItemIdentifiersResult;
        if (!parameters.hasSet()) {
            if (parameters.hasFrom() && !parameters.hasUntil())
                listItemIdentifiersResult = itemRepositoryHelper.getItemIdentifiers(getContext(), offset, length,
                        parameters.getMetadataPrefix(), parameters.getFrom());
            else if (!parameters.hasFrom() && parameters.hasUntil())
                listItemIdentifiersResult = itemRepositoryHelper.getItemIdentifiersUntil(getContext(), offset, length,
                        parameters.getMetadataPrefix(), parameters.getUntil());
            else if (parameters.hasFrom() && parameters.hasUntil())
                listItemIdentifiersResult = itemRepositoryHelper.getItemIdentifiers(getContext(), offset, length,
                        parameters.getMetadataPrefix(), parameters.getFrom(),
                        parameters.getUntil());
            else
                listItemIdentifiersResult = itemRepositoryHelper.getItemIdentifiers(getContext(), offset, length,
                        parameters.getMetadataPrefix());
        } else {
            if (!getRepository().getSetRepository().exists(parameters.getSet()) && !getContext().hasSet(parameters.getSet()))
                throw new NoMatchesException();

            if (parameters.hasFrom() && !parameters.hasUntil())
                listItemIdentifiersResult = itemRepositoryHelper.getItemIdentifiers(getContext(), offset, length,
                        parameters.getMetadataPrefix(), parameters.getSet(),
                        parameters.getFrom());
            else if (!parameters.hasFrom() && parameters.hasUntil())
                listItemIdentifiersResult = itemRepositoryHelper.getItemIdentifiersUntil(getContext(), offset, length,
                        parameters.getMetadataPrefix(), parameters.getSet(),
                        parameters.getUntil());
            else if (parameters.hasFrom() && parameters.hasUntil())
                listItemIdentifiersResult = itemRepositoryHelper.getItemIdentifiers(getContext(), offset, length,
                        parameters.getMetadataPrefix(), parameters.getSet(),
                        parameters.getFrom(), parameters.getUntil());
            else
                listItemIdentifiersResult = itemRepositoryHelper.getItemIdentifiers(getContext(), offset, length,
                        parameters.getMetadataPrefix(), parameters.getSet());
        }

        List<ItemIdentifier> itemIdentifiers = listItemIdentifiersResult.getResults();
        if (itemIdentifiers.isEmpty()) throw new NoMatchesException();

        for (ItemIdentifier itemIdentifier : itemIdentifiers)
            result.getHeaders().add(createHeader(parameters, itemIdentifier));

        ResumptionToken.Value currentResumptionToken = new ResumptionToken.Value();
        if (parameters.hasResumptionToken()) {
            currentResumptionToken = parameters.getResumptionToken();
        } else if (listItemIdentifiersResult.hasMore()) {
            currentResumptionToken = parameters.extractResumptionToken();
        }

        ResumptionTokenHelper resumptionTokenHelper = new ResumptionTokenHelper(currentResumptionToken,
                getRepository().getConfiguration().getMaxListIdentifiers());
        result.withResumptionToken(
            resumptionTokenHelper
                .withTotalResults(listItemIdentifiersResult.getTotal())
                .resolve(listItemIdentifiersResult.hasMore()));

        return result;
    }

    private int getOffset(OAICompiledRequest parameters) {
        if (!parameters.hasResumptionToken())
            return 0;
        if (parameters.getResumptionToken().getOffset() == null)
            return 0;
        return parameters.getResumptionToken().getOffset().intValue();
    }


    private Header createHeader(OAICompiledRequest parameters,
                                    ItemIdentifier itemIdentifier) throws BadArgumentException,
            OAIException,
        NoMetadataFormatsException {
        MetadataFormat format = getContext().formatForPrefix(parameters
                .getMetadataPrefix());
        if (!itemIdentifier.isDeleted() && !canDisseminate(itemIdentifier, format))
            throw new InternalOAIException("The item repository is currently providing items which cannot be disseminated with format "+format.getPrefix());

        Header header = new Header();
        header.withDatestamp(itemIdentifier.getDatestamp());
        header.withIdentifier(itemIdentifier.getIdentifier());
        if (itemIdentifier.isDeleted())
            header.withStatus(Header.Status.DELETED);

        for (Set set : getContext().getSets())
            if (set.getCondition().getFilter(getRepository().getFilterResolver()).isItemShown(itemIdentifier))
                header.withSetSpec(set.getSpec());

        for (Set set : itemIdentifier.getSets())
            header.withSetSpec(set.getSpec());

        return header;
    }

    private boolean canDisseminate(ItemIdentifier itemIdentifier, MetadataFormat format) {
        return !format.hasCondition() ||
                format.getCondition().getFilter(getRepository().getFilterResolver()).isItemShown(itemIdentifier);
    }
}
