package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.parameters;

import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.DoubleValue;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.SimpleType;

public class DoubleValueParser extends SimpleTypeParser {

    @Override
    protected SimpleType parse(String name, String data) {
        return (SimpleType) new DoubleValue()
                .withValue(Double.valueOf(data))
                .withName(name);
    }
}
