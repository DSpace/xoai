package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.conditions;


import com.lyncode.xoai.dataprovider.xml.xoaiconfig.conditions.FilterConditionConfiguration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.conditions.NotConditionConfiguration;

public class NotConditionParser extends UnaryConditionParser {
    @Override
    protected FilterConditionConfiguration build(FilterConditionConfiguration parse) {
        return new NotConditionConfiguration().withCondition(parse);
    }
}
