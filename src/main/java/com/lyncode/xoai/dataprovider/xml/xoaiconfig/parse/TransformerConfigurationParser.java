package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse;

import com.lyncode.xoai.dataprovider.xml.read.XmlReader;
import com.lyncode.xoai.dataprovider.xml.read.XmlReaderException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.TransformerConfiguration;

public class TransformerConfigurationParser extends Parser<TransformerConfiguration> {
    @Override
    public TransformerConfiguration parse(XmlReader reader) throws ParseException {
        try {
            TransformerConfiguration configuration = new TransformerConfiguration(reader.getAttribute("id"));

            reader.proceedToNextElement();
            if (reader.getName().equals("XSLT"))
                configuration.withXslt(reader.getText());
            else throw new ParseException("XSLT element expected");

            reader.proceedToNextElement();
            if (reader.isStart() && reader.getName().equals("Description")) {
                configuration.withDescription(reader.getText());
                reader.proceedToNextElement();
            }

            reader.proceedToNextElement();
            return configuration;
        } catch (XmlReaderException e) {
            throw new ParseException(e);
        }
    }
}
