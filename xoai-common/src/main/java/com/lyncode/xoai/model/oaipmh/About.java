package com.lyncode.xoai.model.oaipmh;

import com.lyncode.xml.exceptions.XmlWriteException;
import com.lyncode.xoai.xml.EchoElement;
import com.lyncode.xoai.xml.XmlWriter;
import com.lyncode.xoai.xml.XmlWritable;

public class About implements XmlWritable {
    private final String value;

    public About(String xmlValue) {
        this.value = xmlValue;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void write(XmlWriter writer) throws XmlWriteException {
        if (this.value != null) {
            EchoElement elem = new EchoElement(value);
            elem.write(writer);
        }
    }
}
