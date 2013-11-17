package com.lyncode.xoai.dataprovider.filter.conditions;

import com.lyncode.xoai.dataprovider.data.ItemIdentifier;

public class NotCondition implements Condition {
    private Condition condition = null;

    public NotCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public boolean isItemShown(ItemIdentifier item) {
        return !this.condition.isItemShown(item);
    }

    public Condition getCondition() {
        return condition;
    }

}
