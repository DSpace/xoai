package com.lyncode.xoai.builders;

import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Parameter;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.asList;

public class XOAIDataProviderFilterBuilder {
    private String id;
    private String clazz;
    private Collection<Parameter> parameters = new ArrayList<Parameter>();

    public XOAIDataProviderFilterBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public XOAIDataProviderFilterBuilder withClass(String clazz) {
        this.clazz = clazz;
        return this;
    }

    public XOAIDataProviderFilterBuilder withParameters(Parameter... parameters) {
        this.parameters.addAll(asList(parameters));
        return this;
    }

    public Configuration.Filters.Filter build() {
        Configuration.Filters.Filter filter = new Configuration.Filters.Filter();
        filter.setId(id);
//        filter.setClazz(clazz);

//        filter.getParameter().addAll(parameters);

        return filter;
    }
}
