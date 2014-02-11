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

import com.lyncode.xml.exceptions.XmlReaderException;
import com.lyncode.xoai.model.oaipmh.MetadataFormat;
import com.lyncode.xoai.serviceprovider.client.OAIClient;
import com.lyncode.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import com.lyncode.xoai.serviceprovider.exceptions.OAIRequestException;
import com.lyncode.xoai.serviceprovider.model.Context;
import com.lyncode.xoai.serviceprovider.parameters.ListMetadataParameters;
import com.lyncode.xoai.serviceprovider.parsers.MetadataFormatParser;

import java.util.ArrayList;
import java.util.List;

import static com.lyncode.xoai.model.oaipmh.Verb.Type.ListMetadataFormats;
import static com.lyncode.xoai.serviceprovider.parameters.Parameters.parameters;

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
