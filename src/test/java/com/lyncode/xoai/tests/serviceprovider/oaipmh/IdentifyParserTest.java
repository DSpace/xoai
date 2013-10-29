package com.lyncode.xoai.tests.serviceprovider.oaipmh;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.IdentifyParser;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.IdentifyType;
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


public class IdentifyParserTest extends AbstractParseTest {
    static String XML = "<Identify>\r\n" +
            "        <repositoryName>DSpace Demo Repository</repositoryName>\r\n" +
            "        <baseURL>http://demo.dspace.org/oai/request</baseURL>\r\n" +
            "        <protocolVersion>2.0</protocolVersion>\r\n" +
            "        <adminEmail>dspacedemo@gmail.com</adminEmail>\r\n" +
            "        <earliestDatestamp>1650-06-26T19:58:25Z</earliestDatestamp>\r\n" +
            "        <deletedRecord>persistent</deletedRecord>\r\n" +
            "        <granularity>YYYY-MM-DDThh:mm:ssZ</granularity>\r\n" +
            "        <description>\r\n" +
            "<XOAIDescription xmlns=\"http://www.lyncode.com/XOAIConfiguration\">XOAI: OAI-PMH Java Toolkit</XOAIDescription>\r\n" +
            "</description>\r\n" +
            "    </Identify>";

    @Test
    public void testIdentify() throws XMLStreamException, ParseException {
        XMLInputFactory factory = XMLInputFactory2.newFactory();
        XMLEventReader reader = factory.createXMLEventReader(new ByteArrayInputStream(XML.getBytes()));

        reader.nextEvent();
        reader.peek();

        IdentifyParser parser = new IdentifyParser(theConfiguration());

        //System.out.println(parser.parse(reader));
        IdentifyType result = parser.parse(reader);


        assertEquals("DSpace Demo Repository", result.getRepositoryName());
    }

}
