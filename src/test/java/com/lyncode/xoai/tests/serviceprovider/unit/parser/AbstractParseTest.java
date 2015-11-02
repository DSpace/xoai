package com.lyncode.xoai.tests.serviceprovider.unit.parser;

import com.lyncode.xoai.dataprovider.services.impl.BaseDateProvider;
import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Before;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractParseTest<T> {
    private static final XMLInputFactory factory = XMLInputFactory2.newFactory();
    private OAIServiceConfiguration config;
    private T result;

    private XMLEventReader reader;

    @Before
    public void setUp() {
        config = mock(OAIServiceConfiguration.class);
        when(config.getFormatter()).thenReturn(new BaseDateProvider());
    }

    protected OAIServiceConfiguration theConfiguration() {
        return config;
    }

    protected XMLEventReader aXmlEventReader(String input) throws XMLStreamException {
        try
        {
            if (reader == null)
                reader = factory.createXMLEventReader(new ByteArrayInputStream(input.getBytes("UTF-8")), "UTF-8");

            return reader;
        }
        catch(UnsupportedEncodingException e)
        {
            throw new RuntimeException("Unable to convert input to UTF-8", e);
        }
    }

    protected XMLEventReader theReader() {
        return reader;
    }

    protected T theResult() {
        return this.result;
    }

    protected abstract T parse(XMLEventReader reader) throws ParseException;

    protected T afterParsingTheGivenContent() throws ParseException {
        this.result = parse(theReader());
        return this.result;
    }

    protected void inPosition() throws XMLStreamException {
        theReader().nextEvent();
        theReader().peek();
    }
}
