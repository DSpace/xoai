package com.lyncode.xoai.serviceprovider.oaipmh;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.MetadataFormatType;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

public class MetadataFormatParser extends ElementParser<MetadataFormatType> {
    public static final String NAME = "metadataFormat";
    public static final String metadataPrefix = "metadataPrefix";
    public static final String schema = "schema";
    public static final String metadataNamespace = "metadataNamespace";

    public MetadataFormatParser(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) {
        super(oaiServiceConfiguration);
    }

    @Override
    protected MetadataFormatType parseElement(XMLEventReader reader) throws ParseException {
        MetadataFormatType result = new MetadataFormatType();
        try {
            if (!reader.peek().asStartElement().getName().getLocalPart().equals(NAME))
                throw new ParseException("Expected metadataFormat");

            reader.nextEvent();
            this.nextElement(reader);
            result.setMetadataPrefix(this.getElement(reader, metadataPrefix));
            this.nextElement(reader);
            result.setSchema(this.getElement(reader, schema));
            this.nextElement(reader);
            result.setMetadataNamespace(this.getElement(reader, metadataNamespace));

        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }
        return result;
    }
}
