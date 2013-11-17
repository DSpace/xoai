package com.lyncode.xoai.dataprovider.xml.xoaiconfig;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XMLWritable;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.conditions.FilterConditionConfiguration;

import javax.xml.stream.XMLStreamException;

public class FilterConfiguration implements Referable, XMLWritable {

    private FilterConditionConfiguration definition;
    private String id;

    public FilterConfiguration(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }


    @Override
    public void write(XmlOutputContext writer) throws WritingXmlException {
        if (definition == null) throw new WritingXmlException("Filter condition field is mandatory");
        try {
            writer.getWriter().writeStartElement("Filter");
            writer.getWriter().writeAttribute("id", id);

            writer.getWriter().writeStartElement("Definition");
            definition.write(writer);
            writer.getWriter().writeEndElement();

            writer.getWriter().writeEndElement();

        } catch (XMLStreamException e) {
            throw new WritingXmlException(e);
        }
    }

    public FilterConditionConfiguration getDefinition() {
        return definition;
    }

    public FilterConfiguration withDefinition(FilterConditionConfiguration definition) {
        this.definition = definition;
        return this;
    }
}