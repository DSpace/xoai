package com.lyncode.xoai.dataprovider.filter.conditions;

import com.lyncode.xoai.dataprovider.data.AbstractItemIdentifier;

public class NotCondition extends AbstractCondition {
    private AbstractCondition condition = null;

    public NotCondition(AbstractCondition condition) {
        this.condition = condition;
    }

    @Override
    public boolean isItemShown(AbstractItemIdentifier item) {
        return !this.condition.isItemShown(item);
    }

    public AbstractCondition getCondition() {
        return condition;
    }

}
