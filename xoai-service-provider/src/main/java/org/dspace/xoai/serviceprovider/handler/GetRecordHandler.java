/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.handler;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.dspace.xoai.model.oaipmh.Record;
import org.dspace.xoai.serviceprovider.client.OAIClient;
import org.dspace.xoai.serviceprovider.exceptions.CannotDisseminateFormatException;
import org.dspace.xoai.serviceprovider.exceptions.IdDoesNotExistException;
import org.dspace.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import org.dspace.xoai.serviceprovider.exceptions.OAIRequestException;
import org.dspace.xoai.serviceprovider.model.Context;
import org.dspace.xoai.serviceprovider.parameters.GetRecordParameters;
import org.dspace.xoai.serviceprovider.parsers.GetRecordParser;

import static org.dspace.xoai.model.oaipmh.Verb.Type.GetRecord;
import static org.dspace.xoai.serviceprovider.parameters.Parameters.parameters;

public class GetRecordHandler {

    private final OAIClient client;
    private Context context;

    public GetRecordHandler(Context context) {
        this.context = context;
        this.client = context.getClient();
    }

    public Record handle(GetRecordParameters parameters) throws IdDoesNotExistException, CannotDisseminateFormatException {
        InputStream stream = null;
        try {
            stream = client.execute(parameters()
                    .withVerb(GetRecord)
                    .include(parameters));
            Record record = new GetRecordParser(stream, context,
                parameters.getMetadataPrefix()).parse();
            stream.close();
            return record;
        } catch (OAIRequestException e) {
            throw new InvalidOAIResponse(e);
        }
        catch (IOException e) {
            throw new InvalidOAIResponse(e);
        }
        finally {
            IOUtils.closeQuietly(stream);
        }
    }
}
