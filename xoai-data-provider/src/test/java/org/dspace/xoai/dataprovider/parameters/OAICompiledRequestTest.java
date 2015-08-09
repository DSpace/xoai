/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider.parameters;

import org.dspace.xoai.dataprovider.builder.OAIRequestParametersBuilder;
import org.dspace.xoai.dataprovider.exceptions.BadArgumentException;
import org.dspace.xoai.dataprovider.exceptions.IllegalVerbException;
import org.dspace.xoai.dataprovider.exceptions.UnknownParameterException;
import org.junit.Test;

import static org.dspace.xoai.model.oaipmh.Verb.Type.ListSets;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class OAICompiledRequestTest {

    @Test(expected = IllegalVerbException.class)
    public void illegalVerbMustBeThrownIfUndefined() throws Exception {
        aRequest().with("verb", "invalid").compile();
    }

    @Test(expected = BadArgumentException.class)
    public void fromAndUntilWithDifferentGranularity() throws Exception {
        aRequest()
                .withVerb(ListSets)
                .with("from", "2000-05-30")
                .with("until", "2001-01-20T15:00:00Z")
                .compile();
    }

    @Test(expected = UnknownParameterException.class)
    public void unknownParameter() throws Exception {
        aRequest()
                .withVerb(ListSets)
                .with("a", "2000-05-30")
                .compile();
    }

    @Test
    public void resumptionTokenEmptyByDefault () throws Exception{
        assertThat(aRequest().withVerb(ListSets)
                .compile()
                .getResumptionToken()
                .isEmpty(), is(true));
    }

    private OAIRequestParametersBuilder aRequest() {
        return new OAIRequestParametersBuilder();
    }
}
