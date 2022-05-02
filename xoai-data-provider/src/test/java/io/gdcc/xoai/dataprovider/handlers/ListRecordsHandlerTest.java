/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.handlers;

import io.gdcc.xoai.dataprovider.model.InMemoryItem;
import io.gdcc.xoai.dataprovider.model.MetadataFormat;
import io.gdcc.xoai.dataprovider.exceptions.BadArgumentException;
import io.gdcc.xoai.dataprovider.exceptions.CannotDisseminateFormatException;
import io.gdcc.xoai.dataprovider.exceptions.DoesNotSupportSetsException;
import io.gdcc.xoai.dataprovider.exceptions.NoMatchesException;
import org.junit.jupiter.api.Test;

import static io.gdcc.xoai.model.oaipmh.Verb.Type.ListRecords;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ListRecordsHandlerTest extends AbstractHandlerTest {
    private final ListRecordsHandler underTest = new ListRecordsHandler(aContext(), theRepository());

    @Test
    public void missingMetadataFormat() {
        assertThrows(BadArgumentException.class,
            () -> underTest.handle(request().withVerb(ListRecords)));
    }

    @Test
    public void cannotDisseminateFormat() {
        theItemRepository().withItem(InMemoryItem.item().withDefaults().withIdentifier("1"));
        aContext().withMetadataFormat(EXISTING_METADATA_FORMAT, MetadataFormat.identity());
    
        assertThrows(CannotDisseminateFormatException.class,
            () -> underTest.handle(a(request().withVerb(ListRecords).withMetadataPrefix("abcd"))));
    }

    @Test
    public void noMatchRecords() {
        assertThrows(NoMatchesException.class,
            () -> underTest.handle(request()
                    .withVerb(ListRecords)
                    .withMetadataPrefix(EXISTING_METADATA_FORMAT)));
    }

    @Test
    public void setRequestAndSetsNotSupported() {
        theSetRepository().doesNotSupportSets();
        assertThrows(DoesNotSupportSetsException.class,
            () -> underTest.handle(request()
                    .withVerb(ListRecords)
                    .withMetadataPrefix(EXISTING_METADATA_FORMAT)
                    .withSet("sad")));
    }

    @Test
    public void validResponse() throws Exception {
        theItemRepository().withRandomItems(10);
        String result = write(underTest.handle(request().withVerb(ListRecords).withMetadataPrefix(EXISTING_METADATA_FORMAT)));
        assertThat(result, xPath("count(//record)", asInteger(equalTo(10))));
    }
}
