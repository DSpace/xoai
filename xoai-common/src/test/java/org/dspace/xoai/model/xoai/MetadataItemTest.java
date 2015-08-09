/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.model.xoai;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
