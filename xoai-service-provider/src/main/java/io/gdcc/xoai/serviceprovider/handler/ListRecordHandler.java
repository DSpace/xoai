/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider.handler;

import io.gdcc.xoai.model.oaipmh.Record;
import io.gdcc.xoai.serviceprovider.client.OAIClient;
import io.gdcc.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import io.gdcc.xoai.serviceprovider.exceptions.OAIRequestException;
import io.gdcc.xoai.serviceprovider.lazy.Source;
import io.gdcc.xoai.serviceprovider.model.Context;
import io.gdcc.xoai.serviceprovider.parameters.ListRecordsParameters;
import io.gdcc.xoai.serviceprovider.parameters.Parameters;
import io.gdcc.xoai.serviceprovider.parsers.ListRecordsParser;
import io.gdcc.xoai.xmlio.XmlReader;
import io.gdcc.xoai.xmlio.exceptions.XmlReaderException;
import org.hamcrest.Matcher;

import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static io.gdcc.xoai.model.oaipmh.Verb.Type.ListRecords;
import static io.gdcc.xoai.xmlio.matchers.QNameMatchers.localPart;
import static io.gdcc.xoai.xmlio.matchers.XmlEventMatchers.aStartElement;
import static io.gdcc.xoai.xmlio.matchers.XmlEventMatchers.anEndElement;
import static io.gdcc.xoai.xmlio.matchers.XmlEventMatchers.elementName;
import static io.gdcc.xoai.xmlio.matchers.XmlEventMatchers.text;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;

public class ListRecordHandler implements Source<Record> {
    private final Context context;
    private final ListRecordsParameters parameters;
    private final OAIClient client;
    private String resumptionToken;
    private boolean ended = false;

    public ListRecordHandler(Context context, ListRecordsParameters parameters) {
        this.context = context;
        this.parameters = parameters;
        this.client = context.getClient();
    }

    @Override
    public List<Record> nextIteration() {
        List<Record> records = new ArrayList<>();
        
        Parameters requestParameters = Parameters.parameters().withVerb(ListRecords).include(parameters);
        // Resumption calls must include the resumption token
        if (resumptionToken != null) {
            requestParameters.withResumptionToken(resumptionToken);
        }
        
        try (
            InputStream stream = client.execute(requestParameters);
            XmlReader reader = new XmlReader(stream);
        ){
            // TODO: this was written before Streams and Collections API. Refactor.
            ListRecordsParser parser = new ListRecordsParser(reader, context, parameters.getMetadataPrefix());
            while (parser.hasNext())
                records.add(parser.next());

            // TODO: this is the same as in ListIdentifiersHandler. Deduplicate.
            if (reader.current(resumptionToken())) {
                if (reader.next(text(), anEndElement()).current(text())) {
                    String text = reader.getText();
                    if (text == null || "".equals(text.trim()))
                        ended = true;
                    else
                        resumptionToken = text;
                } else ended = true;
            } else ended = true;
            
            return records;
        } catch (XmlReaderException | OAIRequestException | IOException e) {
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
