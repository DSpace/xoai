package com.lyncode.xoai.model.xoai;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MetadataItemTest {

	@Test
	public void propertyMapOverridesPreviousEntry(){
		MetadataItem item = new MetadataItem();
		item.addProperty("key", "value1");
		item.addProperty("key", "value2");
		
		assertEquals("value2",item.getProperty("key"));
	}
	
	@Test
	public void keyForXMLLANG(){
		MetadataItem item = new MetadataItem();
		item.addProperty(MetadataItem.XMLLANG, "pt-PT");
		
		assertEquals("pt-PT",item.getProperty(MetadataItem.XMLLANG));
		assertEquals("xml:lang",MetadataItem.XMLLANG);
	}
}
