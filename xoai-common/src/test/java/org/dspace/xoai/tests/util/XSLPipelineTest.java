/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.tests.util;

import org.apache.commons.io.IOUtils;
import org.dspace.xoai.xml.XSLPipeline;
import org.junit.Test;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class XSLPipelineTest {
    private static final TransformerFactory tFactory = TransformerFactory.newInstance();
    private static final String TEST_XML = "<test />";

    private ByteArrayInputStream input = new ByteArrayInputStream(TEST_XML.getBytes());

    @Test
    public void shouldGiveTheSameIfNoTransformationIsApplied() throws Exception {
        XSLPipeline underTest = new XSLPipeline(input, true);
        assertEquals(TEST_XML, IOUtils.toString(underTest.process()));
    }

    @Test
    public void shouldTransformWithXmlDeclarationOnTop() throws TransformerException, IOException {
        XSLPipeline underTest = new XSLPipeline(input, false);
        underTest.apply(identityTransformer());
        assertThat(IOUtils.toString(underTest.process()), containsString("<?xml"));
    }

    @Test
    public void shouldTransformWithoutXmlDeclarationOnTop() throws TransformerException, IOException {
        XSLPipeline underTest = new XSLPipeline(input, true);
        underTest.apply(identityTransformer());
        assertThat(IOUtils.toString(underTest.process()), not(containsString("<?xml")));
    }



    protected Transformer identityTransformer() {
        try {
            return tFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
