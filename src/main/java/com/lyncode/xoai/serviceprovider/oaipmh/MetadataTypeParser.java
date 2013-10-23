package com.lyncode.xoai.serviceprovider.oaipmh;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.MetadataType;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.ConcurrentParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;
import com.lyncode.xoai.serviceprovider.parser.XMLParser;

public class MetadataTypeParser extends ElementParser<MetadataType> {
	private static final String NAME = "metadata";

	public MetadataTypeParser(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) {
		super(oaiServiceConfiguration);
	}

    @Override
    protected MetadataType parseElement(XMLEventReader reader) throws ParseException {
        MetadataType type = new MetadataType();
        try {
            if (!reader.peek().asStartElement().getName().getLocalPart().equals(NAME))
                throw new ParseException("Expecting start of metadata element");
            
            reader.nextEvent();
            this.nextElement(reader);
            
            if (reader.peek().isStartElement()) {
                String name = reader.peek().asStartElement().getName().getLocalPart();
                
                XMLParser parser = this.getConfiguration().getMetadataParser();
                if (parser == null) parser = new StringParser(getConfiguration());
                
                type.setAny(ConcurrentParser.parse(parser, reader));
                

                
                this.nextElement(reader);
                if (!reader.peek().isEndElement() || !reader.peek().asEndElement().getName().getLocalPart().equals(name))
                    throw new ParseException("Expecting end of element "+name);
                
                reader.nextEvent();
                this.nextElement(reader);
            }
            
        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }
        return type;
    }
}
