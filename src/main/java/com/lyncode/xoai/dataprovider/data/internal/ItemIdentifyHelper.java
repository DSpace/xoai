package com.lyncode.xoai.dataprovider.data.internal;

import com.lyncode.xoai.dataprovider.core.ReferenceSet;
import com.lyncode.xoai.dataprovider.core.XOAIContext;
import com.lyncode.xoai.dataprovider.data.ItemIdentifier;
import com.lyncode.xoai.dataprovider.sets.StaticSet;

import java.util.List;

public class ItemIdentifyHelper {
    private ItemIdentifier item;

    public ItemIdentifyHelper(ItemIdentifier item) {
        this.item = item;
    }

    public List<ReferenceSet> getSets(XOAIContext context) {
        List<ReferenceSet> list = this.item.getSets();
        for (StaticSet staticSet : context.getStaticSets()) {
            if (staticSet.hasFilter() && staticSet.getFilter().isItemShown(item))
                list.add(staticSet);
            else
                list.add(staticSet);
        }
        return list;
    }


}
