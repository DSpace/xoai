package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.conditions;

import com.lyncode.xoai.dataprovider.xml.read.XmlReader;
import com.lyncode.xoai.dataprovider.xml.read.XmlReaderException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.conditions.FilterConditionConfiguration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.ParseException;

public abstract class BinaryConditionParser extends ConditionParser {
    @Override
    public FilterConditionConfiguration parse(XmlReader reader) throws ParseException {
        try {
            reader.proceedToNextElement();
            FilterConditionConfiguration left = null;
            FilterConditionConfiguration right = null;

            if (reader.elementNameIs("LeftCondition")) {
                reader.proceedToNextElement();
                left = super.parse(reader);
            } else throw new ParseException("Expecting LeftCondition element");

            reader.proceedToNextElement();
            if (reader.elementNameIs("RightCondition")) {
                reader.proceedToNextElement();
                right = super.parse(reader);
                reader.proceedToNextElement();
            } else throw new ParseException("Expecting RightCondition element");

            return build(left, right);
        } catch (XmlReaderException e) {
            throw new ParseException(e);
        }
    }

    protected abstract FilterConditionConfiguration build(FilterConditionConfiguration left, FilterConditionConfiguration right);


}
