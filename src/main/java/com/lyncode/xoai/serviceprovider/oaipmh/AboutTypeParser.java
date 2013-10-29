package com.lyncode.xoai.serviceprovider.oaipmh;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.parser.ConcurrentParser;
import com.lyncode.xoai.serviceprovider.parser.XMLParser;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.AboutType;

import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

public class AboutTypeParser extends ElementParser<AboutType> {
    public static final String NAME = "about";

    public AboutTypeParser(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) {
        super(oaiServiceConfiguration);
    }

    public AboutType parseElement(XMLEventReader reader) throws ParseException {
        AboutType abt = new AboutType();
        try {
            if (!reader.peek().asStartElement().getName().getLocalPart().equals(NAME))
                throw new ParseException("Expecting start of metadata element");

            reader.nextEvent();
            this.nextElement(reader);

            if (reader.peek().isStartElement()) {
                String name = reader.peek().asStartElement().getName().getLocalPart();

                XMLParser parser = this.getConfiguration().getAboutItemParser();
                if (parser == null) parser = new StringParser(getConfiguration());

                abt.setAny(ConcurrentParser.parse(parser, reader));


                this.nextElement(reader);
                if (!reader.peek().isEndElement() || !reader.peek().asEndElement().getName().getLocalPart().equals(name))
                    throw new ParseException("Expecting end of element " + name);

                reader.nextEvent();
                this.nextElement(reader);
            }

        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }
        return abt;
    }
}
