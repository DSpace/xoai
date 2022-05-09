/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider.handler;

import io.gdcc.xoai.model.oaipmh.Record;
import io.gdcc.xoai.serviceprovider.client.OAIClient;
import io.gdcc.xoai.serviceprovider.exceptions.CannotDisseminateFormatException;
import io.gdcc.xoai.serviceprovider.exceptions.IdDoesNotExistException;
import io.gdcc.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import io.gdcc.xoai.serviceprovider.exceptions.OAIRequestException;
import io.gdcc.xoai.serviceprovider.model.Context;
import io.gdcc.xoai.serviceprovider.parameters.GetRecordParameters;
import io.gdcc.xoai.serviceprovider.parameters.Parameters;
import io.gdcc.xoai.serviceprovider.parsers.GetRecordParser;

import java.io.IOException;
import java.io.InputStream;

import static io.gdcc.xoai.model.oaipmh.Verb.Type.GetRecord;

public class GetRecordHandler {

    private final OAIClient client;
    private final Context context;

    public GetRecordHandler(Context context) {
        this.context = context;
        this.client = context.getClient();
    }

    public Record handle(GetRecordParameters parameters) throws IdDoesNotExistException, CannotDisseminateFormatException {
        Parameters requestParameters = Parameters.parameters().withVerb(GetRecord).include(parameters);
        
        try ( InputStream stream = client.execute(requestParameters) ){
            return new GetRecordParser(stream, context, parameters.getMetadataPrefix()).parse();
        } catch (OAIRequestException | IOException e) {
            throw new InvalidOAIResponse(e);
        }
    }
}
