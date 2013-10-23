package com.lyncode.xoai.serviceprovider.oaipmh;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.RequestType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.VerbType;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;
import com.lyncode.xoai.util.DateUtils;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import java.util.Iterator;

public class RequestParser extends ElementParser<RequestType> {
    public static final String NAME = "request";
    public static final String VERB = "verb";
    public static final String IDENTIFIER = "identifier";
    public static final String METADATAPREFIX = "metadataPrefix";
    public static final String FROM = "from";
    public static final String UNTIL = "until";
    public static final String SET = "set";
    public static final String RESUMPTION_TOKEN = "resumptionToken";

    public RequestParser(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) {
        super(oaiServiceConfiguration);
    }

    @SuppressWarnings("unchecked")
    @Override
    public RequestType parseElement(XMLEventReader reader) throws ParseException {
        RequestType result = new RequestType();
        try {

            if (!reader.peek().asStartElement().getName().getLocalPart().equals(NAME))
                throw new ParseException("Expecting " + NAME + " element");


            Iterator<Attribute> attrs = reader.peek().asStartElement().getAttributes();
            while (attrs.hasNext()) {
                Attribute attr = attrs.next();
                if (attr.getName().getLocalPart().equals(VERB)) {
                    result.setVerb(VerbType.fromValue(attr.getValue()));
                } else if (attr.getName().getLocalPart().equals(IDENTIFIER)) {
                    result.setIdentifier(attr.getValue());
                } else if (attr.getName().getLocalPart().equals(METADATAPREFIX)) {
                    result.setMetadataPrefix(attr.getValue());
                } else if (attr.getName().getLocalPart().equals(FROM)) {
                    try {
                        result.setFrom(DateUtils.parse(attr.getValue()));
                    } catch (java.text.ParseException e) {
                        // Unable to parse date! 
                        // FIXME: Must do something? or not?
                    }
                } else if (attr.getName().getLocalPart().equals(UNTIL)) {
                    try {
                        result.setUntil(DateUtils.parse(attr.getValue()));
                    } catch (java.text.ParseException e) {
                        // FIXME: Must do something? or not?
                    }
                } else if (attr.getName().getLocalPart().equals(SET)) {
                    result.setSet(attr.getValue());
                } else if (attr.getName().getLocalPart().equals(RESUMPTION_TOKEN)) {
                    result.setResumptionToken(attr.getValue());
                }
            }

            reader.nextEvent();
            if (reader.peek().isCharacters()) {
                result.setValue(reader.peek().asCharacters().getData());
                reader.nextEvent();
                this.nextElement(reader);
            }

        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }
        return result;
    }
}
