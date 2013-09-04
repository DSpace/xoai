package com.lyncode.xoai.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.lyncode.xoai.dataprovider.core.ItemMetadata;
import com.lyncode.xoai.dataprovider.data.AbstractItem;
import com.lyncode.xoai.dataprovider.exceptions.XSLTransformationException;

import static org.mockito.Mockito.*;


public class XSLTUtilsTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void shouldTransformItRight() throws IOException, XSLTransformationException {
        InputStream xmlSchemaTransform = this.getClass().getResourceAsStream("TransformSchemaSample.xsl");
        InputStream xmlMetadataTransform = this.getClass().getResourceAsStream("TransformMetadataSample.xsl");
        
        AbstractItem item = mock(AbstractItem.class);
        ItemMetadata metadata = new ItemMetadata(IOUtils.toString(this.getClass().getResourceAsStream("SampleData.xml")));
        
        when(item.getMetadata()).thenReturn(metadata);
        
        String result = XSLTUtils.transform(xmlMetadataTransform, xmlSchemaTransform, item).replaceAll("\\s", "");
       
        assertEquals(IOUtils.toString(this.getClass().getResourceAsStream("Result.xml")).replaceAll("\\s", ""), result);
    }

    @Test
    public void shouldTransformItRight2() throws IOException, XSLTransformationException {
        InputStream xmlSchemaTransform = this.getClass().getResourceAsStream("TransformSchemaSample.xsl");
        
        AbstractItem item = mock(AbstractItem.class);
        ItemMetadata metadata = new ItemMetadata(IOUtils.toString(this.getClass().getResourceAsStream("SampleData.xml")));
        
        when(item.getMetadata()).thenReturn(metadata);
        
        String result = XSLTUtils.transform(xmlSchemaTransform, item).replaceAll("\\s", "");
        assertEquals(IOUtils.toString(this.getClass().getResourceAsStream("Result2.xml")).replaceAll("\\s", ""), result);
    }

}
