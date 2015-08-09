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
import static org.dspace.xoai.model.oaipmh.Verb.Type.ListIdentifiers;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListIdentifiersHandlerTest extends AbstractHandlerTest {
    private ListIdentifiersHandler underTest = new ListIdentifiersHandler(aContext(), theRepository());

    @Test(expected = BadArgumentException.class)
    public void metadataPrefixIsMandatory () throws Exception {
        underTest.handle(a(request()
                .withVerb(ListIdentifiers)));
    }

    @Test(expected = DoesNotSupportSetsException.class)
    public void doesNotSupportSets () throws Exception {
        theSetRepository().doesNotSupportSets();
        underTest.handle(a(request()
                .withVerb(ListIdentifiers)
                .withMetadataPrefix(EXISTING_METADATA_FORMAT)
                .withSet("hello")));
    }

    @Test(expected = NoMatchesException.class)
    public void responseWithoutItems () throws Exception {
        underTest.handle(a(request()
                .withVerb(ListIdentifiers)
                .withMetadataPrefix(EXISTING_METADATA_FORMAT)));
    }

    @Test
    public void responseWithItems () throws Exception {
        theItemRepository().withRandomItems(10);
        String result = write(underTest.handle(a(request().withVerb(ListIdentifiers).withMetadataPrefix(EXISTING_METADATA_FORMAT))));

        assertThat(result, xPath("count(//header)", asInteger(equalTo(10))));
    }
}
