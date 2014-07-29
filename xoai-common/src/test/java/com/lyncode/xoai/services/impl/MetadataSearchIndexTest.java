package com.lyncode.xoai.services.impl;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.lyncode.xoai.model.xoai.Element;
import com.lyncode.xoai.model.xoai.XOAIMetadata;
import com.lyncode.xoai.services.api.MetadataSearch;

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
