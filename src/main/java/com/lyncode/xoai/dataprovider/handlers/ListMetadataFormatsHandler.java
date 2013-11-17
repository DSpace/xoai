package com.lyncode.xoai.dataprovider.handlers;

import com.lyncode.xoai.dataprovider.core.OAIParameters;
import com.lyncode.xoai.dataprovider.core.XOAIContext;
import com.lyncode.xoai.dataprovider.data.Item;
import com.lyncode.xoai.dataprovider.data.internal.ItemRepositoryHelper;
import com.lyncode.xoai.dataprovider.data.internal.MetadataFormat;
import com.lyncode.xoai.dataprovider.exceptions.HandlerException;
import com.lyncode.xoai.dataprovider.exceptions.NoMetadataFormatsException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.services.api.DateProvider;
import com.lyncode.xoai.dataprovider.xml.oaipmh.ListMetadataFormatsType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.MetadataFormatType;

import java.util.List;


public class ListMetadataFormatsHandler extends VerbHandler<ListMetadataFormatsType> {
    private ItemRepositoryHelper itemRepositoryHelper;
    private XOAIContext context;

    public ListMetadataFormatsHandler(DateProvider formatter, ItemRepositoryHelper itemRepositoryHelper, XOAIContext context) {
        super(formatter);
        this.itemRepositoryHelper = itemRepositoryHelper;
        this.context = context;
    }


    @Override
    public ListMetadataFormatsType handle(OAIParameters params) throws OAIException, HandlerException {
        ListMetadataFormatsType result = new ListMetadataFormatsType();

        if (params.hasIdentifier()) {
            Item item = itemRepositoryHelper.getItem(params.getIdentifier());
            List<MetadataFormat> forms = context.getFormats(item);
            if (forms.isEmpty())
                throw new NoMetadataFormatsException();
            for (MetadataFormat f : forms) {
                MetadataFormatType format = new MetadataFormatType();
                format.setMetadataPrefix(f.getPrefix());
                format.setMetadataNamespace(f.getNamespace());
                format.setSchema(f.getSchemaLocation());
                result.getMetadataFormat().add(format);
            }
        } else {
            List<MetadataFormat> forms = context.getFormats();
            if (forms.isEmpty())
                throw new OAIException(
                        "The respository should have at least one metadata format");
            for (MetadataFormat f : context.getFormats()) {
                MetadataFormatType format = new MetadataFormatType();
                format.setMetadataPrefix(f.getPrefix());
                format.setMetadataNamespace(f.getNamespace());
                format.setSchema(f.getSchemaLocation());
                result.getMetadataFormat().add(format);
            }
        }

        return result;
    }

}
