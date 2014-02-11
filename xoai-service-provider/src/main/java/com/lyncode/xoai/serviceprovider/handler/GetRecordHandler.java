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

import com.lyncode.xoai.model.oaipmh.Record;
import com.lyncode.xoai.serviceprovider.client.OAIClient;
import com.lyncode.xoai.serviceprovider.exceptions.CannotDisseminateFormatException;
import com.lyncode.xoai.serviceprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import com.lyncode.xoai.serviceprovider.exceptions.OAIRequestException;
import com.lyncode.xoai.serviceprovider.model.Context;
import com.lyncode.xoai.serviceprovider.parameters.GetRecordParameters;
import com.lyncode.xoai.serviceprovider.parsers.GetRecordParser;

import static com.lyncode.xoai.model.oaipmh.Verb.Type.GetRecord;
import static com.lyncode.xoai.serviceprovider.parameters.Parameters.parameters;

public class GetRecordHandler {

    private final OAIClient client;
    private Context context;

    public GetRecordHandler(Context context) {
        this.context = context;
        this.client = context.getClient();
    }


    public Record handle(GetRecordParameters parameters) throws IdDoesNotExistException, CannotDisseminateFormatException {
        try {
            return new GetRecordParser(client.execute(parameters()
                    .withVerb(GetRecord)
                    .include(parameters)),
                    context,
                    parameters.getMetadataPrefix()).parse();
        } catch (OAIRequestException e) {
            throw new InvalidOAIResponse(e);
        }
    }
}
