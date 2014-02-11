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
import com.lyncode.xoai.dataprovider.exceptions.DoesNotSupportSetsException;
import com.lyncode.xoai.dataprovider.exceptions.NoMatchesException;
import org.junit.Test;

import static com.lyncode.test.matchers.xml.XPathMatchers.xPath;
import static com.lyncode.xoai.model.oaipmh.Verb.Type.ListIdentifiers;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListIdentifiersHandlerTest extends AbstractHandlerTest {
    private ListIdentifiersHandler underTest = new ListIdentifiersHandler(aContext(), theRepository());

    @Test(expected = BadArgumentException.class)
    public void metadataPrefixIsMandatory () throws Exception {
        underTest.handle(a(request()
                .withVerb(ListIdentifiers)));
    }

    @Test(expected = DoesNotSupportSetsException.class)
    public void doesNotSupportSets () throws Exception {
        theSetRepository().doesNotSupportSets();
        underTest.handle(a(request()
                .withVerb(ListIdentifiers)
                .withMetadataPrefix(EXISTING_METADATA_FORMAT)
                .withSet("hello")));
    }

    @Test(expected = NoMatchesException.class)
    public void responseWithoutItems () throws Exception {
        underTest.handle(a(request()
                .withVerb(ListIdentifiers)
                .withMetadataPrefix(EXISTING_METADATA_FORMAT)));
    }

    @Test
    public void responseWithItems () throws Exception {
        theItemRepository().withRandomItems(10);
        String result = write(underTest.handle(a(request().withVerb(ListIdentifiers).withMetadataPrefix(EXISTING_METADATA_FORMAT))));

        assertThat(result, xPath("count(//header)", asInteger(equalTo(10))));
    }
}
