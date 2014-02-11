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
import com.lyncode.xoai.model.oaipmh.DeletedRecord;
import com.lyncode.xoai.model.oaipmh.Description;
import com.lyncode.xoai.model.oaipmh.Granularity;
import com.lyncode.xoai.model.oaipmh.Identify;
import com.lyncode.xoai.serviceprovider.exceptions.InvalidOAIResponse;

import java.io.InputStream;

import static com.lyncode.xml.matchers.QNameMatchers.localPart;
import static com.lyncode.xml.matchers.XmlEventMatchers.*;
import static com.lyncode.xoai.serviceprovider.xml.IslandParsers.dateParser;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;

public class IdentifyParser {
    private final XmlReader reader;

    public IdentifyParser(InputStream stream) {
        try {
            this.reader = new XmlReader(stream);
        } catch (XmlReaderException e) {
            throw new InvalidOAIResponse(e);
        }
    }


    public Identify parse () {
        try {
            Identify identify = new Identify();
            reader.next(allOf(aStartElement(), elementName(localPart(equalTo("Identify")))));
            reader.next(elementName(localPart(equalTo("repositoryName"))));
            identify.withRepositoryName(reader.next(text()).getText());
            reader.next(elementName(localPart(equalTo("baseURL"))));
            identify.withBaseURL(reader.next(text()).getText());
            reader.next(elementName(localPart(equalTo("protocolVersion"))));
            identify.withProtocolVersion(reader.next(text()).getText());
            reader.next(elementName(localPart(equalTo("adminEmail")))).next(text());
            identify.withAdminEmail(reader.getText());
            while (reader.next(aStartElement()).current(elementName(localPart(equalTo("adminEmail")))))
                identify.withAdminEmail(reader.next(text()).getText());
            identify.withEarliestDatestamp(reader.next(text()).get(dateParser()));
            reader.next(elementName(localPart(equalTo("deletedRecord")))).next(text());
            identify.withDeletedRecord(DeletedRecord.fromValue(reader.getText()));
            reader.next(elementName(localPart(equalTo("granularity")))).next(text());
            identify.withGranularity(Granularity.fromRepresentation(reader.getText()));
            while (reader.next(aStartElement()).current(elementName(localPart(equalTo("compression")))))
                identify.withCompression(reader.next(text()).getText());
            while (reader.next(aStartElement()).current(elementName(localPart(equalTo("description")))))
                identify.withDescription(reader.get(descriptionParser()));
            return identify;
        } catch (XmlReaderException e) {
            throw new InvalidOAIResponse(e);
        }
    }

    private XmlReader.IslandParser<Description> descriptionParser() {
        return new XmlReader.IslandParser<Description>() {
            @Override
            public Description parse(XmlReader reader) throws XmlReaderException {
                return new Description(reader.retrieveCurrentAsString());
            }
        };
    }
}
