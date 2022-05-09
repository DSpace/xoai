/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider.parsers;

import io.gdcc.xoai.model.oaipmh.Record;
import io.gdcc.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import io.gdcc.xoai.serviceprovider.model.Context;
import io.gdcc.xoai.xmlio.XmlReader;
import io.gdcc.xoai.xmlio.exceptions.XmlReaderException;
import org.hamcrest.Matcher;

import javax.xml.stream.events.XMLEvent;

import static io.gdcc.xoai.model.oaipmh.Error.Code.NO_RECORDS_MATCH;
import static io.gdcc.xoai.xmlio.matchers.QNameMatchers.localPart;
import static io.gdcc.xoai.xmlio.matchers.XmlEventMatchers.aStartElement;
import static io.gdcc.xoai.xmlio.matchers.XmlEventMatchers.elementName;
import static io.gdcc.xoai.xmlio.matchers.XmlEventMatchers.theEndOfDocument;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;

public class ListRecordsParser {
    private final XmlReader reader;
    private final Context context;
    private final String metadataPrefix;
    private boolean awaitingNextInvocation = false;

    public ListRecordsParser(XmlReader reader, Context context, String metadataPrefix) {
        this.reader = reader;
        this.context = context;
        this.metadataPrefix = metadataPrefix;
    }

    public boolean hasNext () throws XmlReaderException {
        if (!awaitingNextInvocation)
            reader.next(recordElement(), errorElement(), resumptionToken(), theEndOfDocument());
        awaitingNextInvocation = true;
        if (reader.current(errorElement())) {
            String code = reader.getAttributeValue(localPart(equalTo("code")));
            if (equalTo(NO_RECORDS_MATCH.code()).matches(code))
                return false;
            else
                throw new InvalidOAIResponse("OAI responded with code: "+
                        code);
        }
        return reader.current(recordElement());
    }

    private Matcher<XMLEvent> resumptionToken() {
        return allOf(aStartElement(), elementName(localPart(equalTo("resumptionToken"))));
    }

    public Record next () throws XmlReaderException {
        if (!hasNext()) throw new XmlReaderException("No more records available");
        awaitingNextInvocation = false;
        return new RecordParser(context, metadataPrefix).parse(reader);
    }


    private Matcher<XMLEvent> errorElement() {
        return elementName(localPart(equalTo("error")));
    }

    private Matcher<XMLEvent> recordElement() {
        return allOf(aStartElement(), elementName(localPart(equalTo("record"))));
    }
}
