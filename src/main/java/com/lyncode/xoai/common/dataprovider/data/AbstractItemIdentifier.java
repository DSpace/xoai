/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.xoai.common.dataprovider.data;

import java.util.Date;
import java.util.List;

import com.lyncode.xoai.common.dataprovider.core.ReferenceSet;
import com.lyncode.xoai.common.dataprovider.core.XOAIContext;
import com.lyncode.xoai.common.dataprovider.filter.AbstractFilter;
import com.lyncode.xoai.common.dataprovider.sets.StaticSet;


/**
 * @author DSpace @ Lyncode
 * @version 2.0.0
 */
public abstract class AbstractItemIdentifier {
    public abstract String getIdentifier ();
    /**
     * Creation, modification or deletion date
     *
     * @return
     */
    public abstract Date getDatestamp ();
    public abstract List<ReferenceSet> getSets ();
    public abstract boolean isDeleted ();


    public List<ReferenceSet> getSets (XOAIContext context) {
        List<ReferenceSet> list = this.getSets();
        List<StaticSet> listStatic = context.getStaticSets();
        for (StaticSet s : listStatic) {
            boolean filter = false;
            for (AbstractFilter f : s.getFilters()) {
                if (!f.isItemShown(this)) {
                    filter = true;
                }
            }
            if (!filter)
                list.add(s);
        }
        return list;
    }
}
