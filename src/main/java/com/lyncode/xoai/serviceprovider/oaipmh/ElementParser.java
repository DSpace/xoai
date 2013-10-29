package com.lyncode.xoai.serviceprovider.oaipmh;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.parser.*;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;

public abstract class ElementParser<T> implements XMLParser {
    private OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration;

    public ElementParser(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) {
        this.oaiServiceConfiguration = oaiServiceConfiguration;
    }

    public OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> getConfiguration() {
        return this.oaiServiceConfiguration;
    }


    public T parse(XMLEventReader reader) throws ParseException {
        try {
            this.nextElement(reader);
            if (reader.peek() == null || !reader.peek().isStartElement())
                throw new ParseException("Expecting starting element");

            String name = reader.peek().asStartElement().getName().getLocalPart();

            T result = this.parseElement(reader);

            if (!reader.peek().isEndElement() || !reader.peek().asEndElement().getName().getLocalPart().equals(name))
                throw new ParseException("Expecting end of element " + name);

            reader.nextEvent(); // Step next
            this.nextElement(reader); // Step to next structural element

            return result;
        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }
    }

    protected abstract T parseElement(XMLEventReader reader) throws ParseException;

    protected void nextElement(XMLEventReader reader) throws XMLStreamException {
        while (reader.peek() != null && !this.isElement(reader)) {
            reader.nextEvent();
        }
    }

    private boolean isElement(XMLEventReader reader) throws XMLStreamException {
        return reader.peek().isEndDocument() || reader.peek().isStartElement() || reader.peek().isEndElement() || reader.peek().isStartDocument();
    }


    protected String getElement(XMLEventReader reader, String name) throws XMLStreamException, ParseException {
        String result = "";
        this.nextElement(reader);
        XMLEvent event = reader.peek();
        if (!event.isStartElement() || !event.asStartElement().getName().getLocalPart().equals(name))
            throw new ParseException("Expecting " + name + " element");

        reader.nextEvent();
        event = reader.peek();

        if (event.isCharacters()) {
            result = event.asCharacters().getData();
            reader.nextEvent();
            this.nextElement(reader);

            event = reader.peek();
        }

        if (!event.isEndElement() || !event.asEndElement().getName().getLocalPart().equals(name))
            throw new ParseException("Expecting end of " + name + " element");

        reader.nextEvent();
        this.nextElement(reader);

        return result;
    }
}
