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

import static com.lyncode.xoai.model.oaipmh.Error.Code.BAD_ARGUMENT;
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
