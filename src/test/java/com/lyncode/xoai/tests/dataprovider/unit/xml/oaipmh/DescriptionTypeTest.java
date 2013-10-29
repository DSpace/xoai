package com.lyncode.xoai.tests.dataprovider.unit.xml.oaipmh;

import com.lyncode.xoai.tests.XmlTest;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.oaipmh.DescriptionType;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;

import static com.lyncode.xoai.tests.SyntacticSugar.to;
import static com.lyncode.xoai.tests.XPathMatchers.hasXPath;
import static org.junit.Assert.assertEquals;
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

        assertThat(theOutput(), hasXPath("/description/a", "Test"));
        assertThat(theOutput(), hasXPath("/description/a/@a", "n"));
    }

}
