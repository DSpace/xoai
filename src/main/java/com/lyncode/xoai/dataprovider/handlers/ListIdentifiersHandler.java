package com.lyncode.xoai.dataprovider.handlers;

import com.lyncode.xoai.dataprovider.core.*;
import com.lyncode.xoai.dataprovider.data.AbstractItemIdentifier;
import com.lyncode.xoai.dataprovider.data.AbstractResumptionTokenFormat;
import com.lyncode.xoai.dataprovider.data.MetadataFormat;
import com.lyncode.xoai.dataprovider.data.internal.ItemIdentify;
import com.lyncode.xoai.dataprovider.data.internal.ItemRepository;
import com.lyncode.xoai.dataprovider.data.AbstractIdentify;
import com.lyncode.xoai.dataprovider.data.internal.SetRepository;
import com.lyncode.xoai.dataprovider.exceptions.*;
import com.lyncode.xoai.dataprovider.services.api.DateProvider;
import com.lyncode.xoai.dataprovider.xml.oaipmh.*;

import java.util.List;


public class ListIdentifiersHandler extends VerbHandler<ListIdentifiersType> {
    private final int maxListSize;
    private SetRepository setRepository;
    private ItemRepository itemRepository;
    private AbstractIdentify identify;
    private XOAIContext context;
    private AbstractResumptionTokenFormat resumptionFormat;

    public ListIdentifiersHandler(DateProvider formatter, int maxListSize,
                                  SetRepository setRepository,
                                  ItemRepository itemRepository,
                                  AbstractIdentify identify,
                                  XOAIContext context, AbstractResumptionTokenFormat _format) {

        super(formatter);
        this.maxListSize = maxListSize;
        this.setRepository = setRepository;
        this.itemRepository = itemRepository;
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
                result = itemRepository.getItemIdentifiers(context,
                        token.getOffset(), length,
                        parameters.getMetadataPrefix(), parameters.getFrom());
            else if (!parameters.hasFrom() && parameters.hasUntil())
                result = itemRepository.getItemIdentifiersUntil(context,
                        token.getOffset(), length,
                        parameters.getMetadataPrefix(), parameters.getUntil());
            else if (parameters.hasFrom() && parameters.hasUntil())
                result = itemRepository.getItemIdentifiers(context,
                        token.getOffset(), length,
                        parameters.getMetadataPrefix(), parameters.getFrom(),
                        parameters.getUntil());
            else
                result = itemRepository.getItemIdentifiers(context,
                        token.getOffset(), length,
                        parameters.getMetadataPrefix());
        } else {
            if (!setRepository.exists(context, parameters.getSet()))
                throw new NoMatchesException();
            if (parameters.hasFrom() && !parameters.hasUntil())
                result = itemRepository.getItemIdentifiers(context,
                        token.getOffset(), length,
                        parameters.getMetadataPrefix(), parameters.getSet(),
                        parameters.getFrom());
            else if (!parameters.hasFrom() && parameters.hasUntil())
                result = itemRepository.getItemIdentifiersUntil(context,
                        token.getOffset(), length,
                        parameters.getMetadataPrefix(), parameters.getSet(),
                        parameters.getUntil());
            else if (parameters.hasFrom() && parameters.hasUntil())
                result = itemRepository.getItemIdentifiers(context,
                        token.getOffset(), length,
                        parameters.getMetadataPrefix(), parameters.getSet(),
                        parameters.getFrom(), parameters.getUntil());
            else
                result = itemRepository.getItemIdentifiers(context,
                        token.getOffset(), length,
                        parameters.getMetadataPrefix(), parameters.getSet());
        }

        List<AbstractItemIdentifier> results = result.getResults();
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
            resToken.setValue(resumptionFormat.format(newToken));
            resToken.setCursor(token.getOffset() / maxListSize);
            if (result.hasTotalResults())
                resToken.setCompleteListSize(result.getTotal());
            res.setResumptionToken(resToken);
        }

        for (AbstractItemIdentifier ii : results)
            res.getHeader().add(this.createHeader(parameters, ii));

        return res;
    }


    private HeaderType createHeader(OAIParameters parameters,
                                    AbstractItemIdentifier ii) throws BadArgumentException,
            CannotDisseminateRecordException, OAIException,
            NoMetadataFormatsException, CannotDisseminateFormatException {
        MetadataFormat format = context.getFormatByPrefix(parameters
                .getMetadataPrefix());
        if (!ii.isDeleted() && !format.isApplyable(ii))
            throw new CannotDisseminateRecordException();

        HeaderType header = new HeaderType();
        header.setDatestamp(getFormatter().format(ii.getDatestamp(), identify.getGranularity()));
        header.setIdentifier(ii.getIdentifier());
        if (ii.isDeleted())
            header.setStatus(StatusType.DELETED);

        ItemIdentify ident = new ItemIdentify(ii);

        for (ReferenceSet s : ident.getSets(context))
            header.getSetSpec().add(s.getSetSpec());
        return header;
    }
}
