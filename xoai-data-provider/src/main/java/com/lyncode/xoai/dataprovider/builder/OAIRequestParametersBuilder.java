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

package com.lyncode.xoai.dataprovider.builder;

import com.lyncode.builder.Builder;
import com.lyncode.xoai.dataprovider.exceptions.*;
import com.lyncode.xoai.exceptions.InvalidResumptionTokenException;
import com.lyncode.xoai.model.oaipmh.Verb;
import com.lyncode.xoai.dataprovider.parameters.OAICompiledRequest;
import com.lyncode.xoai.dataprovider.parameters.OAIRequest;
import com.lyncode.xoai.services.impl.UTCDateProvider;

import java.util.*;

import static java.util.Arrays.asList;

public class OAIRequestParametersBuilder implements Builder<OAIRequest> {
    private final UTCDateProvider utcDateProvider = new UTCDateProvider();
    private Map<String, List<String>> params = new HashMap<String, List<String>>();

    public OAIRequestParametersBuilder with(String name, String... values) {
        if (values == null || (values.length > 0 && values[0] == null))
            return without(name);
        if (!params.containsKey(name))
            params.put(name, new ArrayList<String>());

        params.get(name).addAll(asList(values));
        return this;
    }

    @Override
    public OAIRequest build() {
        return new OAIRequest(params);
    }

    public OAIRequestParametersBuilder withVerb(String verb) {
        return with("verb", verb);
    }
    public OAIRequestParametersBuilder withVerb(Verb.Type verb) {
        return with("verb", verb.displayName());
    }

    public OAIRequestParametersBuilder withMetadataPrefix(String mdp) {
        return with("metadataPrefix", mdp);
    }

    public OAIRequestParametersBuilder withFrom(Date date) {
        if (date != null)
            return with("from", utcDateProvider.format(date));
        else
            return without("from");
    }

    private OAIRequestParametersBuilder without(String field) {
        params.remove(field);
        return this;
    }

    public OAIRequestParametersBuilder withUntil(Date date) {
        if (date != null)
            return with("until", utcDateProvider.format(date));
        else
            return without("until");
    }

    public OAIRequestParametersBuilder withIdentifier(String identifier) {
        return with("identifier", identifier);
    }

    public OAIRequestParametersBuilder withResumptionToken(String resumptionToken) {
        return with("resumptionToken", resumptionToken);
    }

    public OAICompiledRequest compile () throws BadArgumentException, InvalidResumptionTokenException, UnknownParameterException, IllegalVerbException, DuplicateDefinitionException {
        return this.build().compile();
    }

    public OAIRequestParametersBuilder withSet(String set) {
        return with("set", set);
    }
}
