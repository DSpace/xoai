package com.lyncode.xoai.dataprovider.xml.oaipmh;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.codehaus.stax2.XMLOutputFactory2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.exceptions.WrittingXmlException;


@PrepareForTest(XOAIManager.class)
@RunWith(PowerMockRunner.class)
public class OAIPMHTest {
    static String EXPECTED = "<OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd\" />";
    XMLOutputFactory factory2;
    
    
    @Before
    public void setUp () {
        factory2 = XMLOutputFactory2.newInstance();
    }

    @Test
    public void shouldOutputItRight() throws XMLStreamException, WrittingXmlException {
        OAIPMH info = new OAIPMH();
        
        XOAIManager manager = Mockito.mock(XOAIManager.class);
        Mockito.when(manager.hasStyleSheet()).thenReturn(false);
        PowerMockito.mockStatic(XOAIManager.class);
        
        PowerMockito.when(XOAIManager.getManager()).thenReturn(manager);
        

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLStreamWriter writter = factory2.createXMLStreamWriter(out);
        info.write(writter);
        writter.close();
        // System.out.println(out.toString());
        assertEquals(EXPECTED, out.toString());
    }

}
