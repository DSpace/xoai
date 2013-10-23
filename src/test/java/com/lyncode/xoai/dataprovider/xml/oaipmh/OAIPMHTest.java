package com.lyncode.xoai.dataprovider.xml.oaipmh;

import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import org.codehaus.stax2.XMLOutputFactory2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;


@PrepareForTest(XOAIManager.class)
@RunWith(PowerMockRunner.class)
public class OAIPMHTest {
    static String EXPECTED = "<OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd\" />";
    XMLOutputFactory factory2;


    @Before
    public void setUp() {
        factory2 = XMLOutputFactory2.newInstance();
    }

    @Test
    public void shouldOutputItRight() throws XMLStreamException, WritingXmlException {
        XOAIManager manager = Mockito.mock(XOAIManager.class);
        OAIPMH info = new OAIPMH(manager);
        Mockito.when(manager.hasStyleSheet()).thenReturn(false);
        PowerMockito.mockStatic(XOAIManager.class);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLStreamWriter writter = factory2.createXMLStreamWriter(out);
        info.write(writter);
        writter.close();
        // System.out.println(out.toString());
        assertEquals(EXPECTED, out.toString());
    }

}
