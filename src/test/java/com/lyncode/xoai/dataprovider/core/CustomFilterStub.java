package com.lyncode.xoai.dataprovider.core;

import com.lyncode.xoai.dataprovider.data.AbstractItemIdentifier;
import com.lyncode.xoai.dataprovider.filter.conditions.CustomCondition;


public class CustomFilterStub extends CustomCondition {

    public CustomFilterStub() {
    }


    @Override
    public boolean isItemShown(AbstractItemIdentifier item) {
        return false;
    }

}
