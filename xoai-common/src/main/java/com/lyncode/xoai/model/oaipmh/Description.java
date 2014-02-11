package com.lyncode.xoai.model.oaipmh;

import com.lyncode.xml.exceptions.XmlWriteException;
import com.lyncode.xoai.model.xoai.XOAIMetadata;
import com.lyncode.xoai.xml.EchoElement;
import com.lyncode.xoai.xml.XmlWriter;
import com.lyncode.xoai.xml.XmlWritable;

public class Description implements XmlWritable {
    public static Description description (XOAIMetadata metadata) {
        return new Description(metadata);
    }

    protected String value;
    private XOAIMetadata XOAIMetadata;

    public Description() {}

    public Description(XOAIMetadata XOAIMetadata) {
        this.XOAIMetadata = XOAIMetadata;
    }

    public Description(String compiledMetadata) {
        value = compiledMetadata;
    }

    public Description withMetadata(XOAIMetadata XOAIMetadata) {
        this.XOAIMetadata = XOAIMetadata;
        return this;
    }
    public Description withMetadata(String metadata) {
        this.value = metadata;
        return this;
    }

    @Override
    public void write(XmlWriter writer) throws XmlWriteException {
        if (XOAIMetadata != null) {
            this.XOAIMetadata.write(writer);
        } else if (this.value != null) {
            EchoElement echo = new EchoElement(value);
            echo.write(writer);
        }
    }

}
