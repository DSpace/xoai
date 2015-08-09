/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.handler;

import com.lyncode.xml.XmlReader;
import com.lyncode.xml.exceptions.XmlReaderException;
import org.dspace.xoai.model.oaipmh.Record;
import org.dspace.xoai.serviceprovider.client.OAIClient;
import org.dspace.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import org.dspace.xoai.serviceprovider.exceptions.OAIRequestException;
import org.dspace.xoai.serviceprovider.lazy.Source;
import org.dspace.xoai.serviceprovider.model.Context;
import org.dspace.xoai.serviceprovider.parameters.ListRecordsParameters;
import org.dspace.xoai.serviceprovider.parsers.ListRecordsParser;
import org.hamcrest.Matcher;

import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.lyncode.xml.matchers.QNameMatchers.localPart;
import static com.lyncode.xml.matchers.XmlEventMatchers.*;
import static org.dspace.xoai.model.oaipmh.Verb.Type.ListRecords;
import static org.dspace.xoai.serviceprovider.parameters.Parameters.parameters;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;

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
