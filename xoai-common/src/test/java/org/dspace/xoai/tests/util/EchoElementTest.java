package org.dspace.xoai.tests.util;

import com.lyncode.xml.exceptions.XmlWriteException;
import org.dspace.xoai.xml.EchoElement;
import org.dspace.xoai.xml.XmlWriter;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class EchoElementTest {

    /*
     * Namespace declarations (such as 'dc' on the root element <oai_dc:dc> below) should be kept when provided, as they
     * are likely to be used later.
     */
    @Test
    public void handleEarlyNamespaceDeclarations() throws XMLStreamException, XmlWriteException, IOException {
        String xml = "<?xml version='1.0' encoding='UTF-8'?>"
                + "<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\n"
                + "\t<dc:title>Invasive Lithobates catesbeianus - American bullfrog occurrences in Flanders</dc:title>\n"
                + "\t<dc:subject>Occurrence</dc:subject>\n"
                + "\t<dc:subject>Observation</dc:subject>\n"
                + "</oai_dc:dc>";

        String result = echoXml(xml);

        assertEquals("EchoElement handles nested namespaces", xml, result);
    }

    /*
     * Namespace declarations must be tracked according to the current context.  The sibling dc: elements below all need
     * a namespace declaration.
     */
    @Test
    public void repeatingNamespaceDeclarations() throws XMLStreamException, XmlWriteException, IOException {
        String xml = "<?xml version='1.0' encoding='UTF-8'?>"
                + "<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\n"
                + "\t<dc:title xmlns:dc=\"http://purl.org/dc/elements/1.1/\">Invasive Lithobates catesbeianus - American bullfrog occurrences in Flanders</dc:title>\n"
                + "\t<dc:subject xmlns:dc=\"http://purl.org/dc/elements/1.1/\">Occurrence</dc:subject>\n"
                + "\t<dc:subject xmlns:dc=\"http://purl.org/dc/elements/1.1/\">Observation</dc:subject>\n"
                + "</oai_dc:dc>";

        String result = echoXml(xml);

        assertEquals("EchoElement handles nested namespaces", xml, result);
    }

    private String echoXml(String xml) throws XmlWriteException, XMLStreamException {
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();

        XmlWriter writer = new XmlWriter(resultStream);
        writer.writeStartDocument();
        writer.write(new EchoElement(xml));
        writer.writeEndDocument();

        return resultStream.toString();
    }
}
