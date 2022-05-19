/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.handlers;

import io.gdcc.xoai.dataprovider.exceptions.BadArgumentException;
import io.gdcc.xoai.dataprovider.exceptions.CannotDisseminateFormatException;
import io.gdcc.xoai.dataprovider.exceptions.DoesNotSupportSetsException;
import io.gdcc.xoai.dataprovider.exceptions.HandlerException;
import io.gdcc.xoai.dataprovider.exceptions.NoMatchesException;
import io.gdcc.xoai.dataprovider.exceptions.NoMetadataFormatsException;
import io.gdcc.xoai.dataprovider.exceptions.OAIException;
import io.gdcc.xoai.dataprovider.handlers.helpers.ItemHelper;
import io.gdcc.xoai.dataprovider.handlers.helpers.ItemRepositoryHelper;
import io.gdcc.xoai.dataprovider.handlers.helpers.MetadataHelper;
import io.gdcc.xoai.dataprovider.handlers.helpers.PreconditionHelper;
import io.gdcc.xoai.dataprovider.handlers.helpers.ResumptionTokenHelper;
import io.gdcc.xoai.dataprovider.handlers.helpers.SetRepositoryHelper;
import io.gdcc.xoai.dataprovider.handlers.results.ListItemsResults;
import io.gdcc.xoai.dataprovider.model.Context;
import io.gdcc.xoai.dataprovider.model.Item;
import io.gdcc.xoai.dataprovider.model.MetadataFormat;
import io.gdcc.xoai.dataprovider.model.Set;
import io.gdcc.xoai.dataprovider.parameters.OAICompiledRequest;
import io.gdcc.xoai.dataprovider.repository.Repository;
import io.gdcc.xoai.model.oaipmh.About;
import io.gdcc.xoai.model.oaipmh.Header;
import io.gdcc.xoai.model.oaipmh.ListRecords;
import io.gdcc.xoai.model.oaipmh.Metadata;
import io.gdcc.xoai.model.oaipmh.Record;
import io.gdcc.xoai.model.oaipmh.ResumptionToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class ListRecordsHandler extends VerbHandler<ListRecords> {
    private static final Logger log = LoggerFactory.getLogger(ListRecordsHandler.class);
    private final ItemRepositoryHelper itemRepositoryHelper;
    private final SetRepositoryHelper setRepositoryHelper;

    public ListRecordsHandler(Context context, Repository repository) {
        super(context, repository);
        this.itemRepositoryHelper = new ItemRepositoryHelper(getRepository().getItemRepository());
        this.setRepositoryHelper = new SetRepositoryHelper(getRepository().getSetRepository());
    }

    @Override
    public ListRecords handle(OAICompiledRequest parameters) throws OAIException, HandlerException {
        ListRecords res = new ListRecords();
        int length = getRepository().getConfiguration().getMaxListRecords();

        if (parameters.hasSet() && !getRepository().getSetRepository().supportSets())
            throw new DoesNotSupportSetsException();

        PreconditionHelper.checkMetadataFormat(getContext(), parameters.getMetadataPrefix());

        log.debug("Getting items from data source");
        int offset = getOffset(parameters);
        ListItemsResults result;
        if (!parameters.hasSet()) {
            if (parameters.hasFrom() && !parameters.hasUntil())
                result = itemRepositoryHelper.getItems(getContext(), offset,
                        length, parameters.getMetadataPrefix(),
                        parameters.getFrom());
            else if (!parameters.hasFrom() && parameters.hasUntil())
                result = itemRepositoryHelper.getItemsUntil(getContext(), offset,
                        length, parameters.getMetadataPrefix(),
                        parameters.getUntil());
            else if (parameters.hasFrom() && parameters.hasUntil())
                result = itemRepositoryHelper.getItems(getContext(), offset,
                        length, parameters.getMetadataPrefix(),
                        parameters.getFrom(), parameters.getUntil());
            else
                result = itemRepositoryHelper.getItems(getContext(), offset,
                        length, parameters.getMetadataPrefix());
        } else {
            if (!setRepositoryHelper.exists(getContext(), parameters.getSet()))
                throw new NoMatchesException();
            if (parameters.hasFrom() && !parameters.hasUntil())
                result = itemRepositoryHelper.getItems(getContext(), offset,
                        length, parameters.getMetadataPrefix(),
                        parameters.getSet(), parameters.getFrom());
            else if (!parameters.hasFrom() && parameters.hasUntil())
                result = itemRepositoryHelper.getItemsUntil(getContext(), offset,
                        length, parameters.getMetadataPrefix(),
                        parameters.getSet(), parameters.getUntil());
            else if (parameters.hasFrom() && parameters.hasUntil())
                result = itemRepositoryHelper.getItems(getContext(), offset,
                        length, parameters.getMetadataPrefix(),
                        parameters.getSet(), parameters.getFrom(),
                        parameters.getUntil());
            else
                result = itemRepositoryHelper.getItems(getContext(), offset,
                        length, parameters.getMetadataPrefix(),
                        parameters.getSet());
        }
        log.debug("Items retrieved from data source");

        List<Item> results = result.getResults();
        if (results.isEmpty()) throw new NoMatchesException();
        log.debug("Now adding records to the OAI-PMH Output");
        for (Item i : results)
            res.withRecord(this.createRecord(parameters, i));


        ResumptionToken.Value currentResumptionToken = new ResumptionToken.Value();
        if (parameters.hasResumptionToken()) {
            currentResumptionToken = parameters.getResumptionToken();
        } else if (result.hasMore()) {
            currentResumptionToken = parameters.extractResumptionToken();
        }

        ResumptionTokenHelper resumptionTokenHelper = new ResumptionTokenHelper(currentResumptionToken,
                getRepository().getConfiguration().getMaxListRecords());
        res.withResumptionToken(
            resumptionTokenHelper
                .withTotalResults(result.getTotal())
                .resolve(result.hasMore()));

        return res;
    }


    private int getOffset(OAICompiledRequest parameters) {
        if (!parameters.hasResumptionToken())
            return 0;
        if (parameters.getResumptionToken().getOffset() == null)
            return 0;
        return parameters.getResumptionToken().getOffset().intValue();
    }

    private Record createRecord(OAICompiledRequest parameters, Item item)
            throws BadArgumentException, OAIException, NoMetadataFormatsException, CannotDisseminateFormatException {
        MetadataFormat format = getContext().formatForPrefix(parameters.getMetadataPrefix());
        Header header = new Header();
        Record record = new Record().withHeader(header);
        header.withIdentifier(item.getIdentifier());

        ItemHelper itemHelperWrap = new ItemHelper(item);

        header.withDatestamp(item.getDatestamp());
        for (Set set : itemHelperWrap.getSets(getContext(), getRepository().getFilterResolver()))
            header.withSetSpec(set.getSpec());
        if (item.isDeleted())
            header.withStatus(Header.Status.DELETED);

        if (!item.isDeleted()) {
            // Next up: <metadata> response part. Skip the pipeline on request by the source.
            // Skip the metadata transformation on request by the source repository.
            Metadata metadata = item.getMetadata();
            if (! metadata.needsProcessing()) {
                record.withMetadata(metadata);
            } else {
                record.withMetadata(MetadataHelper.process(metadata, format, getContext()));
            }
    
            // Last add the <about> section if present (protocol spec says: optional and repeatable)
            for (About about : item.getAbout()) {
                record.withAbout(about);
            }
        }
        return record;
    }
}
