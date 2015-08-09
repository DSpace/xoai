/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.services.impl.metadataSearcherMultipleFields;

import org.dspace.xoai.model.xoai.Element;
import org.dspace.xoai.model.xoai.XOAIMetadata;
import org.dspace.xoai.services.api.MetadataSearch;
import org.dspace.xoai.services.impl.MetadataSearchImpl;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MetadataSearchIndexTest {
	
	@Test
	public void retrieveIndex(){
		XOAIMetadata xoaiMetadata = new XOAIMetadata();
		xoaiMetadata.getElements().add(new Element("dc").withElement(new Element("creator").withField("value", "john doe")));
		MetadataSearch metadataSearch = new MetadataSearchImpl(xoaiMetadata );
		Map<String,List<String>> index = metadataSearch.index();
		
		assertEquals(1,index.size());
		
		assertEquals("john doe",index.get("dc.creator").get(0));
		
	}

	@Test
	public void emptyMetadataReturnsEmpty(){
		XOAIMetadata xoaiMetadata = new XOAIMetadata();
		MetadataSearch metadataSearch = new MetadataSearchImpl(xoaiMetadata );
		Map<String,List<String>> index = metadataSearch.index();
		
		assertEquals(0,index.size());
		
		
	}

}
