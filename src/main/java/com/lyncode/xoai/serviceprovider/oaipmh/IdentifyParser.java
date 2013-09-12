package com.lyncode.xoai.serviceprovider.oaipmh;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.DeletedRecordType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.GranularityType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.IdentifyType;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;
import com.lyncode.xoai.util.DateUtils;

public class IdentifyParser extends ElementParser<IdentifyType> {
	public static final String NAME = "Identify";
	private DescriptionTypeParser parser;

	public IdentifyParser(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) {
		super(oaiServiceConfiguration);
		parser = new DescriptionTypeParser(oaiServiceConfiguration);
	}


    @Override
    protected IdentifyType parseElement(XMLEventReader reader) throws ParseException {
        IdentifyType type = new IdentifyType();
        
        try {
            if (!reader.peek().asStartElement().getName().getLocalPart().equals(NAME))
                throw new ParseException("Expecting "+NAME+" element");
            
            reader.nextEvent();
            type.setRepositoryName(this.getElement(reader, "repositoryName"));
            type.setBaseURL(this.getElement(reader, "baseURL"));
            type.setProtocolVersion(this.getElement(reader, "protocolVersion"));
            while(reader.peek().asStartElement().getName().getLocalPart().equals("adminEmail"))
                type.getAdminEmail().add(this.getElement(reader, "adminEmail"));
            type.setEarliestDatestamp(DateUtils.parse(this.getElement(reader, "earliestDatestamp")));
            type.setDeletedRecord(DeletedRecordType.fromValue(this.getElement(reader, "deletedRecord")));
            type.setGranularity(GranularityType.fromValue(this.getElement(reader, "granularity")));
            while (reader.peek().asStartElement().getName().getLocalPart().equals("compression"))
                type.getCompression().add(this.getElement(reader, "compression"));
            
            while (reader.peek().isStartElement() && reader.peek().asStartElement().getName().getLocalPart().equals("description"))
                type.getDescription().add(parser.parse(reader));
            
        } catch (XMLStreamException e) {
            throw new ParseException(e);
        } catch (java.text.ParseException e) {
            throw new ParseException(e);
        }
        
        
        return type;
    }
}
