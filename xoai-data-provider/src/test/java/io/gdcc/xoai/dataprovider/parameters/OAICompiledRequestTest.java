/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.parameters;

import io.gdcc.xoai.dataprovider.builder.OAIRequestParametersBuilder;
import io.gdcc.xoai.dataprovider.exceptions.BadArgumentException;
import io.gdcc.xoai.dataprovider.exceptions.IllegalVerbException;
import io.gdcc.xoai.dataprovider.exceptions.UnknownParameterException;
import org.junit.jupiter.api.Test;

import static io.gdcc.xoai.model.oaipmh.Verb.Type.ListSets;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OAICompiledRequestTest {

    @Test
    public void illegalVerbMustBeThrownIfUndefined() {
        assertThrows(IllegalVerbException.class,
            () -> aRequest().with("verb", "invalid").compile());
    }

    @Test
    public void fromAndUntilWithDifferentGranularity() {
        assertThrows(BadArgumentException.class,
            () -> aRequest()
                    .withVerb(ListSets)
                    .with("from", "2000-05-30")
                    .with("until", "2001-01-20T15:00:00Z")
                    .compile());
    }

    @Test
    public void unknownParameter() {
        assertThrows(UnknownParameterException.class,
            () -> aRequest()
                    .withVerb(ListSets)
                    .with("a", "2000-05-30")
                    .compile());
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
