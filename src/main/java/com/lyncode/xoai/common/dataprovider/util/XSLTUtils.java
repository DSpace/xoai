package com.lyncode.xoai.common.dataprovider.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.lyncode.xoai.common.dataprovider.data.AbstractItem;
import com.lyncode.xoai.common.dataprovider.exceptions.MarshallingException;
import com.lyncode.xoai.common.dataprovider.exceptions.XSLTransformationException;
import com.lyncode.xoai.common.dataprovider.xml.PrefixMapper;
import com.lyncode.xoai.common.dataprovider.xml.xoai.Metadata;

public class XSLTUtils {
	public static String transform (File xslMetadataTransform, File xslSchemaFormat, AbstractItem item) throws XSLTransformationException {
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			
			// Transformer for metadata values
			Transformer metadataTransformer = tFactory.newTransformer(new StreamSource(xslMetadataTransform));
			// Tranformer for specific schema output
			Transformer schemaTransformer = tFactory.newTransformer(new StreamSource(xslSchemaFormat));
			
			// Pipe (for metadata)
            PipedInputStream mdIN = new PipedInputStream();
            PipedOutputStream mdOUT = new PipedOutputStream(mdIN);
            
            // Pipe (for schema)
            PipedInputStream schemaIN = new PipedInputStream();
            PipedOutputStream schemaOUT = new PipedOutputStream(schemaIN);
            
            // Final result (into String)
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            
            // Marshalling all the metadata from XOAI Binding
         	MarshallingUtils.marshalWithoutXMLHeader(Metadata.class.getPackage().getName(), item.getMetadata(), new PrefixMapper(), mdOUT);
                     
            // Transforming Metadata
            metadataTransformer.transform(new StreamSource(mdIN), new StreamResult(schemaOUT));
            // Transforming Schema Format
            schemaTransformer.transform(new StreamSource(schemaIN), new StreamResult(result));
            
			return result.toString();
		} catch (TransformerConfigurationException e) {
			throw new XSLTransformationException(e);
		} catch (IOException e) {
			throw new XSLTransformationException(e);
		} catch (TransformerException e) {
			throw new XSLTransformationException(e);
		} catch (MarshallingException e) {
			throw new XSLTransformationException(e);
		}
	}
	
	public static String transform (File xslSchemaFormat, AbstractItem item) throws XSLTransformationException {
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			
			// Tranformer for specific schema output
			Transformer schemaTransformer = tFactory.newTransformer(new StreamSource(xslSchemaFormat));
			
			// Marshalling all the metadata from XOAI Binding
            JAXBContext context = JAXBContext.newInstance(Metadata.class.getPackage().getName());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", new PrefixMapper());
            
            
            // Pipe (for schema)
            PipedInputStream schemaIN = new PipedInputStream();
            PipedOutputStream schemaOUT = new PipedOutputStream(schemaIN);
            
            // Final result (into String)
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            
            marshaller.marshal(item.getMetadata(), schemaOUT);
            
            // Transforming Schema Format
            schemaTransformer.transform(new StreamSource(schemaIN), new StreamResult(result));
            
			return result.toString();
		} catch (TransformerConfigurationException e) {
			throw new XSLTransformationException(e);
		} catch (JAXBException e) {
			throw new XSLTransformationException(e);
		} catch (IOException e) {
			throw new XSLTransformationException(e);
		} catch (TransformerException e) {
			throw new XSLTransformationException(e);
		}
	}
}
