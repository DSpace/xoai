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
import org.hamcrest.Matcher;

import javax.xml.stream.events.XMLEvent;

import static com.lyncode.xml.matchers.AttributeMatchers.attributeName;
import static com.lyncode.xml.matchers.QNameMatchers.localPart;
import static com.lyncode.xml.matchers.XmlEventMatchers.*;
import static org.dspace.xoai.model.oaipmh.Header.Status.DELETED;
import static org.dspace.xoai.serviceprovider.xml.IslandParsers.dateParser;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;

public class HeaderParser {
    public Header parse (XmlReader reader) throws XmlReaderException {
        Header header = new Header();
        if (reader.hasAttribute(attributeName(localPart(equalTo("status")))))
            header.withStatus(DELETED);
        reader.next(elementName(localPart(equalTo("identifier")))).next(text());
        header.withIdentifier(reader.getText());
        reader.next(elementName(localPart(equalTo("datestamp")))).next(text());
        header.withDatestamp(reader.get(dateParser()));
        while (reader.next(endOfHeader(), setSpecElement()).current(setSpecElement()))
            header.withSetSpec(reader.next(text()).getText());
        return header;
    }


    private Matcher<XMLEvent> setSpecElement() {
        return allOf(aStartElement(), elementName(localPart(equalTo("setSpec"))));
    }

    private Matcher<XMLEvent> endOfHeader() {
        return allOf(anEndElement(), elementName(localPart(equalTo("header"))));
    }
}
