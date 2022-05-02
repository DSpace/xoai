/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.handlers.helpers;

import io.gdcc.xoai.dataprovider.model.ItemIdentifier;

public class ItemIdentifyHelper {
    private final ItemIdentifier item;

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
