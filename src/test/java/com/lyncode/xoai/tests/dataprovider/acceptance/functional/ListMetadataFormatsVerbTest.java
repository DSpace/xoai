package com.lyncode.xoai.tests.dataprovider.acceptance.functional;

import com.lyncode.xoai.builders.MapBuilder;
import com.lyncode.xoai.builders.OAIRequestParametersBuilder;
import com.lyncode.xoai.dataprovider.OAIRequestParameters;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.exceptions.InvalidContextException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class ListMetadataFormatsVerbTest extends AbstractDataProviderTest {

    @Test
    public void showExistingMetadataFormats () throws WritingXmlException, OAIException, InvalidContextException, IOException, XMLStreamException, ConfigurationException {
        afterHandling(aRequest().withVerb("ListMetadataFormats"));
        assertThat(theResult(), hasXPath("//o:metadataPrefix", theFormatPrefix()));
    }
}
