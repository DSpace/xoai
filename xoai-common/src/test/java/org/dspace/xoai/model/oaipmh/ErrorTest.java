/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.model.oaipmh;

import org.junit.Test;

import static org.dspace.xoai.model.oaipmh.Error.Code.BAD_ARGUMENT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ErrorTest extends AbstractOAIPMHTest {
    @Test
    public void shouldWriteTheCodeCorrectly() throws Exception {
        Error error = new Error("Message").withCode(BAD_ARGUMENT);

        String result = writingResult(error);
        assertThat(result, xPath("/@code", is(equalTo(BAD_ARGUMENT.code()))));
        assertThat(result, xPath("/text()", is(equalTo("Message"))));
    }
}
