/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.model.xoai;

import com.lyncode.xml.XmlWriter;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.dspace.xoai.model.xoai.XOAIMetadata.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class XOAIMetadataTest {
    @Test
    public void testParse() throws Exception {
        InputStream stream = XOAIMetadataTest.class.getClassLoader().getResourceAsStream("xoai-example.xml");

        XOAIMetadata XOAIMetadata = parse(stream);
        assertThat(XOAIMetadata.getElements(), hasSize(5));
    }

    @Test
    public void testWrite() throws Exception {
        InputStream stream = XOAIMetadataTest.class.getClassLoader().getResourceAsStream("xoai-example.xml");
        XOAIMetadata read = parse(stream);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XmlWriter writer = new XmlWriter(output);
        read.write(writer);
        System.out.println(output.toString());
    }
}
