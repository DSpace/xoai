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

package com.lyncode.xoai.dataprovider.filter;

import com.lyncode.xoai.dataprovider.data.Filter;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.filter.conditions.*;
import com.lyncode.xoai.dataprovider.services.api.FilterResolver;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.ConditionConfiguration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.FilterConfiguration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.conditions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 * @version 3.1.0
 */
public class FilterManager {
    // private static Logger log = LogManager.getLogger(FilterManager.class);
    private Map<String, Condition> combinedFilters;
    private Map<String, CustomCondition> customConditions;

    public FilterManager(FilterResolver resolver, List<FilterConfiguration> filters, List<ConditionConfiguration> conditions) throws ConfigurationException {
        this.customConditions = new HashMap<String, CustomCondition>();
        for (ConditionConfiguration conditionConfiguration : conditions) {
            try {
                Class<?> filterClass = Class.forName(conditionConfiguration.getClazz());
                if (!Filter.class.isAssignableFrom(filterClass))
                    throw new ConfigurationException("Class " + conditionConfiguration.getClazz() + " does not implements Filter");

                customConditions.put(conditionConfiguration.getId(), new CustomCondition(resolver, (Class<? extends Filter>) filterClass, conditionConfiguration.getConfiguration()));
            } catch (ClassNotFoundException ex) {
                throw new ConfigurationException(ex.getMessage(), ex);
            }
        }

        combinedFilters = new HashMap<String, Condition>();
        for (FilterConfiguration combinedFilter : filters) {
            combinedFilters.put(combinedFilter.getId(), this.getDefinition(resolver, combinedFilter.getDefinition()));
        }
    }

    private Condition getDefinition(FilterResolver resolver, FilterConditionConfiguration filterCondition) {
        if (filterCondition.is(AndConditionConfiguration.class))
            return new AndCondition(
                    resolver,
                    this.getDefinition(resolver, ((AndConditionConfiguration) filterCondition).getLeft()),
                    this.getDefinition(resolver, ((AndConditionConfiguration) filterCondition).getRight()));
        else if (filterCondition.is(OrConditionConfiguration.class))
            return new OrCondition(
                    resolver,
                    this.getDefinition(resolver, ((OrConditionConfiguration) filterCondition).getLeft()),
                    this.getDefinition(resolver, ((OrConditionConfiguration) filterCondition).getRight()));
        else if (filterCondition.is(NotConditionConfiguration.class))
            return new NotCondition(resolver, this.getDefinition(resolver, ((NotConditionConfiguration) filterCondition).getCondition()));
        else return customConditions.get(((CustomConditionConfiguration) filterCondition).getFilter().getReference());
    }

    public boolean filterExists(String id) {
        return this.combinedFilters.containsKey(id) || this.customConditions.containsKey(id);
    }


    public Condition getFilter(String id) {
        if (combinedFilters.containsKey(id))
            return combinedFilters.get(id);
        return customConditions.get(id);
    }
}
