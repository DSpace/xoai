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

package com.lyncode.xoai.common.dataprovider.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lyncode.xoai.common.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.common.dataprovider.xml.xoaiconfig.Configuration.Filters;

/**
 * @author DSpace @ Lyncode
 * @version 2.1.0
 */
public class FilterManager {
	// private static Logger log = LogManager.getLogger(FilterManager.class);
	private Map<String, AbstractFilter> _contexts;

	public FilterManager(Filters filters) throws ConfigurationException {
		_contexts = new HashMap<String, AbstractFilter>();
		for (com.lyncode.xoai.common.dataprovider.xml.xoaiconfig.Configuration.Filters.Filter f : filters
				.getFilter()) {
			try {
				Class<?> c = Class.forName(f.getClazz());
				Object obj = c.newInstance();
				if (obj instanceof AbstractFilter) {
					((AbstractFilter) obj).load(f.getParameter());
					_contexts.put(f.getId(), (AbstractFilter) obj);
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

	public boolean filterExists(String id) {
		return this._contexts.containsKey(id);
	}

	public AbstractFilter getFilter(String id) {
		return _contexts.get(id);
	}

	public List<AbstractFilter> getFilters() {
		return new ArrayList<AbstractFilter>(_contexts.values());
	}
}
