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

package com.lyncode.xoai.dataprovider.configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Parameter;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.2
 */
public class Parameters {
	private Map<String, List<String>> _params;

	public Parameters(List<Parameter> param) {
		_params = new HashMap<String, List<String>>();
		for (Parameter p : param)
			_params.put(p.getKey(), p.getValue());
	}

	public List<String> getParameter(String key) {
		return _params.get(key);
	}

	public boolean hasParameter(String key) {
		return _params.containsKey(key);
	}

	public String getFirstParameter(String key) {
		if (this.hasParameter(key) && this.getParameter(key).size() > 0)
			return this.getParameter(key).get(0);
		else
			return null;
	}

	public Set<String> getKeys() {
		return this._params.keySet();
	}
}
