package com.lyncode.xoai.tests.serviceprovider.unit.parser;

import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.GetRecordParser;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.GetRecordType;
import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import static com.lyncode.xoai.tests.syntax.SyntacticSugar.given;
import static com.lyncode.xoai.tests.syntax.SyntacticSugar.with;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class GetRecordParserTest extends AbstractParseTest<GetRecordType> {
    static String XML = "<GetRecord>\r\n" +
            "        <record>\r\n" +
            "            <header>\r\n" +
            "                <identifier>oai:demo.dspace.org:10673/4</identifier>\r\n" +
            "                <datestamp>2013-09-10T20:40:03Z</datestamp>\r\n" +
            "                <setSpec>com_10673_1</setSpec>\r\n" +
            "                <setSpec>col_10673_2</setSpec>\r\n" +
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
            "        </record>\r\n" +
            "    </GetRecord>";


    @Test
    public void testHeaderParse() throws XMLStreamException, ParseException {
        given(aXmlEventReader(with(XML)));
        inPosition();

        afterParsingTheGivenContent();

        assertEquals("oai:demo.dspace.org:10673/4", theResult().getRecord().getHeader().getIdentifier());
        assertTrue(theResult().getRecord().getHeader().getSetSpec().contains("com_10673_1"));
        assertTrue(theResult().getRecord().getHeader().getSetSpec().contains("col_10673_2"));
    }

    @Test
    public void shouldPersistXsiXmlnsDefinition() throws XMLStreamException, ParseException {
        given(aXmlEventReader(with(XML)));
        inPosition();

        afterParsingTheGivenContent();

        assertThat(theMetadata(), containsString("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""));
    }

    private String theMetadata() {
        return (String) theResult().getRecord().getMetadata().getAny();
    }

    @Override
    protected GetRecordType parse(XMLEventReader reader) throws ParseException {
        return new GetRecordParser(theConfiguration()).parse(reader);
    }
}
