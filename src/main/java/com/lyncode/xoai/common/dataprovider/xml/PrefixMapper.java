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

package com.lyncode.xoai.common.dataprovider.xml;

import java.util.HashMap;
import java.util.Map;

import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;

/**
 * @author DSpace @ Lyncode
 * @version 2.1.0
 */
public class PrefixMapper extends NamespacePrefixMapper {
	private Map<String, String> _prefix;

	public PrefixMapper() {
		_prefix = new HashMap<String, String>();
	}

	public void addPrefix(String namespace, String prefix) {
		_prefix.put(namespace, prefix);
	}

	public String getPreferredPrefix(String namespaceUri, String suggestion,
			boolean requirePrefix) {
		if (_prefix.containsKey(namespaceUri))
			return _prefix.get(namespaceUri);
		return suggestion;
	}

	@Override
	public String[] getPreDeclaredNamespaceUris() {
		return _prefix.keySet().toArray(new String[_prefix.size()]);
	}
}
