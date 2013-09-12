package com.lyncode.xoai.serviceprovider.oaipmh;

import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.OAIPMHerrorType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.OAIPMHerrorcodeType;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;

public class ErrorParser extends ElementParser<OAIPMHerrorType> {
    public static final String NAME = "error";
    public static final String CODE = "code";

	public ErrorParser(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) {
		super(oaiServiceConfiguration);
	}

    @SuppressWarnings("unchecked")
    @Override
    public OAIPMHerrorType parseElement(XMLEventReader reader) throws ParseException {
        OAIPMHerrorType error = new OAIPMHerrorType();
        try {
            
            if (!reader.peek().asStartElement().getName().getLocalPart().equals(NAME))
                throw new ParseException("Expecting "+NAME+" element");
            

            Iterator<Attribute> attrs = reader.peek().asStartElement().getAttributes();
            while (attrs.hasNext()) {
                Attribute attr = attrs.next();
                if (attr.getName().getLocalPart().equals(CODE)) {
                    error.setCode(OAIPMHerrorcodeType.fromValue(attr.getValue()));
                }
            }
            
            reader.nextEvent();
            if (reader.peek().isCharacters()) {
                error.setValue(reader.peek().asCharacters().getData());
                reader.nextEvent();
                this.nextElement(reader);
            }

        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }
        return error;
    }
}
