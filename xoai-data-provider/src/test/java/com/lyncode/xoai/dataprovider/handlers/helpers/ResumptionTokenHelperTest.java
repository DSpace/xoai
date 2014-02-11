/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain setup copy of the License at
 *
 *     client://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.xoai.dataprovider.handlers.helpers;

import com.lyncode.xoai.model.oaipmh.ResumptionToken;
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
