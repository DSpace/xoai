/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider.handlers;

import com.lyncode.xml.exceptions.XmlWriteException;
import org.dspace.xoai.dataprovider.exceptions.CannotDisseminateRecordException;
import org.dspace.xoai.dataprovider.exceptions.HandlerException;
import org.dspace.xoai.dataprovider.exceptions.IdDoesNotExistException;
import org.dspace.xoai.dataprovider.exceptions.OAIException;
import org.dspace.xoai.dataprovider.model.Context;
import org.dspace.xoai.dataprovider.model.Item;
import org.dspace.xoai.dataprovider.model.MetadataFormat;
import org.dspace.xoai.dataprovider.model.Set;
import org.dspace.xoai.dataprovider.parameters.OAICompiledRequest;
import org.dspace.xoai.dataprovider.repository.Repository;
import org.dspace.xoai.model.oaipmh.*;
import org.dspace.xoai.xml.XSLPipeline;
import org.dspace.xoai.xml.XmlWriter;

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
        Item item = getRepository().getItemRepository().getItem(parameters.getIdentifier());

        if (getContext().hasCondition() &&
                !getContext().getCondition().getFilter(getRepository().getFilterResolver()).isItemShown(item))
            throw new IdDoesNotExistException("This context does not include this item");

        if (format.hasCondition() &&
                !format.getCondition().getFilter(getRepository().getFilterResolver()).isItemShown(item))
            throw new CannotDisseminateRecordException("Format not applicable to this item");


        header.withIdentifier(item.getIdentifier());
        header.withDatestamp(item.getDatestamp());

        for (Set set : getContext().getSets())
            if (set.getCondition().getFilter(getRepository().getFilterResolver()).isItemShown(item))
                header.withSetSpec(set.getSpec());

        for (Set set : item.getSets())
            header.withSetSpec(set.getSpec());

        if (item.isDeleted())
            header.withStatus(Header.Status.DELETED);

        if (!item.isDeleted()) {
            Metadata metadata = null;
            try {
                if (getContext().hasTransformer()) {
                    metadata = new Metadata(toPipeline(item)
                            .apply(getContext().getTransformer())
                            .apply(format.getTransformer())
                            .process());
                } else {
                    metadata = new Metadata(toPipeline(item)
                            .apply(format.getTransformer())
                            .process());
                }
            } catch (XMLStreamException e) {
                throw new OAIException(e);
            } catch (TransformerException e) {
                throw new OAIException(e);
            } catch (IOException e) {
                throw new OAIException(e);
            } catch (XmlWriteException e) {
                throw new OAIException(e);
            }

            record.withMetadata(metadata);

            if (item.getAbout() != null) {
                for (About about : item.getAbout())
                    record.withAbout(about);
            }
        }
        return result;
    }

    private XSLPipeline toPipeline(Item item) throws XmlWriteException, XMLStreamException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XmlWriter writer = new XmlWriter(output);
        Metadata metadata = item.getMetadata();
        metadata.write(writer);
        writer.close();
        return new XSLPipeline(new ByteArrayInputStream(output.toByteArray()), true);
    }
}
