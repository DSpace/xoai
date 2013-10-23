package com.lyncode.xoai.dataprovider.handlers;

import com.lyncode.xoai.dataprovider.core.OAIParameters;
import com.lyncode.xoai.dataprovider.core.ReferenceSet;
import com.lyncode.xoai.dataprovider.core.XOAIContext;
import com.lyncode.xoai.dataprovider.data.AbstractAbout;
import com.lyncode.xoai.dataprovider.data.AbstractIdentify;
import com.lyncode.xoai.dataprovider.data.MetadataFormat;
import com.lyncode.xoai.dataprovider.data.internal.Item;
import com.lyncode.xoai.dataprovider.data.internal.ItemRepository;
import com.lyncode.xoai.dataprovider.exceptions.*;
import com.lyncode.xoai.dataprovider.xml.oaipmh.*;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;


public class GetRecordHandler extends VerbHandler<GetRecordType> {

    private XOAIContext context;
    private ItemRepository itemRepository;
    private AbstractIdentify identify;

    public GetRecordHandler(XOAIContext context, ItemRepository itemRepository, AbstractIdentify identify) {
        super();
        this.context = context;
        this.itemRepository = itemRepository;
        this.identify = identify;
    }


    @Override
    public GetRecordType handle(OAIParameters parameters) throws OAIException, HandlerException {
        GetRecordType result = new GetRecordType();
        RecordType record = new RecordType();
        HeaderType header = new HeaderType();
        MetadataFormat format = context.getFormatByPrefix(parameters.getMetadataPrefix());
        Item item = new Item(itemRepository.getItem(parameters.getIdentifier()));
        if (!context.isItemShown(item.getItem()))
            throw new IdDoesNotExistException("Context ignores this item");
        if (!format.isApplyable(item.getItem()))
            throw new CannotDisseminateRecordException("Format not appliable to this item");
        header.setIdentifier(item.getItem().getIdentifier());
        header.setDatestamp(new DateInfo(item.getItem().getDatestamp(),
                identify.getGranularity().toGranularityType()));
        for (ReferenceSet s : item.getSets(context))
            header.getSetSpec().add(s.getSetSpec());
        if (item.getItem().isDeleted())
            header.setStatus(StatusType.DELETED);
        record.setHeader(header);

        if (!item.getItem().isDeleted()) {
            MetadataType metadata = null;
            try {
                if (context.getTransformer().hasTransformer()) {
                    metadata = new MetadataType(item.toPipeline(true)
                            .apply(context.getTransformer().getXslTransformer().getValue())
                            .apply(format.getTransformer())
                            .getTransformed());
                } else {
                    metadata = new MetadataType(item.toPipeline(true)
                            .apply(format.getTransformer())
                            .getTransformed());
                }
            } catch (WritingXmlException e) {
                throw new OAIException(e);
            } catch (XMLStreamException e) {
                throw new OAIException(e);
            } catch (TransformerException e) {
                throw new OAIException(e);
            }

            record.setMetadata(metadata);

            if (item.getItem().hasAbout()) {
                for (AbstractAbout abj : item.getItem().getAbout()) {
                    AboutType about = new AboutType();
                    about.setAny(abj.getXML());
                    record.getAbout().add(about);
                }
            }
        }

        result.setRecord(record);
        return result;
    }

}
