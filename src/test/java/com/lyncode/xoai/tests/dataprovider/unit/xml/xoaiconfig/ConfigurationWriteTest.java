package com.lyncode.xoai.tests.dataprovider.unit.xml.xoaiconfig;

import com.lyncode.xoai.builders.MapBuilder;
import com.lyncode.xoai.builders.dataprovider.ConfigurationBuilder;
import com.lyncode.xoai.builders.dataprovider.ContextBuilder;
import com.lyncode.xoai.builders.dataprovider.FormatBuilder;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration;
import com.lyncode.xoai.util.matchers.XPathMatchers;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

public class ConfigurationWriteTest {

    private String result;

    @Test
    public void shouldWriteTheCorrectXML() throws IOException, ConfigurationException {
        afterWritingA(sampleConfiguration());

        assertThat(theResult(), hasXPath("/c:Configuration/c:Contexts/c:Context"));
        assertThat(theResult(), hasXPath("/c:Configuration/c:Formats/c:Format"));
    }

    private Matcher<String> hasXPath(String s) {
        return XPathMatchers.hasXPath(s, new MapBuilder<String, String>()
                .withPair("c", "http://www.lyncode.com/XOAIConfiguration"));
    }

    private String theResult() {
        return result;
    }

    private void afterWritingA(Configuration configuration) throws ConfigurationException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        configuration.write(output);
        output.close();
        result = output.toString();
    }

    private Configuration sampleConfiguration() {
        ConfigurationBuilder configuration = new ConfigurationBuilder().withDefaults().withIndentation(true);

        configuration.withFormats(new FormatBuilder()
                .withId("xoai")
                .withNamespace("xoainamespace")
                .withPrefix("xoai")
                .withSchemaLocation("schemalocation")
                .withXslt("xsltLocation")
                .build());

        configuration.withContexts(new ContextBuilder()
                .withBaseUrl("xoai")
                .withFormats("xoai")
                .build());


        return configuration.build();
    }
}
