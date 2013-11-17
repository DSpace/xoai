package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;

import javax.xml.stream.XMLStreamException;

public abstract class SimpleType<T> extends ParameterValue<T> {
    private T value;

    public T getValue() {
        return value;
    }

    public SimpleType withValue(T value) {
        this.value = value;
        return this;
    }

    public boolean is(Class<?> clazz) {
        return (this.getClass().isAssignableFrom(clazz));
    }

    @Override
    public void write(XmlOutputContext writer) throws WritingXmlException {
        try {
            writer.getWriter().writeStartElement(getXmlName());
            if (hasName())
                writer.getWriter().writeAttribute("name", getName());
            writer.getWriter().writeCharacters(String.valueOf(value));
            writer.getWriter().writeEndElement();
        } catch (XMLStreamException e) {
            throw new WritingXmlException(e);
        }
    }


    public int asInteger() {
        return (Integer) getValue();
    }

    public String asString() {
        return (String) getValue();
    }

    public Double asDouble() {
        return (Double) getValue();
    }

    public Float asFloat() {
        return (Float) getValue();
    }

    public Boolean asBoolean() {
        return (Boolean) getValue();
    }

    protected abstract String getXmlName();
}
