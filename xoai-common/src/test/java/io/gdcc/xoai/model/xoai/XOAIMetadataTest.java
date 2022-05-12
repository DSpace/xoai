/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.model.xoai;

import io.gdcc.xoai.xml.XmlWriter;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.text.IsEmptyString.emptyOrNullString;
import static org.hamcrest.core.IsNot.not;

public class XOAIMetadataTest {
    @Test
    public void testParse() throws Exception {
        InputStream stream = XOAIMetadataTest.class.getClassLoader().getResourceAsStream("xoai-example.xml");

        XOAIMetadata XOAIMetadata = io.gdcc.xoai.model.xoai.XOAIMetadata.parse(stream);
        assertThat(XOAIMetadata.getElements(), hasSize(5));
    }

    @Test
    public void testWrite() throws Exception {
        InputStream stream = XOAIMetadataTest.class.getClassLoader().getResourceAsStream("xoai-example.xml");
        XOAIMetadata read = XOAIMetadata.parse(stream);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XmlWriter writer = new XmlWriter(output);
        read.write(writer);
        
        String xml = output.toString();
        assertThat(xml, not(emptyOrNullString()));
    }
}
