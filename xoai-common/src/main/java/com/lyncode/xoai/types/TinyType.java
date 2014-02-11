package com.lyncode.xoai.types;

public class TinyType<T> {
    private T value = null;

    public TinyType() {
    }

    public TinyType(T val) {
        this.value = val;
    }


    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
