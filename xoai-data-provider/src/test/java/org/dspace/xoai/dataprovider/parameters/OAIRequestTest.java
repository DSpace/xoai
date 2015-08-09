/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider.parameters;

import org.dspace.xoai.dataprovider.builder.OAIRequestParametersBuilder;
import org.junit.Test;

import static org.dspace.xoai.dataprovider.parameters.OAIRequest.Parameter.From;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class OAIRequestTest {
    private OAIRequestParametersBuilder builder = new OAIRequestParametersBuilder();

    @Test
    public void emptyParameterValueShouldReturnNullValue() throws Exception {
        OAIRequest parameters = builder.with("from", "").build();
        assertThat(parameters.get(From), is(nullValue(String.class)));
    }
}
