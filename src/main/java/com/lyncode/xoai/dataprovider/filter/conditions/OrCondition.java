package com.lyncode.xoai.dataprovider.filter.conditions;

import com.lyncode.xoai.dataprovider.data.Filter;
import com.lyncode.xoai.dataprovider.services.api.FilterResolver;

public class OrCondition implements Condition {
    private Condition left = null;
    private Condition right = null;
    private FilterResolver filterResolver;

    public OrCondition(FilterResolver filterResolver, Condition left, Condition right) {
        this.left = left;
        this.right = right;
        this.filterResolver = filterResolver;
    }


    /**
     * @return the left
     */
    public Condition getLeft() {
        return left;
    }

    /**
     * @return the right
     */
    public Condition getRight() {
        return right;
    }

    @Override
    public Filter getFilter() {
        return filterResolver.getFilter(this);
    }
}
