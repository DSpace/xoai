package com.lyncode.xoai.xml;

import org.apache.commons.io.IOUtils;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XSLPipeline {
    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;
    private List<Transformer> transformers = new ArrayList<Transformer>();
    private boolean omitXMLDeclaration;

    public XSLPipeline(InputStream inputStream, boolean omitXMLDeclaration) {
        this.inputStream = inputStream;
        this.omitXMLDeclaration = omitXMLDeclaration;
    }

    public XSLPipeline apply(Transformer xslTransformer) {
        transformers.add(xslTransformer);
        return this;
    }

    public InputStream process() throws TransformerException {
        for (Transformer transformer : transformers) {
            outputStream = new ByteArrayOutputStream();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, (omitXMLDeclaration) ? "yes" : "no");
            transformer.transform(new StreamSource(inputStream), new StreamResult(outputStream));
            inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        }
        return inputStream;
    }
}
