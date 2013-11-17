package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.parameters;

import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.IntValue;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.SimpleType;

public class IntValueParser extends SimpleTypeParser {

    @Override
    protected SimpleType parse(String name, String data) {
        return (SimpleType) new IntValue()
                .withValue(Integer.valueOf(data))
                .withName(name);
    }
}
