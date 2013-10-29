package com.lyncode.xoai.tests.dataprovider.acceptance.functional;

import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.exceptions.InvalidContextException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.services.api.DateProvider;
import com.lyncode.xoai.dataprovider.services.impl.BaseDateProvider;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Date;

import static com.lyncode.xoai.dataprovider.core.DeleteMethod.PERSISTENT;
import static com.lyncode.xoai.dataprovider.core.Granularity.Second;
import static com.lyncode.xoai.tests.SyntacticSugar.given;
import static org.hamcrest.MatcherAssert.assertThat;

public class IdentifyVerbTest extends AbstractDataProviderTest {

    private static final String REPOSITORY_NAME = "Repository";
    private static final String BASE_URL = "http://localhost:8080/xoai";
    private static final Date EARLIEST_DATE = new Date();
    private static final String ADMIN_EMAIL_1 = "jmelo@lyncode.com";
    private static final String ADMIN_EMAIL_2 = "another@email.com";
    private static final String DESCRIPTION = "Description";

    private static final DateProvider formatter = new BaseDateProvider();


    @Test
    public void identifyMustRespondToWithTheConfiguredData () throws InvalidContextException, ConfigurationException, OAIException, XMLStreamException, WritingXmlException, IOException, XPathExpressionException, ParserConfigurationException {
        given(theRepositoryIsConfiguredto())
            .resolveTheRepositoryNameTo(REPOSITORY_NAME)
            .and().resolveAllAdminEmailsTo(ADMIN_EMAIL_1, ADMIN_EMAIL_2)
            .and().resolveTheDeletedMethodTo(PERSISTENT)
            .and().resolveAllDescriptionsTo("<desc>" + DESCRIPTION + "</desc>")
            .and().resolveTheGranularityTo(Second)
            .and().resolveBaseUrlTo(BASE_URL)
            .and().resolveTheEarliestDateTo(EARLIEST_DATE);

        afterHandling(aRequest().withVerb("Identify"));

        assertThat(theResult(), hasXPath("//o:repositoryName", REPOSITORY_NAME));
        assertThat(theResult(), hasXPath("//o:granularity", Second.toGranularityType().value()));
        assertThat(theResult(), hasXPath("//o:earliestDatestamp", formatter.format(EARLIEST_DATE, Second)));
        assertThat(theResult(), hasXPath("//o:adminEmail[1]", ADMIN_EMAIL_1));
        assertThat(theResult(), hasXPath("//o:adminEmail[2]", ADMIN_EMAIL_2));
        assertThat(theResult(), hasXPath("//o:desc", DESCRIPTION));
    }

}
