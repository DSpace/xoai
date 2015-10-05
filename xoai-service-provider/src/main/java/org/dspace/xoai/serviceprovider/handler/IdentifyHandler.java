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
        InputStream stream = null;
        try {
            stream = client.execute(parameters()
                    .withVerb(Identify));
            Identify identify = new IdentifyParser(stream).parse();
            stream.close();
            return identify;
        } catch (OAIRequestException e) {
            throw new InvalidOAIResponse(e);
        } catch (IOException e) {
            throw new InvalidOAIResponse(e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }
}
