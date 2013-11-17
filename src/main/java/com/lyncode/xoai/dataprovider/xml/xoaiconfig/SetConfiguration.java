package com.lyncode.xoai.dataprovider.xml.xoaiconfig;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XMLWritable;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;

import javax.xml.stream.XMLStreamException;

public class SetConfiguration implements Referable, XMLWritable {
    private String spec;
    private String name;
    private BundleReference filter;
    private String id;

    public SetConfiguration(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void write(XmlOutputContext writer) throws WritingXmlException {
        if (name == null) throw new WritingXmlException("Set name is mandatory");
        if (spec == null) throw new WritingXmlException("Set spec is mandatory");
        try {
            writer.getWriter().writeStartElement("Set");
            writer.getWriter().writeAttribute("id", id);

            writer.getWriter().writeStartElement("Spec");
            writer.getWriter().writeCharacters(spec);
            writer.getWriter().writeEndElement();

            writer.getWriter().writeStartElement("Name");
            writer.getWriter().writeCharacters(name);
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

    public boolean hasFilter() {
        return filter != null;
    }

    public String getSpec() {
        return spec;
    }

    public SetConfiguration withSpec(String spec) {
        this.spec = spec;
        return this;
    }

    public String getName() {
        return name;
    }

    public SetConfiguration withName(String name) {
        this.name = name;
        return this;
    }

    public BundleReference getFilter() {
        return filter;
    }

    public SetConfiguration withFilter(String filterId) {
        this.filter = new BundleReference(filterId);
        return this;
    }
}
