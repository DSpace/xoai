package com.lyncode.xoai.common.dataprovider.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.lyncode.xoai.common.dataprovider.exceptions.MarshallingException;
import com.lyncode.xoai.common.dataprovider.xml.PrefixMapper;
import com.lyncode.xoai.common.dataprovider.xml.xoaidescription.XOAIDescription;

public class MarshallingUtils {
    public static String marshalWithoutXMLHeader (String cont, Object obj, PrefixMapper mapper) throws MarshallingException {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            JAXBContext context = JAXBContext.newInstance(cont);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", mapper);
            marshaller.marshal(obj, output);
            return output.toString().replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
        } catch (JAXBException ex) {
            throw new MarshallingException(ex);
        }
    }
    
    public static void marshalWithoutXMLHeader (String cont, Object obj, PrefixMapper mapper, OutputStream out) throws MarshallingException {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            JAXBContext context = JAXBContext.newInstance(cont);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", mapper);
            marshaller.marshal(obj, output);
            out.write(output.toString().replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "").getBytes());
        } catch (JAXBException ex) {
            throw new MarshallingException(ex);
        } catch (IOException e) {
            throw new MarshallingException(e);
		}
    }

    public static String export (XOAIDescription xml) throws MarshallingException {
        return marshalWithoutXMLHeader("com.lyncode.xoai.common.xml.xoaidescription", xml, new PrefixMapper());
    }
}
