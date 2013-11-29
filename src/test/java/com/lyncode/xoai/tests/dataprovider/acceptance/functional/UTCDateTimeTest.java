package com.lyncode.xoai.tests.dataprovider.acceptance.functional;

import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.exceptions.InvalidContextException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.tests.dataprovider.acceptance.AbstractDataProviderTest;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import static com.lyncode.test.support.matchers.PatternMatcher.pattern;
import static com.lyncode.xoai.dataprovider.core.Granularity.Day;
import static com.lyncode.xoai.tests.syntax.SyntacticSugar.given;
import static com.lyncode.xoai.tests.syntax.SyntacticSugar.with;
import static org.hamcrest.MatcherAssert.assertThat;

// http://www.openarchives.org/OAI/openarchivesprotocol.html#Dates
public class UTCDateTimeTest extends AbstractDataProviderTest {
    @Test
    public void responseDateShouldUseUTCWithGranularityOfSeconds() throws WritingXmlException, OAIException, InvalidContextException, IOException, XMLStreamException, ConfigurationException {
        given(theRepositoryIsConfiguredto()
                .resolveTheGranularityTo(Day));

        afterHandling(aRequest().withVerb("Wrong"));

        assertThat(theResult(), xPath("//o:responseDate", with(pattern("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z"))));
    }
}
