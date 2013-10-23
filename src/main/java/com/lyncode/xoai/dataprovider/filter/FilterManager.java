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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.filter.conditions.AbstractCondition;
import com.lyncode.xoai.dataprovider.filter.conditions.AndCondition;
import com.lyncode.xoai.dataprovider.filter.conditions.CustomCondition;
import com.lyncode.xoai.dataprovider.filter.conditions.NotCondition;
import com.lyncode.xoai.dataprovider.filter.conditions.OrCondition;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.ConditionDefinitionType;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration.Filters;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 3.1.0
 */
public class FilterManager {
	// private static Logger log = LogManager.getLogger(FilterManager.class);
	private Map<String, Filter> _filters;
	private Map<String, CustomCondition> _conditions;
	

	public FilterManager(Filters filters) throws ConfigurationException {
	    _conditions = new HashMap<String, CustomCondition>();
		if (filters != null && filters.getFilter() != null) {
			for (com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration.Filters.CustomFilter f : filters
					.getCustomFilter()) {
				try {
					Class<?> c = Class.forName(f.getClazz());
					Object obj = c.newInstance();
					if (obj instanceof AbstractCondition) {
						((AbstractCondition) obj).load(f.getParameter());
						_conditions.put(f.getId(), (CustomCondition) obj);
					}
				} catch (InstantiationException ex) {
					throw new ConfigurationException(ex.getMessage(), ex);
				} catch (IllegalAccessException ex) {
					throw new ConfigurationException(ex.getMessage(), ex);
				} catch (ClassNotFoundException ex) {
					throw new ConfigurationException(ex.getMessage(), ex);
				}
			}
		}
		
		_filters = new HashMap<String, Filter>();
		for (com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration.Filters.Filter f : filters.getFilter()) {
		    _filters.put(f.getId(), new Filter(this.getDefinition(f.getDefinition())));
		}
	}

	private AbstractCondition getDefinition(ConditionDefinitionType definition) {
	    if (definition.getAnd() != null)
	        return new AndCondition(
	                                this.getDefinition(definition.getAnd().getLeftCondition()),
	                                this.getDefinition(definition.getAnd().getLeftCondition()));
	    else if (definition.getOr() != null)
	        return new OrCondition(
                                    this.getDefinition(definition.getOr().getLeftCondition()),
                                    this.getDefinition(definition.getOr().getLeftCondition()));
	    else if (definition.getNot() != null)
	        return new NotCondition(this.getDefinition(definition.getNot().getCondition()));
	    else return _conditions.get(definition.getCustom().getRefid());
    }
	
	public List<AbstractCondition> getConditions() {
	    return new ArrayList<AbstractCondition>(_conditions.values());
	}

    public boolean filterExists(String id) {
		return this._filters.containsKey(id);
	}

	public Filter getFilter(String id) {
		return _filters.get(id);
	}

	public List<Filter> getFilters() {
		return new ArrayList<Filter>(_filters.values());
	}
}
