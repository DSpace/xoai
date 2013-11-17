package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;

public class ParameterList extends ParameterValue<List<Object>> {

    protected List<ParameterValue> values;

    public List<ParameterValue> getValues() {
        return this.values;
    }

    public ParameterValue get(int index) {
        return values.get(index);
    }

    public int size() {
        return values.size();
    }

    public ParameterList withValues(Collection<ParameterValue> values) {
        if (this.values == null)
            this.values = new ArrayList<ParameterValue>();

        this.values.addAll(values);

        return this;
    }

    public ParameterList withValues(ParameterValue... values) {
        if (this.values == null)
            this.values = new ArrayList<ParameterValue>();

        this.values.addAll(asList(values));

        return this;
    }

    @Override
    public void write(XmlOutputContext writer) throws WritingXmlException {
        try {
            writer.getWriter().writeStartElement("list");
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
