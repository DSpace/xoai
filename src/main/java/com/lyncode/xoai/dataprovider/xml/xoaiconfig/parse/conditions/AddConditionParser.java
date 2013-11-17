package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.conditions;

import com.lyncode.xoai.dataprovider.xml.xoaiconfig.conditions.AndConditionConfiguration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.conditions.FilterConditionConfiguration;

public class AddConditionParser extends BinaryConditionParser {
    @Override
    protected FilterConditionConfiguration build(FilterConditionConfiguration left, FilterConditionConfiguration right) {
        return new AndConditionConfiguration()
                .withLeft(left)
                .withRight(right);
    }
}
