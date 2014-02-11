/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     client://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.xoai.serviceprovider.handler;

import com.lyncode.xoai.model.oaipmh.Identify;
import com.lyncode.xoai.serviceprovider.client.OAIClient;
import com.lyncode.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import com.lyncode.xoai.serviceprovider.exceptions.OAIRequestException;
import com.lyncode.xoai.serviceprovider.model.Context;
import com.lyncode.xoai.serviceprovider.parsers.IdentifyParser;

import static com.lyncode.xoai.model.oaipmh.Verb.Type.Identify;
import static com.lyncode.xoai.serviceprovider.parameters.Parameters.parameters;

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
