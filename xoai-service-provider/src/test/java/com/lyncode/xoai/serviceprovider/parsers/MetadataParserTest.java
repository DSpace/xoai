package com.lyncode.xoai.serviceprovider.parsers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import java.io.InputStream;

import org.junit.Test;

import com.lyncode.xoai.model.xoai.XOAIMetadata;
import com.lyncode.xoai.services.api.MetadataSearch;

public class MetadataParserTest {
    @Test
    public void parseMetadata() throws Exception {
        InputStream input = getClass().getClassLoader().getResourceAsStream("test/xoai.xml");

        XOAIMetadata metadata = new MetadataParser().parse(input);
        MetadataSearch searcher = metadata.searcher();
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