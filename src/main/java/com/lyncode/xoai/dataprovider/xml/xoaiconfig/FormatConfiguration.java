package com.lyncode.xoai.dataprovider.xml.xoaiconfig;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XMLWritable;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;

import javax.xml.stream.XMLStreamException;

public class FormatConfiguration implements XMLWritable, Referable {
    private String prefix;
    private String xslt;
    private String namespace;
    private String schemaLocation;
    private BundleReference filter;
    private String id;

    public FormatConfiguration(String id) {
        this.id = id;
    }

    @Override
    public void write(XmlOutputContext writer) throws WritingXmlException {
        validate();
        try {
            writer.getWriter().writeStartElement("Format");
            writer.getWriter().writeAttribute("id", id);

            writer.getWriter().writeStartElement("Prefix");
            writer.getWriter().writeCharacters(prefix);
            writer.getWriter().writeEndElement();

            writer.getWriter().writeStartElement("XSLT");
            writer.getWriter().writeCharacters(xslt);
            writer.getWriter().writeEndElement();

            writer.getWriter().writeStartElement("Namespace");
            writer.getWriter().writeCharacters(namespace);
            writer.getWriter().writeEndElement();

            writer.getWriter().writeStartElement("SchemaLocation");
            writer.getWriter().writeCharacters(schemaLocation);
            writer.getWriter().writeEndElement();

            if (hasFilter()) {
                writer.getWriter().writeStartElement("Filter");
                filter.write(writer);
                writer.getWriter().writeEndElement();
            }

            writer.getWriter().writeEndElement();
        } catch (XMLStreamException e) {
            throw new WritingXmlException(e);
        }
    }

    private void validate() throws WritingXmlException {
        if (prefix == null) throw new WritingXmlException("FormatConfiguration XSLT prefix is mandatory");
        if (xslt == null) throw new WritingXmlException("FormatConfiguration XSLT field is mandatory");
        if (namespace == null) throw new WritingXmlException("FormatConfiguration XSLT namespace is mandatory");
        if (schemaLocation == null)
            throw new WritingXmlException("FormatConfiguration XSLT schemaLocation is mandatory");
    }

    public String getPrefix() {
        return prefix;
    }

    public String getXslt() {
        return xslt;
    }

    public BundleReference getFilter() {
        return filter;
    }

    public boolean hasFilter() {
        return filter != null;
    }

    public String getId() {
        return id;
    }

    public String getSchemaLocation() {
        return schemaLocation;
    }

    public String getNamespace() {
        return namespace;
    }

    public FormatConfiguration withPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public FormatConfiguration withXslt(String xslt) {
        this.xslt = xslt;
        return this;
    }

    public FormatConfiguration withNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public FormatConfiguration withSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
        return this;
    }

    public FormatConfiguration withFilter(String filterId) {
        this.filter = new BundleReference(filterId);
        return this;
    }
}
