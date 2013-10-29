package com.lyncode.xoai.dataprovider.xml;

import com.lyncode.xoai.dataprovider.core.Granularity;
import com.lyncode.xoai.dataprovider.services.api.DateProvider;
import com.lyncode.xoai.dataprovider.services.impl.BaseDateProvider;
import org.codehaus.stax2.XMLOutputFactory2;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Date;

public class XmlOutputContext {
    private static XMLOutputFactory factory = XMLOutputFactory2.newFactory();

    public static XmlOutputContext emptyContext(OutputStream out) throws XMLStreamException {
        return new XmlOutputContext(new BaseDateProvider(), factory.createXMLStreamWriter(out), Granularity.Second);
    }

    private DateProvider formatter;
    private XMLStreamWriter writer;
    private Granularity granularity;

    public XmlOutputContext(DateProvider formatter, XMLStreamWriter writer, Granularity granularity) {
        this.formatter = formatter;
        this.writer = writer;
        this.granularity = granularity;
    }

    public Date parse (String string) throws ParseException {
        return this.formatter.parse(string, granularity);
    }

    public String format (Date date) {
        return formatter.format(date, granularity);
    }

    public XMLStreamWriter getWriter() {
        return writer;
    }
}
