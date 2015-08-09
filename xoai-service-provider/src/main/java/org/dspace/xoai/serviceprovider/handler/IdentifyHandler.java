/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.handler;

import org.dspace.xoai.model.oaipmh.Identify;
import org.dspace.xoai.serviceprovider.client.OAIClient;
import org.dspace.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import org.dspace.xoai.serviceprovider.exceptions.OAIRequestException;
import org.dspace.xoai.serviceprovider.model.Context;
import org.dspace.xoai.serviceprovider.parsers.IdentifyParser;

import static org.dspace.xoai.model.oaipmh.Verb.Type.Identify;
import static org.dspace.xoai.serviceprovider.parameters.Parameters.parameters;

public class IdentifyHandler {
    private final OAIClient client;

    public IdentifyHandler (Context context) {
        this.client = context.getClient();
    }

    public Identify handle () {
        try {
            return new IdentifyParser(client.execute(parameters()
                    .withVerb(Identify)))
                    .parse();
        } catch (OAIRequestException e) {
            throw new InvalidOAIResponse(e);
        }
    }
}
