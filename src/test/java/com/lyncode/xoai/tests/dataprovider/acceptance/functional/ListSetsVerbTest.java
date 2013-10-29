package com.lyncode.xoai.tests.dataprovider.acceptance.functional;

import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.exceptions.InvalidContextException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.tests.dataprovider.acceptance.AbstractDataProviderTest;
import org.dom4j.DocumentException;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import static com.lyncode.xoai.tests.SyntacticSugar.and;
import static com.lyncode.xoai.tests.SyntacticSugar.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

public class ListSetsVerbTest extends AbstractDataProviderTest {

    private static final String NAME_1 = "NAME_1";
    private static final String SPEC_1 = "SPEC_1";
    private static final String NAME_2 = "NAME_2";
    private static final String SPEC_2 = "SPEC_2";

    @Test
    public void shouldListGivenSetList () throws WritingXmlException, OAIException, InvalidContextException, IOException, XMLStreamException, ConfigurationException {
        given(theSetRepository())
                .withRandomSets(2);

        afterHandling(aRequest().withVerb("ListSets"));

        assertThat(theResult(), xPath("count(//o:set)", is(String.valueOf(2))));
    }

    @Test
    public void shouldReturnResumptionTokenIfExceedsTheMaximumSetsPerPage () throws WritingXmlException, OAIException, InvalidContextException, IOException, XMLStreamException, ConfigurationException, DocumentException {
        given(theConfiguration().withMaxListSets(5));
        and(given(theSetRepository()))
            .withRandomSets(9);

        afterHandling(aRequest().withVerb("ListSets"));

        assertThat(theResult(), xPath("count(//o:set)", is(String.valueOf(5))));
        assertThat(theResult(), hasXPath("//o:resumptionToken"));

        String resumptionToken = getXPath("//o:resumptionToken");

        afterHandling(aRequest().withVerb("ListSets").withResumptionToken(resumptionToken));

        assertThat(theResult(), xPath("count(//o:set)", is(String.valueOf(4))));
        assertThat(theResult(), xPath("//o:resumptionToken", is(empty())));
    }

    private String empty() {
        return "";
    }

    @Test
    public void shouldReturnAnErrorIfItDoesNotSupportSets () throws WritingXmlException, OAIException, InvalidContextException, IOException, XMLStreamException, ConfigurationException {
        given(theSetRepository().doesntSupportSets());

        afterHandling(aRequest().withVerb("ListSets"));

        assertThat(theResult(), xPath("count(//o:error)", is(String.valueOf(1))));
        assertThat(theResult(), xPath("//o:error/@code", is("noSetHierarchy")));
    }

}
