package com.lyncode.xoai.tests.dataprovider.unit.xml;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.EchoElement;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;
import com.lyncode.xoai.tests.XmlTest;
import org.junit.Test;

import static com.lyncode.xoai.tests.SyntacticSugar.to;
import static com.lyncode.xoai.tests.XPathMatchers.hasXPath;
import static org.hamcrest.MatcherAssert.assertThat;

public class EchoElementTest extends XmlTest {

    @Test
    public void shouldOutputTheSameThing () throws WritingXmlException {
        givenAnEmptyStreamWriter();
        EchoElement echoElement = new EchoElement("<a />");
        echoElement.write(to(theContextWriter()));
        assertThat(theOutput(), hasXPath("/a"));
    }

    @Test
    public void shouldOutputTheSameThingWithAttributes () throws WritingXmlException {
        givenAnEmptyStreamWriter();
        EchoElement echoElement = new EchoElement("<a attribute=\"b\" />");

        echoElement.write(to(theContextWriter()));

        assertThat(theOutput(), hasXPath("/a"));
        assertThat(theOutput(), hasXPath("/a/@attribute", "b"));
    }
}
