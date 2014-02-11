package com.lyncode.xoai.dataprovider.handlers;

import com.lyncode.xoai.dataprovider.exceptions.InternalOAIException;
import com.lyncode.xoai.dataprovider.parameters.OAICompiledRequest;
import com.lyncode.xoai.dataprovider.handlers.helpers.ItemRepositoryHelper;
import com.lyncode.xoai.dataprovider.exceptions.HandlerException;
import com.lyncode.xoai.dataprovider.exceptions.NoMetadataFormatsException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.model.Context;
import com.lyncode.xoai.dataprovider.model.Item;
import com.lyncode.xoai.dataprovider.model.MetadataFormat;
import com.lyncode.xoai.model.oaipmh.ListMetadataFormats;
import com.lyncode.xoai.dataprovider.repository.Repository;

import java.util.List;


public class ListMetadataFormatsHandler extends VerbHandler<ListMetadataFormats> {
    private ItemRepositoryHelper itemRepositoryHelper;

    public ListMetadataFormatsHandler(Context context, Repository repository) {
        super(context, repository);
        itemRepositoryHelper = new ItemRepositoryHelper(repository.getItemRepository());

        // Static validation
        if (getContext().getMetadataFormats() == null ||
                getContext().getMetadataFormats().isEmpty())
            throw new InternalOAIException("The context must expose at least one metadata format");
    }


    @Override
    public ListMetadataFormats handle(OAICompiledRequest params) throws OAIException, HandlerException {
        ListMetadataFormats result = new ListMetadataFormats();

        if (params.hasIdentifier()) {
            Item item = itemRepositoryHelper.getItem(params.getIdentifier());
            List<MetadataFormat> metadataFormats = getContext().formatFor(getRepository().getFilterResolver(), item);
            if (metadataFormats.isEmpty())
                throw new NoMetadataFormatsException();
            for (MetadataFormat metadataFormat : metadataFormats) {
                com.lyncode.xoai.model.oaipmh.MetadataFormat format = new com.lyncode.xoai.model.oaipmh.MetadataFormat()
                    .withMetadataPrefix(metadataFormat.getPrefix())
                    .withMetadataNamespace(metadataFormat.getNamespace())
                    .withSchema(metadataFormat.getSchemaLocation());
                result.withMetadataFormat(format);
            }
        } else {
            for (MetadataFormat metadataFormat : getContext().getMetadataFormats()) {
                com.lyncode.xoai.model.oaipmh.MetadataFormat format = new com.lyncode.xoai.model.oaipmh.MetadataFormat()
                        .withMetadataPrefix(metadataFormat.getPrefix())
                        .withMetadataNamespace(metadataFormat.getNamespace())
                        .withSchema(metadataFormat.getSchemaLocation());
                result.withMetadataFormat(format);
            }
        }

        return result;
    }

}
