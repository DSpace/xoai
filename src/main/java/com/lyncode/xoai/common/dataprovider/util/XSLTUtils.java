package com.lyncode.xoai.common.dataprovider.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.xoai.common.dataprovider.data.AbstractItem;
import com.lyncode.xoai.common.dataprovider.exceptions.MarshallingException;
import com.lyncode.xoai.common.dataprovider.exceptions.XSLTransformationException;
import com.lyncode.xoai.common.dataprovider.xml.PrefixMapper;
import com.lyncode.xoai.common.dataprovider.xml.xoai.Metadata;

public class XSLTUtils {
	private static Logger log = LogManager.getLogger(XSLTUtils.class);

	public static String transform(File xslMetadataTransform,
			File xslSchemaFormat, AbstractItem item)
			throws XSLTransformationException {
		try {
			log.debug("Transform Called");
			TransformerFactory tFactory = TransformerFactory.newInstance();

			// Transformer for metadata values
			Transformer metadataTransformer = tFactory
					.newTransformer(new StreamSource(xslMetadataTransform));
			// Tranformer for specific schema output
			Transformer schemaTransformer = tFactory
					.newTransformer(new StreamSource(xslSchemaFormat));

			// Pipe (for metadata)
			ByteArrayOutputStream mdOUT = new ByteArrayOutputStream();

			// Pipe (for schema)
			ByteArrayOutputStream schemaOUT = new ByteArrayOutputStream();

			// Final result (into String)
			ByteArrayOutputStream result = new ByteArrayOutputStream();

			byte[] array = null;
			if (item.getMetadata().isCompiled()) {
			    array = item.getMetadata().getCompiled().replaceAll(
                        Pattern.quote("<?") + "xml.*" + Pattern.quote("?>"),
                        "").getBytes();
			} else {
	            // Marshalling all the metadata from XOAI Binding
	            log.debug("Start Marshalling");
    			MarshallingUtils.marshalWithoutXMLHeader(Metadata.class
    					.getPackage().getName(), item.getMetadata(),
    					new PrefixMapper(), mdOUT);
    			array = mdOUT.toByteArray();
			}
			log.debug("Metadata Object marshalled into one end of the Metadata Pipe Stream");
			// Closing Output
			mdOUT.close();
			// Transforming Metadata
			ByteArrayInputStream mdIN = new ByteArrayInputStream(array);
			log.debug("Start Transform (Metadata)");
			metadataTransformer.transform(new StreamSource(mdIN),
					new StreamResult(schemaOUT));
			log.debug("XSL Transform applied to the other end of the Metadata Pipe Stream, putting the result in one end of the Schema Pipe Stream");
			// Closing Schema Output & Metadata Input
			schemaOUT.close();
			mdIN.close();
			// Transforming Schema Format
			ByteArrayInputStream schemaIN = new ByteArrayInputStream(
					schemaOUT.toByteArray());
			log.debug("Start Transform (Schema)");
			schemaTransformer.transform(new StreamSource(schemaIN),
					new StreamResult(result));
			log.debug("XML Transform applied to the other end of the Schema Pipe Stream, putting the result into a byte stream array");
			// Closing Schema Input & Output
			schemaIN.close();
			result.close();

			return result.toString().replaceAll(
					Pattern.quote("<?") + "xml.*" + Pattern.quote("?>"), "");
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

	public static String transform(File xslSchemaFormat, AbstractItem item)
			throws XSLTransformationException {
		try {
			log.debug("Transform Called");
			TransformerFactory tFactory = TransformerFactory.newInstance();

			// Tranformer for specific schema output
			Transformer schemaTransformer = tFactory
					.newTransformer(new StreamSource(xslSchemaFormat));

			// Pipe (for schema)
			ByteArrayOutputStream schemaOUT = new ByteArrayOutputStream();

			// Final result (into String)
			ByteArrayOutputStream result = new ByteArrayOutputStream();

            byte[] array = null;
            if (item.getMetadata().isCompiled()) {
                array = item.getMetadata().getCompiled().replaceAll(
                        Pattern.quote("<?") + "xml.*" + Pattern.quote("?>"),
                        "").getBytes();
            } else {
                // Marshalling all the metadata from XOAI Binding
                log.debug("Start Marshalling");
                MarshallingUtils.marshalWithoutXMLHeader(Metadata.class
                        .getPackage().getName(), item.getMetadata(),
                        new PrefixMapper(), schemaOUT);
                array = schemaOUT.toByteArray();
            }
			log.debug("Metadata Object marshalled into one end of the Schema Pipe Stream");
			// Closing Output
			schemaOUT.close();

			// Transforming Schema Format
			ByteArrayInputStream schemaIN = new ByteArrayInputStream(
					array);
			log.debug("Start Transform (Schema)");
			schemaTransformer.transform(new StreamSource(schemaIN),
					new StreamResult(result));
			log.debug("XML Transform applied to the other end of the Schema Pipe Stream, putting the result into a byte stream array");

			// Closing input and output
			schemaIN.close();
			result.close();

			return result.toString().replaceAll(
					Pattern.quote("<?") + "xml.*" + Pattern.quote("?>"), "");
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
}
