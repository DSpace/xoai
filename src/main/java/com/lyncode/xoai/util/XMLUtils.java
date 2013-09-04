package com.lyncode.xoai.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


public class XMLUtils {
    static final TransformerFactory factory = TransformerFactory.newInstance();
    public static String format(String unformattedXml) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Transformer transformer;
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
