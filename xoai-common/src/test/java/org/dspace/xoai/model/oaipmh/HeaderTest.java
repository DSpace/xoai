/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.model.oaipmh;

import org.junit.Test;

import java.util.Date;

import static org.dspace.xoai.model.oaipmh.Header.Status.DELETED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HeaderTest extends AbstractOAIPMHTest {

    private static final Date DATE = new Date();

    @Test
    public void testWrite() throws Exception {
        Header header = new Header()
                .withDatestamp(DATE)
                .withIdentifier("Id")
                .withSetSpec("Set1")
                .withSetSpec("Set2")
                .withStatus(DELETED);

        String result = writingResult(header);
        assertThat(result, xPath("/@status", is(equalTo(DELETED.value()))));
        assertThat(result, xPath("/identifier", is(equalTo("Id"))));
        assertThat(result, xPath("/datestamp", is(toDateTime(DATE))));
        assertThat(result, hasXPath("/setSpec[text()='Set1']"));
        assertThat(result, hasXPath("/setSpec[text()='Set2']"));
    }

}
