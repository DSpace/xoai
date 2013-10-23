package com.lyncode.xoai.serviceprovider.oaipmh;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;
import org.codehaus.stax2.XMLOutputFactory2;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;

public class StringParser extends ElementParser<String> {
    private static XMLOutputFactory factory = XMLOutputFactory2.newFactory();

    public StringParser(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) {
        super(oaiServiceConfiguration);
    }

    @SuppressWarnings("unchecked")
    public String parseElement(XMLEventReader reader) throws ParseException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int count = 0;
        try {
            XMLStreamWriter writer = factory.createXMLStreamWriter(output);
            ;
            //writer.writeStartDocument();
            while (reader.peek() != null) {
                XMLEvent event = reader.peek();
                if (event.isStartElement()) {
                    count++;
                    StartElement start = event.asStartElement();
                    writer.writeStartElement(start.getName().getPrefix(), start.getName().getLocalPart(), start.getName().getNamespaceURI());

                    Iterator<Namespace> it = start.getNamespaces();
                    while (it.hasNext()) {
                        Namespace n = it.next();
                        writer.writeNamespace(n.getPrefix(), n.getNamespaceURI());
                    }

                    Iterator<Attribute> attrs = start.getAttributes();
                    while (attrs.hasNext()) {
                        Attribute attr = attrs.next();
                        writer.writeAttribute(attr.getName().getPrefix(), attr.getName().getNamespaceURI(), attr.getName().getLocalPart(), attr.getValue());
                    }

                } else if (event.isEndElement()) {
                    count--;
                    if (count == 0) {
                        break;
                    } else writer.writeEndElement();
                } else if (event.isCharacters()) {
                    writer.writeCharacters(event.asCharacters().getData());
                }

                if (reader.hasNext())
                    reader.nextEvent();
                else
                    break;
            }

            if (count > 0) throw new ParseException("Unterminated structure");

            writer.flush();
            writer.close();
            String res = output.toString();
            // System.out.println(res);
            return res;
        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }
    }
}