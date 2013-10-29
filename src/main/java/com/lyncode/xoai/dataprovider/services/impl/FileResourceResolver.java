package com.lyncode.xoai.dataprovider.services.impl;

import com.lyncode.xoai.dataprovider.services.api.ResourceResolver;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileResourceResolver implements ResourceResolver {
    private static TransformerFactory tFactory = TransformerFactory.newInstance();
    private String basePath;

    public FileResourceResolver(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public InputStream getResource(String path) throws IOException {
        return new FileInputStream(new File(basePath, path));
    }

    @Override
    public Transformer getTransformer(String path) throws IOException, TransformerConfigurationException {
        return tFactory.newTransformer(new StreamSource(getResource(path)));
    }
}
