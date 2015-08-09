/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.services.impl.metadataSearchImpl;

import org.dspace.xoai.model.xoai.Element;
import org.dspace.xoai.model.xoai.XOAIMetadata;
import org.dspace.xoai.services.impl.MetadataSearchImpl;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;

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
