/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.builder;

import io.gdcc.xoai.dataprovider.exceptions.BadArgumentException;
import io.gdcc.xoai.dataprovider.exceptions.DuplicateDefinitionException;
import io.gdcc.xoai.dataprovider.exceptions.IllegalVerbException;
import io.gdcc.xoai.dataprovider.exceptions.UnknownParameterException;
import io.gdcc.xoai.dataprovider.parameters.OAICompiledRequest;
import io.gdcc.xoai.dataprovider.parameters.OAIRequest;
import io.gdcc.xoai.exceptions.InvalidResumptionTokenException;
import io.gdcc.xoai.model.oaipmh.Verb;
import io.gdcc.xoai.services.api.DateProvider;
import io.gdcc.xoai.types.Builder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class OAIRequestParametersBuilder implements Builder<OAIRequest> {
    private final Map<String, List<String>> params = new HashMap<>();

    public OAIRequestParametersBuilder with(String name, String... values) {
        if (values == null || (values.length > 0 && values[0] == null))
            return without(name);
        if (!params.containsKey(name))
            params.put(name, new ArrayList<>());

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

    public OAIRequestParametersBuilder withFrom(Instant date) {
        if (date != null)
            return with("from", DateProvider.format(date));
        else
            return without("from");
    }

    private OAIRequestParametersBuilder without(String field) {
        params.remove(field);
        return this;
    }

    public OAIRequestParametersBuilder withUntil(Instant date) {
        if (date != null)
            return with("until", DateProvider.format(date));
        else
            return without("until");
    }

    public OAIRequestParametersBuilder withIdentifier(String identifier) {
        return with("identifier", identifier);
    }

    public OAIRequestParametersBuilder withResumptionToken(String resumptionToken) {
        return with("resumptionToken", resumptionToken);
    }

    public OAICompiledRequest compile() throws BadArgumentException, InvalidResumptionTokenException, UnknownParameterException, IllegalVerbException, DuplicateDefinitionException {
        return this.build().compile();
    }

    public OAIRequestParametersBuilder withSet(String set) {
        return with("set", set);
    }
}
