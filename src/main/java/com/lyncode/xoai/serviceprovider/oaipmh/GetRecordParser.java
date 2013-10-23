package com.lyncode.xoai.serviceprovider.oaipmh;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.GetRecordType;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

public class GetRecordParser extends ElementParser<GetRecordType> {
    public static final String NAME = "GetRecord";

    private RecordParser recordParser;

    public GetRecordParser(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) {
        super(oaiServiceConfiguration);
        recordParser = new RecordParser(oaiServiceConfiguration);
    }

    @Override
    protected GetRecordType parseElement(XMLEventReader reader) throws ParseException {
        GetRecordType result = new GetRecordType();

        try {
            if (!reader.peek().asStartElement().getName().getLocalPart().equals(NAME))
                throw new ParseException("Expected " + NAME + " element");

            reader.nextEvent();
            this.nextElement(reader);
            result.setRecord(recordParser.parse(reader));


        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }
        return result;
    }

}
