package com.lyncode.xoai.dataprovider.handlers;

import com.lyncode.xoai.dataprovider.core.*;
import com.lyncode.xoai.dataprovider.data.About;
import com.lyncode.xoai.dataprovider.data.Item;
import com.lyncode.xoai.dataprovider.data.internal.ItemHelper;
import com.lyncode.xoai.dataprovider.data.internal.ItemRepositoryHelper;
import com.lyncode.xoai.dataprovider.data.internal.MetadataFormat;
import com.lyncode.xoai.dataprovider.data.internal.SetRepositoryHelper;
import com.lyncode.xoai.dataprovider.exceptions.*;
import com.lyncode.xoai.dataprovider.services.api.DateProvider;
import com.lyncode.xoai.dataprovider.services.api.RepositoryConfiguration;
import com.lyncode.xoai.dataprovider.services.api.ResumptionTokenFormatter;
import com.lyncode.xoai.dataprovider.xml.oaipmh.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;


public class ListRecordsHandler extends VerbHandler<ListRecordsType> {
    private static Logger log = LogManager.getLogger(ListRecordsHandler.class);
    private final int maxListSize;
    private SetRepositoryHelper setRepository;
    private ItemRepositoryHelper itemRepositoryHelper;
    private RepositoryConfiguration identify;
    private XOAIContext context;
    private ResumptionTokenFormatter resumptionFormat;


    public ListRecordsHandler(DateProvider formatter, int maxListSize,
                              SetRepositoryHelper setRepository,
                              ItemRepositoryHelper itemRepositoryHelper,
                              RepositoryConfiguration identify,
                              XOAIContext context, ResumptionTokenFormatter format) {

        super(formatter);
        this.maxListSize = maxListSize;
        this.setRepository = setRepository;
        this.itemRepositoryHelper = itemRepositoryHelper;
        this.identify = identify;
        this.context = context;
        this.resumptionFormat = format;
    }


    @Override
    public ListRecordsType handle(OAIParameters parameters) throws OAIException, HandlerException {
        ListRecordsType res = new ListRecordsType();
        ResumptionToken token = parameters.getResumptionToken();
        int length = maxListSize;

        if (parameters.hasSet() && !setRepository.supportSets())
            throw new DoesNotSupportSetsException();

        log.debug("Getting items from data source");
        ListItemsResults result;
        if (!parameters.hasSet()) {
            if (parameters.hasFrom() && !parameters.hasUntil())
                result = itemRepositoryHelper.getItems(context, token.getOffset(),
                        length, parameters.getMetadataPrefix(),
                        parameters.getFrom());
            else if (!parameters.hasFrom() && parameters.hasUntil())
                result = itemRepositoryHelper.getItemsUntil(context, token.getOffset(),
                        length, parameters.getMetadataPrefix(),
                        parameters.getUntil());
            else if (parameters.hasFrom() && parameters.hasUntil())
                result = itemRepositoryHelper.getItems(context, token.getOffset(),
                        length, parameters.getMetadataPrefix(),
                        parameters.getFrom(), parameters.getUntil());
            else
                result = itemRepositoryHelper.getItems(context, token.getOffset(),
                        length, parameters.getMetadataPrefix());
        } else {
            if (!setRepository.exists(context, parameters.getSet()))
                throw new NoMatchesException();
            if (parameters.hasFrom() && !parameters.hasUntil())
                result = itemRepositoryHelper.getItems(context, token.getOffset(),
                        length, parameters.getMetadataPrefix(),
                        parameters.getSet(), parameters.getFrom());
            else if (!parameters.hasFrom() && parameters.hasUntil())
                result = itemRepositoryHelper.getItemsUntil(context, token.getOffset(),
                        length, parameters.getMetadataPrefix(),
                        parameters.getSet(), parameters.getUntil());
            else if (parameters.hasFrom() && parameters.hasUntil())
                result = itemRepositoryHelper.getItems(context, token.getOffset(),
                        length, parameters.getMetadataPrefix(),
                        parameters.getSet(), parameters.getFrom(),
                        parameters.getUntil());
            else
                result = itemRepositoryHelper.getItems(context, token.getOffset(),
                        length, parameters.getMetadataPrefix(),
                        parameters.getSet());
        }
        log.debug("Items retrived from data source");

        List<Item> results = result.getResults();
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
            if (!newToken.isEmpty())
                resToken.setValue(resumptionFormat.format(newToken));
            resToken.setCursor(token.getOffset() / maxListSize);
            if (result.hasTotalResults())
                resToken.setCompleteListSize(result.getTotal());
            res.setResumptionToken(resToken);
        }

        log.debug("Now adding records to the OAI-PMH Output");
        for (Item i : results)
            res.getRecord().add(this.createRecord(parameters, i));

        return res;
    }


    private RecordType createRecord(OAIParameters parameters, Item item)
            throws BadArgumentException, CannotDisseminateRecordException,
            OAIException, NoMetadataFormatsException, CannotDisseminateFormatException {
        log.debug("Metadata format: " + parameters.getMetadataPrefix());
        MetadataFormat format = context.getFormatByPrefix(parameters
                .getMetadataPrefix());
        RecordType record = new RecordType();
        HeaderType header = new HeaderType();
        log.debug("ItemHelper: " + item.getIdentifier());
        header.setIdentifier(item.getIdentifier());

        ItemHelper itemHelperWrap = new ItemHelper(item);

        header.setDatestamp(getFormatter().format(item.getDatestamp(), identify.getGranularity()));
        for (ReferenceSet s : itemHelperWrap.getSets(context))
            header.getSetSpec().add(s.getSetSpec());
        if (item.isDeleted())
            header.setStatus(StatusType.DELETED);
        record.setHeader(header);

        if (!item.isDeleted()) {
            MetadataType metadata = null;
            try {
                if (context.getTransformer().hasTransformer()) {
                    metadata = new MetadataType(itemHelperWrap.toPipeline(true)
                            .apply(context.getTransformer().getXslTransformer().getValue())
                            .apply(format.getTransformer())
                            .getTransformed());
                } else {
                    metadata = new MetadataType(itemHelperWrap.toPipeline(true)
                            .apply(format.getTransformer())
                            .getTransformed());
                }
            } catch (WritingXmlException e) {
                throw new OAIException(e);
            } catch (XMLStreamException e) {
                throw new OAIException(e);
            } catch (TransformerException e) {
                throw new OAIException(e);
            } catch (IOException e) {
                throw new OAIException(e);
            }

            record.setMetadata(metadata);

            log.debug("Outputting About");
            if (item.getAbout() != null) {
                for (About abj : item.getAbout()) {
                    AboutType about = new AboutType();
                    about.setAny(abj.getXML());
                    record.getAbout().add(about);
                }
            }
        }
        return record;
    }
}
