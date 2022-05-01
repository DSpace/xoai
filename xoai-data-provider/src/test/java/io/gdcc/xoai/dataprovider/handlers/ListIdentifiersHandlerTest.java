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

import static io.gdcc.xoai.model.oaipmh.Verb.Type.ListIdentifiers;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ListIdentifiersHandlerTest extends AbstractHandlerTest {
    private final ListIdentifiersHandler underTest = new ListIdentifiersHandler(aContext(), theRepository());

    @Test
    public void metadataPrefixIsMandatory () {
        assertThrows(BadArgumentException.class, () -> underTest.handle(a(request().withVerb(ListIdentifiers))));
    }

    @Test
    public void cannotDisseminateFormat() {
        theItemRepository().withItem(InMemoryItem.item().withDefaults().withIdentifier("1"));
        aContext().withMetadataFormat(EXISTING_METADATA_FORMAT, MetadataFormat.identity());
        assertThrows(CannotDisseminateFormatException.class,
            () -> underTest.handle(a(request().withVerb(ListIdentifiers).withMetadataPrefix("abcd"))));
    }

    @Test
    public void doesNotSupportSets() {
        theSetRepository().doesNotSupportSets();
        assertThrows(DoesNotSupportSetsException.class,
            () -> underTest.handle(a(request()
                    .withVerb(ListIdentifiers)
                    .withMetadataPrefix(EXISTING_METADATA_FORMAT)
                    .withSet("hello"))));
    }

    @Test
    public void responseWithoutItems()  {
        assertThrows(NoMatchesException.class,
            () -> underTest.handle(a(request()
                    .withVerb(ListIdentifiers)
                    .withMetadataPrefix(EXISTING_METADATA_FORMAT))));
    }

    @Test
    public void responseWithItems () throws Exception {
        theItemRepository().withRandomItems(10);
        String result = write(underTest.handle(a(request().withVerb(ListIdentifiers).withMetadataPrefix(EXISTING_METADATA_FORMAT))));

        assertThat(result, xPath("count(//header)", asInteger(equalTo(10))));
    }
}
