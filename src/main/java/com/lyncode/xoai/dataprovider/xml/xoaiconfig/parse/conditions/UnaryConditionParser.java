package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.conditions;

import com.lyncode.xoai.dataprovider.xml.read.XmlReader;
import com.lyncode.xoai.dataprovider.xml.read.XmlReaderException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.conditions.FilterConditionConfiguration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.ParseException;

public abstract class UnaryConditionParser extends ConditionParser {
    @Override
    public FilterConditionConfiguration parse(XmlReader reader) throws ParseException {
        try {
            reader.proceedToNextElement();
            if (reader.elementNameIs("Condition")) {
                reader.proceedToNextElement();
                FilterConditionConfiguration build = build(super.parse(reader));
                reader.proceedToNextElement();
                reader.proceedToNextElement();
                return build;
            } else throw new ParseException("Expecting Condition");
        } catch (XmlReaderException e) {
            throw new ParseException(e);
        }
    }

    protected abstract FilterConditionConfiguration build(FilterConditionConfiguration parse);
}
