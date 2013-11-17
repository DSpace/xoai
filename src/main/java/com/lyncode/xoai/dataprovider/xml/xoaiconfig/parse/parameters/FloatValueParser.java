package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.parameters;

import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.FloatValue;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.SimpleType;

public class FloatValueParser extends SimpleTypeParser {

    @Override
    protected SimpleType parse(String name, String data) {
        return (SimpleType) new FloatValue()
                .withValue(Float.valueOf(data))
                .withName(name);
    }
}
