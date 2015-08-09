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
import org.dspace.xoai.model.oaipmh.MetadataFormat;
import org.dspace.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import org.hamcrest.Matcher;

import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;

import static com.lyncode.xml.matchers.QNameMatchers.localPart;
import static com.lyncode.xml.matchers.XmlEventMatchers.*;
import static org.dspace.xoai.model.oaipmh.Error.Code.NO_METADATA_FORMATS;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;

public class MetadataFormatParser {
    private final XmlReader reader;
    private boolean awaitingNextInvocation = false;

    public MetadataFormatParser(InputStream inputStream) {
        try {
            reader = new XmlReader(inputStream);
        } catch (XmlReaderException e) {
            throw new InvalidOAIResponse(e);
        }
    }

    public boolean hasNext () throws XmlReaderException {
        if (!awaitingNextInvocation) {
            reader.next(metadataElement(), errorElement(), theEndOfDocument());
            awaitingNextInvocation = true;
        }
        if (reader.current(errorElement())) {
            String code = reader.getAttributeValue(localPart(equalTo("code")));
            if (equalTo(NO_METADATA_FORMATS.code()).matches(code))
                return false;
            else
                throw new InvalidOAIResponse("OAI responded with code: "+
                        code);
        }
        return reader.current(metadataElement());
    }

    private Matcher<XMLEvent> errorElement() {
        return elementName(localPart(equalTo("error")));
    }

    private Matcher<XMLEvent> metadataElement() {
        return allOf(aStartElement(), elementName(localPart(equalTo("metadataFormat"))));
    }

    public MetadataFormat next () throws XmlReaderException {
        if (!hasNext()) throw new XmlReaderException("No more metadata elements available");
        awaitingNextInvocation = false;
        return new MetadataFormat()
                .withMetadataPrefix(reader.next(elementName(localPart(equalTo("metadataPrefix")))).next(text()).getText())
                .withSchema(reader.next(elementName(localPart(equalTo("schema")))).next(text()).getText())
                .withMetadataNamespace(reader.next(elementName(localPart(equalTo("metadataNamespace")))).next(text()).getText());
    }
}
