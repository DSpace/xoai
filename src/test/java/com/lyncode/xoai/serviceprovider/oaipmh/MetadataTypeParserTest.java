package com.lyncode.xoai.serviceprovider.oaipmh;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;
import org.mockito.Mockito;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.MetadataType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.RequestType;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;


public class MetadataTypeParserTest {
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
        XMLInputFactory factory = XMLInputFactory2.newFactory();
        XMLEventReader reader = factory.createXMLEventReader(new ByteArrayInputStream(XML.getBytes()));

        reader.nextEvent();
        reader.peek();
        
        OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config = Mockito.mock(OAIServiceConfiguration.class);
        MetadataTypeParser parser = new MetadataTypeParser(config);
        
        //System.out.println(parser.parse(reader));
        MetadataType result = parser.parse(reader);
        

        System.out.println(result.getAny());
    }

}
