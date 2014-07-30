/**
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

package com.lyncode.xoai.services.impl;

import static org.apache.commons.lang3.StringUtils.join;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lyncode.xoai.model.xoai.Element;
import com.lyncode.xoai.model.xoai.Field;
import com.lyncode.xoai.model.xoai.XOAIMetadata;
import com.lyncode.xoai.services.api.MetadataSearch;

public class MetadataSearchImpl implements MetadataSearch {
    private static final String DEFAULT_FIELD = "value";
    private Map<String, List<String>> index = new HashMap<String, List<String>>();

    public MetadataSearchImpl (XOAIMetadata metadata) {
        for (Element element : metadata.getElements()) {
            consume(new ArrayList<String>(), element);
        }
    }

    private void consume(List<String> newNames, Element element) {
        List<String> names = new ArrayList<String>(newNames);
        names.add(element.getName());

        if (!element.getFields().isEmpty()) {
            for (Field field : element.getFields()) {
            	if (field.getName() != null && !field.getName().equals(DEFAULT_FIELD)) {
        			add(join(names, ".")+":"+ field.getName(), field.getValue());
        		} else {
        			add(join(names, "."), field.getValue());	
        		}
            }
        }

        if (!element.getElements().isEmpty()) {
            for (Element subElement : element.getElements()) {
                consume(names, subElement);
            }
        }
    }

    private void add(String name, String value) {
        if (!index.containsKey(name))
            index.put(name, new ArrayList<String>());

        index.get(name).add(value);
    }

    @Override
    public String findOne(String xoaiPath) {
        List<String> strings = index.get(xoaiPath);
        if (strings != null && !strings.isEmpty())
            return strings.get(0);
        return null;
    }

    @Override
    public List<String> findAll(String xoaiPath) {
        return index.get(xoaiPath);
    }

	@Override
	public Map<String, List<String>> index() {
		return index;
	}
}
