package com.lyncode.xoai.services.impl.metadataSearcherMultipleFields;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.lyncode.xoai.model.xoai.Element;
import com.lyncode.xoai.model.xoai.MetadataItem;
import com.lyncode.xoai.model.xoai.XOAIMetadata;
import com.lyncode.xoai.services.api.MetadataSearch;
import com.lyncode.xoai.services.impl.MetadataSearcherItems;

public class MetadataSearcherItemsTest {

	MetadataSearch<MetadataItem> searcher;

	private XOAIMetadata metadata;
	private Element subjectElement;

	@Before
	public void setUp() {

		metadata = new XOAIMetadata();

		Element parentElement = new Element("dc");

		subjectElement = new Element("subject");
		subjectElement.withField(null, "Ciências da Educação");
		subjectElement.withField("xml:lang", "pt-PT");

		parentElement.withElement(subjectElement);

		metadata.getElements().add(parentElement);
	}

	@Test
	public void indexHasOneKey() {
		searcher = new MetadataSearcherItems(metadata);
		
		assertEquals(1,searcher.index().size());
		assertEquals("dc.subject",searcher.index().keySet().iterator().next());
	}
	
	@Test
	public void elementHasValueAndLangInformation() {
		searcher = new MetadataSearcherItems(metadata);
		
		List<MetadataItem> items = searcher.index().get("dc.subject");
		assertEquals(1,items.size());
		assertEquals("Ciências da Educação",items.get(0).getValue());
		assertEquals("pt-PT",items.get(0).getProperty("xml:lang"));
	}
	

	@Test
	public void normalBehaviourWhenOnlyValueExists() {
		subjectElement.getFields().clear();//resetting the fields
		subjectElement.withField(null, "Ciências da Educação");
		
		searcher = new MetadataSearcherItems(metadata);
		
		List<MetadataItem> items = searcher.index().get("dc.subject");
		assertEquals(1,items.size());
		assertEquals("Ciências da Educação",items.get(0).getValue());
	}
}
