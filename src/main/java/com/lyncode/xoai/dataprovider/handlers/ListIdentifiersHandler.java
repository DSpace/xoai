package com.lyncode.xoai.dataprovider.handlers;

import java.util.List;

import com.lyncode.xoai.dataprovider.core.ListItemIdentifiersResult;
import com.lyncode.xoai.dataprovider.core.OAIParameters;
import com.lyncode.xoai.dataprovider.core.ReferenceSet;
import com.lyncode.xoai.dataprovider.core.ResumptionToken;
import com.lyncode.xoai.dataprovider.core.XOAIContext;
import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.data.AbstractIdentify;
import com.lyncode.xoai.dataprovider.data.AbstractItemIdentifier;
import com.lyncode.xoai.dataprovider.data.MetadataFormat;
import com.lyncode.xoai.dataprovider.data.internal.ItemIdentify;
import com.lyncode.xoai.dataprovider.data.internal.ItemRepository;
import com.lyncode.xoai.dataprovider.data.internal.SetRepository;
import com.lyncode.xoai.dataprovider.exceptions.BadArgumentException;
import com.lyncode.xoai.dataprovider.exceptions.CannotDisseminateFormatException;
import com.lyncode.xoai.dataprovider.exceptions.CannotDisseminateRecordException;
import com.lyncode.xoai.dataprovider.exceptions.DoesNotSupportSetsException;
import com.lyncode.xoai.dataprovider.exceptions.HandlerException;
import com.lyncode.xoai.dataprovider.exceptions.NoMatchesException;
import com.lyncode.xoai.dataprovider.exceptions.NoMetadataFormatsException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.xml.oaipmh.DateInfo;
import com.lyncode.xoai.dataprovider.xml.oaipmh.HeaderType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.ListIdentifiersType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.ResumptionTokenType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.StatusType;


public class ListIdentifiersHandler extends VerbHandler<ListIdentifiersType> {
    private SetRepository setRepository;
    private ItemRepository itemRepository;
    private AbstractIdentify identify;
    private XOAIContext context;
    
    
    public ListIdentifiersHandler(SetRepository setRepository,
                                  ItemRepository itemRepository,
                                  AbstractIdentify identify,
                                  XOAIContext context) {

        super();
        this.setRepository = setRepository;
        this.itemRepository = itemRepository;
        this.identify = identify;
        this.context = context;
    }


    @Override
    public ListIdentifiersType handle(OAIParameters parameters) throws OAIException, HandlerException {
        ListIdentifiersType res = new ListIdentifiersType();
        ResumptionToken token = parameters.getResumptionToken();

        if (parameters.hasSet() && !setRepository.supportSets())
            throw new DoesNotSupportSetsException();

        int length = XOAIManager.getManager().getMaxListIdentifiersSize();
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
            resToken.setValue(newToken.toString());
            resToken.setCursor(token.getOffset()/XOAIManager.getManager().getMaxListIdentifiersSize());
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
        header.setDatestamp(new DateInfo(ii.getDatestamp(), identify.getGranularity().toGranularityType()));
        header.setIdentifier(ii.getIdentifier());
        if (ii.isDeleted())
            header.setStatus(StatusType.DELETED);
        
        ItemIdentify ident = new ItemIdentify(ii);
        
        for (ReferenceSet s : ident.getSets(context))
            header.getSetSpec().add(s.getSetSpec());
        return header;
    }
}
