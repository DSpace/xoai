/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider;

import com.lyncode.test.matchers.xml.XPathMatchers;
import com.lyncode.xml.exceptions.XmlWriteException;
import org.dspace.xoai.dataprovider.handlers.AbstractHandlerTest;
import org.dspace.xoai.model.oaipmh.ResumptionToken;
import org.dspace.xoai.xml.XmlWritable;
import org.dspace.xoai.xml.XmlWriter;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;

import static org.dspace.xoai.model.oaipmh.Verb.Type.ListRecords;
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
