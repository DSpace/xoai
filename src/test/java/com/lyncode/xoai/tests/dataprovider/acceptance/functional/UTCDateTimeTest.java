package com.lyncode.xoai.tests.dataprovider.acceptance.functional;

import com.lyncode.xoai.dataprovider.core.Granularity;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.exceptions.InvalidContextException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.tests.dataprovider.acceptance.AbstractDataProviderTest;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import static com.lyncode.xoai.tests.SyntacticSugar.given;

// http://www.openarchives.org/OAI/openarchivesprotocol.html#Dates
public class UTCDateTimeTest extends AbstractDataProviderTest {
    @Test
    public void responseDateShouldUseUTCWithFranularityOfSecondsEvenIfTheRepositoryIsConfiguredToUseDayGranularity () throws WritingXmlException, OAIException, InvalidContextException, IOException, XMLStreamException, ConfigurationException {
        given(theRepositoryIsConfiguredto().resolveTheGranularityTo(Granularity.Day));

        afterHandling(aRequest().withVerb("Wrong"));

//        assertThat();
    }
}
