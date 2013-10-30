package com.lyncode.xoai.tests.dataprovider.unit.xml;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.EchoElement;
import com.lyncode.xoai.tests.dataprovider.unit.XmlTest;
import org.junit.Test;

import static com.lyncode.xoai.tests.syntax.SyntacticSugar.to;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class EchoElementTest extends XmlTest {

    @Test
    public void shouldOutputTheSameThing() throws WritingXmlException {
        givenAnEmptyStreamWriter();
        EchoElement echoElement = new EchoElement("<a />");
        echoElement.write(to(theContextWriter()));
        assertThat(theOutput(), hasXPath("/a"));
    }

    @Test
    public void shouldOutputTheSameThingWithAttributes() throws WritingXmlException {
        givenAnEmptyStreamWriter();
        EchoElement echoElement = new EchoElement("<a attribute=\"b\" />");

        echoElement.write(to(theContextWriter()));

        assertThat(theOutput(), hasXPath("/a"));
        assertThat(theOutput(), hasXPath("/a/@attribute", is("b")));
    }
}
