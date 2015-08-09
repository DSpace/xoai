/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider.handlers.helpers;

import org.dspace.xoai.model.oaipmh.ResumptionToken;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotEquals;

public class ResumptionTokenHelperTest {
    private static final long MAX_PER_PAGE = 100;
    private static final long TOTAL_RESULTS = 1000;
    private static final boolean HAS_MORE_RESULTS = true;
    private static final boolean NOT_HAS_MORE_RESULTS = false;

    @Test
    public void shouldReturnSecondPageIfResumptionTokenValueIsEmpty() throws Exception {
        ResumptionToken.Value value = new ResumptionToken.Value();
        assertThat(value.isEmpty(), is(true));

        ResumptionTokenHelper underTest = new ResumptionTokenHelper(value, MAX_PER_PAGE)
                .withTotalResults(TOTAL_RESULTS);

        ResumptionToken result = underTest.resolve(HAS_MORE_RESULTS);
        assertThat(result.getCompleteListSize(), is(TOTAL_RESULTS));
        assertThat(result.getValue().getOffset(), is(MAX_PER_PAGE));
    }


    @Test
    public void onlyOffsetShouldChangeBetweenConsecutiveIterations() throws Exception {
        ResumptionToken.Value value = new ResumptionToken.Value()
                .withMetadataPrefix("test").withSetSpec("set");
        ResumptionTokenHelper underTest = new ResumptionTokenHelper(value, MAX_PER_PAGE)
                .withTotalResults(TOTAL_RESULTS);

        ResumptionToken.Value result = underTest.resolve(HAS_MORE_RESULTS).getValue();

        assertNotEquals(value.getOffset(), result.getOffset());
        assertEquals(value.getFrom(), result.getFrom());
        assertEquals(value.getUntil(), result.getUntil());
        assertEquals(value.getMetadataPrefix(), result.getMetadataPrefix());
        assertEquals(value.getSetSpec(), result.getSetSpec());
    }

    @Test
    public void shouldReturnEmptyValueIfNoMoreResultsAndWithResumptionToken() throws Exception {
        ResumptionToken.Value value = new ResumptionToken.Value().next(100);
        assertThat(value.isEmpty(), is(false));

        ResumptionTokenHelper underTest = new ResumptionTokenHelper(value, MAX_PER_PAGE)
                .withTotalResults(TOTAL_RESULTS);

        ResumptionToken result = underTest.resolve(NOT_HAS_MORE_RESULTS);
        assertThat(result.getCompleteListSize(), is(TOTAL_RESULTS));
        assertThat(result.getValue().isEmpty(), is(true));
    }

    @Test
    public void shouldReturnNullResumptionTokenIfResumptionTokenValueIsEmptyAndNoMoreResults() throws Exception {
        ResumptionToken.Value value = new ResumptionToken.Value();
        ResumptionTokenHelper underTest = new ResumptionTokenHelper(value, MAX_PER_PAGE)
                .withTotalResults(TOTAL_RESULTS);

        ResumptionToken result = underTest.resolve(NOT_HAS_MORE_RESULTS);
        assertThat(result, is(nullValue()));
    }
}
