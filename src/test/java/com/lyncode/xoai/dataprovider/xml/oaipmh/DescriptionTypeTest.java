package com.lyncode.xoai.dataprovider.xml.oaipmh;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.codehaus.stax2.XMLOutputFactory2;
import org.junit.Before;
import org.junit.Test;

import com.lyncode.xoai.dataprovider.exceptions.WrittingXmlException;



public class DescriptionTypeTest {
    static String XML = "<a a='n'>Test</a>";
    static String RESULT_OUTPUT_XML = "<?xml version='1.0' encoding='UTF-8'?><description><a a=\"n\">Test</a></description>";
    
    XMLOutputFactory factory2;
    
    
    @Before
    public void setUp () {
        factory2 = XMLOutputFactory2.newInstance();
    }

    @Test
    public void shouldWriteXML() throws XMLStreamException, WrittingXmlException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLStreamWriter writter = factory2.createXMLStreamWriter(out);
        
        DescriptionType type = new DescriptionType();
        type.setAny(XML);
        
        writter.writeStartDocument();
        writter.writeStartElement("description");
        type.write(writter);
        writter.writeEndElement();
        writter.writeEndDocument();
        
        writter.close();
        
        assertEquals(RESULT_OUTPUT_XML, out.toString());
    }

}
