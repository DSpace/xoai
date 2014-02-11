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
import com.lyncode.xoai.model.oaipmh.MetadataFormat;
import com.lyncode.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import org.hamcrest.Matcher;

import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;

import static com.lyncode.xml.matchers.QNameMatchers.localPart;
import static com.lyncode.xml.matchers.XmlEventMatchers.*;
import static com.lyncode.xoai.model.oaipmh.Error.Code.NO_METADATA_FORMATS;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;

public class MetadataFormatParser {
    private final XmlReader reader;
    private boolean awaitingNextInvocation = false;

    public MetadataFormatParser(InputStream inputStream) {
        try {
            reader = new XmlReader(inputStream);
        } catch (XmlReaderException e) {
            throw new InvalidOAIResponse(e);
        }
    }

    public boolean hasNext () throws XmlReaderException {
        if (!awaitingNextInvocation) {
            reader.next(metadataElement(), errorElement(), theEndOfDocument());
            awaitingNextInvocation = true;
        }
        if (reader.current(errorElement())) {
            String code = reader.getAttributeValue(localPart(equalTo("code")));
            if (equalTo(NO_METADATA_FORMATS.code()).matches(code))
                return false;
            else
                throw new InvalidOAIResponse("OAI responded with code: "+
                        code);
        }
        return reader.current(metadataElement());
    }

    private Matcher<XMLEvent> errorElement() {
        return elementName(localPart(equalTo("error")));
    }

    private Matcher<XMLEvent> metadataElement() {
        return allOf(aStartElement(), elementName(localPart(equalTo("metadataFormat"))));
    }

    public MetadataFormat next () throws XmlReaderException {
        if (!hasNext()) throw new XmlReaderException("No more metadata elements available");
        awaitingNextInvocation = false;
        return new MetadataFormat()
                .withMetadataPrefix(reader.next(elementName(localPart(equalTo("metadataPrefix")))).next(text()).getText())
                .withSchema(reader.next(elementName(localPart(equalTo("schema")))).next(text()).getText())
                .withMetadataNamespace(reader.next(elementName(localPart(equalTo("metadataNamespace")))).next(text()).getText());
    }
}
