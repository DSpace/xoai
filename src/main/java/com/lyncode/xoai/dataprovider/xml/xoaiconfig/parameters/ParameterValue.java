package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters;

import com.lyncode.xoai.dataprovider.xml.XMLWritable;

public abstract class ParameterValue<T> implements XMLWritable {
    private String name;

    public SimpleType asSimpleType() {
        return (SimpleType) this;
    }

    public ParameterList asParameterList() {
        return (ParameterList) this;
    }

    public ParameterMap asParameterMap() {
        return (ParameterMap) this;
    }

    public String getName() {
        return name;
    }

    public ParameterValue withName(String value) {
        this.name = value;
        return this;
    }


    public boolean hasName() {
        return name != null;
    }
}
