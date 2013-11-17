package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.conditions;

import com.lyncode.xoai.dataprovider.xml.read.XmlReader;
import com.lyncode.xoai.dataprovider.xml.read.XmlReaderException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.conditions.FilterConditionConfiguration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.ParseException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.Parser;

public class ConditionParser extends Parser<FilterConditionConfiguration> {

    @Override
    public FilterConditionConfiguration parse(XmlReader reader) throws ParseException {
        try {
            if (reader.elementNameIs("Not"))
                return new NotConditionParser().parse(reader);
            else if (reader.elementNameIs("And"))
                return new AddConditionParser().parse(reader);
            else if (reader.elementNameIs("Or"))
                return new OrConditionParser().parse(reader);
            else if (reader.elementNameIs("Custom"))
                return new CustomConditionParser().parse(reader);
            throw new ParseException("Unknown operator " + reader.getName());
        } catch (XmlReaderException e) {
            throw new ParseException(e);
        }
    }
}
