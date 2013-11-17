package com.lyncode.xoai.dataprovider.filter.conditions;

import com.lyncode.xoai.dataprovider.data.Filter;
import com.lyncode.xoai.dataprovider.services.api.FilterResolver;

public class AndCondition implements Condition {
    private FilterResolver resolver;
    private Condition left = null;
    private Condition right = null;

    public AndCondition(FilterResolver resolver, Condition left, Condition right) {
        this.resolver = resolver;
        this.left = left;
        this.right = right;
    }

    public Condition getLeft() {
        return left;
    }

    public Condition getRight() {
        return right;
    }


    @Override
    public Filter getFilter() {
        return resolver.getFilter(this);
    }
}
