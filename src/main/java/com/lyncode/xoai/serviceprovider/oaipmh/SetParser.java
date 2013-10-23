package com.lyncode.xoai.serviceprovider.oaipmh;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.DescriptionType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.SetType;
import com.lyncode.xoai.serviceprovider.parser.*;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

public class SetParser extends ElementParser<SetType> {
    public static final String NAME = "set";
    public static final String SETSPEC = "setSpec";
    public static final String SETNAME = "setName";
    public static final String SETDESC = "setDescription";

    public SetParser(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) {
        super(oaiServiceConfiguration);
    }

    @Override
    public SetType parseElement(XMLEventReader reader) throws ParseException {
        SetType result = new SetType();
        try {
            StartElement start = reader.peek().asStartElement();
            if (!start.getName().getLocalPart().equals(NAME))
                throw new ParseException("Expecting " + NAME + " element");

            reader.nextEvent();
            this.nextElement(reader);
            result.setSetSpec(this.getElement(reader, SETSPEC));
            this.nextElement(reader);
            result.setSetName(this.getElement(reader, SETNAME));

            this.nextElement(reader);
            while (reader.peek().isStartElement() && reader.peek().asStartElement().getName().getLocalPart().equals(SETDESC)) {
                reader.nextEvent();
                this.nextElement(reader);

                XMLParser parser = this.getConfiguration().getAboutSetParser();
                if (parser == null) parser = new StringParser(getConfiguration());

                DescriptionType desc = new DescriptionType();
                desc.setAny(ConcurrentParser.parse(parser, reader));
                result.getSetDescription().add(desc);

                this.nextElement(reader);
                // Should be the end
                if (!reader.peek().isEndElement() || !reader.peek().asEndElement().getName().getLocalPart().equals(SETDESC))
                    throw new ParseException("Expecting end of " + SETDESC + " element");
                reader.nextEvent();
                this.nextElement(reader);
            }
        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }

        return result;
    }

}
