package com.lyncode.xoai.tests.dataprovider.acceptance.functional;

import com.lyncode.xoai.builders.DateBuilder;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.exceptions.InvalidContextException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.tests.dataprovider.acceptance.AbstractDataProviderTest;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Date;

import static com.lyncode.xoai.dataprovider.core.Granularity.Second;
import static com.lyncode.xoai.tests.SyntacticSugar.and;
import static com.lyncode.xoai.tests.SyntacticSugar.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class BasicErrorScenariosTest extends AbstractDataProviderTest {

    private static final Date DATE_IN_PAST = new DateBuilder().subtractDays(1).build();
    private static final Date NOW = new Date();

    @Test
    public void shouldNotAllowTwoVerbs () throws WritingXmlException, OAIException, InvalidContextException, IOException, XMLStreamException, ConfigurationException {
        afterHandling(aRequest().with("verb", "one", and("two")));

        assertThat(theResult(), xPath("//o:error/@code", is("badVerb")));
    }

    @Test
    public void shouldNotAllowWrongVerbs () throws WritingXmlException, OAIException, InvalidContextException, IOException, XMLStreamException, ConfigurationException {
        afterHandling(aRequest().withVerb("wrong"));

        assertThat(theResult(), xPath("//o:error/@code", is("badVerb")));
    }

    @Test
    public void shouldNotAllowNoVerbs () throws WritingXmlException, OAIException, InvalidContextException, IOException, XMLStreamException, ConfigurationException {
        afterHandling(aRequest().withVerb(missing()));

        assertThat(theResult(), xPath("//o:error/@code", is("badVerb")));
    }

    @Test
    public void shouldNotAllowMalformedFromDate () throws WritingXmlException, OAIException, InvalidContextException, IOException, XMLStreamException, ConfigurationException {
        afterHandling(aRequest().withVerb("ListRecords").with("from", "unExpectedFormat"));

        assertThat(theResult(), xPath("//o:error/@code", is("badArgument")));
        assertThat(theResult(), xPath("//o:error", is("The from parameter given is not valid")));
    }

    @Test
    public void shouldNotAllowMalformedUntilDate () throws WritingXmlException, OAIException, InvalidContextException, IOException, XMLStreamException, ConfigurationException {
        afterHandling(aRequest().withVerb("ListRecords").with("until", "unExpectedFormat"));

        assertThat(theResult(), xPath("//o:error/@code", is("badArgument")));
        assertThat(theResult(), xPath("//o:error", is("The until parameter given is not valid")));
    }

    @Test
    public void shouldNotAllowUntilDatesBeforeFromDates () throws WritingXmlException, OAIException, InvalidContextException, IOException, XMLStreamException, ConfigurationException {
        given(theRepositoryIsConfiguredto())
                .resolveTheGranularityTo(Second);

        afterHandling(aRequest().withVerb("ListRecords")
                .withMetadataPrefix("xoai")
                .withFrom(NOW)
                .withUntil(DATE_IN_PAST));

        assertThat(theResult(), xPath("//o:error/@code", is("badArgument")));
        assertThat(theResult(), xPath("//o:error", is("The 'from' date must be less then the 'until' one")));
    }
}
