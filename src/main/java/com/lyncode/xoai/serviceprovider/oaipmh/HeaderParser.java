package com.lyncode.xoai.serviceprovider.oaipmh;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.HeaderType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.StatusType;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;
import com.lyncode.xoai.util.DateUtils;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import java.util.Iterator;

public class HeaderParser extends ElementParser<HeaderType> {
    public static final String NAME = "header";
    public static final String IDENTIFIER = "identifier";
    public static final String DATESTAMP = "datestamp";
    public static final String SETSPEC = "setSpec";
    public static final String STATUS = "status";


    public HeaderParser(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) {
        super(oaiServiceConfiguration);
    }

    @SuppressWarnings("unchecked")
    @Override
    public HeaderType parseElement(XMLEventReader reader) throws ParseException {
        HeaderType result = new HeaderType();
        try {
            StartElement start = reader.peek().asStartElement();
            if (!start.getName().getLocalPart().equals(NAME))
                throw new ParseException("Expecting " + NAME + " element");

            Iterator<Attribute> attrs = start.getAttributes();
            while (attrs.hasNext()) {
                Attribute attr = attrs.next();
                if (attr.getName().getLocalPart().equals(STATUS)) {
                    result.setStatus(StatusType.fromValue(attr.getValue()));
                }
            }

            reader.nextEvent();
            this.nextElement(reader);
            result.setIdentifier(this.getElement(reader, IDENTIFIER));
            this.nextElement(reader);
            result.setDatestamp(DateUtils.parse(this.getElement(reader, DATESTAMP)));

            this.nextElement(reader);
            while (reader.peek() != null && reader.peek().isStartElement()) {
                result.getSetSpec().add(this.getElement(reader, SETSPEC));
                this.nextElement(reader);
            }

        } catch (XMLStreamException e) {
            throw new ParseException(e);
        } catch (java.text.ParseException e) {
            throw new ParseException(e);
        }

        return result;
    }
}
