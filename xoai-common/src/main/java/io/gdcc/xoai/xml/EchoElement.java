/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.xml;

import io.gdcc.xoai.xmlio.XmlReader;
import io.gdcc.xoai.xmlio.exceptions.XmlReaderException;
import io.gdcc.xoai.xmlio.exceptions.XmlWriteException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EchoElement implements XmlWritable {
    private final Deque<Set<String>> declaredPrefixes = new ArrayDeque<>();
    private final String xmlString;
    private final InputStream xmlInputStream;

    public EchoElement(final String xmlString) {
        this.xmlString = xmlString;
        this.xmlInputStream = null;
    }
    
    public EchoElement(final InputStream xmlInputStream) {
        this.xmlInputStream = xmlInputStream;
        this.xmlString = null;
    }
    
    @Override
    public void write(final XmlWriter writer) throws XmlWriteException {
        if (xmlInputStream != null) {
            write(writer, xmlInputStream);
        } else if (xmlString != null) {
            write(writer, new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8)));
        } else {
            throw new XmlWriteException("Cannot write XML when none given (both stream and string null)");
        }
    }
    
    private void write(final XmlWriter writer, final InputStream inStream) throws XmlWriteException {
        try (
            inStream;
            XmlReader reader = new XmlReader(inStream)
        ){
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                if (event.isStartElement()) {
                    declaredPrefixes.push(new HashSet<>());

                    QName name = event.asStartElement().getName();
                    writer.writeStartElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());
                    addNamespaceIfRequired(writer, name);

                    // Copy any other namespace declarations
                    Iterator<Namespace> itNamespaces = event.asStartElement().getNamespaces();
                    while (itNamespaces.hasNext()) {
                        Namespace namespace = itNamespaces.next();
                        addNamespaceIfRequired(writer, new QName(namespace.getNamespaceURI(), "", namespace.getPrefix()));
                    }

                    // Copy attributes
                    Iterator<Attribute> itAttributes = event.asStartElement().getAttributes();
                    while (itAttributes.hasNext()) {
                        Attribute attr = itAttributes.next();
                        QName attrName = attr.getName();
                        addNamespaceIfRequired(writer, attrName);
                        writer.writeAttribute(attrName.getPrefix(), attrName.getNamespaceURI(), attrName.getLocalPart(), attr.getValue());
                    }
                } else if (event.isEndElement()) {
                    declaredPrefixes.pop();
                    writer.writeEndElement();
                } else if (event.isCharacters()) {
                    writer.writeCharacters(event.asCharacters().getData());
                }
            }
        } catch (XMLStreamException | XmlReaderException | IOException e) {
            throw new XmlWriteException("Error trying to write XML", e);
        }
    }

    private void addNamespaceIfRequired(XmlWriter writer, QName name) throws XMLStreamException {
        // Search for namespace in scope, starting from the root.
        for (Set<String> ancestorNamespaces : declaredPrefixes) {
            if (ancestorNamespaces.contains(name.getPrefix() + name.getNamespaceURI())) { // Prefixes might be reused.
                return;
            }
        }

        writer.writeNamespace(name.getPrefix(), name.getNamespaceURI());
        declaredPrefixes.peek().add(name.getPrefix() + name.getNamespaceURI());
    }
}
