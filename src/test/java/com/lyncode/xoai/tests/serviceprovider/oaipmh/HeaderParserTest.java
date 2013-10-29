package com.lyncode.xoai.tests.serviceprovider.oaipmh;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.HeaderParser;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.HeaderType;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;
import org.mockito.Mockito;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class HeaderParserTest {
    static String XML = "<header>\r\n" +
            "                <identifier>oai:demo.dspace.org:10673/4</identifier>\r\n" +
            "                <datestamp>2013-09-10T20:40:03Z</datestamp>\r\n" +
            "                <setSpec>com_10673_1</setSpec>\r\n" +
            "                <setSpec>col_10673_2</setSpec>\r\n" +
            "            </header>";


    @Test
    public void testHeaderParse() throws XMLStreamException, ParseException {
        XMLInputFactory factory = XMLInputFactory2.newFactory();
        XMLEventReader reader = factory.createXMLEventReader(new ByteArrayInputStream(XML.getBytes()));

        reader.nextEvent();
        reader.peek();

        OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config = Mockito.mock(OAIServiceConfiguration.class);
        HeaderParser parser = new HeaderParser(config);

        //System.out.println(parser.parse(reader));
        HeaderType result = parser.parse(reader);

        assertEquals("oai:demo.dspace.org:10673/4", result.getIdentifier());
        assertTrue(result.getSetSpec().contains("com_10673_1"));
        assertTrue(result.getSetSpec().contains("col_10673_2"));
    }

}
