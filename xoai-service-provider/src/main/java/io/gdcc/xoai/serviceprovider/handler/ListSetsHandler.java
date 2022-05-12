/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider.handler;

import io.gdcc.xoai.model.oaipmh.Set;
import io.gdcc.xoai.serviceprovider.client.OAIClient;
import io.gdcc.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import io.gdcc.xoai.serviceprovider.exceptions.OAIRequestException;
import io.gdcc.xoai.serviceprovider.lazy.Source;
import io.gdcc.xoai.serviceprovider.model.Context;
import io.gdcc.xoai.serviceprovider.parameters.Parameters;
import io.gdcc.xoai.serviceprovider.parsers.ListSetsParser;
import io.gdcc.xoai.xmlio.XmlReader;
import io.gdcc.xoai.xmlio.exceptions.XmlReaderException;
import org.hamcrest.Matcher;

import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static io.gdcc.xoai.model.oaipmh.Verb.Type.ListSets;
import static io.gdcc.xoai.xmlio.matchers.QNameMatchers.localPart;
import static io.gdcc.xoai.xmlio.matchers.XmlEventMatchers.aStartElement;
import static io.gdcc.xoai.xmlio.matchers.XmlEventMatchers.anEndElement;
import static io.gdcc.xoai.xmlio.matchers.XmlEventMatchers.elementName;
import static io.gdcc.xoai.xmlio.matchers.XmlEventMatchers.text;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;

public class ListSetsHandler implements Source<Set> {
    private final OAIClient client;
    private String resumptionToken;
    private boolean ended = false;

    public ListSetsHandler(Context context) {
        this.client = context.getClient();
    }

    @Override
    public List<Set> nextIteration() {
        // Default to first call variant
        Parameters requestParams = Parameters.parameters().withVerb(ListSets);
        // On resumption calls, add the token
        if (resumptionToken != null) {
            requestParams.withResumptionToken(resumptionToken);
        }
        
        try (
            InputStream stream = client.execute(requestParams);
            XmlReader reader = new XmlReader(stream);
        ) {
            List<Set> sets = new ListSetsParser(reader).parse();
    
            // TODO: this is the same as in ListRecordsHandler and ListIdentifierHandler. Deduplicate.
            if (reader.current(resumptionToken())) {
                if (reader.next(text(), anEndElement()).current(text())) {
                    String text = reader.getText();
                    if (text == null || "".equals(text.trim()))
                        ended = true;
                    else
                        resumptionToken = text;
                } else ended = true;
            } else ended = true;
            
            return sets;
        } catch (XmlReaderException | IOException | OAIRequestException e) {
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
