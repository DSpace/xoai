package com.lyncode.xoai.util;

import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class XMLUtils {
    /**
     * Initialize and return the javax TransformerFactory with some basic security
     * applied to avoid XXE attacks and other unwanted content inclusion
     * @return transformer factory to generate new transformers
     * @throws TransformerConfigurationException
     */
    public static TransformerFactory getTransformerFactory() throws TransformerConfigurationException {
        TransformerFactory factory = TransformerFactory.newInstance();

        // Process XML securely
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

        // No external DTDs or Stylesheets
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

        return factory;
    }

    public static String format(String unformattedXml) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Transformer transformer;
            TransformerFactory factory = getTransformerFactory();
            transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //initialize StreamResult with File object to save to file
            StreamResult result = new StreamResult(out);
            StreamSource source = new StreamSource(new ByteArrayInputStream(unformattedXml.getBytes()));
            transformer.transform(source, result);
            return out.toString();
        } catch (Exception e) {
            return "";
        }
    }

}
