package com.lyncode.xoai.builders.dataprovider;


import com.lyncode.xoai.builders.Builder;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.util.Arrays.asList;

public class ConfigurationBuilder implements Builder<Configuration> {

    private int maxListIdentifiers;
    private int maxListRecords;
    private int maxListSets;
    private Configuration.Contexts contexts = new Configuration.Contexts();
    private Configuration.Formats formats = new Configuration.Formats();

    private Boolean indentation = true;
    private String stylesheet;
    private Configuration.Sets sets;
    private Configuration.Filters filters;
    private Configuration.Transformers transformers;

    public ConfigurationBuilder withDefaults() {
        this.maxListIdentifiers = 100;
        this.maxListRecords = 100;
        this.maxListSets = 100;
        this.indentation = true;

        return this;
    }

    public ConfigurationBuilder withMaxListIdentifiers(int value) {
        this.maxListIdentifiers = value;
        return this;
    }

    public ConfigurationBuilder withIndentation(boolean value) {
        this.indentation = value;
        return this;
    }

    public ConfigurationBuilder withMaxListRecords(int value) {
        this.maxListRecords = value;
        return this;
    }

    public ConfigurationBuilder withStylesheet(String value) {
        this.stylesheet = value;
        return this;
    }

    public ConfigurationBuilder withMaxListSets(int value) {
        this.maxListSets = value;
        return this;
    }

    public ConfigurationBuilder withContexts(Configuration.Contexts.Context... contexts) {
        this.contexts.getContext().addAll(asList(contexts));
        return this;
    }

    public ConfigurationBuilder withFormats(Configuration.Formats.Format... formats) {
        this.formats.getFormat().addAll(asList(formats));
        return this;
    }

    public ConfigurationBuilder withSets(Configuration.Sets.Set... sets) {
        if (this.sets == null)
            this.sets = new Configuration.Sets();
        this.sets.getSet().addAll(asList(sets));
        return this;
    }

    public ConfigurationBuilder withFilters(Configuration.Filters.Filter... filters) {
        if (this.filters == null)
            this.filters = new Configuration.Filters();
        this.filters.getFilter().addAll(asList(filters));
        return this;
    }

    public ConfigurationBuilder withTransformers(Configuration.Transformers.Transformer... transformers) {
        if (this.transformers == null)
            this.transformers = new Configuration.Transformers();
        this.transformers.getTransformer().addAll(asList(transformers));
        return this;
    }

    public Configuration build() {
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
        configuration.setTransformers(transformers);

        return configuration;
    }

    public InputStream toStream() throws ConfigurationException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        build().write(output);
        output.close();
        return new ByteArrayInputStream(output.toByteArray());
    }
}
