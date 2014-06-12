package com.lyncode.xoai.serviceprovider.parsers;

import com.lyncode.xoai.model.xoai.XOAIMetadata;
import com.lyncode.xoai.services.api.MetadataSearch;
import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

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
}