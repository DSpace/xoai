package com.lyncode.xoai.dataprovider.filter.conditions;

import com.lyncode.xoai.dataprovider.data.ItemIdentifier;

public class OrCondition implements Condition {
    private Condition left = null;
    private Condition right = null;

    public OrCondition(Condition left, Condition right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isItemShown(ItemIdentifier item) {
        return this.left.isItemShown(item) || this.right.isItemShown(item);
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

}
