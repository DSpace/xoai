package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse;

import com.lyncode.xoai.dataprovider.xml.read.XmlReader;
import com.lyncode.xoai.dataprovider.xml.read.XmlReaderException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.FormatConfiguration;

public class FormatConfigurationParser extends Parser<FormatConfiguration> {
    @Override
    public FormatConfiguration parse(XmlReader reader) throws ParseException {
        try {
            FormatConfiguration formatConfiguration = new FormatConfiguration(reader.getAttribute("id"))
                    .withPrefix(reader.getNextElementText("Prefix"))
                    .withXslt(reader.getNextElementText("XSLT"))
                    .withNamespace(reader.getNextElementText("Namespace"))
                    .withSchemaLocation(reader.getNextElementText("SchemaLocation"));

            reader.proceedToNextElement();
            if (reader.isStart() && reader.getName().equals("Filter")) {
                formatConfiguration.withFilter(reader.getAttribute("ref"));
                reader.proceedToNextElement();
            }

            reader.proceedToNextElement();
            return formatConfiguration;
        } catch (XmlReaderException e) {
            throw new ParseException(e);
        }
    }
}
