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
import com.lyncode.xoai.model.oaipmh.About;
import com.lyncode.xoai.model.oaipmh.Metadata;
import com.lyncode.xoai.model.oaipmh.Record;
import com.lyncode.xoai.serviceprovider.exceptions.InternalHarvestException;
import com.lyncode.xoai.serviceprovider.model.Context;
import com.lyncode.xoai.xml.XSLPipeline;
import org.hamcrest.Matcher;

import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;

import static com.lyncode.xml.matchers.QNameMatchers.localPart;
import static com.lyncode.xml.matchers.XmlEventMatchers.aStartElement;
import static com.lyncode.xml.matchers.XmlEventMatchers.anEndElement;
import static com.lyncode.xml.matchers.XmlEventMatchers.elementName;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;

public class RecordParser {
    private final Context context;
    private String metadataPrefix;

    public RecordParser(Context context, String metadataPrefix) {
        this.context = context;
        this.metadataPrefix = metadataPrefix;
    }

    public Record parse (XmlReader reader) throws XmlReaderException {
        HeaderParser headerParser = new HeaderParser();

        reader.next(elementName(localPart(equalTo("header"))));
        Record record = new Record()
                .withHeader(headerParser.parse(reader));


        if (!record.getHeader().isDeleted()) {
            reader.next(elementName(localPart(equalTo("metadata")))).next(aStartElement());
            String content = reader.retrieveCurrentAsString();
//            System.out.println(content);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
            XSLPipeline pipeline = new XSLPipeline(inputStream, true)
                    .apply(context.getMetadataTransformer(metadataPrefix));
            if (context.hasTransformer())
                pipeline.apply(context.getTransformer());
            try {
                record.withMetadata(new Metadata(new MetadataParser().parse(pipeline.process())));
            } catch (TransformerException e) {
                throw new InternalHarvestException("Unable to process transformer");
            }
        }

        if (reader.next(aboutElement(), endOfRecord()).current(aboutElement())) {
            reader.next(aStartElement());
            record.withAbout(new About(reader.retrieveCurrentAsString()));
        }

        return record;
    }

    private Matcher<XMLEvent> endOfRecord() {
        return allOf(anEndElement(), elementName(localPart(equalTo("record"))));
    }

    private Matcher<XMLEvent> aboutElement() {
        return elementName(localPart(equalTo("about")));
    }
}
