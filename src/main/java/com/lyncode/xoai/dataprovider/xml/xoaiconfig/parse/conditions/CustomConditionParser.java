package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.conditions;

import com.lyncode.xoai.dataprovider.xml.read.XmlReader;
import com.lyncode.xoai.dataprovider.xml.read.XmlReaderException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.conditions.CustomConditionConfiguration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.ParseException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.Parser;

public class CustomConditionParser extends Parser<CustomConditionConfiguration> {
    @Override
    public CustomConditionConfiguration parse(XmlReader reader) throws ParseException {
        try {
            CustomConditionConfiguration customConditionConfiguration =
                    new CustomConditionConfiguration(reader.getAttribute("ref"));
            reader.proceedToNextElement();
            reader.proceedToNextElement();
            return customConditionConfiguration;
        } catch (XmlReaderException e) {
            throw new ParseException(e);
        }
    }
}
