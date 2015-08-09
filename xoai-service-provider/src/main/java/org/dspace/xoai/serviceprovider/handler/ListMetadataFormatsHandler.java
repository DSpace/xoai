/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.handler;

import com.lyncode.xml.exceptions.XmlReaderException;
import org.dspace.xoai.model.oaipmh.MetadataFormat;
import org.dspace.xoai.serviceprovider.client.OAIClient;
import org.dspace.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import org.dspace.xoai.serviceprovider.exceptions.OAIRequestException;
import org.dspace.xoai.serviceprovider.model.Context;
import org.dspace.xoai.serviceprovider.parameters.ListMetadataParameters;
import org.dspace.xoai.serviceprovider.parsers.MetadataFormatParser;

import java.util.ArrayList;
import java.util.List;

import static org.dspace.xoai.model.oaipmh.Verb.Type.ListMetadataFormats;
import static org.dspace.xoai.serviceprovider.parameters.Parameters.parameters;

public class ListMetadataFormatsHandler {
    private OAIClient client;

    public ListMetadataFormatsHandler(Context context) {
        this.client = context.getClient();
    }


    public List<MetadataFormat> handle(ListMetadataParameters parameters) {
        List<MetadataFormat> result = new ArrayList<MetadataFormat>();
        try {
            MetadataFormatParser parser = new MetadataFormatParser(client.execute(parameters()
                    .withVerb(ListMetadataFormats)
                    .include(parameters)));
            while (parser.hasNext())
                result.add(parser.next());
            return result;
        } catch (XmlReaderException e) {
            throw new InvalidOAIResponse(e);
        } catch (OAIRequestException e) {
            throw new InvalidOAIResponse(e);
        }
    }
}
