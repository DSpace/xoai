package com.lyncode.xoai.dataprovider.xml.xoaiconfig;


import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XMLWritable;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;

import javax.xml.stream.XMLStreamException;

public class TransformerConfiguration implements Referable, XMLWritable {
    private String xslt;
    private String description;
    private String id;

    public TransformerConfiguration(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void write(XmlOutputContext writer) throws WritingXmlException {
        if (xslt == null) throw new WritingXmlException("Transformer XSLT field is mandatory");
        try {
            writer.getWriter().writeStartElement("Transformer");
            writer.getWriter().writeAttribute("id", id);

            writer.getWriter().writeStartElement("XSLT");
            writer.getWriter().writeCharacters(xslt);
            writer.getWriter().writeEndElement();

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

    private boolean hasDescription() {
        return description != null;
    }

    public String getDescription() {
        return description;
    }

    public String getXslt() {
        return xslt;
    }

    public TransformerConfiguration withDescription(String description) {
        this.description = description;
        return this;
    }

    public TransformerConfiguration withXslt(String xslt) {
        this.xslt = xslt;
        return this;
    }
}
