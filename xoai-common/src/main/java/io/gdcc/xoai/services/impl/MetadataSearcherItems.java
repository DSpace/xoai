/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.services.impl;

import io.gdcc.xoai.model.xoai.Element;
import io.gdcc.xoai.model.xoai.Field;
import io.gdcc.xoai.model.xoai.MetadataItem;
import io.gdcc.xoai.model.xoai.XOAIMetadata;
import io.gdcc.xoai.services.api.MetadataSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation whose searches return {@link MetadataItem} elements.
 * Useful for when more information for each OAI metadata item is available (e.g., xml attributes like xml:lang).
 * @author mmalmeida
 *
 */
public class MetadataSearcherItems extends AbstractMetadataSearcher<MetadataItem> implements MetadataSearch<MetadataItem> {

	public MetadataSearcherItems(XOAIMetadata metadata) {
		super(metadata);
	}

	@Override
	protected void consume(List<String> newNames, Element element) {
		 List<String> names = new ArrayList<>(newNames);
	        names.add(element.getName());

	        if (!element.getFields().isEmpty()) {
	        	add(String.join(".", names), element.getFields());
	        }

	        if (!element.getElements().isEmpty()) {
	            for (Element subElement : element.getElements()) {
	                consume(names, subElement);
	            }
	        }		
	}

	private void add(String name, List<Field> fields) {
		 if (!index.containsKey(name))
	            index.put(name, new ArrayList<>());
		 
		 MetadataItem newElement = new MetadataItem();
		 for (Field field : fields) {
			 if (field.getName() != null && !field.getName().equals(DEFAULT_FIELD)) {
				 newElement.addProperty(field.getName(),field.getValue());
			 }else{
				 newElement.setValue(field.getValue());
			 }
		 }
	        index.get(name).add(newElement);
		
	}

	@Override
	public MetadataItem findOne(String xoaiPath) {
		return super.findOne(xoaiPath);
	}

	@Override
	public List<MetadataItem> findAll(String xoaiPath) {
		return super.findAll(xoaiPath);
	}


	

	

}
