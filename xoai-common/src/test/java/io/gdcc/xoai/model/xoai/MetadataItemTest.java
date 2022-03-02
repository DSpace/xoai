/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.model.xoai;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class MetadataItemTest {

	@Test
	public void propertyMapOverridesPreviousEntry(){
		MetadataItem item = new MetadataItem();
		item.addProperty("key", "value1");
		item.addProperty("key", "value2");
		
		assertThat("value2", equalTo(item.getProperty("key")));
	}
	
	@Test
	public void keyForXMLLANG(){
		MetadataItem item = new MetadataItem();
		item.addProperty(MetadataItem.XMLLANG, "pt-PT");
		
		assertThat("pt-PT", equalTo(item.getProperty(MetadataItem.XMLLANG)));
		assertThat("xml:lang", equalTo(MetadataItem.XMLLANG));
	}
}
