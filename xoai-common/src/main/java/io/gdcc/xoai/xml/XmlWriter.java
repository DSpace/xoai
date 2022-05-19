/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.xml;

import io.gdcc.xoai.xmlio.XmlIoWriter;
import io.gdcc.xoai.xmlio.exceptions.XmlWriteException;
import io.gdcc.xoai.model.oaipmh.Granularity;
import io.gdcc.xoai.model.oaipmh.ResumptionToken;
import io.gdcc.xoai.services.api.DateProvider;
import io.gdcc.xoai.services.api.ResumptionTokenFormat;
import io.gdcc.xoai.services.impl.SimpleResumptionTokenFormat;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;

public class XmlWriter extends XmlIoWriter implements AutoCloseable {
    public static String toString(XmlWritable writable) throws XMLStreamException, XmlWriteException {
        final OutputStream out = new ByteArrayOutputStream();
        
        try (
            out;
            XmlWriter writer = new XmlWriter(out, defaultContext())
        ) {
            writable.write(writer);
        } catch (IOException e) {
            throw new XmlWriteException(e);
        }
    
        // the try-with-resources above will take care that writer and stream are closed before reading back
        // the data, making sure everything has been flushed.
        return out.toString();
    }

    public static WriterContext defaultContext () {
        return new WriterContext(Granularity.Second, new SimpleResumptionTokenFormat());
    }

    public static class WriterContext {
        private final Granularity granularity;
        private final ResumptionTokenFormat formatter;

        public WriterContext(Granularity granularity, ResumptionTokenFormat formatter) {
            this.granularity = granularity;
            this.formatter = formatter;
        }
    }
    
    private final WriterContext writerContext;

    public XmlWriter(OutputStream output) throws XMLStreamException {
        super(output);
        this.writerContext = defaultContext();
    }

    public XmlWriter(OutputStream output, WriterContext writerContext) throws XMLStreamException {
        super(output);
        this.writerContext = writerContext;
    }



    public void writeDate(Instant date) throws XmlWriteException {
        try {
            this.writeCharacters(DateProvider.format(date, writerContext.granularity));
        } catch (XMLStreamException e) {
            throw new XmlWriteException(e);
        }
    }

    public void writeDate(Instant date, Granularity granularity) throws XmlWriteException {
        try {
            this.writeCharacters(DateProvider.format(date, granularity));
        } catch (XMLStreamException e) {
            throw new XmlWriteException(e);
        }
    }

    public void writeElement(String elementName, String elementValue) throws XmlWriteException {
        try {
            this.writeStartElement(elementName);
            this.writeCharacters(elementValue);
            this.writeEndElement();
        } catch (XMLStreamException e) {
            throw new XmlWriteException(e);
        }
    }

    public void writeElement (String elementName, XmlWritable writable) throws XmlWriteException {
        try {
            if (writable != null) {
                this.writeStartElement(elementName);
                writable.write(this);
                this.writeEndElement();
            }
        } catch (XMLStreamException e) {
            throw new XmlWriteException(e);
        }
    }

    public void writeElement(String elementName, Instant date, Granularity granularity) throws XmlWriteException {
        this.writeElement(elementName, DateProvider.format(date, granularity));
    }
    public void writeElement(String elementName, Instant date) throws XmlWriteException {
        this.writeElement(elementName, DateProvider.format(date, writerContext.granularity));
    }

    public void writeAttribute(String name, Instant date) throws XmlWriteException {
        try {
            this.writeAttribute(name, DateProvider.format(date, writerContext.granularity));
        } catch (XMLStreamException e) {
            throw new XmlWriteException(e);
        }
    }

    public void writeAttribute(String name, Instant value, Granularity granularity) throws XmlWriteException {
        try {
            this.writeAttribute(name, DateProvider.format(value, granularity));
        } catch (XMLStreamException e) {
            throw new XmlWriteException(e);
        }
    }

    @Override
    public void writeAttribute(String localName, String value) throws XMLStreamException {
        if (value != null)
            super.writeAttribute(localName, value);
    }

    @Override
    public void writeCharacters(String text) throws XMLStreamException {
        if (text != null)
            super.writeCharacters(text);
    }

    public void write(XmlWritable writable) throws XmlWriteException {
        if (writable != null)
            writable.write(this);
    }

    public void write(ResumptionToken.Value value) throws XmlWriteException {
        try {
            if (!value.isEmpty())
                writeCharacters(writerContext.formatter.format(value));
        } catch (XMLStreamException e) {
            throw new XmlWriteException(e);
        }
    }
}
