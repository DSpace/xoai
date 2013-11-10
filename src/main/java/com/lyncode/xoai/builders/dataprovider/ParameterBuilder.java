package com.lyncode.xoai.builders.dataprovider;

import com.lyncode.xoai.builders.Builder;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Parameter;

import static java.util.Arrays.asList;

public class ParameterBuilder implements Builder<Parameter> {
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

    private String key;
    private String[] values;

    public ParameterBuilder withKey(String key) {
        this.key = key;
        return this;
    }

    public ParameterBuilder withValue(String... values) {
        this.values = values;
        return this;
    }

    @Override
    public Parameter build() {
        Parameter parameter = new Parameter();
        parameter.setKey(key);
        parameter.getValue().addAll(asList(values));
        return new Parameter();
    }
}
