package com.lyncode.xoai.dataprovider.xml.xoaiconfig;

import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XMLWritable;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;
import com.lyncode.xoai.dataprovider.xml.read.XmlReader;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.ConfigurationParser;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;


public class Configuration implements XMLWritable {
    public static Configuration readConfiguration(InputStream input) throws ConfigurationException {
        try {
            return new ConfigurationParser().parse(new XmlReader(input));
        } catch (XMLStreamException e) {
            throw new ConfigurationException(e.getMessage(), e);
        } catch (com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.ParseException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    private List<ContextConfiguration> contextConfigurations = new ArrayList<ContextConfiguration>();
    private List<FormatConfiguration> formatConfigurations = new ArrayList<FormatConfiguration>();
    private List<TransformerConfiguration> transformerConfigurations = new ArrayList<TransformerConfiguration>();
    private List<FilterConfiguration> filters = new ArrayList<FilterConfiguration>();
    private List<ConditionConfiguration> conditions = new ArrayList<ConditionConfiguration>();
    private List<SetConfiguration> sets = new ArrayList<SetConfiguration>();

    private int maxListRecordsSize = 100;
    private int maxListSetsSize = 100;
    private int maxListIdentifiersSize = 100;
    private boolean indented = false;
    private String stylesheet;
    private String descriptionFile;


    public Integer getMaxListRecordsSize() {
        return maxListRecordsSize;
    }

    public Configuration withMaxListRecordsSize(int maxListRecordsSize) {
        this.maxListRecordsSize = maxListRecordsSize;
        return this;
    }

    public Integer getMaxListSetsSize() {
        return maxListSetsSize;
    }

    public Configuration withMaxListSetsSize(int maxListSetsSize) {
        this.maxListSetsSize = maxListSetsSize;
        return this;
    }

    public Integer getMaxListIdentifiersSize() {
        return maxListIdentifiersSize;
    }

    public Configuration withMaxListIdentifiersSize(int maxListIdentifiersSize) {
        this.maxListIdentifiersSize = maxListIdentifiersSize;
        return this;
    }

    public Boolean getIndented() {
        return indented;
    }

    public Configuration withIndented(Boolean indented) {
        this.indented = indented;
        return this;
    }

    public String getStylesheet() {
        return stylesheet;
    }

    public Configuration withStylesheet(String stylesheet) {
        this.stylesheet = stylesheet;
        return this;
    }

    public String getDescriptionFile() {
        return descriptionFile;
    }

    public Configuration withDescriptionFile(String descriptionFile) {
        this.descriptionFile = descriptionFile;
        return this;
    }

    public List<ContextConfiguration> getContexts() {
        return contextConfigurations;
    }

    public Configuration withContextConfigurations(Collection<ContextConfiguration> contextConfigurations) {
        this.contextConfigurations.addAll(contextConfigurations);
        return this;
    }

    public Configuration withContextConfigurations(ContextConfiguration... contextConfigurations) {
        this.contextConfigurations.addAll(asList(contextConfigurations));
        return this;
    }

    public List<FormatConfiguration> getFormats() {
        return formatConfigurations;
    }

    public Configuration withFormatConfigurations(Collection<FormatConfiguration> formatConfigurations) {
        this.formatConfigurations.addAll(formatConfigurations);
        return this;
    }

    public Configuration withFormatConfigurations(FormatConfiguration... formatConfigurations) {
        this.formatConfigurations.addAll(asList(formatConfigurations));
        return this;
    }

    public List<TransformerConfiguration> getTransformers() {
        return transformerConfigurations;
    }

    public Configuration withTransformerConfigurations(Collection<TransformerConfiguration> transformerConfigurations) {
        this.transformerConfigurations.addAll(transformerConfigurations);
        return this;
    }

    public Configuration withTransformerConfigurations(TransformerConfiguration... transformerConfigurations) {
        this.transformerConfigurations.addAll(asList(transformerConfigurations));
        return this;
    }

    public List<FilterConfiguration> getFilters() {
        return filters;
    }

    public Configuration withFilters(List<FilterConfiguration> filters) {
        this.filters.addAll(filters);
        return this;
    }

    public Configuration withFilters(FilterConfiguration... filters) {
        this.filters.addAll(asList(filters));
        return this;
    }

    public List<ConditionConfiguration> getConditions() {
        return conditions;
    }

    public Configuration withConditions(List<ConditionConfiguration> conditions) {
        this.conditions.addAll(conditions);
        return this;
    }

    public Configuration withConditions(ConditionConfiguration... conditions) {
        this.conditions.addAll(asList(conditions));
        return this;
    }

    public List<SetConfiguration> getSets() {
        return sets;
    }

    public Configuration withSets(List<SetConfiguration> sets) {
        this.sets.addAll(sets);
        return this;
    }

    public Configuration withSets(SetConfiguration... sets) {
        this.sets.addAll(asList(sets));
        return this;
    }

    @Override
    public void write(XmlOutputContext writer) throws WritingXmlException {
        if (!hasContexts()) throw new WritingXmlException("Configuration must have at least one Context");
        if (!hasFormats()) throw new WritingXmlException("Configuration must have at least one Format");
        try {
            writer.getWriter().writeStartElement("Configuration");
            writer.getWriter().writeNamespace("", "http://www.lyncode.com/XOAIConfiguration");
            writer.getWriter().writeAttribute("maxListRecordsSize", String.valueOf(maxListRecordsSize));
            writer.getWriter().writeAttribute("maxListSetsSize", String.valueOf(maxListRecordsSize));
            writer.getWriter().writeAttribute("maxListIdentifiersSize", String.valueOf(maxListRecordsSize));
            writer.getWriter().writeAttribute("indented", String.valueOf(indented));
            if (hasStylesheet())
                writer.getWriter().writeAttribute("stylesheet", stylesheet);
            if (hasDescriptionFile())
                writer.getWriter().writeAttribute("descriptionFile", descriptionFile);

            writer.getWriter().writeStartElement("Contexts");
            for (ContextConfiguration context : contextConfigurations) {
                context.write(writer);
            }
            writer.getWriter().writeEndElement();

            writer.getWriter().writeStartElement("Formats");
            for (FormatConfiguration formatConfiguration : formatConfigurations) {
                formatConfiguration.write(writer);
            }
            writer.getWriter().writeEndElement();


            if (hasTransformers()) {
                writer.getWriter().writeStartElement("Transformers");
                for (TransformerConfiguration transformerConfiguration : transformerConfigurations) {
                    transformerConfiguration.write(writer);
                }
                writer.getWriter().writeEndElement();
            }

            if (hasFilters()) {
                writer.getWriter().writeStartElement("Filters");
                for (FilterConfiguration filterConfiguration : filters) {
                    filterConfiguration.write(writer);
                }
                for (ConditionConfiguration conditionConfiguration : conditions) {
                    conditionConfiguration.write(writer);
                }
                writer.getWriter().writeEndElement();
            }

            if (hasSets()) {
                writer.getWriter().writeStartElement("Sets");
                for (SetConfiguration setConfiguration : sets) {
                    setConfiguration.write(writer);
                }
                writer.getWriter().writeEndElement();
            }

            writer.getWriter().writeEndElement();
            writer.getWriter().close();
        } catch (XMLStreamException e) {
            throw new WritingXmlException(e);
        }
    }

    public boolean hasSets() {
        return sets != null && !sets.isEmpty();
    }

    public boolean hasFilters() {
        return hasFilterDefinitions() || hasConditionDefinitions();
    }

    public boolean hasConditionDefinitions() {
        return conditions != null && !conditions.isEmpty();
    }

    public boolean hasFilterDefinitions() {
        return filters != null && !filters.isEmpty();
    }

    public boolean hasTransformers() {
        return transformerConfigurations != null && !transformerConfigurations.isEmpty();
    }

    public boolean hasDescriptionFile() {
        return descriptionFile != null;
    }

    public boolean hasStylesheet() {
        return this.stylesheet != null;
    }

    public boolean hasFormats() {
        return formatConfigurations != null && !formatConfigurations.isEmpty();
    }

    public boolean hasContexts() {
        return contextConfigurations != null && !contextConfigurations.isEmpty();
    }

    public void write(ByteArrayOutputStream output) throws XMLStreamException, WritingXmlException {
        this.write(XmlOutputContext.emptyContext(output));
    }
}
