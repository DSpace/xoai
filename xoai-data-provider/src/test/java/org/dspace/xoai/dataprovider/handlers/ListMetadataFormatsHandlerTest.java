/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider.handlers;

import org.dspace.xoai.dataprovider.exceptions.IdDoesNotExistException;
import org.dspace.xoai.dataprovider.exceptions.InternalOAIException;
import org.dspace.xoai.dataprovider.exceptions.NoMetadataFormatsException;
import org.junit.Test;

import static com.lyncode.test.matchers.xml.XPathMatchers.xPath;
import static org.dspace.xoai.dataprovider.model.InMemoryItem.item;
import static org.dspace.xoai.dataprovider.model.MetadataFormat.identity;
import static org.dspace.xoai.model.oaipmh.Verb.Type.ListMetadataFormats;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListMetadataFormatsHandlerTest extends AbstractHandlerTest {
    @Test(expected = InternalOAIException.class)
    public void initializationErrorIfInvalidConfiguration() throws Exception {
        new ListMetadataFormatsHandler(aContext().withoutMetadataFormats(), theRepository());
    }

    @Test
    public void validResponseForAllMetadataFormats () throws Exception{
        aContext().withMetadataFormat("xoai", identity());
        ListMetadataFormatsHandler underTest = new ListMetadataFormatsHandler(theContext(), theRepository());
        String result = write(underTest.handle(a(request().withVerb(ListMetadataFormats))));

        assertThat(result, xPath("count(//metadataFormat)", asInteger(is(equalTo(1)))));
    }


    @Test(expected = IdDoesNotExistException.class)
    public void itemNotExists () throws Exception{
        aContext().withMetadataFormat("xoai", identity());
        ListMetadataFormatsHandler underTest = new ListMetadataFormatsHandler(theContext(), theRepository());
        underTest.handle(a(request().withVerb(ListMetadataFormats).withIdentifier("1")));
    }

    @Test(expected = NoMetadataFormatsException.class)
    public void noFormatForItem () throws Exception{
        theItemRepository().withItem(item().withDefaults().withIdentifier("1"));
        aContext().withMetadataFormat("xoai", identity(), alwaysFalseCondition());
        ListMetadataFormatsHandler underTest = new ListMetadataFormatsHandler(theContext(), theRepository());
        underTest.handle(a(request().withVerb(ListMetadataFormats).withIdentifier("1")));
    }


    @Test
    public void validResponseForAnItem () throws Exception{
        theItemRepository().withItem(item().withDefaults().withIdentifier("1"));
        aContext().withMetadataFormat("xoai", identity());
        ListMetadataFormatsHandler underTest = new ListMetadataFormatsHandler(theContext(), theRepository());
        String result = write(underTest.handle(a(request().withVerb(ListMetadataFormats).withIdentifier("1"))));

        assertThat(result, xPath("count(//metadataFormat)", asInteger(is(equalTo(1)))));
    }

}
