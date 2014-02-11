package com.lyncode.xoai.dataprovider.filter;

import com.lyncode.xoai.dataprovider.model.ItemIdentifier;

public interface Filter {
    boolean isItemShown(ItemIdentifier item);
}
