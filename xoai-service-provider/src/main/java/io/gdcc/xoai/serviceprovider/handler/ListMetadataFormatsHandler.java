/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider.handler;

import com.lyncode.xml.exceptions.XmlReaderException;
import io.gdcc.xoai.serviceprovider.exceptions.IdDoesNotExistException;
import io.gdcc.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import io.gdcc.xoai.serviceprovider.exceptions.OAIRequestException;
import io.gdcc.xoai.serviceprovider.model.Context;
import io.gdcc.xoai.serviceprovider.parameters.ListMetadataParameters;
import io.gdcc.xoai.serviceprovider.parameters.Parameters;
import io.gdcc.xoai.serviceprovider.parsers.MetadataFormatParser;
import org.apache.commons.io.IOUtils;
import org.dspace.xoai.model.oaipmh.MetadataFormat;
import io.gdcc.xoai.serviceprovider.client.OAIClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.dspace.xoai.model.oaipmh.Verb.Type.ListMetadataFormats;

public class ListMetadataFormatsHandler {
    private OAIClient client;

    public ListMetadataFormatsHandler(Context context) {
        this.client = context.getClient();
    }


    public List<MetadataFormat> handle(ListMetadataParameters parameters) throws IdDoesNotExistException {
        List<MetadataFormat> result = new ArrayList<MetadataFormat>();
        InputStream stream = null;
        try {
            stream = client.execute(Parameters.parameters()
                    .withVerb(ListMetadataFormats)
                    .include(parameters));
            MetadataFormatParser parser = new MetadataFormatParser(stream);
            while (parser.hasNext())
                result.add(parser.next());
            stream.close();
            return result;
        } catch (XmlReaderException e) {
            throw new InvalidOAIResponse(e);
        } catch (OAIRequestException e) {
            throw new InvalidOAIResponse(e);
        } catch (IOException e) {
            throw new InvalidOAIResponse(e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }
}
