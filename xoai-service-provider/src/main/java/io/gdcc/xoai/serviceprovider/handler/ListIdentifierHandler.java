/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider.handler;

import io.gdcc.xoai.model.oaipmh.Header;
import io.gdcc.xoai.serviceprovider.client.OAIClient;
import io.gdcc.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import io.gdcc.xoai.serviceprovider.exceptions.OAIRequestException;
import io.gdcc.xoai.serviceprovider.lazy.Source;
import io.gdcc.xoai.serviceprovider.model.Context;
import io.gdcc.xoai.serviceprovider.parameters.ListIdentifiersParameters;
import io.gdcc.xoai.serviceprovider.parameters.Parameters;
import io.gdcc.xoai.serviceprovider.parsers.ListIdentifiersParser;
import io.gdcc.xoai.xmlio.XmlReader;
import io.gdcc.xoai.xmlio.exceptions.XmlReaderException;
import org.hamcrest.Matcher;

import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static io.gdcc.xoai.model.oaipmh.Verb.Type.ListIdentifiers;
import static io.gdcc.xoai.xmlio.matchers.QNameMatchers.localPart;
import static io.gdcc.xoai.xmlio.matchers.XmlEventMatchers.aStartElement;
import static io.gdcc.xoai.xmlio.matchers.XmlEventMatchers.anEndElement;
import static io.gdcc.xoai.xmlio.matchers.XmlEventMatchers.elementName;
import static io.gdcc.xoai.xmlio.matchers.XmlEventMatchers.text;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;

public class ListIdentifierHandler implements Source<Header> {
    private final ListIdentifiersParameters parameters;
    private final OAIClient client;
    private String resumptionToken = null;
    private boolean ended = false;

    public ListIdentifierHandler(Context context, ListIdentifiersParameters parameters) {
        this.parameters = parameters;
        this.client = context.getClient();
    }

    @Override
    public List<Header> nextIteration() {
        List<Header> headers = new ArrayList<>();
    
        Parameters requestParameters = Parameters.parameters().withVerb(ListIdentifiers).include(parameters);
        // Resumption calls must include the resumption token
        if (resumptionToken != null) {
            requestParameters.withResumptionToken(resumptionToken);
        }
        
        try (
            InputStream stream = client.execute(requestParameters);
            XmlReader reader = new XmlReader(stream)
        ){
            // TODO: this was written before Streams and Collections API. Refactor.
            ListIdentifiersParser parser = new ListIdentifiersParser(reader);
            while (parser.hasNext())
                headers.add(parser.next());
    
            // TODO: this is the same as in ListRecordsHandler. Deduplicate.
            if (reader.current(resumptionToken())) {
                if (reader.next(text(), anEndElement()).current(text())) {
                    String text = reader.getText();
                    if (text == null || "".equals(text.trim()))
                        ended = true;
                    else
                        resumptionToken = text;
                } else ended = true;
            } else ended = true;
            
            return headers;
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
