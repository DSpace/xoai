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
import org.dspace.xoai.model.oaipmh.Set;
import org.dspace.xoai.serviceprovider.exceptions.EncapsulatedKnownException;
import org.dspace.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import org.dspace.xoai.serviceprovider.exceptions.NoSetHierarchyException;
import org.hamcrest.Matcher;

import javax.xml.namespace.QName;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.List;

import static com.lyncode.xml.matchers.QNameMatchers.localPart;
import static com.lyncode.xml.matchers.XmlEventMatchers.*;
import static org.dspace.xoai.model.oaipmh.Error.Code.NO_RECORDS_MATCH;
import static org.dspace.xoai.model.oaipmh.Error.Code.NO_SET_HIERARCHY;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;

public class ListSetsParser {
    private final XmlReader reader;
    private boolean awaitingNextInvocation = false;

    public ListSetsParser(XmlReader reader) {
        this.reader = reader;
    }

    public boolean hasNext () throws XmlReaderException {
        if (!awaitingNextInvocation)
            reader.next(setElement(), errorElement(), resumptionToken(), theEndOfDocument());
        awaitingNextInvocation = true;
        if (reader.current(errorElement())) {
            String code = reader.getAttributeValue(localPart(equalTo("code")));
            if (equalTo(NO_RECORDS_MATCH.code()).matches(code))
                return false;
            else if (equalTo(NO_SET_HIERARCHY.code()).matches(code))
                throw new EncapsulatedKnownException(new NoSetHierarchyException());
            else
                throw new InvalidOAIResponse("OAI responded with code: " + code);
        }
        return reader.current(setElement());
    }

    private Matcher<XMLEvent> resumptionToken() {
        return allOf(aStartElement(), elementName(localPart(equalTo("resumptionToken"))));
    }

    public Set next() throws XmlReaderException {
        if (!hasNext()) throw new XmlReaderException("No more identifiers available");
        awaitingNextInvocation = false;
        return parseSet();
    }

    @SuppressWarnings("unchecked")
	private Set parseSet() throws XmlReaderException {
        Set set = new Set();
        
        String setName = null;
        String setSpec = null;
        
        while(setName == null || setSpec == null) {
        	reader.next(aStartElement());
            QName elementName = reader.getName();
            reader.next(text());
            String extractedText = reader.getText();
            while(reader.next(anEndElement(),text()).current(text())){
            	extractedText += reader.getText();
            }
            if(elementName.getLocalPart().equals("setName")) {
            	setName = extractedText;
            } else if(elementName.getLocalPart().equals("setSpec")) {
            	setSpec = extractedText;
            }
        }
        set.withName(setName);
        set.withSpec(setSpec);
        return set;
    }


    private Matcher<XMLEvent> errorElement() {
        return elementName(localPart(equalTo("error")));
    }

    private Matcher<XMLEvent> setElement() {
        return allOf(aStartElement(), elementName(localPart(equalTo("set"))));
    }
    private Matcher<XMLEvent> endSetElement() {
        return allOf(anEndElement(), elementName(localPart(equalTo("set"))));
    }

	/**
	 * Parses its xml completely
	 * @return - All sets within the xml
	 * @throws XmlReaderException
	 */
	public List<Set> parse() throws XmlReaderException {
		List<Set> sets = new ArrayList<Set>();
		while (hasNext())
            sets.add(next());
		return sets;
	}
}
