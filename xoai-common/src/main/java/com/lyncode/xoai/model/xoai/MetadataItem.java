package com.lyncode.xoai.model.xoai;

import java.util.HashMap;
import java.util.Map;

public class MetadataItem {
	String value;
	Map<String,String> properties = new HashMap<String, String>();

	public void addProperty(String name, String value) {
		//TODO - TDD a better implementation of this
		properties.put(name, value);
		
	}

	public void setValue(String value) {
		this.value = value;
		
	}

	public Object getValue() {
		return value;
	}

	public String getProperty(String property) {
		return properties.get(property);
	}

}
