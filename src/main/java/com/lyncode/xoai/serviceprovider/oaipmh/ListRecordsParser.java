package com.lyncode.xoai.serviceprovider.oaipmh;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.ListRecordsType;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

public class ListRecordsParser extends ElementParser<ListRecordsType> {
    public static final String NAME = "ListRecords";
    private ResumptionTokenParser resumptionTokenParser;
    private RecordParser recordParser;

    public ListRecordsParser(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) {
        super(oaiServiceConfiguration);
        resumptionTokenParser = new ResumptionTokenParser(oaiServiceConfiguration);
        recordParser = new RecordParser(oaiServiceConfiguration);
    }

    @Override
    protected ListRecordsType parseElement(XMLEventReader reader) throws ParseException {
        ListRecordsType result = new ListRecordsType();

        try {
            if (!reader.peek().asStartElement().getName().getLocalPart().equals(NAME))
                throw new ParseException("Expected " + NAME + " element");

            reader.nextEvent();
            this.nextElement(reader);

            while (reader.peek().asStartElement().getName().getLocalPart().equals("record")) {
                result.getRecord().add(recordParser.parse(reader));
            }

            result.setResumptionToken(resumptionTokenParser.parse(reader));

        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }

        return result;
    }
}
