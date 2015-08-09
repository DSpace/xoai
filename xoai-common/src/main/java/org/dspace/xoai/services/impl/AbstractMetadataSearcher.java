/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.services.impl;

import org.dspace.xoai.model.xoai.Element;
import org.dspace.xoai.model.xoai.XOAIMetadata;
import org.dspace.xoai.services.api.MetadataSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractMetadataSearcher<T> implements MetadataSearch<T> {
	
	protected static final String DEFAULT_FIELD = "value";
	protected Map<String, List<T>> index = new HashMap<String, List<T>>();
	
	
    public AbstractMetadataSearcher (XOAIMetadata metadata) {
        for (Element element : metadata.getElements()) {
            consume(new ArrayList<String>(), element);
        }
    }
	@Override
	public T findOne(String xoaiPath){
		 List<T> elements = index.get(xoaiPath);
	        if (elements != null && !elements.isEmpty())
	            return elements.get(0);
	        return null;
	};
	@Override
	public List<T> findAll(String xoaiPath){
		return index.get(xoaiPath);
	};
	
	@Override
	public Map<String, List<T>> index() {
		return index;
	}

	protected void init(XOAIMetadata metadata) {
		for (Element element : metadata.getElements()) {
            consume(new ArrayList<String>(), element);
        }
		
	}
	protected abstract void consume(List<String> newNames, Element element);

}
