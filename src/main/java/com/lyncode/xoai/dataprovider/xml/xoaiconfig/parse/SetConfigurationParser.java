package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse;

import com.lyncode.xoai.dataprovider.xml.read.XmlReader;
import com.lyncode.xoai.dataprovider.xml.read.XmlReaderException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.SetConfiguration;

public class SetConfigurationParser extends Parser<SetConfiguration> {

    @Override
    public SetConfiguration parse(XmlReader reader) throws ParseException {
        try {

            SetConfiguration set = new SetConfiguration(reader.getAttribute("id"))
                    .withSpec(reader.getNextElementText("Spec"))
                    .withName(reader.getNextElementText("Name"));

            reader.proceedToNextElement();

            if (reader.isStart()) {
                set.withFilter(reader.getAttribute("ref"));
                reader.proceedToNextElement();
                reader.proceedToNextElement();
            }

            reader.proceedToNextElement();

            return set;
        } catch (XmlReaderException e) {
            throw new ParseException(e);
        }
    }

}
