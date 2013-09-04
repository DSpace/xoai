package com.lyncode.xoai.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLOutputFactory2;

import com.lyncode.xoai.dataprovider.data.AbstractItem;
import com.lyncode.xoai.dataprovider.exceptions.WrittingXmlException;
import com.lyncode.xoai.dataprovider.exceptions.XSLTransformationException;

public class XSLTUtils {
    static TransformerFactory tFactory = TransformerFactory.newInstance();
    static XMLOutputFactory outputFactory = XMLOutputFactory2.newFactory();
	private static Logger log = LogManager.getLogger(XSLTUtils.class);
	
	public static String transform(InputStream xslMetadataTransform, InputStream xslSchemaFormat, AbstractItem item) throws XSLTransformationException {
	    try {
            log.debug("Transform Called");

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
                array = item.getMetadata().getCompiled().getBytes();
            } else {
                log.debug("Start Marshalling");
                XMLStreamWriter writer = outputFactory.createXMLStreamWriter(mdOUT);
                item.getMetadata().getMetadata().write(writer);
                writer.flush();
                writer.close();
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
        } catch (WrittingXmlException e) {
            throw new XSLTransformationException(e);
        } catch (XMLStreamException e) {
            throw new XSLTransformationException(e);
        }
	}

	public static String transform(File xslMetadataTransform,
			File xslSchemaFormat, AbstractItem item)
			throws XSLTransformationException, FileNotFoundException {
	    return transform(new FileInputStream(xslMetadataTransform), new FileInputStream(xslSchemaFormat), item);
	}
	
	public static String transform(InputStream xslSchemaFormat, AbstractItem item) throws XSLTransformationException {
	    try {
            log.debug("Transform Called");

            // Tranformer for specific schema output
            Transformer schemaTransformer = tFactory
                    .newTransformer(new StreamSource(xslSchemaFormat));

            // Pipe (for schema)
            ByteArrayOutputStream schemaOUT = new ByteArrayOutputStream();

            // Final result (into String)
            ByteArrayOutputStream result = new ByteArrayOutputStream();

            byte[] array = null;
            if (item.getMetadata().isCompiled()) {
                array = item.getMetadata().getCompiled().getBytes();
            } else {
                log.debug("Start Marshalling");
                XMLStreamWriter writer = outputFactory.createXMLStreamWriter(schemaOUT);
                item.getMetadata().getMetadata().write(writer);
                writer.flush();
                writer.close();
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

            return result.toString();
        } catch (TransformerConfigurationException e) {
            throw new XSLTransformationException(e);
        } catch (IOException e) {
            throw new XSLTransformationException(e);
        } catch (TransformerException e) {
            throw new XSLTransformationException(e);
        } catch (WrittingXmlException e) {
            throw new XSLTransformationException(e);
        } catch (XMLStreamException e) {
            throw new XSLTransformationException(e);
        }
	}

	public static String transform(File xslSchemaFormat, AbstractItem item)
			throws XSLTransformationException, FileNotFoundException {
	    return transform(new FileInputStream(xslSchemaFormat), item);
	}
}
