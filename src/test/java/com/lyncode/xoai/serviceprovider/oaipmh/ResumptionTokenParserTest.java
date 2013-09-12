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
import com.lyncode.xoai.serviceprovider.oaipmh.spec.ResumptionTokenType;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;


public class ResumptionTokenParserTest {
    static String XML = "<resumptionToken completeListSize=\"16816\" cursor=\"0\">MToxMDB8Mjp8Mzp8NDp8NTpvYWlfZGM=</resumptionToken>";

    @Test
    public void testResumptionToken() throws XMLStreamException, ParseException {
        XMLInputFactory factory = XMLInputFactory2.newFactory();
        XMLEventReader reader = factory.createXMLEventReader(new ByteArrayInputStream(XML.getBytes()));

        reader.nextEvent();
        reader.peek();
        
        OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config = Mockito.mock(OAIServiceConfiguration.class);
        ResumptionTokenParser parser = new ResumptionTokenParser(config);
        
        //System.out.println(parser.parse(reader));
        ResumptionTokenType result = parser.parse(reader);
        
        assertEquals((long) 16816, result.getCompleteListSize());
        assertEquals((long) 0, result.getCursor());
    }

}
