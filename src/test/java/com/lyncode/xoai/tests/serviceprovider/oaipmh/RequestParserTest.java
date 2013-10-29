package com.lyncode.xoai.tests.serviceprovider.oaipmh;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.RequestParser;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.RequestType;
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


public class RequestParserTest {
    static String XML = "<request verb=\"ListRecords\" metadataPrefix=\"oai_dc\">http://demo.dspace.org/oai/request</request>";

    @Test
    public void testRequest() throws XMLStreamException, ParseException {
        XMLInputFactory factory = XMLInputFactory2.newFactory();
        XMLEventReader reader = factory.createXMLEventReader(new ByteArrayInputStream(XML.getBytes()));

        reader.nextEvent();
        reader.peek();

        OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config = Mockito.mock(OAIServiceConfiguration.class);
        RequestParser parser = new RequestParser(config);

        //System.out.println(parser.parse(reader));
        RequestType result = parser.parse(reader);


        assertEquals("ListRecords", result.getVerb().value());
        assertEquals("oai_dc", result.getMetadataPrefix());
        assertEquals("http://demo.dspace.org/oai/request", result.getValue());
    }

}
