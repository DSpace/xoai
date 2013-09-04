package com.lyncode.xoai.dataprovider.xml.oaipmh;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.codehaus.stax2.XMLOutputFactory2;
import org.junit.Before;
import org.junit.Test;

import com.lyncode.xoai.dataprovider.exceptions.WrittingXmlException;


public class OAIPMHTest {
    static String EXPECTED = "<?xml version='1.0' encoding='UTF-8'?><OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd\" />";
    XMLOutputFactory factory2;
    
    
    @Before
    public void setUp () {
        factory2 = XMLOutputFactory2.newInstance();
    }

    @Test
    public void shouldOutputItRight() throws XMLStreamException, WrittingXmlException {
        OAIPMH info = new OAIPMH();
        

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLStreamWriter writter = factory2.createXMLStreamWriter(out);
        info.write(writter);
        writter.close();
        
        assertEquals(EXPECTED, out.toString());
    }

}
