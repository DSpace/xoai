package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.conditions;

import com.lyncode.xoai.dataprovider.xml.xoaiconfig.conditions.FilterConditionConfiguration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.conditions.OrConditionConfiguration;

public class OrConditionParser extends BinaryConditionParser {
    @Override
    protected FilterConditionConfiguration build(FilterConditionConfiguration left, FilterConditionConfiguration right) {
        return new OrConditionConfiguration().withLeft(left).withRight(right);
    }
}
