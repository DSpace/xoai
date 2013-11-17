package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse;

import com.lyncode.xoai.dataprovider.xml.read.XmlReader;
import com.lyncode.xoai.dataprovider.xml.read.XmlReaderException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.FilterConfiguration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.conditions.ConditionParser;

public class FilterConfigurationParser extends Parser<FilterConfiguration> {
    private final ConditionParser conditionParser = new ConditionParser();

    @Override
    public FilterConfiguration parse(XmlReader reader) throws ParseException {
        try {
            FilterConfiguration filterConfiguration = new FilterConfiguration(reader.getAttribute("id"));
            reader.proceedToNextElement();

            if (reader.isStart() && reader.elementNameIs("Definition")) {
                reader.proceedToNextElement();
                filterConfiguration.withDefinition(conditionParser.parse(reader));
                reader.proceedToNextElement();
            } else
                throw new ParseException("Expecting Definition Element");

            reader.proceedToNextElement();
            return filterConfiguration;
        } catch (XmlReaderException e) {
            throw new ParseException(e);
        }
    }
}
