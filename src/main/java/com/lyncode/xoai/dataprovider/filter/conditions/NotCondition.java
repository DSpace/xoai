package com.lyncode.xoai.dataprovider.filter.conditions;

import com.lyncode.xoai.dataprovider.data.Filter;
import com.lyncode.xoai.dataprovider.services.api.FilterResolver;

public class NotCondition implements Condition {
    private Condition condition = null;
    private FilterResolver filterResolver;

    public NotCondition(FilterResolver filterResolver, Condition condition) {
        this.condition = condition;
        this.filterResolver = filterResolver;
    }

    public Condition getCondition() {
        return condition;
    }

    @Override
    public Filter getFilter() {
        return filterResolver.getFilter(this);
    }
}
