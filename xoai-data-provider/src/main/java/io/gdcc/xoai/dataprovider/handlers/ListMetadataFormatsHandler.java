/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.handlers;

import io.gdcc.xoai.dataprovider.exceptions.HandlerException;
import io.gdcc.xoai.dataprovider.exceptions.InternalOAIException;
import io.gdcc.xoai.dataprovider.exceptions.NoMetadataFormatsException;
import io.gdcc.xoai.dataprovider.exceptions.OAIException;
import io.gdcc.xoai.dataprovider.handlers.helpers.ItemRepositoryHelper;
import io.gdcc.xoai.dataprovider.model.Context;
import io.gdcc.xoai.dataprovider.model.Item;
import io.gdcc.xoai.dataprovider.model.MetadataFormat;
import io.gdcc.xoai.dataprovider.parameters.OAICompiledRequest;
import io.gdcc.xoai.dataprovider.repository.Repository;
import io.gdcc.xoai.model.oaipmh.ListMetadataFormats;

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
                io.gdcc.xoai.model.oaipmh.MetadataFormat format = new io.gdcc.xoai.model.oaipmh.MetadataFormat()
                    .withMetadataPrefix(metadataFormat.getPrefix())
                    .withMetadataNamespace(metadataFormat.getNamespace())
                    .withSchema(metadataFormat.getSchemaLocation());
                result.withMetadataFormat(format);
            }
        } else {
            for (MetadataFormat metadataFormat : getContext().getMetadataFormats()) {
                io.gdcc.xoai.model.oaipmh.MetadataFormat format = new io.gdcc.xoai.model.oaipmh.MetadataFormat()
                        .withMetadataPrefix(metadataFormat.getPrefix())
                        .withMetadataNamespace(metadataFormat.getNamespace())
                        .withSchema(metadataFormat.getSchemaLocation());
                result.withMetadataFormat(format);
            }
        }

        return result;
    }

}
