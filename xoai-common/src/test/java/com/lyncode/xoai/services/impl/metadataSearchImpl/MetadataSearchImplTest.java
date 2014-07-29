package com.lyncode.xoai.services.impl.metadataSearchImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.lyncode.xoai.model.xoai.Element;
import com.lyncode.xoai.model.xoai.XOAIMetadata;
import com.lyncode.xoai.services.impl.MetadataSearchImpl;

public class MetadataSearchImplTest {

	private XOAIMetadata metadata;
	private Element creatorElement;
	private Element subjectElement;
	
	@Before
	public void setUp() {
		metadata = new XOAIMetadata();
		
		Element parentElement = new Element("dc");
		
		creatorElement = new Element("creator");
		creatorElement.withField("value", "Sousa, Jesus Maria Angélica Fernandes");
		
		subjectElement = new Element("subject");
		subjectElement.withField(null, "Ciências da Educação");
		
		parentElement.withElement(creatorElement);
		parentElement.withElement(subjectElement);
		
		metadata.withElement(parentElement);
	}

	@Test
	public void metadataSearchImplConstructorTest() {
		MetadataSearchImpl searcher = new MetadataSearchImpl(metadata);
		
		assertEquals(2, searcher.index().size());
		assertThat(searcher.findOne("dc.creator"), equalTo("Sousa, Jesus Maria Angélica Fernandes"));
		assertThat(searcher.findOne("dc.subject"), equalTo("Ciências da Educação"));
	}
	
	/**
	 * Example to showcase that a field with a ":" will be added to the MetadataSearchImpl's index
	 */
	@Test
	public void langPropertyIsAdded() {
		creatorElement.withField("xml:lang", "pt-PT");
		subjectElement.withField("xml:lang", "pt-PT");
		MetadataSearchImpl searcher = new MetadataSearchImpl(metadata);
		
		assertEquals(4, searcher.index().size());
		assertThat(searcher.findOne("dc.creator:xml:lang"), equalTo("pt-PT"));
		assertThat(searcher.findOne("dc.subject:xml:lang"), equalTo("pt-PT"));
	}
}
