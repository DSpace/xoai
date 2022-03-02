/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.services.impl.metadataSearcherMultipleFields;

import io.gdcc.xoai.model.xoai.Element;
import io.gdcc.xoai.model.xoai.MetadataItem;
import io.gdcc.xoai.model.xoai.XOAIMetadata;
import io.gdcc.xoai.services.api.MetadataSearch;
import io.gdcc.xoai.services.impl.MetadataSearcherItems;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class MetadataSearcherItemsTest {

	MetadataSearch<MetadataItem> searcher;

	private XOAIMetadata metadata;
	private Element subjectElement;

	private Element parentElement;

	@BeforeEach
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
		
		assertThat(1, equalTo(searcher.index().size()));
		assertThat("dc.subject", equalTo(searcher.index().keySet().iterator().next()));
	}
	
	@Test
	public void elementHasValueAndLangInformation() {
		searcher = new MetadataSearcherItems(metadata);
		
		List<MetadataItem> items = searcher.index().get("dc.subject");
		assertThat(1, equalTo(items.size()));
		assertThat("Ciências da Educação", equalTo(items.get(0).getValue()));
		assertThat("pt-PT", equalTo(items.get(0).getProperty("xml:lang")));
	}
	

	@Test
	public void normalBehaviourWhenOnlyValueExists() {
		subjectElement.getFields().clear();//resetting the fields
		subjectElement.withField(null, "Ciências da Educação");
		
		searcher = new MetadataSearcherItems(metadata);
		
		List<MetadataItem> items = searcher.index().get("dc.subject");
		assertThat(1, equalTo(items.size()));
		assertThat("Ciências da Educação", equalTo(items.get(0).getValue()));
	}
	
	//FindOne Tests - should be tested in abstract
	@Test
	public void findOneFindsElement() {
		
		searcher = new MetadataSearcherItems(metadata);
		
		MetadataItem element = searcher.findOne("dc.subject");
		assertThat("Ciências da Educação", equalTo(element.getValue()));
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
		assertThat(2, equalTo(elements.size()));
	}
}
