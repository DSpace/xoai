package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse;

import com.lyncode.xoai.dataprovider.xml.read.XmlReader;
import com.lyncode.xoai.dataprovider.xml.read.XmlReaderException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.ContextConfiguration;

public class ContextConfigurationParser extends Parser<ContextConfiguration> {

    @Override
    public ContextConfiguration parse(XmlReader reader) throws ParseException {
        try {
            ContextConfiguration configuration = new ContextConfiguration(reader.getAttribute("baseurl"));

            String name = reader.getAttribute("name");
            if (name != null) configuration.withName(name);

            reader.proceedToNextElement();

            if (reader.elementNameIs("Transformer")) {
                configuration.withTransformer(reader.getAttribute("ref"));
                reader.proceedToTheNextStartElement();
            }

            if (reader.elementNameIs("Filter")) {
                configuration.withFilter(reader.getAttribute("red"));
                reader.proceedToTheNextStartElement();
            }

            while (reader.elementNameIs("Set")) {
                configuration.withSet(reader.getAttribute("ref"));
                reader.proceedToTheNextStartElement();
            }

            while (reader.elementNameIs("Format")) {
                configuration.withFormat(reader.getAttribute("ref"));
                reader.proceedToNextElement();
                reader.proceedToNextElement();
            }

            if (reader.isStart() && reader.elementNameIs("Description")) {
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
