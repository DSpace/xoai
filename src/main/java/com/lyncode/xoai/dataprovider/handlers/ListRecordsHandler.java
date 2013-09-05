package com.lyncode.xoai.dataprovider.handlers;

import java.io.FileNotFoundException;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.xoai.dataprovider.core.ListItemsResults;
import com.lyncode.xoai.dataprovider.core.OAIParameters;
import com.lyncode.xoai.dataprovider.core.ReferenceSet;
import com.lyncode.xoai.dataprovider.core.ResumptionToken;
import com.lyncode.xoai.dataprovider.core.XOAIContext;
import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.data.AbstractAbout;
import com.lyncode.xoai.dataprovider.data.AbstractIdentify;
import com.lyncode.xoai.dataprovider.data.AbstractItem;
import com.lyncode.xoai.dataprovider.data.AbstractResumptionTokenFormat;
import com.lyncode.xoai.dataprovider.data.MetadataFormat;
import com.lyncode.xoai.dataprovider.data.internal.Item;
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
import com.lyncode.xoai.dataprovider.exceptions.XSLTransformationException;
import com.lyncode.xoai.dataprovider.xml.oaipmh.AboutType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.DateInfo;
import com.lyncode.xoai.dataprovider.xml.oaipmh.HeaderType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.ListRecordsType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.MetadataType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.RecordType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.ResumptionTokenType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.StatusType;
import com.lyncode.xoai.util.XSLTUtils;


public class ListRecordsHandler extends VerbHandler<ListRecordsType> {
    private static Logger log = LogManager.getLogger(ListRecordsHandler.class);
    private SetRepository setRepository;
    private ItemRepository itemRepository;
    private AbstractIdentify identify;
    private XOAIContext context;
    private AbstractResumptionTokenFormat resumptionFormat;
    
    

    public ListRecordsHandler(SetRepository setRepository,
                              ItemRepository itemRepository,
                              AbstractIdentify identify,
                              XOAIContext context, AbstractResumptionTokenFormat format) {

        super();
        this.setRepository = setRepository;
        this.itemRepository = itemRepository;
        this.identify = identify;
        this.context = context;
        this.resumptionFormat = format;
    }


    @Override
    public ListRecordsType handle(OAIParameters parameters) throws OAIException, HandlerException {
        ListRecordsType res = new ListRecordsType();
        ResumptionToken token = parameters.getResumptionToken();
        int length = XOAIManager.getManager().getMaxListRecordsSize();

        if (parameters.hasSet() && !setRepository.supportSets())
            throw new DoesNotSupportSetsException();

        log.debug("Getting items from data source");
        ListItemsResults result;
        if (!parameters.hasSet()) {
            if (parameters.hasFrom() && !parameters.hasUntil())
                result = itemRepository.getItems(context, token.getOffset(),
                        length, parameters.getMetadataPrefix(),
                        parameters.getFrom());
            else if (!parameters.hasFrom() && parameters.hasUntil())
                result = itemRepository.getItemsUntil(context, token.getOffset(),
                        length, parameters.getMetadataPrefix(),
                        parameters.getUntil());
            else if (parameters.hasFrom() && parameters.hasUntil())
                result = itemRepository.getItems(context, token.getOffset(),
                        length, parameters.getMetadataPrefix(),
                        parameters.getFrom(), parameters.getUntil());
            else
                result = itemRepository.getItems(context, token.getOffset(),
                        length, parameters.getMetadataPrefix());
        } else {
            if (!setRepository.exists(context, parameters.getSet()))
                throw new NoMatchesException();
            if (parameters.hasFrom() && !parameters.hasUntil())
                result = itemRepository.getItems(context, token.getOffset(),
                        length, parameters.getMetadataPrefix(),
                        parameters.getSet(), parameters.getFrom());
            else if (!parameters.hasFrom() && parameters.hasUntil())
                result = itemRepository.getItemsUntil(context, token.getOffset(),
                        length, parameters.getMetadataPrefix(),
                        parameters.getSet(), parameters.getUntil());
            else if (parameters.hasFrom() && parameters.hasUntil())
                result = itemRepository.getItems(context, token.getOffset(),
                        length, parameters.getMetadataPrefix(),
                        parameters.getSet(), parameters.getFrom(),
                        parameters.getUntil());
            else
                result = itemRepository.getItems(context, token.getOffset(),
                        length, parameters.getMetadataPrefix(),
                        parameters.getSet());
        }
        log.debug("Items retrived from data source");

        List<AbstractItem> results = result.getResults();
        if (results.isEmpty())
            throw new NoMatchesException();

        ResumptionToken newToken;
        if (result.hasMore()) {
            newToken = new ResumptionToken(token.getOffset() + length,
                    parameters);
        } else {
            newToken = new ResumptionToken();
        }
        
        if (parameters.hasResumptionToken() || !newToken.isEmpty()) {
            ResumptionTokenType resToken = new ResumptionTokenType();
            resToken.setValue(resumptionFormat.format(newToken));
            resToken.setCursor(token.getOffset()/XOAIManager.getManager().getMaxListRecordsSize());
            if (result.hasTotalResults())
                resToken.setCompleteListSize(result.getTotal());
            res.setResumptionToken(resToken);
        }

        log.debug("Now adding records to the OAI-PMH Output");
        for (AbstractItem i : results)
            res.getRecord().add(this.createRecord(parameters, i));

        return res;
    }


    private RecordType createRecord(OAIParameters parameters, AbstractItem item)
            throws BadArgumentException, CannotDisseminateRecordException,
            OAIException, NoMetadataFormatsException, CannotDisseminateFormatException {
        log.debug("Metadata format: " + parameters.getMetadataPrefix());
        MetadataFormat format = context.getFormatByPrefix(parameters
                .getMetadataPrefix());
        RecordType record = new RecordType();
        HeaderType header = new HeaderType();
        log.debug("Item: " + item.getIdentifier());
        header.setIdentifier(item.getIdentifier());
        
        Item itemWrap = new Item(item);
        
        header.setDatestamp(new DateInfo(item.getDatestamp(), identify.getGranularity().toGranularityType()));
        for (ReferenceSet s : itemWrap.getSets(context))
            header.getSetSpec().add(s.getSetSpec());
        if (item.isDeleted())
            header.setStatus(StatusType.DELETED);
        record.setHeader(header);

        if (!item.isDeleted()) {
            try {
                log.debug("Outputting Metadata");
                MetadataType metadata = null;
                if (context.getTransformer().hasTransformer()) {
                    log.debug("Transforming metadata (using transformer)");
                    metadata = new MetadataType(XSLTUtils.transform(context
                                                                    .getTransformer().getXSLTFile(), format
                                                                    .getXSLTFile(), item));
                } else {
                    log.debug("Transforming metadata (without transformer)");
                    metadata = new MetadataType(XSLTUtils.transform(format.getXSLTFile(), item));
                }
                record.setMetadata(metadata);
            } catch (XSLTransformationException e) {
                throw new OAIException(e);
            } catch (FileNotFoundException e) {
                throw new OAIException(e);
            }

            log.debug("Outputting About");
            if (item.hasAbout()) {
                for (AbstractAbout abj : item.getAbout()) {
                    AboutType about = new AboutType();
                    about.setAny(abj.getXML());
                    record.getAbout().add(about);
                }
            }
        }
        return record;
    }
}
