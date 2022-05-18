/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.tests.util;

import io.gdcc.xoai.xml.XSLPipeline;
import org.junit.jupiter.api.Test;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class XSLPipelineTest {
    private static final TransformerFactory tFactory = TransformerFactory.newInstance();
    private static final String TEST_XML = "<test />";

    private final ByteArrayInputStream input = new ByteArrayInputStream(TEST_XML.getBytes());

    @Test
    void shouldGiveTheSameIfNoTransformationIsApplied() throws Exception {
        XSLPipeline underTest = new XSLPipeline(input, true);
        
        String result = new String(underTest.process().readAllBytes(), StandardCharsets.UTF_8);
        assertThat(TEST_XML, equalTo(result));
    }
    
    @Test
    void shouldReturnUnchangedWhenAddingNullTransformer() throws Exception {
        //given
        XSLPipeline underTest = new XSLPipeline(input, true);
        
        //when
        underTest.apply(null);
        String result = new String(underTest.process().readAllBytes(), StandardCharsets.UTF_8);
        
        //then
        assertThat(TEST_XML, equalTo(result));
    }

    @Test
    void shouldTransformWithXmlDeclarationOnTop() throws TransformerException, IOException {
        XSLPipeline underTest = new XSLPipeline(input, false);
        underTest.apply(identityTransformer());
        
        String result = new String(underTest.process().readAllBytes(), StandardCharsets.UTF_8);
        assertThat(result, containsString("<?xml"));
    }

    @Test
    void shouldTransformWithoutXmlDeclarationOnTop() throws TransformerException, IOException {
        XSLPipeline underTest = new XSLPipeline(input, true);
        underTest.apply(identityTransformer());
    
        String result = new String(underTest.process().readAllBytes(), StandardCharsets.UTF_8);
        assertThat(result, not(containsString("<?xml")));
    }


    protected Transformer identityTransformer() {
        try {
            return tFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
