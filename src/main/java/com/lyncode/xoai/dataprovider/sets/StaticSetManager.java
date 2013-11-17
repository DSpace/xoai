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

package com.lyncode.xoai.dataprovider.sets;

import com.lyncode.xoai.dataprovider.core.Set;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.filter.FilterManager;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.SetConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 3.1.0
 */
public class StaticSetManager {
    // private static Logger log = LogManager.getLogger(StaticSetManager.class);
    private Map<String, StaticSet> sets;

    public StaticSetManager(List<SetConfiguration> setConfigurations, FilterManager filterManager)
            throws ConfigurationException {
        sets = new HashMap<String, StaticSet>();
        for (SetConfiguration configuredSet : setConfigurations) {
            StaticSet set = null;
            if (configuredSet.hasFilter())
                set = new StaticSet(filterManager.getFilter(configuredSet.getFilter().getReference()),
                        configuredSet.getSpec(), configuredSet.getName());
            else
                set = new StaticSet(configuredSet.getSpec(), configuredSet.getName());
            sets.put(configuredSet.getId(), set);
        }
    }

    public boolean setExists(String id) {
        return this.sets.containsKey(id);
    }

    public StaticSet getSet(String id) {
        return sets.get(id);
    }

    public List<Set> getSets() {
        return new ArrayList<Set>(sets.values());
    }
}
