package com.lyncode.xoai.dataprovider.services.api;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public interface ResourceResolver {
    InputStream getResource(String path) throws IOException;
    Transformer getTransformer (String path) throws IOException, TransformerConfigurationException;
}
