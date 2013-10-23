package com.lyncode.xoai.testsupport.builders.dataprovider;

import com.lyncode.xoai.dataprovider.xml.xoaiconfig.BundleReference;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Parameter;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.asList;

public class XOAIDataProviderSetBuilder {

    private String id;
    private String name;
    private String pattern;
    private Collection<Parameter> parameters = new ArrayList<Parameter>();
    private Collection<BundleReference> filters = new ArrayList<BundleReference>();

    public XOAIDataProviderSetBuilder withId (String id) {
        this.id = id;
        return this;
    }
    public XOAIDataProviderSetBuilder withName (String name) {
        this.name = name;
        return this;
    }
    public XOAIDataProviderSetBuilder withSpec (String spec) {
        this.pattern = spec;
        return this;
    }


    public XOAIDataProviderSetBuilder withParameter (Parameter... parameters) {
        this.parameters.addAll(asList(parameters));
        return this;
    }
    public XOAIDataProviderSetBuilder withFilters (String... ids) {
        this.filters.addAll(XOAIDataProviderBundleReferenceBuilder.build(ids));
        return this;
    }

    public Configuration.Sets.Set build () {
        Configuration.Sets.Set set = new Configuration.Sets.Set();
        set.setId(id);
        set.setName(name);
        set.setPattern(pattern);

        set.getParameter().addAll(parameters);
        set.getFilter().addAll(filters);
        return set;
    }
}
