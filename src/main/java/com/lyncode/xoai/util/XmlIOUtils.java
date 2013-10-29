package com.lyncode.xoai.util;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XMLWritable;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;


public class XmlIOUtils {
    public static void writeValue(XMLStreamWriter writer, String name, String value) throws XMLStreamException {
        writer.writeStartElement(name);
        writer.writeCharacters(value);
        writer.writeEndElement();
    }

    public static void writeElement(XmlOutputContext context, String name, XMLWritable elem) throws XMLStreamException, WritingXmlException {
        context.getWriter().writeStartElement(name);
        elem.write(context);
        context.getWriter().writeEndElement();
    }
}
