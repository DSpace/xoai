package com.lyncode.xoai.tests.dataprovider.unit.xml.xoaiconfig;

import com.lyncode.xoai.builders.XOAIDataProviderConfigurationBuilder;
import com.lyncode.xoai.builders.XOAIDataProviderContextBuilder;
import com.lyncode.xoai.builders.XOAIDataProviderFormatBuilder;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration.readConfiguration;
import static org.junit.Assert.assertEquals;

public class ConfigurationReadTest {
    private Configuration configuration;

    @Test
    public void readConfigurationFromInputStream() throws ConfigurationException {
        afterReadConfiguration(sampleConfiguration());

        assertEquals("xoai", theConfiguration().getFormats().getFormat().get(0).getId());
    }

    private Configuration theConfiguration() {
        return configuration;
    }

    private void afterReadConfiguration(InputStream intput) throws ConfigurationException {
        this.configuration = readConfiguration(intput);
    }

    private ByteArrayInputStream sampleConfiguration() {
        return new ByteArrayInputStream(getSampleConfiguration().getBytes());
    }

    private String getSampleConfiguration() {
        XOAIDataProviderConfigurationBuilder configuration = new XOAIDataProviderConfigurationBuilder().withDefaults().withIndentation(true);

        configuration.withFormats(new XOAIDataProviderFormatBuilder()
                .withId("xoai")
                .withNamespace("xoainamespace")
                .withPrefix("xoai")
                .withSchemaLocation("schemalocation")
                .withXslt("xsltLocation")
                .build());

        configuration.withContexts(new XOAIDataProviderContextBuilder()
                .withBaseUrl("xoai")
                .withFormats("xoai")
                .build());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            configuration.build().write(outputStream);
            outputStream.close();
            return outputStream.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
