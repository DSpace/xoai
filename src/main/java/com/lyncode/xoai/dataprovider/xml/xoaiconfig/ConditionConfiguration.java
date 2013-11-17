package com.lyncode.xoai.dataprovider.xml.xoaiconfig;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XMLWritable;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.RootParameterMap;

import javax.xml.stream.XMLStreamException;

public class ConditionConfiguration implements Referable, XMLWritable {
    private String clazz;
    private RootParameterMap configuration;
    private String id;

    public ConditionConfiguration(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void write(XmlOutputContext writer) throws WritingXmlException {
        if (clazz == null) throw new WritingXmlException("Filter class field is mandatory");
        try {
            writer.getWriter().writeStartElement("CustomCondition");
            writer.getWriter().writeAttribute("id", id);

            writer.getWriter().writeStartElement("Class");
            writer.getWriter().writeCharacters(clazz);
            writer.getWriter().writeEndElement();

            if (hasConfiguration()) {
                writer.getWriter().writeStartElement("Configuration");
                configuration.write(writer);
                writer.getWriter().writeEndElement();
            }

            writer.getWriter().writeEndElement();
        } catch (XMLStreamException e) {
            throw new WritingXmlException(e);
        }


    }

    public boolean hasConfiguration() {
        return configuration != null;
    }

    public RootParameterMap getConfiguration() {
        return configuration;
    }

    public ConditionConfiguration withConfiguration(RootParameterMap configuration) {
        this.configuration = configuration;
        return this;
    }

    public String getClazz() {
        return clazz;
    }

    public ConditionConfiguration withClazz(String clazz) {
        this.clazz = clazz;
        return this;
    }
}
