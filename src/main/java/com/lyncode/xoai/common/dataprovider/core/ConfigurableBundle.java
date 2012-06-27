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

package com.lyncode.xoai.common.dataprovider.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.xoai.common.dataprovider.configuration.Parameters;
import com.lyncode.xoai.common.dataprovider.xml.xoaiconfig.Parameter;

/**
 * @author DSpace @ Lyncode
 * @version 2.0.0
 */
public abstract class ConfigurableBundle {
	private static Logger log = LogManager.getLogger(ConfigurableBundle.class);
	private Parameters _parameters;

	public void load(List<Parameter> parameters) {
		_parameters = new Parameters(parameters);
	}

	public List<String> getParameters(String key) {
		if (!_parameters.hasParameter(key))
			return new ArrayList<String>();
		return _parameters.getParameter(key);
	}

	public String getParameter(String key) {
		String p = _parameters.getFirstParameter(key);
		log.debug("Parameter: " + key + " = " + p);
		return p;
	}

	public String getParameter(String key, String none) {
		if (!hasParameter(key))
			return none;
		String p = _parameters.getFirstParameter(key);
		log.debug("Parameter: " + key + " = " + p);
		return p;
	}

	public boolean hasParameter(String key) {
		return _parameters.hasParameter(key);
	}

	public List<String> getKeys() {
		return new ArrayList<String>(_parameters.getKeys());
	}
}
