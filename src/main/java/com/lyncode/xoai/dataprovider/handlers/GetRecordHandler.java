package com.lyncode.xoai.dataprovider.handlers;

import java.io.FileNotFoundException;

import com.lyncode.xoai.dataprovider.core.OAIParameters;
import com.lyncode.xoai.dataprovider.core.ReferenceSet;
import com.lyncode.xoai.dataprovider.core.XOAIContext;
import com.lyncode.xoai.dataprovider.data.AbstractAbout;
import com.lyncode.xoai.dataprovider.data.AbstractIdentify;
import com.lyncode.xoai.dataprovider.data.MetadataFormat;
import com.lyncode.xoai.dataprovider.data.internal.Item;
import com.lyncode.xoai.dataprovider.data.internal.ItemRepository;
import com.lyncode.xoai.dataprovider.exceptions.CannotDisseminateRecordException;
import com.lyncode.xoai.dataprovider.exceptions.HandlerException;
import com.lyncode.xoai.dataprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.exceptions.XSLTransformationException;
import com.lyncode.xoai.dataprovider.xml.oaipmh.AboutType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.DateInfo;
import com.lyncode.xoai.dataprovider.xml.oaipmh.GetRecordType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.HeaderType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.MetadataType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.RecordType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.StatusType;
import com.lyncode.xoai.util.XSLTUtils;


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
                    metadata = new MetadataType(XSLTUtils.transform(context
                                                                    .getTransformer().getXSLTFile(), format
                                                                    .getXSLTFile(), item.getItem()));
                } else {
                    metadata = new MetadataType(XSLTUtils.transform(format.getXSLTFile(), item.getItem()));
                }
            } catch (XSLTransformationException e) {
                throw new OAIException(e);
            } catch (FileNotFoundException e) {
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
