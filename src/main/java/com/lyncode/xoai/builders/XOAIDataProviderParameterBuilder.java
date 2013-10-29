package com.lyncode.xoai.builders;

import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Parameter;

import static java.util.Arrays.asList;

public class XOAIDataProviderParameterBuilder {
    public static Parameter build(String key, String value) {
        Parameter parameter = new Parameter();
        parameter.setKey(key);
        parameter.getValue().add(value);
        return parameter;
    }

    public static Parameter build(String key, String... values) {
        Parameter parameter = new Parameter();
        parameter.setKey(key);
        parameter.getValue().addAll(asList(values));
        return parameter;
    }
}
