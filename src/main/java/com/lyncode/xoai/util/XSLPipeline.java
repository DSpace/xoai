package com.lyncode.xoai.util;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class XSLPipeline {
    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;
    private boolean omitXMLDeclaration;

    public XSLPipeline(InputStream inputStream, boolean omitXMLDeclaration) {
        this.inputStream = inputStream;
        this.omitXMLDeclaration = omitXMLDeclaration;
    }

    public XSLPipeline apply(Transformer xslTransformer) throws TransformerException {
        outputStream = new ByteArrayOutputStream();
        xslTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, (omitXMLDeclaration) ? "yes" : "no");
        xslTransformer.transform(new StreamSource(inputStream), new StreamResult(outputStream));
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        return this;
    }

    public InputStream getTransformed() {
        return this.inputStream;
    }
}
