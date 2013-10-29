package com.lyncode.xoai.tests.dataprovider.acceptance.functional;

import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.exceptions.InvalidContextException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.tests.dataprovider.acceptance.AbstractDataProviderTest;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ListMetadataFormatsVerbTest extends AbstractDataProviderTest {

    @Test
    public void showExistingMetadataFormats () throws WritingXmlException, OAIException, InvalidContextException, IOException, XMLStreamException, ConfigurationException {
        afterHandling(aRequest().withVerb("ListMetadataFormats"));
        assertThat(theResult(), xPath("//o:metadataPrefix", is(theFormatPrefix())));
    }
}
