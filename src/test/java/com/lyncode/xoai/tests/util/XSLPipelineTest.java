package com.lyncode.xoai.tests.util;

import com.lyncode.xoai.tests.XmlTest;
import com.lyncode.xoai.util.XSLPipeline;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
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

public class XSLPipelineTest extends XmlTest {
    private static final TransformerFactory tFactory = TransformerFactory.newInstance();
    private static final String TEST_XML = "<test />";

    private ByteArrayInputStream input = new ByteArrayInputStream(TEST_XML.getBytes());

    @Test
    public void shouldGiveTheSameIfNoTransformationIsApplied () throws IOException {
        XSLPipeline underTest = new XSLPipeline(input, true);
        assertEquals(TEST_XML, IOUtils.toString(underTest.getTransformed()));
    }

    @Test
    public void shouldTransformWithXmlDeclarationOnTop () throws TransformerException, IOException {
        XSLPipeline underTest = new XSLPipeline(input, false);
        underTest.apply(identityTransformer());
        assertThat(IOUtils.toString(underTest.getTransformed()), containsString("<?xml"));
    }
    @Test
    public void shouldTransformWithoutXmlDeclarationOnTop () throws TransformerException, IOException {
        XSLPipeline underTest = new XSLPipeline(input, true);
        underTest.apply(identityTransformer());
        assertThat(IOUtils.toString(underTest.getTransformed()), not(containsString("<?xml")));
    }
}
