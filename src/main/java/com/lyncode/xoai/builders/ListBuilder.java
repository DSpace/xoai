package com.lyncode.xoai.builders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListBuilder<T> implements Builder<List<T>> {
    private List<T> list;

    public ListBuilder() {
        list = new ArrayList<T>();
    }

    public ListBuilder<T> add(Collection<T> list) {
        list.addAll(list);
        return this;
    }

    public ListBuilder<T> add(T... list) {
        for (T t : list)
            this.list.add(t);
        return this;
    }

    public List<T> build() {
        return list;
    }

    public <E> List<E> build(Transformer<T, E> transformer) {
        List<E> transformed = new ArrayList<E>();
        for (T elem : this.list)
            transformed.add(transformer.transform(elem));
        return transformed;
    }

    public abstract static class Transformer<T, E> {
        public abstract E transform(T elem);
    }
}
