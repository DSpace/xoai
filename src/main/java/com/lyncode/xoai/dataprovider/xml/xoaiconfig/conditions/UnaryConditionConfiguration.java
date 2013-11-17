package com.lyncode.xoai.dataprovider.xml.xoaiconfig.conditions;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;

import javax.xml.stream.XMLStreamException;

public abstract class UnaryConditionConfiguration extends FilterConditionConfiguration {
    private FilterConditionConfiguration condition;

    public FilterConditionConfiguration getCondition() {
        return condition;
    }

    public UnaryConditionConfiguration withCondition(FilterConditionConfiguration condition) {
        this.condition = condition;
        return this;
    }

    @Override
    public void write(XmlOutputContext writer) throws WritingXmlException {
        if (this.condition == null) throw new WritingXmlException("Condition of " + getName() + " is mandatory");
        try {
            writer.getWriter().writeStartElement(getName());

            writer.getWriter().writeStartElement("Condition");
            condition.write(writer);
            writer.getWriter().writeEndElement();

            writer.getWriter().writeEndElement();
        } catch (XMLStreamException e) {
            throw new WritingXmlException(e);
        }
    }

    protected abstract String getName();
}
