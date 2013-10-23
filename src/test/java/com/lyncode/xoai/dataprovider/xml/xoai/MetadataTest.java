package com.lyncode.xoai.dataprovider.xml.xoai;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.xoai.Element.Field;
import org.codehaus.stax2.XMLOutputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertTrue;


public class MetadataTest {

    @Test
    public void test() throws XMLStreamException, WritingXmlException {
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
