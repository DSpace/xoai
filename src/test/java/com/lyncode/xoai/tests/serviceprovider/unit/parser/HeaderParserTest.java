package com.lyncode.xoai.tests.serviceprovider.unit.parser;

import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.HeaderParser;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.HeaderType;
import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import static com.lyncode.xoai.tests.syntax.SyntacticSugar.given;
import static com.lyncode.xoai.tests.syntax.SyntacticSugar.with;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class HeaderParserTest extends AbstractParseTest<HeaderType> {
    static String XML = "<header>\r\n" +
            "                <identifier>oai:demo.dspace.org:10673/4</identifier>\r\n" +
            "                <datestamp>2013-09-10T20:40:03Z</datestamp>\r\n" +
            "                <withSpec>com_10673_1</withSpec>\r\n" +
            "                <withSpec>col_10673_2</withSpec>\r\n" +
            "            </header>";


    @Test
    public void testHeaderParse() throws XMLStreamException, ParseException {
        given(aXmlEventReader(with(XML)));
        inPosition();

        afterParsingTheGivenContent();
        assertEquals("oai:demo.dspace.org:10673/4", theResult().getIdentifier());
        assertTrue(theResult().getSetSpec().contains("com_10673_1"));
        assertTrue(theResult().getSetSpec().contains("col_10673_2"));
    }

    @Override
    protected HeaderType parse(XMLEventReader reader) throws ParseException {
        return new HeaderParser(theConfiguration()).parse(reader);
    }
}
