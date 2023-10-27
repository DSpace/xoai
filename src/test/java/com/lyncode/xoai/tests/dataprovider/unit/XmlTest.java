package com.lyncode.xoai.tests.dataprovider.unit;

import com.lyncode.builder.MapBuilder;
import com.lyncode.test.matchers.xml.XPathMatchers;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;
import org.hamcrest.Matcher;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;

public abstract class XmlTest {
    private static TransformerFactory tFactory = TransformerFactory.newInstance();

    private ByteArrayOutputStream output;
    private XmlOutputContext context;

    protected String theOutput() {
        try {
            context.getWriter().close();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return output.toString();
    }

    protected Templates identityTemplate() {
        try {
            return tFactory.newTemplates(new StreamSource(
                    this.getClass().getClassLoader().getResourceAsStream("identity_transform.xsl")));
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    protected XMLStreamWriter theWriter() {
        return context.getWriter();
    }


    protected XmlOutputContext theContextWriter() {
        return context;
    }

    protected void givenAnEmptyStreamWriter() {
        try {
            output = new ByteArrayOutputStream();
            context = XmlOutputContext.emptyContext(output);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    protected Matcher<? super String> hasXPath(String s) {
        return XPathMatchers.hasXPath(s, new MapBuilder<String, String>()
                .withPair("xoai", "http://www.lyncode.com/xoai")
                .withPair("xsi", "http://www.w3.org/2001/XMLSchema-instance"));
    }

    protected <T> Matcher<? super String> hasXPath(String s, Matcher<T> val) {
        return XPathMatchers.xPath(s, val, new MapBuilder<String, String>()
                .withPair("xoai", "http://www.lyncode.com/xoai")
                .withPair("xsi", "http://www.w3.org/2001/XMLSchema-instance"));
    }
}
