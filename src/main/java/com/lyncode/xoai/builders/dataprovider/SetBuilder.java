package com.lyncode.xoai.builders.dataprovider;

import com.lyncode.xoai.builders.Builder;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.BundleReference;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Parameter;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.asList;

public class SetBuilder implements Builder<Configuration.Sets.Set> {

    private String id;
    private String name;
    private String pattern;
    private Collection<Parameter> parameters = new ArrayList<Parameter>();
    private Collection<BundleReference> filters = new ArrayList<BundleReference>();

    public SetBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public SetBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public SetBuilder withSpec(String spec) {
        this.pattern = spec;
        return this;
    }


    public SetBuilder withParameter(Parameter... parameters) {
        this.parameters.addAll(asList(parameters));
        return this;
    }

    public SetBuilder withFilters(String... ids) {
        this.filters.addAll(BundleReferenceBuilder.build(ids));
        return this;
    }

    public Configuration.Sets.Set build() {
        Configuration.Sets.Set set = new Configuration.Sets.Set();
        set.setId(id);
        set.setName(name);
        set.setPattern(pattern);

        set.getParameter().addAll(parameters);
        set.getFilter().addAll(filters);
        return set;
    }
}
