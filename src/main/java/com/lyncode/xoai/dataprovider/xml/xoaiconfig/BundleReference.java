package com.lyncode.xoai.dataprovider.xml.xoaiconfig;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XMLWritable;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;

import javax.xml.stream.XMLStreamException;

public class BundleReference implements XMLWritable {
    protected String reference;

    public BundleReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public void write(XmlOutputContext writer) throws WritingXmlException {
        try {
            writer.getWriter().writeAttribute("ref", reference);
        } catch (XMLStreamException e) {
            throw new WritingXmlException(e);
        }
    }
}
