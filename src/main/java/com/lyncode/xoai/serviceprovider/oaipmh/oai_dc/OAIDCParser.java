package com.lyncode.xoai.serviceprovider.oaipmh.oai_dc;

import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.schemas.oai_dc.OAIDC;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class OAIDCParser implements MetadataParser {

    public OAIDCParser() {
    }

    @Override
    public Object parse(XMLEventReader reader) throws ParseException {
        boolean started = false;
        OAIDC oaidc = new OAIDC();
        XMLEventReader eventReader = reader;
        try {
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    if (event.asStartElement().getName().getLocalPart().toLowerCase().equals("dc"))
                        started = true;
                    else if (started) {
                        String name = event.asStartElement().getName().getLocalPart();
                        while (eventReader.hasNext()) {
                            XMLEvent event2 = eventReader.nextEvent();
                            if (event2.isEndElement() && event2.asEndElement().getName().getLocalPart().equals(name))
                                oaidc.add(name, "");
                            else if (event2.isCharacters())
                                oaidc.add(name, event2.asCharacters().getData());
                        }
                    } else throw new ParseException("Unexpected element found");
                } else if (event.isEndElement()) {
                    if (event.asStartElement().getName().getLocalPart().toLowerCase().equals("dc"))
                        started = false;
                }
            }

            return oaidc;
        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }
    }
}
