package com.lyncode.xoai.serviceprovider.oaipmh;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.ResumptionTokenType;
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

public class ResumptionTokenParser extends ElementParser<ResumptionTokenType> {
    public static final String NAME = "resumptionToken";
    public static final String EXPIRATION_DATE = "expirationDate";
    public static final String COMPLETE_LIST_SIZE = "completeListSize";
    public static final String CURSOR = "cursor";


    public ResumptionTokenParser(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) {
        super(oaiServiceConfiguration);
    }

    @SuppressWarnings("unchecked")
    public ResumptionTokenType parseElement(XMLEventReader reader) throws ParseException {
        ResumptionTokenType res = new ResumptionTokenType();
        try {
            StartElement start = reader.peek().asStartElement();
            if (!start.getName().getLocalPart().equals(NAME))
                throw new ParseException("Expecting resumption token element");

            Iterator<Attribute> attrs = start.getAttributes();
            while (attrs.hasNext()) {
                Attribute attr = attrs.next();
                if (attr.getName().getLocalPart().equals(EXPIRATION_DATE)) {
                    res.setExpirationDate(DateUtils.parse(attr.getValue()));
                } else if (attr.getName().getLocalPart().equals(COMPLETE_LIST_SIZE)) {
                    res.setCompleteListSize(Long.parseLong(attr.getValue()));
                } else if (attr.getName().getLocalPart().equals(CURSOR)) {
                    res.setCursor(Long.parseLong(attr.getValue()));
                }
            }

            reader.nextEvent();
            if (reader.peek().isCharacters()) {
                res.setValue(reader.peek().asCharacters().getData());
                reader.nextEvent();
                this.nextElement(reader);
            }

        } catch (XMLStreamException e) {
            throw new ParseException(e);
        } catch (java.text.ParseException e) {
            throw new ParseException(e);
        }

        return res;
    }
}
