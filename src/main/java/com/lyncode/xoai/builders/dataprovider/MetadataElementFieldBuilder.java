package com.lyncode.xoai.builders.dataprovider;

import com.lyncode.xoai.dataprovider.xml.xoai.Element;

public class MetadataElementFieldBuilder {

    private String name;
    private String value;

    public MetadataElementFieldBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MetadataElementFieldBuilder withValue(String value) {
        this.value = value;
        return this;
    }

    public Element.Field build() {
        Element.Field field = new Element.Field();
        field.setName(name);
        field.setValue(value);
        return field;
    }
}
