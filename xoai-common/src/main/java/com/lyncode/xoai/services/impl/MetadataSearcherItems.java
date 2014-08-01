package com.lyncode.xoai.services.impl;

import com.lyncode.xoai.model.xoai.Element;
import com.lyncode.xoai.model.xoai.Field;
import com.lyncode.xoai.model.xoai.MetadataItem;
import com.lyncode.xoai.model.xoai.XOAIMetadata;
import com.lyncode.xoai.services.api.MetadataSearch;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.join;

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
		 List<String> names = new ArrayList<String>(newNames);
	        names.add(element.getName());

	        if (!element.getFields().isEmpty()) {
	        	add(join(names, "."), element.getFields());
	        }

	        if (!element.getElements().isEmpty()) {
	            for (Element subElement : element.getElements()) {
	                consume(names, subElement);
	            }
	        }		
	}

	private void add(String name, List<Field> fields) {
		 if (!index.containsKey(name))
	            index.put(name, new ArrayList<MetadataItem>());
		 
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
