/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider.handlers;

import org.dspace.xoai.dataprovider.exceptions.HandlerException;
import org.dspace.xoai.dataprovider.exceptions.InternalOAIException;
import org.dspace.xoai.dataprovider.exceptions.NoMetadataFormatsException;
import org.dspace.xoai.dataprovider.exceptions.OAIException;
import org.dspace.xoai.dataprovider.handlers.helpers.ItemRepositoryHelper;
import org.dspace.xoai.dataprovider.model.Context;
import org.dspace.xoai.dataprovider.model.Item;
import org.dspace.xoai.dataprovider.model.MetadataFormat;
import org.dspace.xoai.dataprovider.parameters.OAICompiledRequest;
import org.dspace.xoai.dataprovider.repository.Repository;
import org.dspace.xoai.model.oaipmh.ListMetadataFormats;

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
                org.dspace.xoai.model.oaipmh.MetadataFormat format = new org.dspace.xoai.model.oaipmh.MetadataFormat()
                    .withMetadataPrefix(metadataFormat.getPrefix())
                    .withMetadataNamespace(metadataFormat.getNamespace())
                    .withSchema(metadataFormat.getSchemaLocation());
                result.withMetadataFormat(format);
            }
        } else {
            for (MetadataFormat metadataFormat : getContext().getMetadataFormats()) {
                org.dspace.xoai.model.oaipmh.MetadataFormat format = new org.dspace.xoai.model.oaipmh.MetadataFormat()
                        .withMetadataPrefix(metadataFormat.getPrefix())
                        .withMetadataNamespace(metadataFormat.getNamespace())
                        .withSchema(metadataFormat.getSchemaLocation());
                result.withMetadataFormat(format);
            }
        }

        return result;
    }

}
