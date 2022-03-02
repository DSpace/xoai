/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

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

package io.gdcc.xoai.services.impl;

import io.gdcc.xoai.model.xoai.Element;
import io.gdcc.xoai.model.xoai.Field;
import io.gdcc.xoai.model.xoai.XOAIMetadata;
import io.gdcc.xoai.services.api.MetadataSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MetadataSearchImpl extends AbstractMetadataSearcher<String> implements MetadataSearch<String> {

    public MetadataSearchImpl (XOAIMetadata metadata) {
    	super(metadata);
    }

    protected void consume(List<String> newNames, Element element) {
        List<String> names = new ArrayList<>(newNames);
        names.add(element.getName());

        if (!element.getFields().isEmpty()) {
            for (Field field : element.getFields()) {
            	if (field.getName() != null && !field.getName().equals(DEFAULT_FIELD)) {
        			add(String.join(".", names)+":"+ field.getName(), field.getValue());
        		} else {
        			add(String.join(".", names), field.getValue());
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
            index.put(name, new ArrayList<>());

        index.get(name).add(value);
    }

    @Override
    public String findOne(String xoaiPath) {
    	return super.findOne(xoaiPath);
    }

    @Override
    public List<String> findAll(String xoaiPath) {
        return super.findAll(xoaiPath);
    }

	@Override
	public Map<String, List<String>> index() {
		return index;
	}
}
