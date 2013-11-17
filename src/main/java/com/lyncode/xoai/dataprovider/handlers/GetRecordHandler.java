package com.lyncode.xoai.dataprovider.handlers;

import com.lyncode.xoai.dataprovider.core.OAIParameters;
import com.lyncode.xoai.dataprovider.core.ReferenceSet;
import com.lyncode.xoai.dataprovider.core.XOAIContext;
import com.lyncode.xoai.dataprovider.data.About;
import com.lyncode.xoai.dataprovider.data.internal.ItemHelper;
import com.lyncode.xoai.dataprovider.data.internal.ItemRepositoryHelper;
import com.lyncode.xoai.dataprovider.data.internal.MetadataFormat;
import com.lyncode.xoai.dataprovider.exceptions.*;
import com.lyncode.xoai.dataprovider.services.api.DateProvider;
import com.lyncode.xoai.dataprovider.services.api.RepositoryConfiguration;
import com.lyncode.xoai.dataprovider.xml.oaipmh.*;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.IOException;


public class GetRecordHandler extends VerbHandler<GetRecordType> {

    private XOAIContext context;
    private ItemRepositoryHelper itemRepositoryHelper;
    private RepositoryConfiguration identify;

    public GetRecordHandler(DateProvider formatter, XOAIContext context, ItemRepositoryHelper itemRepositoryHelper, RepositoryConfiguration identify) {
        super(formatter);
        this.context = context;
        this.itemRepositoryHelper = itemRepositoryHelper;
        this.identify = identify;
    }


    @Override
    public GetRecordType handle(OAIParameters parameters) throws OAIException, HandlerException {
        GetRecordType result = new GetRecordType();
        RecordType record = new RecordType();
        HeaderType header = new HeaderType();
        MetadataFormat format = context.getFormatByPrefix(parameters.getMetadataPrefix());
        ItemHelper itemHelper = new ItemHelper(itemRepositoryHelper.getItem(parameters.getIdentifier()));
        if (!context.isItemShown(itemHelper.getItem()))
            throw new IdDoesNotExistException("ContextConfiguration ignores this itemHelper");
        if (!format.isApplicable(itemHelper.getItem()))
            throw new CannotDisseminateRecordException("FormatConfiguration not appliable to this itemHelper");
        header.setIdentifier(itemHelper.getItem().getIdentifier());
        header.setDatestamp(getFormatter().format(itemHelper.getItem().getDatestamp(),
                identify.getGranularity()));
        for (ReferenceSet s : itemHelper.getSets(context))
            header.getSetSpec().add(s.getSetSpec());
        if (itemHelper.getItem().isDeleted())
            header.setStatus(StatusType.DELETED);
        record.setHeader(header);

        if (!itemHelper.getItem().isDeleted()) {
            MetadataType metadata = null;
            try {
                if (context.getTransformer().hasTransformer()) {
                    metadata = new MetadataType(itemHelper.toPipeline(true)
                            .apply(context.getTransformer().getXslTransformer().getValue())
                            .apply(format.getTransformer())
                            .getTransformed());
                } else {
                    metadata = new MetadataType(itemHelper.toPipeline(true)
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

            if (itemHelper.getItem().getAbout() != null) {
                for (About abj : itemHelper.getItem().getAbout()) {
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
