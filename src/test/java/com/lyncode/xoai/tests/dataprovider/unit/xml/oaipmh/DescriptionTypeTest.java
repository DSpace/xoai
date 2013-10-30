package com.lyncode.xoai.tests.dataprovider.unit.xml.oaipmh;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.oaipmh.DescriptionType;
import com.lyncode.xoai.tests.dataprovider.unit.XmlTest;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;

import static com.lyncode.xoai.tests.syntax.SyntacticSugar.to;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class DescriptionTypeTest extends XmlTest {
    private static String XML = "<a a=\"n\">Test</a>";

    @Test
    public void shouldWriteXML() throws XMLStreamException, WritingXmlException {
        givenAnEmptyStreamWriter();

        DescriptionType type = new DescriptionType();
        type.setAny(XML);

        theWriter().writeStartElement("description");
        type.write(to(theContextWriter()));

        assertThat(theOutput(), hasXPath("/description/a", is("Test")));
        assertThat(theOutput(), hasXPath("/description/a/@a", is("n")));
    }

}
