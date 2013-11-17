package com.lyncode.xoai.dataprovider.xml.read;

import org.codehaus.stax2.XMLInputFactory2;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.Iterator;

public class XmlReader {
    private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory2.newFactory();
    private final XMLEventReader xmlEventParser;

    public XmlReader(InputStream stream) throws XMLStreamException {
        this.xmlEventParser = XML_INPUT_FACTORY.createXMLEventReader(stream);
    }

    public boolean isStart() throws XmlReaderException {
        return getPeek().isStartElement();
    }

    public boolean nextIsStart() throws XmlReaderException {
        try {
            xmlEventParser.nextEvent();
            return isStart();
        } catch (XMLStreamException e) {
            throw new XmlReaderException(e);
        }
    }

    public void proceedToTheNextStartElement() throws XmlReaderException {
        try {
            xmlEventParser.nextEvent();
            while (!getPeek().isStartElement())
                xmlEventParser.nextEvent();
        } catch (XMLStreamException e) {
            throw new XmlReaderException(e);
        }
    }

    public boolean isEnd() throws XmlReaderException {
        return getPeek().isEndElement();
    }


    public String getAttribute(String name) throws XmlReaderException {
        Iterator<Attribute> attributes = getPeek().asStartElement().getAttributes();
        while (attributes.hasNext()) {
            Attribute next = attributes.next();
            if (next.getName().getLocalPart().toLowerCase().equals(name.toLowerCase()))
                return next.getValue();
        }
        return null;
    }

    public boolean isText() throws XmlReaderException {
        return getPeek().isCharacters();
    }

    public String getText() throws XmlReaderException {
        while (!isText()) {
            try {
                xmlEventParser.nextEvent();
                String data = getPeek().asCharacters().getData();
                proceedToNextElement();
                return data;
            } catch (XMLStreamException e) {
                throw new XmlReaderException(e);
            }
        }
        return null;
    }

    public String getNextElementText(String elementName) throws XmlReaderException {
        proceedToNextElement();
        if (!isStart()) throw new XmlReaderException("Expecting a start element '" + elementName + "'");
        String text = getText();
        if (!isEnd()) throw new XmlReaderException("Expecting a end element '" + elementName + "'");
        return text;
    }

    public void proceedToNextElement() throws XmlReaderException {
        try {
            xmlEventParser.nextEvent();
            while (!isStart() && !isEnd())
                xmlEventParser.nextEvent();
        } catch (XMLStreamException e) {
            throw new XmlReaderException(e);
        }
    }

    public boolean elementNameIs(String name) throws XmlReaderException {
        return getPeek().asStartElement().getName().getLocalPart().toLowerCase().equals(name.toLowerCase());
    }

    public XMLEvent getPeek() throws XmlReaderException {
        try {
            return this.xmlEventParser.peek();
        } catch (XMLStreamException e) {
            throw new XmlReaderException(e);
        }
    }

    public String getName() throws XmlReaderException {
        if (getPeek().isStartElement())
            return getPeek().asStartElement().getName().getLocalPart();
        else if (getPeek().isEndDocument())
            return getPeek().asEndElement().getName().getLocalPart();
        else return null;
    }
}
