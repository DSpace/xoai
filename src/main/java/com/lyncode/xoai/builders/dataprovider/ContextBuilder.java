package com.lyncode.xoai.builders.dataprovider;

import com.lyncode.xoai.builders.Builder;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.BundleReference;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration;

import java.util.ArrayList;
import java.util.List;

public class ContextBuilder implements Builder<Configuration.Contexts.Context> {
    private String baseUrl;
    private List<BundleReference> formats;
    private List filters;
    private List<BundleReference> sets;
    private BundleReference transformer;
    private String description;
    private String name;

    public ContextBuilder() {
        this.formats = new ArrayList<BundleReference>();
        this.filters = new ArrayList<BundleReference>();
        this.sets = new ArrayList<BundleReference>();
    }

    public ContextBuilder withBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public ContextBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ContextBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ContextBuilder withFormats(String... ids) {
        this.formats.addAll(BundleReferenceBuilder.build(ids));
        return this;
    }

    public ContextBuilder withFormats(List<String> ids) {
        this.formats.addAll(BundleReferenceBuilder.build(ids));
        return this;
    }

    public ContextBuilder withSets(String... ids) {
        this.sets.addAll(BundleReferenceBuilder.build(ids));
        return this;
    }

    public ContextBuilder withSets(List<String> ids) {
        this.sets.addAll(BundleReferenceBuilder.build(ids));
        return this;
    }

    public ContextBuilder withTransformer(String id) {
        this.transformer = BundleReferenceBuilder.build(id);
        return this;
    }

    public ContextBuilder withFilters(String... ids) {
        this.filters.addAll(BundleReferenceBuilder.build(ids));
        return this;
    }

    public ContextBuilder withFilters(List<String> ids) {
        this.filters.addAll(BundleReferenceBuilder.build(ids));
        return this;
    }

    public Configuration.Contexts.Context build() {
        Configuration.Contexts.Context context = new Configuration.Contexts.Context();
        context.setBaseurl(this.baseUrl);
        context.setName(name);
        context.setDescription(description);
        context.setTransformer(this.transformer);
        context.getFilter().addAll(this.filters);
        context.getFormat().addAll(this.formats);
        context.getSet().addAll(this.sets);

        return context;
    }
}
