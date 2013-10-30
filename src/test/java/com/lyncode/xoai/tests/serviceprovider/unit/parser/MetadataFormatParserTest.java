package com.lyncode.xoai.tests.serviceprovider.unit.parser;

import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.MetadataFormatParser;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.MetadataFormatType;
import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import static com.lyncode.xoai.tests.syntax.SyntacticSugar.given;
import static com.lyncode.xoai.tests.syntax.SyntacticSugar.with;
import static org.junit.Assert.assertEquals;


public class MetadataFormatParserTest extends AbstractParseTest<MetadataFormatType> {
    static String XML = "\r\n" +
            "        <metadataFormat>\r\n" +
            "            <metadataPrefix>uketd_dc</metadataPrefix>\r\n" +
            "            <schema>http://naca.central.cranfield.ac.uk/ethos-oai/2.0/uketd_dc.xsd</schema>\r\n" +
            "            <metadataNamespace>http://naca.central.cranfield.ac.uk/ethos-oai/2.0/</metadataNamespace>\r\n" +
            "        </metadataFormat>";

    @Test
    public void testMetadataFormatParser() throws XMLStreamException, ParseException {
        given(aXmlEventReader(with(XML)));
        inPosition();

        afterParsingTheGivenContent();

        assertEquals("uketd_dc", theResult().getMetadataPrefix());
    }

    @Override
    protected MetadataFormatType parse(XMLEventReader reader) throws ParseException {
        return new MetadataFormatParser(theConfiguration()).parse(reader);
    }
}
