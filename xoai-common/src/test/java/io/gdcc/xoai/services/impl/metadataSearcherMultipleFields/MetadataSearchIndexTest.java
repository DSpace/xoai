/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.services.impl.metadataSearcherMultipleFields;

import io.gdcc.xoai.model.xoai.Element;
import io.gdcc.xoai.model.xoai.XOAIMetadata;
import io.gdcc.xoai.services.api.MetadataSearch;
import io.gdcc.xoai.services.impl.MetadataSearchImpl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class MetadataSearchIndexTest {
	
	@Test
	public void retrieveIndex(){
		XOAIMetadata xoaiMetadata = new XOAIMetadata();
		xoaiMetadata.getElements().add(new Element("dc").withElement(new Element("creator").withField("value", "john doe")));
		MetadataSearch metadataSearch = new MetadataSearchImpl(xoaiMetadata );
		Map<String,List<String>> index = metadataSearch.index();
		
		assertThat(1, equalTo(index.size()));
		assertThat("john doe", equalTo(index.get("dc.creator").get(0)));
	}

	@Test
	public void emptyMetadataReturnsEmpty(){
		XOAIMetadata xoaiMetadata = new XOAIMetadata();
		MetadataSearch metadataSearch = new MetadataSearchImpl(xoaiMetadata );
		Map<String,List<String>> index = metadataSearch.index();
		
		assertThat(0, equalTo(index.size()));
	}

}
