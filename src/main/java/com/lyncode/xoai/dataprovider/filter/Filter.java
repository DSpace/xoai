package com.lyncode.xoai.dataprovider.filter;

import com.lyncode.xoai.dataprovider.data.AbstractItemIdentifier;
import com.lyncode.xoai.dataprovider.filter.conditions.AbstractCondition;


public class Filter {
    private AbstractCondition condition;
    
    public Filter (AbstractCondition condition) {
        this.condition = condition;
    }
    

    public boolean isItemShown(AbstractItemIdentifier item) {
        return this.condition.isItemShown(item);
    }
    
    public AbstractCondition getCondition () {
        return this.condition;
    }
}
