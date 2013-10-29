package com.lyncode.xoai.builders;

import com.lyncode.xoai.dataprovider.OAIRequestParameters;
import com.lyncode.xoai.dataprovider.services.api.DateProvider;
import com.lyncode.xoai.dataprovider.services.impl.BaseDateProvider;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Arrays.asList;

public class OAIRequestParametersBuilder {
    private Map<String, List<String>> parameters = new TreeMap<String, List<String>>();
    private DateProvider formatter = new BaseDateProvider();

    public OAIRequestParametersBuilder withVerb (String verb) {
        if (verb != null)
            parameters.put("verb", asList(new String[]{ verb }));
        return this;
    }
    public OAIRequestParametersBuilder withFrom (Date from) {
        if (from != null)
            parameters.put("from", asList(new String[]{ formatter.format(from) }));
        return this;
    }
    public OAIRequestParametersBuilder withUntil (Date until) {
        if (until != null)
            parameters.put("until", asList(new String[]{ formatter.format(until) }));
        return this;
    }
    public OAIRequestParametersBuilder withIdentifier (String id) {
        if (id != null)
            parameters.put("identifier", asList(new String[]{ id }));
        return this;
    }
    public OAIRequestParametersBuilder withMetadataPrefix (String metadataPrefix) {
        if (metadataPrefix != null)
            parameters.put("metadataPrefix", asList(new String[]{ metadataPrefix }));
        return this;
    }
    public OAIRequestParametersBuilder withResumptionToken (String resumptionToken) {
        if (resumptionToken != null)
            parameters.put("resumptionToken", asList(new String[]{ resumptionToken }));
        return this;
    }
    public OAIRequestParametersBuilder withSet (String set) {
        if (set != null)
            parameters.put("set", asList(new String[]{ set }));
        return this;
    }

    public OAIRequestParametersBuilder with (String name, String... values) {
        parameters.put(name, asList(values));
        return this;
    }

    public OAIRequestParameters build () {
        return new OAIRequestParameters(parameters);
    }
}
