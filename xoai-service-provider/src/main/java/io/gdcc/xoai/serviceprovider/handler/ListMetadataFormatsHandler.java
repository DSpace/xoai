/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider.handler;

import io.gdcc.xoai.model.oaipmh.MetadataFormat;
import io.gdcc.xoai.serviceprovider.client.OAIClient;
import io.gdcc.xoai.serviceprovider.exceptions.IdDoesNotExistException;
import io.gdcc.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import io.gdcc.xoai.serviceprovider.exceptions.OAIRequestException;
import io.gdcc.xoai.serviceprovider.model.Context;
import io.gdcc.xoai.serviceprovider.parameters.ListMetadataParameters;
import io.gdcc.xoai.serviceprovider.parameters.Parameters;
import io.gdcc.xoai.serviceprovider.parsers.MetadataFormatParser;
import io.gdcc.xoai.xmlio.exceptions.XmlReaderException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static io.gdcc.xoai.model.oaipmh.Verb.Type.ListMetadataFormats;

public class ListMetadataFormatsHandler {
    private final OAIClient client;

    public ListMetadataFormatsHandler(Context context) {
        this.client = context.getClient();
    }

    public List<MetadataFormat> handle(ListMetadataParameters parameters) throws IdDoesNotExistException {
        List<MetadataFormat> result = new ArrayList<>();
        Parameters requestParameters = Parameters.parameters().withVerb(ListMetadataFormats).include(parameters);
        
        try ( InputStream stream = client.execute(requestParameters) ) {
            MetadataFormatParser parser = new MetadataFormatParser(stream);
            while (parser.hasNext())
                result.add(parser.next());
            return result;
        } catch (XmlReaderException | OAIRequestException | IOException e) {
            throw new InvalidOAIResponse(e);
        }
    }
}
