package com.lyncode.xoai.dataprovider.xml.xoaiconfig.conditions;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;

import javax.xml.stream.XMLStreamException;

public abstract class BinaryConditionConfiguration extends FilterConditionConfiguration {
    private FilterConditionConfiguration left;
    private FilterConditionConfiguration right;

    public FilterConditionConfiguration getLeft() {
        return left;
    }

    public BinaryConditionConfiguration withLeft(FilterConditionConfiguration left) {
        this.left = left;
        return this;
    }


    public FilterConditionConfiguration getRight() {
        return right;
    }

    public BinaryConditionConfiguration withRight(FilterConditionConfiguration right) {
        this.right = right;
        return this;
    }

    @Override
    public void write(XmlOutputContext writer) throws WritingXmlException {
        if (this.left == null) throw new WritingXmlException("Left side of " + getName() + " is mandatory");
        if (this.right == null) throw new WritingXmlException("Right side of " + getName() + " is mandatory");
        try {
            writer.getWriter().writeStartElement(getName());

            writer.getWriter().writeStartElement("LeftCondition");
            left.write(writer);
            writer.getWriter().writeEndElement();


            writer.getWriter().writeStartElement("RightCondition");
            right.write(writer);
            writer.getWriter().writeEndElement();

            writer.getWriter().writeEndElement();
        } catch (XMLStreamException e) {
            throw new WritingXmlException(e);
        }
    }

    protected abstract String getName();
}
