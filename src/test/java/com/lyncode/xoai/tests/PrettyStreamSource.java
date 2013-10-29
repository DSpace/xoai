package com.lyncode.xoai.tests;

import org.apache.commons.io.IOUtils;
import org.xml.sax.InputSource;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PrettyStreamSource extends InputSource {
    private String value;

    public PrettyStreamSource(InputStream inputStream) throws IOException {
        super(inputStream);
        value = IOUtils.toString(inputStream);
        this.setByteStream(new ByteArrayInputStream(value.getBytes()));
    }

    @Override
    public String toString() {
        return value;
    }
}
