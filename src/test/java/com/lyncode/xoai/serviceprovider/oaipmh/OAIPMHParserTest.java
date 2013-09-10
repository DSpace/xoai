package com.lyncode.xoai.serviceprovider.oaipmh;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.lyncode.xoai.serviceprovider.oaipmh.spec.OAIPMHtype;


public class OAIPMHParserTest {
    static String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!-- Testing --><?xml-stylesheet type=\"text/xsl\" href=\"static/style.xsl\"?>" +
    		"<OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd\">" +
    		"<responseDate>2013-09-10T10:29:45Z</responseDate>" +
    		"<request verb=\"ListRecords\" metadataPrefix=\"oai_dc\">http://localhost:8080/oai/driver</request>" +
    		"<ListRecords>" +
    		    "<record>" +
    		        "<header>" +
    		            "<identifier>oai:localhost:123456789/3</identifier>" +
    		            "<datestamp>2013-09-08T23:48:45Z</datestamp>" +
    		            "<setSpec>com_123456789_1</setSpec>" +
    		            "<setSpec>col_123456789_2</setSpec>" +
    		            "<setSpec>driver</setSpec>" +
    		        "</header>" +
    		        "<metadata>" +
    		            "<oai_dc:dc xmlns:dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		                "<dc:title>Test Item</dc:title>\r\n" + 
    		                "<dc:creator>Melo, João</dc:creator>\r\n" + 
    		                "<dc:date>2013-09-09</dc:date>\r\n" + 
    		                "<dc:identifier>http://hdl.handle.net/123456789/3</dc:identifier>\r\n" + 
    		            "</oai_dc:dc>\r\n" + 
    		        "</metadata>" +
    		    "</record>" +
    		"</ListRecords>" +
    		"</OAI-PMH>";

    @Test
    public void shouldParseItRight() throws FileNotFoundException, XMLStreamException, ParseException {
        OAIPMHParser parser = OAIPMHParser.newInstance(new ByteArrayInputStream(input.getBytes()), Logger.getLogger(OAIPMHParserTest.class));
        OAIPMHtype result = parser.parse();
        
        assertNotNull(result);
    }

}
