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

import com.lyncode.xoai.dataprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.dataprovider.exceptions.InternalOAIException;
import com.lyncode.xoai.dataprovider.exceptions.NoMetadataFormatsException;
import org.junit.Test;

import static com.lyncode.test.matchers.xml.XPathMatchers.xPath;
import static com.lyncode.xoai.dataprovider.model.InMemoryItem.item;
import static com.lyncode.xoai.dataprovider.model.MetadataFormat.identity;
import static com.lyncode.xoai.model.oaipmh.Verb.Type.ListMetadataFormats;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListMetadataFormatsHandlerTest extends AbstractHandlerTest {
    @Test(expected = InternalOAIException.class)
    public void initializationErrorIfInvalidConfiguration() throws Exception {
        new ListMetadataFormatsHandler(aContext().withoutMetadataFormats(), theRepository());
    }

    @Test
    public void validResponseForAllMetadataFormats () throws Exception{
        aContext().withMetadataFormat("xoai", identity());
        ListMetadataFormatsHandler underTest = new ListMetadataFormatsHandler(theContext(), theRepository());
        String result = write(underTest.handle(a(request().withVerb(ListMetadataFormats))));

        assertThat(result, xPath("count(//metadataFormat)", asInteger(is(equalTo(1)))));
    }


    @Test(expected = IdDoesNotExistException.class)
    public void itemNotExists () throws Exception{
        aContext().withMetadataFormat("xoai", identity());
        ListMetadataFormatsHandler underTest = new ListMetadataFormatsHandler(theContext(), theRepository());
        underTest.handle(a(request().withVerb(ListMetadataFormats).withIdentifier("1")));
    }

    @Test(expected = NoMetadataFormatsException.class)
    public void noFormatForItem () throws Exception{
        theItemRepository().withItem(item().withDefaults().withIdentifier("1"));
        aContext().withMetadataFormat("xoai", identity(), alwaysFalseCondition());
        ListMetadataFormatsHandler underTest = new ListMetadataFormatsHandler(theContext(), theRepository());
        underTest.handle(a(request().withVerb(ListMetadataFormats).withIdentifier("1")));
    }


    @Test
    public void validResponseForAnItem () throws Exception{
        theItemRepository().withItem(item().withDefaults().withIdentifier("1"));
        aContext().withMetadataFormat("xoai", identity());
        ListMetadataFormatsHandler underTest = new ListMetadataFormatsHandler(theContext(), theRepository());
        String result = write(underTest.handle(a(request().withVerb(ListMetadataFormats).withIdentifier("1"))));

        assertThat(result, xPath("count(//metadataFormat)", asInteger(is(equalTo(1)))));
    }

}
