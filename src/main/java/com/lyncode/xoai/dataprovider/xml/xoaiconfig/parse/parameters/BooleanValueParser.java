package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.parameters;

import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.BooleanValue;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.SimpleType;

public class BooleanValueParser extends SimpleTypeParser {

    @Override
    protected SimpleType parse(String name, String data) {
        return (SimpleType) new BooleanValue()
                .withValue(Boolean.valueOf(data))
                .withName(name);
    }
}
