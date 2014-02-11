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

package com.lyncode.xoai.dataprovider.handlers;

import com.lyncode.xoai.dataprovider.exceptions.BadArgumentException;
import com.lyncode.xoai.dataprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.model.oaipmh.GetRecord;
import org.junit.Before;
import org.junit.Test;

import static com.lyncode.test.matchers.xml.XPathMatchers.xPath;
import static com.lyncode.xoai.dataprovider.model.InMemoryItem.item;
import static com.lyncode.xoai.dataprovider.model.MetadataFormat.identity;
import static com.lyncode.xoai.model.oaipmh.Verb.Type.GetRecord;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetRecordHandlerTest extends AbstractHandlerTest {
    private GetRecordHandler underTest;

    @Before
    public void setup () {
        underTest = new GetRecordHandler(aContext(), theRepository());
    }

    @Test(expected = BadArgumentException.class)
    public void getRecordRequiresMetadataPrefixParameter() throws Exception {
        underTest.handle(a(request().withVerb(GetRecord).withIdentifier("a")));
    }

    @Test(expected = BadArgumentException.class)
    public void getRecordRequiresIdentifierParameter() throws Exception {
        underTest.handle(a(request().withVerb(GetRecord).withMetadataPrefix("a")));
    }

    @Test(expected = IdDoesNotExistException.class)
    public void idDoesNotExists() throws Exception {
        underTest.handle(a(request().withVerb(GetRecord)
                .withMetadataPrefix("xoai")
                .withIdentifier("1")));
    }

    @Test
    public void validResponse() throws Exception {
        theItemRepository().withItem(item().withDefaults().withIdentifier("1"));
        aContext().withMetadataFormat("xoai", identity());
        GetRecord handle = underTest.handle(a(request().withVerb(GetRecord)
                .withMetadataPrefix("xoai")
                .withIdentifier("1")));

        String result = write(handle);

        assertThat(result, xPath("//header/identifier", is(equalTo("1"))));
    }
}
