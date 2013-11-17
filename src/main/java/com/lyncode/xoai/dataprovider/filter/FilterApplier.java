package com.lyncode.xoai.dataprovider.filter;

import com.lyncode.xoai.dataprovider.data.ItemIdentifier;
import com.lyncode.xoai.dataprovider.filter.conditions.Condition;


public class FilterApplier {
    private Condition condition;

    public FilterApplier(Condition condition) {
        this.condition = condition;
    }

    public boolean isItemShown(ItemIdentifier item) {
        return condition.isItemShown(item);
    }

    public Condition getCondition() {
        return condition;
    }
}
