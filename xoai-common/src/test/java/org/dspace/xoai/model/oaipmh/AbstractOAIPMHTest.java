/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.model.oaipmh;

import com.lyncode.test.matchers.xml.XPathMatchers;
import com.lyncode.xml.exceptions.XmlWriteException;
import org.dspace.xoai.services.impl.UTCDateProvider;
import org.dspace.xoai.xml.XmlWritable;
import org.dspace.xoai.xml.XmlWriter;
import org.hamcrest.Matcher;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayOutputStream;
import java.util.Date;


public abstract class AbstractOAIPMHTest {

    protected String writingResult (XmlWritable writable) throws XMLStreamException, XmlWriteException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        XmlWriter writer = new XmlWriter(stream);
        writer.writeStartDocument();
        writer.writeStartElement("root");
        writer.write(writable);
        writer.writeEndElement();
        writer.writeEndDocument();

        return stream.toString();
    }

    protected Matcher<String> hasXPath(String xpath) {
        return XPathMatchers.hasXPath("/root" + xpath);
    }

    protected Matcher<? super String> xPath(String xpath, Matcher<String> stringMatcher) {
        return XPathMatchers.xPath("/root" + xpath, stringMatcher);
    }

    protected String toDateTime(Date date) {
        return new UTCDateProvider().format(date, Granularity.Second);
    }
}
