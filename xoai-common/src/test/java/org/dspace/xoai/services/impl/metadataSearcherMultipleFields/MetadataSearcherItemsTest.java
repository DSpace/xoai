/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.services.impl.metadataSearcherMultipleFields;

import org.dspace.xoai.model.xoai.Element;
import org.dspace.xoai.model.xoai.MetadataItem;
import org.dspace.xoai.model.xoai.XOAIMetadata;
import org.dspace.xoai.services.api.MetadataSearch;
import org.dspace.xoai.services.impl.MetadataSearcherItems;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MetadataSearcherItemsTest {

	MetadataSearch<MetadataItem> searcher;

	private XOAIMetadata metadata;
	private Element subjectElement;

	private Element parentElement;

	@Before
	public void setUp() {

		metadata = new XOAIMetadata();

		parentElement = new Element("dc");

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
	
	//FindOne Tests - should be tested in abstract
	@Test
	public void findOneFindsElement() {
		
		searcher = new MetadataSearcherItems(metadata);
		
		MetadataItem element = searcher.findOne("dc.subject");
		assertEquals("Ciências da Educação",element.getValue());
	}
	
	//FindAll Tests - should be tested in abstract
	@Test
	public void findAllFindsElements() {
		
		subjectElement = new Element("subject");
		subjectElement.withField(null, "English subject");
		subjectElement.withField("xml:lang", "en-GB");

		parentElement.withElement(subjectElement);
		
		searcher = new MetadataSearcherItems(metadata);
		
		List<MetadataItem> elements = searcher.findAll("dc.subject");
		assertEquals(2,elements.size());
	}
}
