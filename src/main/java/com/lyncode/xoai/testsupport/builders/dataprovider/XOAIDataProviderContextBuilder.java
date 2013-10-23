package com.lyncode.xoai.testsupport.builders.dataprovider;

import com.lyncode.xoai.dataprovider.xml.xoaiconfig.BundleReference;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration;

import java.util.ArrayList;
import java.util.List;

public class XOAIDataProviderContextBuilder {
    private String baseUrl;
    private List<BundleReference> formats;
    private List filters;
    private List<BundleReference> sets;
    private BundleReference transformer;

    public XOAIDataProviderContextBuilder() {
        this.formats = new ArrayList<BundleReference>();
        this.filters = new ArrayList<BundleReference>();
        this.sets = new ArrayList<BundleReference>();
    }

    public XOAIDataProviderContextBuilder withDefaults() {
        this.baseUrl = "http://www.lyncode.com/dspace/xsd/xoai";
        return this;
    }

    public XOAIDataProviderContextBuilder withFormats(String... ids) {
        this.formats.addAll(XOAIDataProviderBundleReferenceBuilder.build(ids));
        return this;
    }

    public XOAIDataProviderContextBuilder withFormats(List<String> ids) {
        this.formats.addAll(XOAIDataProviderBundleReferenceBuilder.build(ids));
        return this;
    }

    public XOAIDataProviderContextBuilder withSets(String... ids) {
        this.sets.addAll(XOAIDataProviderBundleReferenceBuilder.build(ids));
        return this;
    }

    public XOAIDataProviderContextBuilder withSets(List<String> ids) {
        this.sets.addAll(XOAIDataProviderBundleReferenceBuilder.build(ids));
        return this;
    }

    public XOAIDataProviderContextBuilder withTransformer(String id) {
        this.transformer = XOAIDataProviderBundleReferenceBuilder.build(id);
        return this;
    }

    public XOAIDataProviderContextBuilder withFilters(String... ids) {
        this.filters.addAll(XOAIDataProviderBundleReferenceBuilder.build(ids));
        return this;
    }

    public XOAIDataProviderContextBuilder withFilters(List<String> ids) {
        this.filters.addAll(XOAIDataProviderBundleReferenceBuilder.build(ids));
        return this;
    }

    public Configuration.Contexts.Context build() {
        Configuration.Contexts.Context context = new Configuration.Contexts.Context();
        context.setBaseurl(this.baseUrl);
        context.setTransformer(this.transformer);
        context.getFilter().addAll(this.filters);
        context.getFormat().addAll(this.formats);
        context.getSet().addAll(this.sets);

        return context;
    }
}
