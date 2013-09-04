package com.lyncode.xoai.dataprovider.filter.conditions;

import com.lyncode.xoai.dataprovider.data.AbstractItemIdentifier;

public class AndCondition extends AbstractCondition {
    private AbstractCondition left = null;
    private AbstractCondition right = null;

    public AndCondition(AbstractCondition left, AbstractCondition right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isItemShown(AbstractItemIdentifier item) {
        return this.left.isItemShown(item) && this.right.isItemShown(item);
    }

}
