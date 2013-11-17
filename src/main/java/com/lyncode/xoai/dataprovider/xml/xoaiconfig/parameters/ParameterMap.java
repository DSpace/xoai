package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;


public class ParameterMap extends ParameterValue<Map<String, Object>> {
    private List<ParameterValue> values;

    public List<ParameterValue> getValues() {
        return this.values;
    }

    public ParameterValue get(String key) {
        for (ParameterValue value : this.values)
            if (value.hasName() && value.getName().equals(key))
                return value;

        return null;
    }

    public ParameterMap withValues(Collection<ParameterValue> values) {
        if (this.values == null)
            this.values = new ArrayList<ParameterValue>();

        this.values.addAll(values);

        return this;
    }

    public ParameterMap withValues(ParameterValue... values) {
        if (this.values == null)
            this.values = new ArrayList<ParameterValue>();

        this.values.addAll(asList(values));

        return this;
    }

    @Override
    public void write(XmlOutputContext writer) throws WritingXmlException {
        try {
            writer.getWriter().writeStartElement("map");
            if (hasName())
                writer.getWriter().writeAttribute("name", getName());
            for (ParameterValue value : getValues())
                value.write(writer);
            writer.getWriter().writeEndElement();
        } catch (XMLStreamException e) {
            throw new WritingXmlException(e);
        }
    }
}
