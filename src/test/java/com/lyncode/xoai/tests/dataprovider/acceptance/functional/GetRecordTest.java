package com.lyncode.xoai.tests.dataprovider.acceptance.functional;

import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.exceptions.InvalidContextException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.tests.dataprovider.stubs.StubbedItem;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import static com.lyncode.xoai.tests.SyntacticSugar.given;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetRecordTest extends AbstractDataProviderTest {

    @Test
    public void shouldReturnIdDoesNotExistIfNotFound () throws WritingXmlException, OAIException, InvalidContextException, IOException, XMLStreamException, ConfigurationException {
        given(theItemRepository().withNoItems());

        afterHandling(aRequest().withVerb("GetRecord").withIdentifier("one").withMetadataPrefix(theFormatPrefix()));

        assertThat(theResult(), hasXPath("//o:error/@code", "idDoesNotExist"));
    }


    @Test
    public void shouldReturnGivenItem () throws WritingXmlException, OAIException, InvalidContextException, IOException, XMLStreamException, ConfigurationException {
        given(theItemRepository().withItem(anItem()
                .with("identifier", "12345")
                .with("deleted", false))
                .withRandomItems(20));

        afterHandling(aRequest().withVerb("GetRecord").withIdentifier("12345").withMetadataPrefix(theFormatPrefix()));

        assertThat(theResult(), hasXPath("//o:record/o:header/o:identifier", "12345"));
        assertThat(theResult(), hasXPath("//o:record/o:metadata"));
    }

    private StubbedItem anItem() {
        return new StubbedItem();
    }
}
