package com.lyncode.xoai.testsupport.builders.dataprovider;


import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration;

import static java.util.Arrays.asList;

public class XOAIDataProviderConfigurationBuilder {

    private int maxListIdentifiers;
    private int maxListRecords;
    private int maxListSets;
    private Configuration.Contexts contexts;
    private Configuration.Formats formats;
    private Boolean indentation = true;
    private String stylesheet;
    private Configuration.Sets sets;
    private Configuration.Filters filters;

    public XOAIDataProviderConfigurationBuilder withDefaults () {
        this.maxListIdentifiers = 100;
        this.maxListRecords = 100;
        this.maxListSets = 100;
        this.indentation = true;

        this.contexts = new Configuration.Contexts();
        this.formats = new Configuration.Formats();
        return this;
    }

    public XOAIDataProviderConfigurationBuilder withMaxListIdentifiers (int value) {
        this.maxListIdentifiers = value;
        return this;
    }
    public XOAIDataProviderConfigurationBuilder withIndentation (boolean value) {
        this.indentation = value;
        return this;
    }
    public XOAIDataProviderConfigurationBuilder withMaxListRecords (int value) {
        this.maxListRecords = value;
        return this;
    }
    public XOAIDataProviderConfigurationBuilder withStylesheet (String value) {
        this.stylesheet = value;
        return this;
    }
    public XOAIDataProviderConfigurationBuilder withMaxListSets (int value) {
        this.maxListSets = value;
        return this;
    }

    public XOAIDataProviderConfigurationBuilder withContexts (Configuration.Contexts.Context... contexts) {
        this.contexts.getContext().addAll(asList(contexts));
        return this;
    }

    public XOAIDataProviderConfigurationBuilder withFormats (Configuration.Formats.Format... formats) {
        this.formats.getFormat().addAll(asList(formats));
        return this;
    }

    public XOAIDataProviderConfigurationBuilder withSets (Configuration.Sets.Set... sets) {
        this.sets.getSet().addAll(asList(sets));
        return this;
    }

    public XOAIDataProviderConfigurationBuilder withFilters (Configuration.Filters.Filter... filters) {
        this.filters.getFilter().addAll(asList(filters));
        return this;
    }

    public Configuration build () {
        Configuration configuration = new Configuration();
        configuration.setMaxListIdentifiersSize(maxListIdentifiers);
        configuration.setMaxListRecordsSize(maxListRecords);
        configuration.setMaxListSetsSize(maxListSets);
        configuration.setIndentation(indentation);
        configuration.setStylesheet(stylesheet);

        configuration.setContexts(contexts);
        configuration.setFormats(formats);
        configuration.setSets(sets);
        configuration.setFilters(filters);

        return configuration;
    }
}
