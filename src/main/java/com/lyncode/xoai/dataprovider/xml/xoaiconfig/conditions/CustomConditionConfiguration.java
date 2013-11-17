package com.lyncode.xoai.dataprovider.xml.xoaiconfig.conditions;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.BundleReference;

import javax.xml.stream.XMLStreamException;

public class CustomConditionConfiguration extends FilterConditionConfiguration {
    public BundleReference getFilter() {
        return filter;
    }

    private BundleReference filter;

    public CustomConditionConfiguration(String reference) {
        filter = new BundleReference(reference);
    }

    @Override
    public void write(XmlOutputContext writer) throws WritingXmlException {
        try {
            writer.getWriter().writeStartElement("CustomFilter");
            filter.write(writer);
            writer.getWriter().writeEndElement();
        } catch (XMLStreamException e) {
            throw new WritingXmlException(e);
        }
    }
}
