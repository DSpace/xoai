package com.lyncode.xoai.dataprovider.services.impl;

import com.lyncode.xoai.dataprovider.services.api.ResourceResolver;

import com.lyncode.xoai.util.XMLUtils;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileResourceResolver implements ResourceResolver {
    private String basePath;

    public FileResourceResolver(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public InputStream getResource(String path) throws IOException {
        return new FileInputStream(new File(basePath, path));
    }


    @Override
    public Templates getTemplates(String path) throws IOException, TransformerConfigurationException {
        TransformerFactory tFactory = XMLUtils.getTransformerFactory();
        return tFactory.newTemplates(new StreamSource(getResource(path)));
    }
}
