package com.lyncode.xoai.tests.dataprovider.unit.core;


import com.lyncode.builder.MapBuilder;
import com.lyncode.test.support.matchers.XPathMatchers;
import com.lyncode.xoai.builders.dataprovider.ElementBuilder;
import com.lyncode.xoai.dataprovider.data.Item;
import com.lyncode.xoai.dataprovider.data.internal.ItemHelper;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.tests.helpers.AbstractItemBuilder;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import static com.lyncode.xoai.tests.syntax.SyntacticSugar.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ItemSerializationTest {
    public static final String TEST_1 = "test1";
    public static final String TEST_2 = "test2";
    public static final String FIELD_1 = "field1";
    public static final String FIELD_2 = "field2";
    private AbstractItemBuilder abstractItem = new AbstractItemBuilder();

    @Test
    public void shouldOutputDefinedData() throws WritingXmlException, XMLStreamException, IOException {
        given(theSourceItem())
                .withMetadata(
                        anElement().withName("test1").withField(FIELD_1, TEST_1),
                        anElement().withName("test2").withField(FIELD_2, TEST_2)
                );

        ItemHelper itemHelper = new ItemHelper(theItem());

        assertThat(serializing(itemHelper), hasXPath("/o:metadata"));
        assertThat(serializing(itemHelper), xPath("//o:field[@name='" + FIELD_1 + "']", is(TEST_1)));
        assertThat(serializing(itemHelper), xPath("//o:field[@name='" + FIELD_2 + "']", is(TEST_2)));
    }

    private String serializing(ItemHelper itemHelper) throws IOException, WritingXmlException, XMLStreamException {
        return IOUtils.toString(itemHelper.toPipeline(true).getTransformed());
    }

    private AbstractItemBuilder theSourceItem() {
        return abstractItem;
    }

    private Item theItem() {
        return abstractItem.build();
    }

    private ElementBuilder anElement() {
        return new ElementBuilder();
    }

    protected Matcher<? super String> hasXPath(String s) {
        return XPathMatchers.hasXPath(s, new MapBuilder<String, String>()
                .withPair("o", "http://www.lyncode.com/xoai")
                .withPair("xsi", "http://www.w3.org/2001/XMLSchema-instance"));
    }

    protected <T> Matcher<? super String> xPath(String s, Matcher<T> val) {
        return XPathMatchers.xPath(s, val, new MapBuilder<String, String>()
                .withPair("o", "http://www.lyncode.com/xoai")
                .withPair("xsi", "http://www.w3.org/2001/XMLSchema-instance"));
    }
}
