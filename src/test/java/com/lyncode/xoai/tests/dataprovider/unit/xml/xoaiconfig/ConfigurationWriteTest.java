package com.lyncode.xoai.tests.dataprovider.unit.xml.xoaiconfig;

import com.lyncode.test.support.matchers.XPathMatchers;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.ContextConfiguration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.FormatConfiguration;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

public class ConfigurationWriteTest {

    private String result;

    @Test
    public void shouldWriteTheCorrectXML() throws IOException, ConfigurationException, WritingXmlException, XMLStreamException {
        afterWritingA(sampleConfiguration());

        assertThat(theResult(), hasXPath("/c:Configuration/c:Contexts/c:Context"));
        assertThat(theResult(), hasXPath("/c:Configuration/c:Formats/c:Format"));
    }

    private Matcher<String> hasXPath(String s) {
        return XPathMatchers.hasXPath(s, "http://www.lyncode.com/XOAIConfiguration");
    }

    private String theResult() {
        return result;
    }

    private void afterWritingA(Configuration configuration) throws ConfigurationException, IOException, WritingXmlException, XMLStreamException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        configuration.write(output);
        output.close();
        result = output.toString();
    }

    private Configuration sampleConfiguration() {
        Configuration configuration = new Configuration().withIndented(true);

        configuration.withFormatConfigurations(new FormatConfiguration("xoai")
                .withNamespace("xoainamespace")
                .withPrefix("xoai")
                .withSchemaLocation("schemalocation")
                .withXslt("xsltLocation"));

        configuration.withContextConfigurations(new ContextConfiguration("xoai")
                .withFormats("xoai"));


        return configuration;
    }
}
