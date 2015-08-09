/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider.handlers;

import org.dspace.xoai.dataprovider.exceptions.BadArgumentException;
import org.dspace.xoai.dataprovider.exceptions.DoesNotSupportSetsException;
import org.dspace.xoai.dataprovider.exceptions.NoMatchesException;
import org.junit.Test;

import static com.lyncode.test.matchers.xml.XPathMatchers.xPath;
import static org.dspace.xoai.model.oaipmh.Verb.Type.ListRecords;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListRecordsHandlerTest extends AbstractHandlerTest {
    private ListRecordsHandler underTest = new ListRecordsHandler(aContext(), theRepository());

    @Test(expected = BadArgumentException.class)
    public void missingMetadataFormat() throws Exception {
        underTest.handle(request()
                .withVerb(ListRecords));
    }

    @Test(expected = NoMatchesException.class)
    public void noMatchRecords () throws Exception {
        underTest.handle(request()
                .withVerb(ListRecords)
                .withMetadataPrefix(EXISTING_METADATA_FORMAT));
    }

    @Test(expected = DoesNotSupportSetsException.class)
    public void setRequestAndSetsNotSupported () throws Exception {
        theSetRepository().doesNotSupportSets();
        underTest.handle(request()
                .withVerb(ListRecords)
                .withMetadataPrefix(EXISTING_METADATA_FORMAT)
                .withSet("sad"));
    }

    @Test
    public void validResponse() throws Exception {
        theItemRepository().withRandomItems(10);
        String result = write(underTest.handle(request().withVerb(ListRecords).withMetadataPrefix(EXISTING_METADATA_FORMAT)));
        assertThat(result, xPath("count(//record)", asInteger(equalTo(10))));
    }
}
