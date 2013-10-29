package com.lyncode.xoai.tests.serviceprovider.oaipmh;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.MetadataFormatParser;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.MetadataFormatType;
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


public class MetadataFormatParserTest {
    static String XML = "\r\n" +
            "        <metadataFormat>\r\n" +
            "            <metadataPrefix>uketd_dc</metadataPrefix>\r\n" +
            "            <schema>http://naca.central.cranfield.ac.uk/ethos-oai/2.0/uketd_dc.xsd</schema>\r\n" +
            "            <metadataNamespace>http://naca.central.cranfield.ac.uk/ethos-oai/2.0/</metadataNamespace>\r\n" +
            "        </metadataFormat>";

    @Test
    public void testMetadataFormatParser() throws XMLStreamException, ParseException {
        XMLInputFactory factory = XMLInputFactory2.newFactory();
        XMLEventReader reader = factory.createXMLEventReader(new ByteArrayInputStream(XML.getBytes()));

        reader.nextEvent();
        reader.peek();

        OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config = Mockito.mock(OAIServiceConfiguration.class);
        MetadataFormatParser parser = new MetadataFormatParser(config);

        //System.out.println(parser.parse(reader));
        MetadataFormatType result = parser.parse(reader);


        assertEquals("uketd_dc", result.getMetadataPrefix());
    }

}
