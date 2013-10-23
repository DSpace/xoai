package com.lyncode.xoai.dataprovider.xml.xoai;

import com.lyncode.xoai.dataprovider.xml.xoai.Element.Field;
import org.codehaus.stax2.XMLInputFactory2;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Stack;


public class XOAIParser {
    public static Metadata parse(InputStream st) throws XMLStreamException {
        XOAIParser parser = new XOAIParser(st);
        return parser.parse();
    }

    private static final String METADATA = "metadata";
    private static final String FIELD = "field";
    private static final String ELEMENT = "element";
    private static XMLInputFactory factory = XMLInputFactory2.newFactory();

    private XMLEventReader reader;
    private Metadata metadata;
    private Stack<Element> stack;

    public XOAIParser(InputStream stream) throws XMLStreamException {
        this.reader = factory.createXMLEventReader(stream);
        this.metadata = null;
        this.stack = new Stack<Element>();
    }

    public Metadata parse() throws XMLStreamException {
        if (metadata != null) return metadata;
        metadata = new Metadata();
        boolean started = false;
        while (this.reader.hasNext()) {
            XMLEvent event = this.reader.nextTag();
            if (event == null) return metadata;
            if (event.isStartElement() && event.asStartElement().getName().getLocalPart().toLowerCase().equals(METADATA)) {
                if (started) throw new XMLStreamException("Wrong schema");
                started = true;
            }

            if (event.isStartElement() && event.asStartElement().getName().getLocalPart().toLowerCase().equals(ELEMENT)) {
                stack.push(new Element());
                parseElement();
                metadata.getElement().add(stack.pop());
            }
        }

        return metadata;
    }

    private void parseElement() throws XMLStreamException {
        boolean end = false;
        while (!end) {
            XMLEvent event = this.reader.nextTag();
            if (event == null) return;
            if (event.isEndElement() && event.asEndElement().getName().getLocalPart().toLowerCase().equals(ELEMENT)) {
                end = true;
            }

            if (event.isStartElement() && event.asStartElement().getName().getLocalPart().toLowerCase().equals(ELEMENT)) {
                stack.push(new Element());
                stack.peek().setName(this.getName(event.asStartElement()));
                parseElement();
                Element newElem = stack.pop();
                stack.peek().getElement().add(newElem);
            }

            if (event.isStartElement() && event.asStartElement().getName().getLocalPart().toLowerCase().equals(FIELD)) {
                parseField(event);
            }
        }
    }

    private String getName(StartElement asStartElement) {
        @SuppressWarnings("unchecked")
        Iterator<Attribute> it = asStartElement.getAttributes();
        while (it.hasNext()) {
            Attribute attr = it.next();
            if (attr.getName().getLocalPart().toLowerCase().equals("name")) {
                return attr.getValue();
            }
        }
        return null;
    }

    private void parseField(XMLEvent event) throws XMLStreamException {
        Field f = new Field();
        f.setName(this.getName(event.asStartElement()));
        f.setValue(this.reader.getElementText());

        stack.peek().getField().add(f);
    }

}
