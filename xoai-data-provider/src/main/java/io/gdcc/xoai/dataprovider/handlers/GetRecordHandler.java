/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.handlers;

import io.gdcc.xoai.dataprovider.exceptions.CannotDisseminateFormatException;
import io.gdcc.xoai.dataprovider.exceptions.HandlerException;
import io.gdcc.xoai.dataprovider.exceptions.IdDoesNotExistException;
import io.gdcc.xoai.dataprovider.exceptions.OAIException;
import io.gdcc.xoai.dataprovider.handlers.helpers.MetadataHelper;
import io.gdcc.xoai.dataprovider.model.Context;
import io.gdcc.xoai.dataprovider.model.Item;
import io.gdcc.xoai.dataprovider.model.MetadataFormat;
import io.gdcc.xoai.dataprovider.model.Set;
import io.gdcc.xoai.dataprovider.parameters.OAICompiledRequest;
import io.gdcc.xoai.dataprovider.repository.Repository;

import io.gdcc.xoai.model.oaipmh.About;
import io.gdcc.xoai.model.oaipmh.GetRecord;
import io.gdcc.xoai.model.oaipmh.Header;
import io.gdcc.xoai.model.oaipmh.Metadata;
import io.gdcc.xoai.model.oaipmh.Record;
import io.gdcc.xoai.xml.EchoElement;
import io.gdcc.xoai.xml.XSLPipeline;
import io.gdcc.xoai.xml.XmlWriter;
import io.gdcc.xoai.xmlio.exceptions.XmlWriteException;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class GetRecordHandler extends VerbHandler<GetRecord> {
    public GetRecordHandler(Context context, Repository repository) {
        super(context, repository);
    }

    @Override
    public GetRecord handle(OAICompiledRequest parameters) throws OAIException, HandlerException {
        Header header = new Header();
        Record record = new Record().withHeader(header);
        GetRecord result = new GetRecord(record);

        MetadataFormat format = getContext().formatForPrefix(parameters.getMetadataPrefix());
        if (format == null) {
            throw new CannotDisseminateFormatException("Format "+parameters.getMetadataPrefix()+" not applicable to this item");
        }

        // Retrieve the item from our source repository, indicating the metadata format to enable prefilled metadata
        Item item = getRepository().getItemRepository().getItem(parameters.getIdentifier(), format);

        if (getContext().hasCondition() &&
            ! getContext().getCondition().getFilter(getRepository().getFilterResolver()).isItemShown(item))
            throw new IdDoesNotExistException("This context does not include this item");

        if (format.hasCondition() &&
            ! format.getCondition().getFilter(getRepository().getFilterResolver()).isItemShown(item))
            throw new CannotDisseminateFormatException("Format "+parameters.getMetadataPrefix()+" not applicable to this item");


        // Build the <header> part of the response
        header.withIdentifier(item.getIdentifier());
        header.withDatestamp(item.getDatestamp());

        for (Set set : getContext().getSets())
            if (set.getCondition().getFilter(getRepository().getFilterResolver()).isItemShown(item))
                header.withSetSpec(set.getSpec());

        for (Set set : item.getSets())
            header.withSetSpec(set.getSpec());

        // No <metadata> or <about> may be present if this item is deleted
        if (item.isDeleted()) {
            header.withStatus(Header.Status.DELETED);
        } else {
            // Next up: <metadata> response part. Skip the pipeline on request by the source.
            // Skip the metadata transformation on request by the source repository.
            Metadata metadata = item.getMetadata();
            if (! metadata.needsProcessing()) {
                record.withMetadata(metadata);
            } else {
                record.withMetadata(MetadataHelper.process(metadata, format, getContext()));
            }
            
            // Last add the <about> section if present (protocol spec says: optional and repeatable)
            for (About about : item.getAbout())
                record.withAbout(about);
        }
        return result;
    }
}
