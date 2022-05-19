/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.model.oaipmh;

import io.gdcc.xoai.services.api.DateProvider;
import io.gdcc.xoai.xmlio.exceptions.XmlWriteException;
import io.gdcc.xoai.xml.XmlWritable;
import io.gdcc.xoai.xml.XmlWriter;
import org.hamcrest.Matcher;
import org.xmlunit.matchers.EvaluateXPathMatcher;
import org.xmlunit.matchers.HasXPathMatcher;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayOutputStream;
import java.time.Instant;


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

    protected HasXPathMatcher hasXPath(String xpath) {
        return HasXPathMatcher.hasXPath("/root" + xpath);
    }

    protected Matcher<? super String> xPath(String xpath, Matcher<String> stringMatcher) {
        return EvaluateXPathMatcher.hasXPath("/root" + xpath, stringMatcher);
    }

    protected String toDateTime(Instant date) {
        return DateProvider.format(date, Granularity.Second);
    }
}
