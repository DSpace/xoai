package com.lyncode.xoai.serviceprovider.oaipmh;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.RecordType;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;

public class RecordParser extends ElementParser<RecordType> {
	public static final String NAME = "record";

	private HeaderParser headerParser;
	private MetadataTypeParser metadataParser;
	private AboutTypeParser aboutParser;
	
	public RecordParser(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) {
		super(oaiServiceConfiguration);
		headerParser = new HeaderParser(oaiServiceConfiguration);
		metadataParser = new MetadataTypeParser(oaiServiceConfiguration);
		aboutParser = new AboutTypeParser(oaiServiceConfiguration);
	}

    @Override
    protected RecordType parseElement(XMLEventReader reader) throws ParseException {
        RecordType record = new RecordType();
        try {
            if (!reader.peek().asStartElement().getName().getLocalPart().equals(NAME))
                throw new ParseException("Expecting record element");
            
            reader.nextEvent();
            this.nextElement(reader);
            
            record.setHeader(headerParser.parse(reader));
            record.setMetadata(metadataParser.parse(reader));
            
            while (reader.peek().isStartElement()) {
                record.getAbout().add(aboutParser.parse(reader));
            }
            
        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }
        
        return record;
    }
}
