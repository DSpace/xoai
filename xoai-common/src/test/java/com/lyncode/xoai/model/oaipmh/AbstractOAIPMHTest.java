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

import com.lyncode.test.matchers.xml.XPathMatchers;
import com.lyncode.xml.exceptions.XmlWriteException;
import com.lyncode.xoai.services.impl.UTCDateProvider;
import com.lyncode.xoai.xml.XmlWritable;
import com.lyncode.xoai.xml.XmlWriter;
import org.hamcrest.Matcher;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayOutputStream;
import java.util.Date;


public abstract class AbstractOAIPMHTest {

    protected String writingResult (XmlWritable writable) throws XMLStreamException, XmlWriteException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        XmlWriter writer = new XmlWriter(stream);
        writer.writeStartDocument();
        writer.writeStartElement("root");
        writer.write(writable);
        writer.writeEndElement();
        writer.writeEndDocument();

        return stream.toString();
    }

    protected Matcher<String> hasXPath(String xpath) {
        return XPathMatchers.hasXPath("/root" + xpath);
    }

    protected Matcher<? super String> xPath(String xpath, Matcher<String> stringMatcher) {
        return XPathMatchers.xPath("/root" + xpath, stringMatcher);
    }

    protected String toDateTime(Date date) {
        return new UTCDateProvider().format(date, Granularity.Second);
    }
}
