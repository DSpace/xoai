/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider;

import io.gdcc.xoai.dataprovider.handlers.AbstractHandlerTest;
import io.gdcc.xoai.model.oaipmh.ResumptionToken;
import io.gdcc.xoai.xml.XmlWritable;
import io.gdcc.xoai.xml.XmlWriter;
import io.gdcc.xoai.xmlio.exceptions.XmlWriteException;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.xmlunit.matchers.EvaluateXPathMatcher;
import org.xmlunit.matchers.HasXPathMatcher;

import javax.xml.stream.XMLStreamException;
import java.util.Map;

import static io.gdcc.xoai.model.oaipmh.Verb.Type.ListRecords;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DataProviderTest extends AbstractHandlerTest {
    private static final String OAI_NAMESPACE = "http://www.openarchives.org/OAI/2.0/";
    private final DataProvider dataProvider = new DataProvider(aContext(), theRepository());

    @Test
    public void missingMetadataFormat() throws Exception {
        String result = write(dataProvider.handle(request().withVerb(ListRecords)));
        
        assertThat(result, xPath("//oai:error/@code", equalTo("badArgument")));
    }

    @Test
    public void noMatchRecords() throws Exception {
        String result = write(dataProvider.handle(request()
                .withVerb(ListRecords)
                .withMetadataPrefix(EXISTING_METADATA_FORMAT)));
        
        assertThat(result, xPath("//oai:error/@code", equalTo("noRecordsMatch")));
    }

    @Test
    public void oneRecordMatch() throws Exception {
        theItemRepository().withRandomItems(1);
        String result = write(dataProvider.handle(request()
                .withVerb(ListRecords)
                .withMetadataPrefix(EXISTING_METADATA_FORMAT)));
        
        assertThat(result, xPath("count(//oai:record)", asInteger(equalTo(1))));
    }

    @Test
    public void incompleteResponseFirstPage () throws Exception {
        theItemRepository().withRandomItems(10);
        theRepositoryConfiguration().withMaxListRecords(5);
        String result = write(dataProvider.handle(request()
                .withVerb(ListRecords)
                .withMetadataPrefix(EXISTING_METADATA_FORMAT)));
    
        assertThat(result, xPath("count(//oai:record)", asInteger(equalTo(5))));
        assertThat(result, hasXPath("//oai:resumptionToken"));
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
        
        assertThat(result, xPath("count(//oai:record)", equalTo("5")));
        assertThat(result, xPath("//oai:resumptionToken", equalTo("")));
    }
    
    protected static Matcher<? super String> xPath(String xPath, Matcher<String> valueMatcher) {
        return EvaluateXPathMatcher.hasXPath(xPath, valueMatcher).withNamespaceContext(Map.of("oai", OAI_NAMESPACE));
    }
    
    protected static Matcher<? super String> hasXPath(String xPath) {
        return HasXPathMatcher.hasXPath(xPath).withNamespaceContext(Map.of("oai", OAI_NAMESPACE));
    }

    @Override
    protected String write(final XmlWritable handle) throws XMLStreamException, XmlWriteException {
        return XmlWriter.toString(writer -> writer.write(handle));
    }
}
