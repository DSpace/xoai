/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.parsers;

import org.dspace.xoai.model.xoai.XOAIMetadata;
import org.dspace.xoai.services.api.MetadataSearch;
import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class MetadataParserTest {
    @Test
    public void parseMetadata() throws Exception {
        InputStream input = getClass().getClassLoader().getResourceAsStream("test/xoai.xml");

        XOAIMetadata metadata = new MetadataParser().parse(input);
        MetadataSearch<String> searcher = metadata.searcher();
        assertThat(metadata.getElements().size(), equalTo(1));
        assertThat(searcher.findOne("dc.creator"), equalTo("Sousa, Jesus Maria Angélica Fernandes"));
        assertThat(searcher.findAll("dc.subject").size(), equalTo(5));
    }
    
    @Test
    public void xmlLangIsPresent() throws Exception {
        InputStream input = getClass().getClassLoader().getResourceAsStream("test/xoai-langExample.xml");

        XOAIMetadata metadata = new MetadataParser().parse(input);
        MetadataSearch searcher = metadata.searcher();
        assertThat(searcher.findAll("dc.title").size(), equalTo(2));
        assertThat(searcher.findAll("dc.title:xml:lang").size(), equalTo(2));
    }
}