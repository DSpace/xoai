package com.lyncode.builder;

import com.google.common.base.Function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;

public class ListBuilder<T> implements Builder<List<T>> {
    private List<T> list;

    public ListBuilder() {
        list = new ArrayList<T>();
    }

    public ListBuilder<T> add(Collection<T> list) {
        this.list.addAll(list);
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

    public <E> List<E> build(Function<T, E> transformer) {
        return newArrayList(transform(list, transformer));
    }
}