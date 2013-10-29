package com.lyncode.xoai.tests.dataprovider.unit.core;


import com.lyncode.xoai.builders.MapBuilder;
import com.lyncode.xoai.builders.MetadataElementBuilder;
import com.lyncode.xoai.dataprovider.data.AbstractItem;
import com.lyncode.xoai.dataprovider.data.internal.Item;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.tests.XPathMatchers;
import com.lyncode.xoai.tests.helpers.AbstractItemBuilder;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import static com.lyncode.xoai.tests.SyntacticSugar.given;
import static org.hamcrest.MatcherAssert.assertThat;

public class ItemSerializationTest {
    public static final String TEST_1 = "test1";
    public static final String TEST_2 = "test2";
    public static final String FIELD_1 = "field1";
    public static final String FIELD_2 = "field2";
    private AbstractItemBuilder abstractItem = new AbstractItemBuilder();
    
    @Test
    public void shouldOutputDefinedData () throws WritingXmlException, XMLStreamException, IOException {
        given(theSourceItem())
                .withMetadata(
                        anElement().withName("test1").withField(FIELD_1, TEST_1),
                        anElement().withName("test2").withField(FIELD_2, TEST_2)
                        );

        Item item = new Item(theItem());

        assertThat(serializing(item), hasXPath("/o:metadata"));
        assertThat(serializing(item), hasXPath("//o:field[@name='"+ FIELD_1 +"']", TEST_1));
        assertThat(serializing(item), hasXPath("//o:field[@name='"+ FIELD_2 +"']", TEST_2));
    }

    private String serializing(Item item) throws IOException, WritingXmlException, XMLStreamException {
        return IOUtils.toString(item.toPipeline(true).getTransformed());
    }

    private AbstractItemBuilder theSourceItem() {
        return abstractItem;
    }

    private AbstractItem theItem() {
        return abstractItem.build();
    }

    private MetadataElementBuilder anElement () {
        return new MetadataElementBuilder();
    }

    protected Matcher<? super String> hasXPath(String s) {
        return XPathMatchers.hasXPath(s, new MapBuilder<String, String>()
                .withPair("o", "http://www.lyncode.com/xoai")
                .withPair("xsi", "http://www.w3.org/2001/XMLSchema-instance"));
    }

    protected Matcher<? super String> hasXPath(String s, String val) {
        return XPathMatchers.hasXPath(s, val, new MapBuilder<String, String>()
                .withPair("o", "http://www.lyncode.com/xoai")
                .withPair("xsi", "http://www.w3.org/2001/XMLSchema-instance"));
    }
}
