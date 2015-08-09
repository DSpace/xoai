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

package org.dspace.xoai.services.impl;

import org.dspace.xoai.model.xoai.Element;
import org.dspace.xoai.model.xoai.Field;
import org.dspace.xoai.model.xoai.XOAIMetadata;
import org.dspace.xoai.services.api.MetadataSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.join;

public class MetadataSearchImpl extends AbstractMetadataSearcher<String> implements MetadataSearch<String> {

    public MetadataSearchImpl (XOAIMetadata metadata) {
    	super(metadata);
    }

    protected void consume(List<String> newNames, Element element) {
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
