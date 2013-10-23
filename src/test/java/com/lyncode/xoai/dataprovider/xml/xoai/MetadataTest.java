package com.lyncode.xoai.dataprovider.xml.xoai;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.codehaus.stax2.XMLOutputFactory2;
import org.junit.Test;

import com.lyncode.xoai.dataprovider.exceptions.WrittingXmlException;
import com.lyncode.xoai.dataprovider.xml.xoai.Element.Field;


public class MetadataTest {

    @Test
    public void test() throws XMLStreamException, WrittingXmlException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLOutputFactory fac = XMLOutputFactory2.newFactory();
        XMLStreamWriter writer = fac.createXMLStreamWriter(out);
        
        Metadata metadata = new Metadata();
        metadata.getElement().add(new Element());
        metadata.getElement().get(0).setName("Name");
        metadata.getElement().get(0).getField().add(new Field());
        metadata.getElement().get(0).getField().get(0).setValue("test");
        metadata.getElement().get(0).getField().get(0).setName("Property");
        
        metadata.write(writer);
        
        writer.flush();
        writer.close();
        
        assertTrue(out.toString().startsWith("<metadata"));
        
    }

}
