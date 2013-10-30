package com.lyncode.xoai.tests.dataprovider.unit.xml.oaipmh;

import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.oaipmh.OAIPMH;
import com.lyncode.xoai.tests.dataprovider.unit.XmlTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static com.lyncode.xoai.tests.syntax.SyntacticSugar.to;
import static com.lyncode.xoai.tests.syntax.SyntacticSugar.when;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


public class OAIPMHTest extends XmlTest {
    private static final String ANY_CHILD = "/OAI-PMH/*";
    private static final String ROOT_ELEMENT = "/";


    private XOAIManager manager = mock(XOAIManager.class);
    private OAIPMH oaipmh = new OAIPMH(manager);

    @Before
    public void setUp() {
        Mockito.when(manager.hasStyleSheet()).thenReturn(false);
    }

    @Test
    public void shouldOutputAndEmptyOaiPmhXml() throws WritingXmlException {
        givenAnEmptyStreamWriter();
        when(oaipmh).write(to(theContextWriter()));
        assertThat(theOutput(), hasXPath(ROOT_ELEMENT));
        assertThat(theOutput(), not(hasXPath(ANY_CHILD)));
    }

}
