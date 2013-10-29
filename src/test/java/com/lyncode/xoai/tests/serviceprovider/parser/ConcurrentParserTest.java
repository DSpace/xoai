package com.lyncode.xoai.tests.serviceprovider.parser;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.StringParser;
import com.lyncode.xoai.serviceprovider.parser.*;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;
import org.mockito.Mockito;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertTrue;


public class ConcurrentParserTest {
    static String XML = "<OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd\"><responseDate>2013-09-11T16:03:46Z</responseDate>\r\n" +
            "    <request verb=\"GetRecord\" identifier=\"oai:demo.dspace.org:10673/4\" metadataPrefix=\"qdc\">http://demo.dspace.org/oai/request</request>\r\n" +
            "    <GetRecord>\r\n" +
            "        <record>\r\n" +
            "            <header>\r\n" +
            "                <identifier>oai:demo.dspace.org:10673/4</identifier>\r\n" +
            "                <datestamp>2013-09-10T20:40:03Z</datestamp>\r\n" +
            "                <setSpec>com_10673_1</setSpec>\r\n" +
            "                <setSpec>col_10673_2</setSpec>\r\n" +
            "            </header>\r\n" +
            "            <metadata></metadata>\r\n" +
            "        </record>\r\n" +
            "    </GetRecord>\r\n" +
            "</OAI-PMH>";

    @Test
    public void testSimpleConcurrentParsing() throws XMLStreamException, ParseException {
        XMLInputFactory factory = XMLInputFactory2.newFactory();
        XMLEventReader reader = factory.createXMLEventReader(new ByteArrayInputStream(XML.getBytes()));

        OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config = Mockito.mock(OAIServiceConfiguration.class);
        StringParser parser = new StringParser(config);

        reader.nextEvent();
        XMLEvent e = reader.peek();

        Object result = ConcurrentParser.parse(parser, reader);

        assertTrue((result instanceof String));
    }

}
