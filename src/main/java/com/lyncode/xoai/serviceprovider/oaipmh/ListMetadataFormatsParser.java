package com.lyncode.xoai.serviceprovider.oaipmh;

import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.ListMetadataFormatsType;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

public class ListMetadataFormatsParser extends ElementParser<ListMetadataFormatsType> {
    public static final String NAME = "ListMetadataFormats";

    private MetadataFormatParser mdfParser;

    public ListMetadataFormatsParser(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) {
        super(oaiServiceConfiguration);
        mdfParser = new MetadataFormatParser(oaiServiceConfiguration);
    }

    @Override
    protected ListMetadataFormatsType parseElement(XMLEventReader reader) throws ParseException {
        ListMetadataFormatsType result = new ListMetadataFormatsType();


        try {
            if (!reader.peek().asStartElement().getName().getLocalPart().equals(NAME))
                throw new ParseException("Expected " + NAME + " element");

            reader.nextEvent();
            this.nextElement(reader);

            while (reader.peek().isStartElement() && reader.peek().asStartElement().getName().getLocalPart().equals("metadataFormat")) {
                result.getMetadataFormat().add(mdfParser.parse(reader));
            }

        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }

        return result;
    }
}
