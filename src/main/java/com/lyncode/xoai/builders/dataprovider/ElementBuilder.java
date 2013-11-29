package com.lyncode.xoai.builders.dataprovider;

import com.lyncode.builder.Builder;
import com.lyncode.xoai.dataprovider.xml.xoai.Element;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.asList;

public class ElementBuilder implements Builder<Element> {
    private String name;
    private Collection<Element> subElements = new ArrayList<Element>();
    private Collection<Element.Field> fields = new ArrayList<Element.Field>();

    public ElementBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ElementBuilder withSubElements(Element... subElements) {
        this.subElements.addAll(asList(subElements));
        return this;
    }

    public ElementBuilder withFields(Element.Field... fields) {
        this.fields.addAll(asList(fields));
        return this;
    }

    public ElementBuilder withField(String name, String value) {
        this.fields.add(new MetadataElementFieldBuilder().withName(name).withValue(value).build());
        return this;
    }

    public Element build() {
        Element element = new Element();
        element.setName(name);
        element.getElement().addAll(subElements);
        element.getField().addAll(fields);
        return element;
    }
}
