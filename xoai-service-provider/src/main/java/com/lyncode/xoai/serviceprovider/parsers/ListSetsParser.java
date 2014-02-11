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

package com.lyncode.xoai.serviceprovider.parsers;

import com.lyncode.xml.XmlReader;
import com.lyncode.xml.exceptions.XmlReaderException;
import com.lyncode.xoai.model.oaipmh.Set;
import com.lyncode.xoai.serviceprovider.exceptions.EncapsulatedKnownException;
import com.lyncode.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import com.lyncode.xoai.serviceprovider.exceptions.NoSetHierarchyException;
import org.hamcrest.Matcher;

import javax.xml.stream.events.XMLEvent;

import static com.lyncode.xml.matchers.QNameMatchers.localPart;
import static com.lyncode.xml.matchers.XmlEventMatchers.*;
import static com.lyncode.xoai.model.oaipmh.Error.Code.NO_RECORDS_MATCH;
import static com.lyncode.xoai.model.oaipmh.Error.Code.NO_SET_HIERARCHY;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;

public class ListSetsParser {
    private final XmlReader reader;
    private boolean awaitingNextInvocation = false;

    public ListSetsParser(XmlReader reader) {
        this.reader = reader;
    }

    public boolean hasNext () throws XmlReaderException {
        if (!awaitingNextInvocation)
            reader.next(setElement(), errorElement(), resumptionToken(), theEndOfDocument());
        awaitingNextInvocation = true;
        if (reader.current(errorElement())) {
            String code = reader.getAttributeValue(localPart(equalTo("code")));
            if (equalTo(NO_RECORDS_MATCH.code()).matches(code))
                return false;
            else if (equalTo(NO_SET_HIERARCHY.code()).matches(code))
                throw new EncapsulatedKnownException(new NoSetHierarchyException());
            else
                throw new InvalidOAIResponse("OAI responded with code: " + code);
        }
        return reader.current(setElement());
    }

    private Matcher<XMLEvent> resumptionToken() {
        return allOf(aStartElement(), elementName(localPart(equalTo("resumptionToken"))));
    }

    public Set next() throws XmlReaderException {
        if (!hasNext()) throw new XmlReaderException("No more identifiers available");
        awaitingNextInvocation = false;
        return parseSet();
    }

    private Set parseSet() throws XmlReaderException {
        Set set = new Set();
        reader
                .next(elementName(localPart(equalTo("setSpec"))))
                .next(text());
        set.withSpec(reader.getText());
        reader
                .next(elementName(localPart(equalTo("setName"))))
                .next(text());
        set.withName(reader.getText());
        return set;
    }


    private Matcher<XMLEvent> errorElement() {
        return elementName(localPart(equalTo("error")));
    }

    private Matcher<XMLEvent> setElement() {
        return allOf(aStartElement(), elementName(localPart(equalTo("set"))));
    }
}
