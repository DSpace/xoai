package com.lyncode.xoai.dataprovider.data.internal;

import com.lyncode.xoai.dataprovider.core.ReferenceSet;
import com.lyncode.xoai.dataprovider.core.XOAIContext;
import com.lyncode.xoai.dataprovider.data.AbstractItemIdentifier;
import com.lyncode.xoai.dataprovider.filter.Filter;
import com.lyncode.xoai.dataprovider.sets.StaticSet;

import java.util.List;

public class ItemIdentify {
    private AbstractItemIdentifier identifier;

    public ItemIdentify(AbstractItemIdentifier id) {
        this.identifier = id;
    }

    public List<ReferenceSet> getSets(XOAIContext context) {
        List<ReferenceSet> list = this.identifier.getSets();
        List<StaticSet> listStatic = context.getStaticSets();
        for (StaticSet s : listStatic) {
            boolean filter = false;
            for (Filter f : s.getFilters()) {
                if (!f.isItemShown(this.identifier)) {
                    filter = true;
                }
            }
            if (!filter)
                list.add(s);
        }
        return list;
    }


}
