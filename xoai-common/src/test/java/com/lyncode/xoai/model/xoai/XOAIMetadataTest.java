/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     client://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.xoai.model.xoai;

import com.lyncode.xml.XmlWriter;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static com.lyncode.xoai.model.xoai.XOAIMetadata.parse;
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
