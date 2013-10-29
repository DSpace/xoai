package com.lyncode.xoai.tests.dataprovider.unit.xml.xoai;

import com.lyncode.xoai.tests.XPathMatchers;
import com.lyncode.xoai.tests.XmlTest;
import com.lyncode.xoai.builders.MetadataBuilder;
import com.lyncode.xoai.builders.MetadataElementBuilder;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.xoai.Metadata;
import com.lyncode.xoai.tests.SyntacticSugar;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;

import static com.lyncode.xoai.tests.SyntacticSugar.to;
import static com.lyncode.xoai.tests.XPathMatchers.hasXPath;
import static org.junit.Assert.assertThat;


public class MetadataTest extends XmlTest {
    private static final String ELEMENT_NAME = "Name";
    public static final String FIELD_NAME = "Property";

    @Test
    public void shouldOutputTheGivenMetadata() throws XMLStreamException, WritingXmlException {
        givenAnEmptyStreamWriter();

        Metadata metadata = new MetadataBuilder()
                .withElement(new MetadataElementBuilder()
                        .withName(ELEMENT_NAME).withField(FIELD_NAME, "value").build())
                .build();

        metadata.write(to(theContextWriter()));

        assertThat(theOutput(), hasXPath("//element[@name='" + ELEMENT_NAME + "']/field[@name='" + FIELD_NAME + "']"));

    }



}
