package com.lyncode.xoai.dataprovider.xml.xoaiconfig;

import com.lyncode.builder.ListBuilder;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XMLWritable;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ContextConfiguration implements XMLWritable {
    private BundleReference transformers;
    private BundleReference filters;
    private List<BundleReference> sets = new ArrayList<BundleReference>();
    private List<BundleReference> formats = new ArrayList<BundleReference>();
    private String description;
    private String name;
    private String baseUrl;

    public ContextConfiguration(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public BundleReference getTransformers() {
        return transformers;
    }

    public BundleReference getFilters() {
        return filters;
    }

    public List<BundleReference> getSets() {
        return sets;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public List<BundleReference> getFormats() {
        return formats;
    }

    public ContextConfiguration withTransformer(String transformerId) {
        this.transformers = new BundleReference(transformerId);
        return this;
    }

    public ContextConfiguration withFilter(String filterId) {
        this.filters = new BundleReference(filterId);
        return this;
    }

    public ContextConfiguration withSets(Collection<String> setsIds) {
        this.sets.addAll(new ListBuilder<String>().add(setsIds).build(new ListBuilder.Transformer<String, BundleReference>() {
            @Override
            public BundleReference transform(String elem) {
                return new BundleReference(elem);
            }
        }));
        return this;
    }

    public ContextConfiguration withSets(String... setsIds) {
        this.sets.addAll(new ListBuilder<String>().add(setsIds).build(new ListBuilder.Transformer<String, BundleReference>() {
            @Override
            public BundleReference transform(String elem) {
                return new BundleReference(elem);
            }
        }));
        return this;
    }

    public ContextConfiguration withSet(String setId) {
        this.sets.add(new BundleReference(setId));
        return this;
    }

    public ContextConfiguration withFormats(Collection<String> formats) {
        this.formats.addAll(new ListBuilder<String>().add(formats).build(new ListBuilder.Transformer<String, BundleReference>() {
            @Override
            public BundleReference transform(String elem) {
                return new BundleReference(elem);
            }
        }));
        return this;
    }

    public ContextConfiguration withFormats(String... formats) {
        this.formats.addAll(new ListBuilder<String>().add(formats).build(new ListBuilder.Transformer<String, BundleReference>() {
            @Override
            public BundleReference transform(String elem) {
                return new BundleReference(elem);
            }
        }));
        return this;
    }

    public ContextConfiguration withFormat(String formatId) {
        this.formats.add(new BundleReference(formatId));
        return this;
    }

    public ContextConfiguration withDescription(String description) {
        this.description = description;
        return this;
    }

    public ContextConfiguration withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public void write(XmlOutputContext writer) throws WritingXmlException {
        validate();
        try {
            writer.getWriter().writeStartElement("Context");
            writer.getWriter().writeAttribute("baseurl", baseUrl);
            if (hasName())
                writer.getWriter().writeAttribute("name", name);

            if (hasTransformer()) {
                writer.getWriter().writeStartElement("Transformer");
                transformers.write(writer);
                writer.getWriter().writeEndElement();
            }

            if (hasFilter()) {
                writer.getWriter().writeStartElement("Filter");
                filters.write(writer);
                writer.getWriter().writeEndElement();
            }

            if (hasSets()) {
                for (BundleReference setReference : sets) {
                    writer.getWriter().writeStartElement("Set");
                    setReference.write(writer);
                    writer.getWriter().writeEndElement();
                }
            }

            for (BundleReference formatReference : formats) {
                writer.getWriter().writeStartElement("Format");
                formatReference.write(writer);
                writer.getWriter().writeEndElement();
            }

            if (hasDescription()) {
                writer.getWriter().writeStartElement("Description");
                writer.getWriter().writeCharacters(description);
                writer.getWriter().writeEndElement();
            }

            writer.getWriter().writeEndElement();

        } catch (XMLStreamException e) {
            throw new WritingXmlException(e);
        }
    }

    public boolean hasDescription() {
        return description != null;
    }

    public boolean hasSets() {
        return sets != null && !sets.isEmpty();
    }

    public boolean hasFilter() {
        return filters != null;
    }

    public boolean hasTransformer() {
        return transformers != null;
    }

    private boolean hasName() {
        return name != null;
    }

    private void validate() throws WritingXmlException {
        if (baseUrl == null) throw new WritingXmlException("ContextConfiguration baseUrl is mandatory");
        if (formats == null || formats.isEmpty())
            throw new WritingXmlException("ContextConfiguration must contain at least one FormatConfiguration");
    }
}
