package com.lyncode.xoai.dataprovider.handlers;

import com.lyncode.xoai.dataprovider.core.*;
import com.lyncode.xoai.dataprovider.data.ItemIdentifier;
import com.lyncode.xoai.dataprovider.data.internal.ItemIdentifyHelper;
import com.lyncode.xoai.dataprovider.data.internal.ItemRepositoryHelper;
import com.lyncode.xoai.dataprovider.data.internal.MetadataFormat;
import com.lyncode.xoai.dataprovider.data.internal.SetRepositoryHelper;
import com.lyncode.xoai.dataprovider.exceptions.*;
import com.lyncode.xoai.dataprovider.services.api.DateProvider;
import com.lyncode.xoai.dataprovider.services.api.RepositoryConfiguration;
import com.lyncode.xoai.dataprovider.services.api.ResumptionTokenFormatter;
import com.lyncode.xoai.dataprovider.xml.oaipmh.HeaderType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.ListIdentifiersType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.ResumptionTokenType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.StatusType;

import java.util.List;


public class ListIdentifiersHandler extends VerbHandler<ListIdentifiersType> {
    private final int maxListSize;
    private SetRepositoryHelper setRepository;
    private ItemRepositoryHelper itemRepositoryHelper;
    private RepositoryConfiguration identify;
    private XOAIContext context;
    private ResumptionTokenFormatter resumptionFormat;

    public ListIdentifiersHandler(DateProvider formatter, int maxListSize,
                                  SetRepositoryHelper setRepository,
                                  ItemRepositoryHelper itemRepositoryHelper,
                                  RepositoryConfiguration identify,
                                  XOAIContext context, ResumptionTokenFormatter _format) {

        super(formatter);
        this.maxListSize = maxListSize;
        this.setRepository = setRepository;
        this.itemRepositoryHelper = itemRepositoryHelper;
        this.identify = identify;
        this.context = context;
        this.resumptionFormat = _format;
    }


    @Override
    public ListIdentifiersType handle(OAIParameters parameters) throws OAIException, HandlerException {
        ListIdentifiersType res = new ListIdentifiersType();
        ResumptionToken token = parameters.getResumptionToken();

        if (parameters.hasSet() && !setRepository.supportSets())
            throw new DoesNotSupportSetsException();

        int length = maxListSize;
        ListItemIdentifiersResult result;
        if (!parameters.hasSet()) {
            if (parameters.hasFrom() && !parameters.hasUntil())
                result = itemRepositoryHelper.getItemIdentifiers(context,
                        token.getOffset(), length,
                        parameters.getMetadataPrefix(), parameters.getFrom());
            else if (!parameters.hasFrom() && parameters.hasUntil())
                result = itemRepositoryHelper.getItemIdentifiersUntil(context,
                        token.getOffset(), length,
                        parameters.getMetadataPrefix(), parameters.getUntil());
            else if (parameters.hasFrom() && parameters.hasUntil())
                result = itemRepositoryHelper.getItemIdentifiers(context,
                        token.getOffset(), length,
                        parameters.getMetadataPrefix(), parameters.getFrom(),
                        parameters.getUntil());
            else
                result = itemRepositoryHelper.getItemIdentifiers(context,
                        token.getOffset(), length,
                        parameters.getMetadataPrefix());
        } else {
            if (!setRepository.exists(context, parameters.getSet()))
                throw new NoMatchesException();
            if (parameters.hasFrom() && !parameters.hasUntil())
                result = itemRepositoryHelper.getItemIdentifiers(context,
                        token.getOffset(), length,
                        parameters.getMetadataPrefix(), parameters.getSet(),
                        parameters.getFrom());
            else if (!parameters.hasFrom() && parameters.hasUntil())
                result = itemRepositoryHelper.getItemIdentifiersUntil(context,
                        token.getOffset(), length,
                        parameters.getMetadataPrefix(), parameters.getSet(),
                        parameters.getUntil());
            else if (parameters.hasFrom() && parameters.hasUntil())
                result = itemRepositoryHelper.getItemIdentifiers(context,
                        token.getOffset(), length,
                        parameters.getMetadataPrefix(), parameters.getSet(),
                        parameters.getFrom(), parameters.getUntil());
            else
                result = itemRepositoryHelper.getItemIdentifiers(context,
                        token.getOffset(), length,
                        parameters.getMetadataPrefix(), parameters.getSet());
        }

        List<ItemIdentifier> results = result.getResults();
        if (results.isEmpty())
            throw new NoMatchesException();

        ResumptionToken newToken;
        if (result.hasMore()) {
            newToken = new ResumptionToken(token.getOffset() + length, parameters);
        } else {
            newToken = new ResumptionToken();
        }

        if (parameters.hasResumptionToken() || !newToken.isEmpty()) {
            ResumptionTokenType resToken = new ResumptionTokenType();
            if (!newToken.isEmpty())
                resToken.setValue(resumptionFormat.format(newToken));
            resToken.setCursor(token.getOffset() / maxListSize);
            if (result.hasTotalResults())
                resToken.setCompleteListSize(result.getTotal());
            res.setResumptionToken(resToken);
        }

        for (ItemIdentifier itemIdentifier : results)
            res.getHeader().add(this.createHeader(parameters, itemIdentifier));

        return res;
    }


    private HeaderType createHeader(OAIParameters parameters,
                                    ItemIdentifier itemIdentifier) throws BadArgumentException,
            CannotDisseminateRecordException, OAIException,
            NoMetadataFormatsException, CannotDisseminateFormatException {
        MetadataFormat format = context.getFormatByPrefix(parameters
                .getMetadataPrefix());
        if (!itemIdentifier.isDeleted() && !format.isApplicable(itemIdentifier))
            throw new CannotDisseminateRecordException();

        HeaderType header = new HeaderType();
        header.setDatestamp(getFormatter().format(itemIdentifier.getDatestamp(), identify.getGranularity()));
        header.setIdentifier(itemIdentifier.getIdentifier());
        if (itemIdentifier.isDeleted())
            header.setStatus(StatusType.DELETED);

        ItemIdentifyHelper ident = new ItemIdentifyHelper(itemIdentifier);

        for (ReferenceSet s : ident.getSets(context))
            header.getSetSpec().add(s.getSetSpec());
        return header;
    }
}
