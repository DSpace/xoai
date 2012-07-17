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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lyncode.xoai.dataprovider.core.Set;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.filter.AbstractFilter;
import com.lyncode.xoai.dataprovider.filter.FilterManager;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.BundleReference;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration.Sets;

/**
 * @author DSpace @ Lyncode
 * @version 2.1.0
 */
public class StaticSetManager {
	// private static Logger log = LogManager.getLogger(StaticSetManager.class);
	private Map<String, StaticSet> _contexts;

	public StaticSetManager(Sets config, FilterManager fm)
			throws ConfigurationException {
		_contexts = new HashMap<String, StaticSet>();
		for (com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration.Sets.Set s : config
				.getSet()) {
			List<AbstractFilter> filters = new ArrayList<AbstractFilter>();
			for (BundleReference r : s.getFilter()) {
				if (!fm.filterExists(r.getRefid()))
					throw new ConfigurationException("Filter referred as "
							+ r.getRefid() + " does not exist");
				filters.add(fm.getFilter(r.getRefid()));
			}
			StaticSet set = new StaticSet(filters, s.getPattern(), s.getName());
			_contexts.put(s.getId(), set);
		}
	}

	public boolean setExists(String id) {
		return this._contexts.containsKey(id);
	}

	public StaticSet getSet(String id) {
		return _contexts.get(id);
	}

	public List<Set> getSets() {
		return new ArrayList<Set>(_contexts.values());
	}
}
