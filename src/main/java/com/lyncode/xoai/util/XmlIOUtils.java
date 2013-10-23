package com.lyncode.xoai.util;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XMLWrittable;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;


public class XmlIOUtils {
    public static void writeValue(XMLStreamWriter writer, String name, String value) throws XMLStreamException {
        writer.writeStartElement(name);
        writer.writeCharacters(value);
        writer.writeEndElement();
    }

    public static void writeElement(XMLStreamWriter writer, String name, XMLWrittable elem) throws XMLStreamException, WritingXmlException {
        writer.writeStartElement(name);
        elem.write(writer);
        writer.writeEndElement();
    }
}
