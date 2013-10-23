package com.lyncode.xoai.serviceprovider.oaipmh;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.DescriptionType;
import com.lyncode.xoai.serviceprovider.parser.*;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

public class DescriptionTypeParser extends ElementParser<DescriptionType> {
    private static final String NAME = "description";

    public DescriptionTypeParser(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) {
        super(oaiServiceConfiguration);
    }

    @Override
    protected DescriptionType parseElement(XMLEventReader reader) throws ParseException {
        DescriptionType type = new DescriptionType();
        try {
            if (!reader.peek().asStartElement().getName().getLocalPart().equals(NAME))
                throw new ParseException("Expecting start of metadata element");

            reader.nextEvent();
            this.nextElement(reader);

            if (reader.peek().isStartElement()) {
                String name = reader.peek().asStartElement().getName().getLocalPart();

                XMLParser parser = this.getConfiguration().getDescriptionParser();
                if (parser == null) parser = new StringParser(getConfiguration());

                type.setAny(ConcurrentParser.parse(parser, reader));


                this.nextElement(reader);
                if (!reader.peek().isEndElement() || !reader.peek().asEndElement().getName().getLocalPart().equals(name))
                    throw new ParseException("Expecting end of element " + name);

                reader.nextEvent();
                this.nextElement(reader);
            }

        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }
        return type;
    }
}
