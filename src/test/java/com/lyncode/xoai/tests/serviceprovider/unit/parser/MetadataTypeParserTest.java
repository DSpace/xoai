package com.lyncode.xoai.tests.serviceprovider.unit.parser;

import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.MetadataTypeParser;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.MetadataType;
import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import static com.lyncode.xoai.tests.SyntacticSugar.given;
import static com.lyncode.xoai.tests.SyntacticSugar.with;


public class MetadataTypeParserTest extends AbstractParseTest<MetadataType> {
    static String XML = "<metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" +
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
            "</metadata>";

    @Test
    public void testRequest() throws XMLStreamException, ParseException {
        given(aXmlEventReader(with(XML)));
        inPosition();

        afterParsingTheGivenContent();
    }

    @Override
    protected MetadataType parse(XMLEventReader reader) throws ParseException {
        return new MetadataTypeParser(theConfiguration()).parse(reader);
    }
}
