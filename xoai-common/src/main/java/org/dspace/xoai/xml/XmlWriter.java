/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.xml;

import com.lyncode.xml.exceptions.XmlWriteException;
import org.dspace.xoai.model.oaipmh.Granularity;
import org.dspace.xoai.model.oaipmh.ResumptionToken;
import org.dspace.xoai.services.api.DateProvider;
import org.dspace.xoai.services.api.ResumptionTokenFormat;
import org.dspace.xoai.services.impl.SimpleResumptionTokenFormat;
import org.dspace.xoai.services.impl.UTCDateProvider;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Date;

public class XmlWriter extends com.lyncode.xml.XmlWriter {
    public static String toString (XmlWritable writable) throws XMLStreamException, XmlWriteException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XmlWriter writer = new XmlWriter(outputStream, defaultContext());
        writable.write(writer);
        writer.close();
        return outputStream.toString();
    }

    public static WriterContext defaultContext () {
        return new WriterContext(Granularity.Second, new SimpleResumptionTokenFormat());
    }

    public static class WriterContext {
        private Granularity granularity;
        private ResumptionTokenFormat formatter;

        public WriterContext(Granularity granularity, ResumptionTokenFormat formatter) {
            this.granularity = granularity;
            this.formatter = formatter;
        }
    }

    private final DateProvider dateProvider;
    private final WriterContext writerContext;

    public XmlWriter(OutputStream output) throws XMLStreamException {
        super(output);
        this.dateProvider = new UTCDateProvider();
        this.writerContext = defaultContext();
    }

    public XmlWriter(OutputStream output, WriterContext writerContext) throws XMLStreamException {
        super(output);
        this.dateProvider = new UTCDateProvider();
        this.writerContext = writerContext;
    }



    public void writeDate(Date date) throws XmlWriteException {
        try {
            this.writeCharacters(dateProvider.format(date, writerContext.granularity));
        } catch (XMLStreamException e) {
            throw new XmlWriteException(e);
        }
    }

    public void writeDate(Date date, Granularity granularity) throws XmlWriteException {
        try {
            this.writeCharacters(dateProvider.format(date, granularity));
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

    public void writeElement(String elementName, Date date, Granularity granularity) throws XmlWriteException {
        this.writeElement(elementName, dateProvider.format(date, granularity));
    }
    public void writeElement(String elementName, Date date) throws XmlWriteException {
        this.writeElement(elementName, dateProvider.format(date, writerContext.granularity));
    }

    public void writeAttribute(String name, Date date) throws XmlWriteException {
        try {
            this.writeAttribute(name, dateProvider.format(date, writerContext.granularity));
        } catch (XMLStreamException e) {
            throw new XmlWriteException(e);
        }
    }

    public void writeAttribute(String name, Date value, Granularity granularity) throws XmlWriteException {
        try {
            this.writeAttribute(name, dateProvider.format(value, granularity));
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
