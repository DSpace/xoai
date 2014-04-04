package com.lyncode.xoai.dataprovider.xml;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import org.codehaus.stax2.XMLInputFactory2;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.util.Iterator;


public class EchoElement implements XMLWritable {
    private static XMLInputFactory factory = XMLInputFactory2.newFactory();
    private String xmlString = null;

    public EchoElement(String xmlString) {
        this.xmlString = xmlString;
    }

    @Override
    public void write(XmlOutputContext context) throws WritingXmlException {
        try {
            XMLEventReader reader = factory.createXMLEventReader(new ByteArrayInputStream(xmlString.getBytes()));
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                if (event.isStartElement()) {
                    QName name = event.asStartElement().getName();
                    context.getWriter().writeStartElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());

                    Iterator<Namespace> it_ns = event.asStartElement().getNamespaces();
                    while (it_ns.hasNext()) {
                        Namespace ns = it_ns.next();
                        context.getWriter().writeNamespace(ns.getPrefix(), ns.getNamespaceURI());
                    }

                    @SuppressWarnings("unchecked")
                    Iterator<Attribute> it = event.asStartElement().getAttributes();

                    while (it.hasNext()) {
                        Attribute attr = it.next();
                        QName attrName = attr.getName();
                        context.getWriter().writeAttribute(attrName.getPrefix(), attrName.getNamespaceURI(), attrName.getLocalPart(), attr.getValue());
                    }
                } else if (event.isEndElement()) {
                    context.getWriter().writeEndElement();
                } else if (event.isCharacters()) {
                    context.getWriter().writeCharacters(event.asCharacters().getData());
                }
            }
        } catch (XMLStreamException e) {
            throw new WritingXmlException("Error trying to output '"+this.xmlString+"'", e);
        }
    }

}
