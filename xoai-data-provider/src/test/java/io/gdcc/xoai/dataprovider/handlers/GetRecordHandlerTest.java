/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.handlers;

import io.gdcc.xoai.dataprovider.exceptions.BadArgumentException;
import io.gdcc.xoai.dataprovider.exceptions.CannotDisseminateFormatException;
import io.gdcc.xoai.dataprovider.exceptions.IdDoesNotExistException;
import io.gdcc.xoai.dataprovider.model.InMemoryItem;
import io.gdcc.xoai.dataprovider.model.MetadataFormat;
import io.gdcc.xoai.model.oaipmh.GetRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.gdcc.xoai.model.oaipmh.Verb.Type.GetRecord;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetRecordHandlerTest extends AbstractHandlerTest {
    private GetRecordHandler underTest;

    @BeforeEach
    public void setup () {
        underTest = new GetRecordHandler(aContext(), theRepository());
    }

    @Test
    public void getRecordRequiresMetadataPrefixParameter() {
        assertThrows(BadArgumentException.class,
            () -> underTest.handle(a(request().withVerb(GetRecord).withIdentifier("a"))));
    }

    @Test
    public void getRecordRequiresIdentifierParameter() {
        assertThrows(BadArgumentException.class,
            () -> underTest.handle(a(request().withVerb(GetRecord).withMetadataPrefix("a"))));
    }

    @Test
    public void idDoesNotExists() {
        assertThrows(IdDoesNotExistException.class,
            () -> underTest.handle(a(request().withVerb(GetRecord).withMetadataPrefix("xoai").withIdentifier("1"))));
    }

    @Test
    public void cannotDisseminateFormat() {
        theItemRepository().withItem(InMemoryItem.item().withDefaults().withIdentifier("1"));
        aContext().withMetadataFormat("xoai", MetadataFormat.identity());
    
        assertThrows(CannotDisseminateFormatException.class,
            () -> underTest.handle(a(request().withVerb(GetRecord).withMetadataPrefix("abcd").withIdentifier("1"))));
    }

    @Test
    public void validResponse() throws Exception {
        theItemRepository().withItem(InMemoryItem.item().withDefaults().withIdentifier("1"));
        aContext().withMetadataFormat("xoai", MetadataFormat.identity());
        GetRecord handle = underTest.handle(a(request().withVerb(GetRecord)
                .withMetadataPrefix("xoai")
                .withIdentifier("1")));

        String result = write(handle);

        assertThat(result, xPath("//header/identifier", is(equalTo("1"))));
    }
}
