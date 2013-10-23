package com.lyncode.xoai.serviceprovider.oaipmh;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.ListIdentifiersType;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

public class ListIdentifiersParser extends ElementParser<ListIdentifiersType> {
    public static final String NAME = "ListIdentifiers";

    private HeaderParser parser;
    private ResumptionTokenParser resParser;

    public ListIdentifiersParser(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) {
        super(oaiServiceConfiguration);
        parser = new HeaderParser(oaiServiceConfiguration);
        resParser = new ResumptionTokenParser(oaiServiceConfiguration);
    }

    @Override
    protected ListIdentifiersType parseElement(XMLEventReader reader) throws ParseException {
        ListIdentifiersType result = new ListIdentifiersType();

        try {
            if (!reader.peek().asStartElement().getName().getLocalPart().equals(NAME))
                throw new ParseException("Expected " + NAME + " element");

            reader.nextEvent();
            this.nextElement(reader);

            while (reader.peek().asStartElement().getName().getLocalPart().equals("header")) {
                result.getHeader().add(parser.parse(reader));
            }

            result.setResumptionToken(resParser.parse(reader));

        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }

        return result;
    }
}
