/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider.handlers;

import org.dspace.xoai.dataprovider.exceptions.DoesNotSupportSetsException;
import org.dspace.xoai.dataprovider.exceptions.IllegalVerbException;
import org.dspace.xoai.dataprovider.exceptions.NoMatchesException;
import org.dspace.xoai.model.oaipmh.ListSets;
import org.dspace.xoai.model.oaipmh.ResumptionToken;
import org.junit.Before;
import org.junit.Test;

import static com.lyncode.test.matchers.xml.XPathMatchers.hasXPath;
import static com.lyncode.test.matchers.xml.XPathMatchers.xPath;
import static org.dspace.xoai.dataprovider.model.Set.set;
import static org.dspace.xoai.model.oaipmh.Verb.Type.ListSets;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListSetsHandlerTest extends AbstractHandlerTest {
    protected ListSetsHandler underTest;

    @Before
    public void setup() {
        underTest = new ListSetsHandler(aContext(), theRepository());
    }

    @Test(expected = IllegalVerbException.class)
    public void setVerbExpected() throws Exception {
        underTest.handle(a(request()));
    }

    @Test(expected = NoMatchesException.class)
    public void emptyRepositoryShouldGiveNoMatches() throws Exception {
        underTest.handle(a(request().withVerb(ListSets)));
    }

    @Test(expected = DoesNotSupportSetsException.class)
    public void doesNotSupportSets() throws Exception {
        theSetRepository().doesNotSupportSets();
        underTest.handle(a(request().withVerb(ListSets)));
    }

    @Test
    public void validResponseWithOnlyOnePage() throws Exception {
        theRepositoryConfiguration().withMaxListSets(100);
        theSetRepository().withRandomSets(10);
        ListSets handle = underTest.handle(a(request().withVerb(ListSets)));
        String result = write(handle);

        assertThat(result, xPath("count(//set)", asInteger(equalTo(10))));
        assertThat(result, not(hasXPath("//resumptionToken")));
    }

    @Test
    public void showsVirtualSetsFirst () throws Exception {
        theSetRepository().withSet("set", "hello");
        theContext().withSet(set("virtual").withName("new").withCondition(alwaysFalseCondition()));

        ListSets handle = underTest.handle(a(request().withVerb(ListSets)));
        String result = write(handle);

        assertThat(result, xPath("count(//set)", asInteger(equalTo(2))));
        assertThat(result, xPath("//set[1]/setSpec", equalTo("virtual")));
        assertThat(result, xPath("//set[2]/setSpec", equalTo("hello")));
    }

    @Test
    public void firstPageOfValidResponseWithTwoPages() throws Exception {
        theRepositoryConfiguration().withMaxListSets(5);
        theSetRepository().withRandomSets(10);
        ListSets handle = underTest.handle(a(request().withVerb(ListSets)));
        String result = write(handle);

        assertThat(result, xPath("count(//set)", asInteger(equalTo(5))));
        assertThat(result, hasXPath("//resumptionToken"));
    }

    @Test
    public void lastPageOfVResponseWithTwoPages() throws Exception {
        theRepositoryConfiguration().withMaxListSets(5);
        theSetRepository().withRandomSets(10);
        ListSets handle = underTest.handle(a(request().withVerb(ListSets)
                .withResumptionToken(valueOf(new ResumptionToken.Value().withOffset(5)))));
        String result = write(handle);

        assertThat(result, xPath("count(//set)", asInteger(equalTo(5))));
        assertThat(result, xPath("//resumptionToken", is(equalTo(""))));
    }

}
