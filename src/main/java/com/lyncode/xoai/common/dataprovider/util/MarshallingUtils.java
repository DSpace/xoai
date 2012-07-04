package com.lyncode.xoai.common.dataprovider.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.xoai.common.dataprovider.exceptions.MarshallingException;
import com.lyncode.xoai.common.dataprovider.xml.PrefixMapper;
import com.lyncode.xoai.common.dataprovider.xml.xoaidescription.XOAIDescription;

public class MarshallingUtils {
	private static Logger log = LogManager.getLogger(MarshallingUtils.class);

	public static String marshalWithoutXMLHeader(String cont, Object obj,
			PrefixMapper mapper) throws MarshallingException {
		try {
			log.debug("Marshalling XML without Header");
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			JAXBContext context = JAXBContext.newInstance(cont);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(
					"com.sun.xml.internal.bind.namespacePrefixMapper", mapper);
			marshaller.marshal(obj, output);
			log.debug("Giving result as string");
			return output
					.toString()
					.replace(
							"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>",
							"");
		} catch (JAXBException ex) {
			throw new MarshallingException(ex);
		}
	}

	public static void marshalWithoutXMLHeader(String cont, Object obj,
			PrefixMapper mapper, OutputStream out) throws MarshallingException {
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			JAXBContext context = JAXBContext.newInstance(cont);
			log.debug("Marshalling XML without Header");
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(
					"com.sun.xml.internal.bind.namespacePrefixMapper", mapper);
			marshaller.marshal(obj, output);
			log.debug("Writing result into output stream");
			log.debug("Result: " + output.toString());
			out.write(output
					.toString()
					.replaceAll(
							Pattern.quote("<?") + "xml.*" + Pattern.quote("?>"),
							"").getBytes());
			log.debug("Result written!");
		} catch (JAXBException ex) {
			throw new MarshallingException(ex);
		} catch (IOException e) {
			throw new MarshallingException(e);
		}
	}

	public static String marshalWithoutXMLHeader(XOAIDescription xml)
			throws MarshallingException {
		return marshalWithoutXMLHeader(XOAIDescription.class.getPackage()
				.getName(), xml, new PrefixMapper());
	}
}
