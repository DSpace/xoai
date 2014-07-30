package com.lyncode.xoai.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lyncode.xoai.model.xoai.Element;
import com.lyncode.xoai.model.xoai.XOAIMetadata;
import com.lyncode.xoai.services.api.MetadataSearch;

public abstract class AbstractMetadataSearcher<T> implements MetadataSearch<T> {
	
	protected static final String DEFAULT_FIELD = "value";
	protected Map<String, List<T>> index = new HashMap<String, List<T>>();
	
	
    public AbstractMetadataSearcher (XOAIMetadata metadata) {
        for (Element element : metadata.getElements()) {
            consume(new ArrayList<String>(), element);
        }
    }
	@Override
	public abstract T findOne(String xoaiPath);
	@Override
	public abstract List<T> findAll(String xoaiPath);
	
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
