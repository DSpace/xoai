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
            if (staticSet.hasCondition() && staticSet.getCondition().getFilter().isItemShown(item)) {
                // this set has a condition and the current item fulfills it so
                // add the set to the item
                list.add(staticSet);
            } else if (!staticSet.hasCondition()) {
                // this set has no condition so always add the set to the item
                list.add(staticSet);
            }
            // otherwise: the set has a condition which is not fulfilled by the
            // current item so don't add.
        }
        return list;
    }

}
