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

package com.lyncode.xoai.serviceprovider.handler;

import static com.lyncode.xml.matchers.QNameMatchers.localPart;
import static com.lyncode.xml.matchers.XmlEventMatchers.aStartElement;
import static com.lyncode.xml.matchers.XmlEventMatchers.anEndElement;
import static com.lyncode.xml.matchers.XmlEventMatchers.elementName;
import static com.lyncode.xml.matchers.XmlEventMatchers.text;
import static com.lyncode.xoai.model.oaipmh.Verb.Type.ListRecords;
import static com.lyncode.xoai.serviceprovider.parameters.Parameters.parameters;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.events.XMLEvent;

import org.hamcrest.Matcher;

import com.lyncode.xml.XmlReader;
import com.lyncode.xml.exceptions.XmlReaderException;
import com.lyncode.xoai.model.oaipmh.Record;
import com.lyncode.xoai.serviceprovider.client.OAIClient;
import com.lyncode.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import com.lyncode.xoai.serviceprovider.exceptions.OAIRequestException;
import com.lyncode.xoai.serviceprovider.lazy.Source;
import com.lyncode.xoai.serviceprovider.model.Context;
import com.lyncode.xoai.serviceprovider.parameters.ListRecordsParameters;
import com.lyncode.xoai.serviceprovider.parsers.ListRecordsParser;

public class ListRecordHandler implements Source<Record> {
    private Context context;
    private ListRecordsParameters parameters;
    private OAIClient client;
    private String resumptionToken;
    private boolean ended = false;

    public ListRecordHandler(Context context, ListRecordsParameters parameters) {
        this.context = context;
        this.parameters = parameters;
        this.client = context.getClient();
    }

    @Override
    public List<Record> nextIteration() {
    	//TODO - refactor - this and ListIdentifierHandler are pretty similar.
        List<Record> records = new ArrayList<Record>();
        try {
            InputStream stream = null;
            if (resumptionToken == null) { // First call
                stream = client.execute(parameters()
                        .withVerb(ListRecords)
                        .include(parameters));
            } else { // Resumption calls
                stream = client.execute(parameters()
                        .withVerb(ListRecords)
                        .include(parameters)
                        .withResumptionToken(resumptionToken));
            }

            XmlReader reader = new XmlReader(stream);
            ListRecordsParser parser = new ListRecordsParser(reader,
                    context, parameters.getMetadataPrefix());
            while (parser.hasNext())
                records.add(parser.next());

            if (reader.current(resumptionToken())) {
                if (reader.next(text(), anEndElement()).current(text())) {
                    String text = reader.getText();
                    if (text == null || "".equals(text.trim()))
                        ended = true;
                    else
                        resumptionToken = text;
                } else ended = true;
            } else ended = true;
			
            stream.close();
            return records;
        } catch (XmlReaderException e) {
            throw new InvalidOAIResponse(e);
        } catch (OAIRequestException e) {
            throw new InvalidOAIResponse(e);
        }
        catch (IOException e) {
        	throw new InvalidOAIResponse(e);
		}
    }

    private Matcher<XMLEvent> resumptionToken() {
        return allOf(aStartElement(), elementName(localPart(equalTo("resumptionToken"))));
    }

    @Override
    public boolean endReached() {
        return ended;
    }
}
