package com.lyncode.xoai.tests.serviceprovider.unit.parser;

import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.RecordParser;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.RecordType;
import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import static com.lyncode.xoai.tests.syntax.SyntacticSugar.given;
import static com.lyncode.xoai.tests.syntax.SyntacticSugar.with;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;


public class RecordParserTest extends AbstractParseTest<RecordType> {
    static String XML = "<record>\r\n" +
            "            <header>\r\n" +
            "                <identifier>oai:demo.dspace.org:10673/4</identifier>\r\n" +
            "                <datestamp>2013-09-10T20:40:03Z</datestamp>\r\n" +
            "                <withSpec>com_10673_1</withSpec>\r\n" +
            "                <withSpec>col_10673_2</withSpec>\r\n" +
            "            </header>\r\n" +
            "            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" +
            "<dc:title>Test Webpage</dc:title>\r\n" +
            "<dc:subject>cat</dc:subject>\r\n" +
            "<dc:subject>calico</dc:subject>\r\n" +
            "<dc:description>This is a Sample HTML webpage including several images and styles (CSS).</dc:description>\r\n" +
            "<dc:date>1982-06-26T19:58:24Z</dc:date>\r\n" +
            "<dc:date>1982-06-26T19:58:24Z</dc:date>\r\n" +
            "<dc:date>1982-11-11</dc:date>\r\n" +
            "<dc:date>1982-11-11</dc:date>\r\n" +
            "<dc:type>text</dc:type>\r\n" +
            "<dc:identifier>123456789</dc:identifier>\r\n" +
            "<dc:identifier>http://hdl.handle.net/10673/4</dc:identifier>\r\n" +
            "<dc:rights>© EverCats.com</dc:rights>\r\n" +
            "</oai_dc:dc>\r\n" +
            "</metadata>\r\n" +
            "        </record>";

    @Test
    public void testRecordParser() throws XMLStreamException, ParseException {
        given(aXmlEventReader(with(XML)));
        inPosition();

        afterParsingTheGivenContent();

        assertEquals("oai:demo.dspace.org:10673/4", theResult().getHeader().getIdentifier());
        assertThat(theResult().getHeader().getSetSpec(), hasItem("com_10673_1"));
        assertThat(theResult().getHeader().getSetSpec(), hasItem("col_10673_2"));
    }

    @Override
    protected RecordType parse(XMLEventReader reader) throws ParseException {
        return new RecordParser(theConfiguration()).parse(reader);
    }
}
