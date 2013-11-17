package com.lyncode.xoai.dataprovider.xml.xoaiconfig.conditions;

import com.lyncode.xoai.dataprovider.xml.XMLWritable;

public abstract class FilterConditionConfiguration implements XMLWritable {
    public boolean is(Class<?> clazz) {
        return (this.getClass().isAssignableFrom(clazz));
    }

    public <T> T as(Class<T> clazz) {
        return clazz.cast(this);
    }
}
