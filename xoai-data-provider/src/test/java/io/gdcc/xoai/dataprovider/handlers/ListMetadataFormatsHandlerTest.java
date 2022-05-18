/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.handlers;

import io.gdcc.xoai.dataprovider.exceptions.IdDoesNotExistException;
import io.gdcc.xoai.dataprovider.exceptions.InternalOAIException;
import io.gdcc.xoai.dataprovider.exceptions.NoMetadataFormatsException;
import io.gdcc.xoai.dataprovider.model.MetadataFormat;
import org.junit.jupiter.api.Test;

import static io.gdcc.xoai.dataprovider.model.InMemoryItem.randomItem;
import static io.gdcc.xoai.model.oaipmh.Verb.Type.ListMetadataFormats;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ListMetadataFormatsHandlerTest extends AbstractHandlerTest {
    @Test
    public void initializationErrorIfInvalidConfiguration() {
        assertThrows(InternalOAIException.class,
            () -> new ListMetadataFormatsHandler(aContext().withoutMetadataFormats(), theRepository()));
    }

    @Test
    public void validResponseForAllMetadataFormats() throws Exception{
        aContext().withMetadataFormat("xoai", MetadataFormat.identity());
        ListMetadataFormatsHandler underTest = new ListMetadataFormatsHandler(theContext(), theRepository());
        String result = write(underTest.handle(a(request().withVerb(ListMetadataFormats))));

        assertThat(result, xPath("count(//metadataFormat)", asInteger(is(equalTo(1)))));
    }


    @Test
    public void itemNotExists() {
        aContext().withMetadataFormat("xoai", MetadataFormat.identity());
        ListMetadataFormatsHandler underTest = new ListMetadataFormatsHandler(theContext(), theRepository());
        
        assertThrows(IdDoesNotExistException.class,
            () -> underTest.handle(a(request().withVerb(ListMetadataFormats).withIdentifier("1"))));
    }

    @Test
    public void noFormatForItem() {
        theItemRepository().withItem(randomItem().withIdentifier("1"));
        aContext().withMetadataFormat("xoai", MetadataFormat.identity(), alwaysFalseCondition());
        ListMetadataFormatsHandler underTest = new ListMetadataFormatsHandler(theContext(), theRepository());
        
        assertThrows(NoMetadataFormatsException.class,
            () -> underTest.handle(a(request().withVerb(ListMetadataFormats).withIdentifier("1"))));
    }


    @Test
    public void validResponseForAnItem() throws Exception{
        theItemRepository().withItem(randomItem().withIdentifier("1"));
        aContext().withMetadataFormat("xoai", MetadataFormat.identity());
        ListMetadataFormatsHandler underTest = new ListMetadataFormatsHandler(theContext(), theRepository());
        String result = write(underTest.handle(a(request().withVerb(ListMetadataFormats).withIdentifier("1"))));

        assertThat(result, xPath("count(//metadataFormat)", asInteger(is(equalTo(1)))));
    }

}
