package com.lyncode.xoai.model.oaipmh;

import com.lyncode.xml.exceptions.XmlWriteException;
import com.lyncode.xoai.model.xoai.XOAIMetadata;
import com.lyncode.xoai.xml.EchoElement;
import com.lyncode.xoai.xml.XmlWritable;
import com.lyncode.xoai.xml.XmlWriter;
import org.apache.commons.io.IOUtils;

import javax.xml.bind.annotation.XmlValue;
import java.io.IOException;
import java.io.InputStream;

public class Metadata implements XmlWritable {
    private String string;

    @XmlValue
    protected XOAIMetadata value;

    public Metadata(XOAIMetadata value) {
        this.value = value;
    }

    public Metadata(String value) {
        this.string = value;
    }

    public Metadata(InputStream value) throws IOException {
        this.string = IOUtils.toString(value);
    }

    @Override
    public void write(XmlWriter writer) throws XmlWriteException {
        if (this.value != null)
            this.value.write(writer);
        else {
            EchoElement elem = new EchoElement(string);
            elem.write(writer);
        }
    }

    public XOAIMetadata getValue () {
        return value;
    }

}
