/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider.handler;

import java.io.IOException;
import java.io.InputStream;

import io.gdcc.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import io.gdcc.xoai.serviceprovider.exceptions.OAIRequestException;
import io.gdcc.xoai.serviceprovider.model.Context;
import io.gdcc.xoai.serviceprovider.parameters.Parameters;
import io.gdcc.xoai.serviceprovider.parsers.IdentifyParser;
import org.apache.commons.io.IOUtils;
import org.dspace.xoai.model.oaipmh.Identify;
import io.gdcc.xoai.serviceprovider.client.OAIClient;

import static org.dspace.xoai.model.oaipmh.Verb.Type.Identify;

public class IdentifyHandler {
    private final OAIClient client;

    public IdentifyHandler (Context context) {
        this.client = context.getClient();
    }

    public Identify handle () {
        InputStream stream = null;
        try {
            stream = client.execute(Parameters.parameters()
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
