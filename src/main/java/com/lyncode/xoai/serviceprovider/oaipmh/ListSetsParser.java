package com.lyncode.xoai.serviceprovider.oaipmh;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.ListSetsType;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;

public class ListSetsParser extends ElementParser<ListSetsType> {
	public static final String NAME = "ListSets";

	private SetParser setParser;
	private ResumptionTokenParser resParser;
	
	public ListSetsParser(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) {
		super(oaiServiceConfiguration);
		
		setParser = new SetParser(oaiServiceConfiguration);
		resParser = new ResumptionTokenParser(oaiServiceConfiguration);
	}

    @Override
    protected ListSetsType parseElement(XMLEventReader reader) throws ParseException {
        ListSetsType result = new ListSetsType();
        
        try {
            if (!reader.peek().asStartElement().getName().getLocalPart().equals(NAME))
                throw new ParseException("Expected "+NAME+" element");
            
            reader.nextEvent();
            this.nextElement(reader);
            
            while (reader.peek().asStartElement().getName().getLocalPart().equals("set")) {
                result.getSet().add(setParser.parse(reader));
            }
            
            result.setResumptionToken(resParser.parse(reader));
            
        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }
        
        return result;
    }
}
