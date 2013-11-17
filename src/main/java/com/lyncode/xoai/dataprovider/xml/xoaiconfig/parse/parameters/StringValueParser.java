package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.parameters;

import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.SimpleType;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.StringValue;

public class StringValueParser extends SimpleTypeParser {

    @Override
    protected SimpleType parse(String name, String data) {
        return (SimpleType) new StringValue()
                .withValue(data)
                .withName(name);
    }
}
