/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     client://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.xoai.dataprovider;

import com.lyncode.test.matchers.xml.XPathMatchers;
import com.lyncode.xml.exceptions.XmlWriteException;
import com.lyncode.xoai.dataprovider.handlers.AbstractHandlerTest;
import com.lyncode.xoai.model.oaipmh.ResumptionToken;
import com.lyncode.xoai.xml.XmlWritable;
import com.lyncode.xoai.xml.XmlWriter;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;

import static com.lyncode.test.matchers.xml.XPathMatchers.hasXPath;
import static com.lyncode.xoai.model.oaipmh.Verb.Type.ListRecords;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DataProviderTest extends AbstractHandlerTest {
    private static final String OAI_NAMESPACE = "http://www.openarchives.org/OAI/2.0/";
    private DataProvider dataProvider = new DataProvider(aContext(), theRepository());

    @Test
    public void missingMetadataFormat() throws Exception {
        String result = write(dataProvider.handle(request().withVerb(ListRecords)));
        assertThat(result, xPath("//error/@code", equalTo("badArgument")));
    }

    @Test
    public void noMatchRecords() throws Exception {
        String result = write(dataProvider.handle(request()
                .withVerb(ListRecords)
                .withMetadataPrefix(EXISTING_METADATA_FORMAT)));
        assertThat(result, xPath("//error/@code", equalTo("noRecordsMatch")));
    }

    @Test
    public void oneRecordMatch() throws Exception {
        theItemRepository().withRandomItems(1);
        String result = write(dataProvider.handle(request()
                .withVerb(ListRecords)
                .withMetadataPrefix(EXISTING_METADATA_FORMAT)));
        assertThat(result, xPath("count(//record)", asInteger(equalTo(1))));
    }

    @Test
    public void incompleteResponseFirstPage () throws Exception {
        theItemRepository().withRandomItems(10);
        theRepositoryConfiguration().withMaxListRecords(5);
        String result = write(dataProvider.handle(request()
                .withVerb(ListRecords)
                .withMetadataPrefix(EXISTING_METADATA_FORMAT)));

        assertThat(result, xPath("count(//record)", asInteger(equalTo(5))));
        assertThat(result, hasXPath("//resumptionToken"));
    }

    @Test
    public void incompleteResponseLastPage () throws Exception {
        theItemRepository().withRandomItems(10);
        theRepositoryConfiguration().withMaxListRecords(5);
        String result = write(dataProvider.handle(request()
                .withVerb(ListRecords)
                .withResumptionToken(valueOf(new ResumptionToken.Value()
                        .withMetadataPrefix(EXISTING_METADATA_FORMAT)
                        .withOffset(5)))));

        assertThat(result, xPath("count(//record)", asInteger(equalTo(5))));
        assertThat(result, xPath("//resumptionToken", equalTo("")));
    }

    private Matcher<String> hasXPath(String xpath) {
        return XPathMatchers.hasXPath(xpath, OAI_NAMESPACE);
    }

    private Matcher<String> xPath (String xpath, Matcher<String> valueMatcher) {
        return XPathMatchers.xPath(xpath, valueMatcher, OAI_NAMESPACE);
    }

    @Override
    protected String write(final XmlWritable handle) throws XMLStreamException, XmlWriteException {
        return XmlWriter.toString(new XmlWritable() {
            @Override
            public void write(XmlWriter writer) throws XmlWriteException {
                writer.write(handle);
            }
        });
    }
}
