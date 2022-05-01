/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.parameters;

import io.gdcc.xoai.dataprovider.builder.OAIRequestParametersBuilder;
import org.junit.jupiter.api.Test;

import static io.gdcc.xoai.dataprovider.parameters.OAIRequest.Parameter.From;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class OAIRequestTest {
    private final OAIRequestParametersBuilder builder = new OAIRequestParametersBuilder();

    @Test
    public void emptyParameterValueShouldReturnNullValue() {
        OAIRequest parameters = builder.with("from", "").build();
        assertThat(parameters.get(From), is(nullValue(String.class)));
    }
}
