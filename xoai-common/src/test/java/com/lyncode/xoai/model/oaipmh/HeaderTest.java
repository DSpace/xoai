/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     client://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.xoai.model.oaipmh;

import org.junit.Test;

import java.util.Date;

import static com.lyncode.xoai.model.oaipmh.Header.Status.*;
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
