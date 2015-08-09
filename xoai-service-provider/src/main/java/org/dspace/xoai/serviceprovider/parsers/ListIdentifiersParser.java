/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.parsers;

import com.lyncode.xml.XmlReader;
import com.lyncode.xml.exceptions.XmlReaderException;
import org.dspace.xoai.model.oaipmh.Header;
import org.dspace.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import org.hamcrest.Matcher;

import javax.xml.stream.events.XMLEvent;

import static com.lyncode.xml.matchers.QNameMatchers.localPart;
import static com.lyncode.xml.matchers.XmlEventMatchers.*;
import static org.dspace.xoai.model.oaipmh.Error.Code.NO_RECORDS_MATCH;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;

public class ListIdentifiersParser {
    private final XmlReader reader;
    private boolean awaitingNextInvocation = false;

    public ListIdentifiersParser(XmlReader reader) {
        this.reader = reader;
    }

    public boolean hasNext () throws XmlReaderException {
        if (!awaitingNextInvocation)
            reader.next(headerElement(), errorElement(), resumptionToken(), theEndOfDocument());
        awaitingNextInvocation = true;
        if (reader.current(errorElement())) {
            String code = reader.getAttributeValue(localPart(equalTo("code")));
            if (equalTo(NO_RECORDS_MATCH.code()).matches(code))
                return false;
            else
                throw new InvalidOAIResponse("OAI responded with code: " + code);
        }
        return reader.current(headerElement());
    }

    private Matcher<XMLEvent> resumptionToken() {
        return allOf(aStartElement(), elementName(localPart(equalTo("resumptionToken"))));
    }

    public Header next() throws XmlReaderException {
        if (!hasNext()) throw new XmlReaderException("No more identifiers available");
        awaitingNextInvocation = false;
        return new HeaderParser().parse(reader);
    }


    private Matcher<XMLEvent> errorElement() {
        return elementName(localPart(equalTo("error")));
    }

    private Matcher<XMLEvent> headerElement() {
        return allOf(aStartElement(), elementName(localPart(equalTo("header"))));
    }
}
