package com.lyncode.xoai.serviceprovider.oaipmh;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.OAIPMHtype;
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


public class OAIPMHParserTest {
    static String XML = "<OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd\"><responseDate>2013-09-12T21:15:35Z</responseDate>\r\n" +
            "    <request verb=\"Identify\">http://demo.dspace.org/oai/request</request>\r\n" +
            "    <Identify>\r\n" +
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
            "    </Identify>\r\n" +
            "</OAI-PMH>";

    @Test
    public void test() throws XMLStreamException, ParseException {
        XMLInputFactory factory = XMLInputFactory2.newFactory();
        XMLEventReader reader = factory.createXMLEventReader(new ByteArrayInputStream(XML.getBytes()));

        reader.nextEvent();
        reader.peek();

        OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config = Mockito.mock(OAIServiceConfiguration.class);
        OAIPMHParser parser = new OAIPMHParser(config);

        //System.out.println(parser.parse(reader));
        OAIPMHtype result = parser.parse(reader);


        assertEquals("DSpace Demo Repository", result.getIdentify().getRepositoryName());
    }

}
