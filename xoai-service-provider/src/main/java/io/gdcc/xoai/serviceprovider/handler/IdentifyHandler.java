/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider.handler;

import io.gdcc.xoai.model.oaipmh.Identify;
import io.gdcc.xoai.serviceprovider.client.OAIClient;
import io.gdcc.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import io.gdcc.xoai.serviceprovider.exceptions.OAIRequestException;
import io.gdcc.xoai.serviceprovider.model.Context;
import io.gdcc.xoai.serviceprovider.parameters.Parameters;
import io.gdcc.xoai.serviceprovider.parsers.IdentifyParser;

import java.io.IOException;
import java.io.InputStream;

import static io.gdcc.xoai.model.oaipmh.Verb.Type.Identify;

public class IdentifyHandler {
    private final OAIClient client;

    public IdentifyHandler (Context context) {
        this.client = context.getClient();
    }

    public Identify handle() {
        Parameters requestParameters = Parameters.parameters().withVerb(Identify);
    
        try ( InputStream stream = client.execute(requestParameters) ){
            return new IdentifyParser(stream).parse();
        } catch (OAIRequestException | IOException e) {
            throw new InvalidOAIResponse(e);
        }
    }
}
