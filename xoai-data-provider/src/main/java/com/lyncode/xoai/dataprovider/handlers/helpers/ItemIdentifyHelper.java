package com.lyncode.xoai.dataprovider.handlers.helpers;

import com.lyncode.xoai.dataprovider.model.ItemIdentifier;

public class ItemIdentifyHelper {
    private ItemIdentifier item;

    public ItemIdentifyHelper(ItemIdentifier item) {
        this.item = item;
    }
//
//    public List<ReferenceSet> getSets(XOAIContext context) {
//        List<ReferenceSet> list = this.item.getSets();
//        for (Set set : context.getStaticSets()) {
//            if (set.hasCondition() && set.getCondition().getFilter().isItemShown(item))
//                list.add(set);
//            else
//                list.add(set);
//        }
//        return list;
//    }


}
