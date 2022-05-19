package io.gdcc.xoai.tests.util;

import io.gdcc.xoai.xmlio.exceptions.XmlWriteException;
import io.gdcc.xoai.xml.EchoElement;
import io.gdcc.xoai.xml.XmlWriter;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

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
    
        assertThat("EchoElement handles nested namespaces", result, equalTo(xml));
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
    
        assertThat("EchoElement handles nested namespaces", result, equalTo(xml));
    }
    
    @Test
    public void copyFromInputStream() throws XMLStreamException, XmlWriteException, IOException {
        // given
        String xml = "<?xml version='1.0' encoding='UTF-8'?>"
            + "<oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\n"
            + "\t<dc:title xmlns:dc=\"http://purl.org/dc/elements/1.1/\">Invasive Lithobates catesbeianus - American bullfrog occurrences in Flanders</dc:title>\n"
            + "\t<dc:subject xmlns:dc=\"http://purl.org/dc/elements/1.1/\">Occurrence</dc:subject>\n"
            + "\t<dc:subject xmlns:dc=\"http://purl.org/dc/elements/1.1/\">Observation</dc:subject>\n"
            + "</oai_dc:dc>";
        final ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
    
        // when
        try (
            resultStream;
            InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            XmlWriter writer = new XmlWriter(resultStream)
        ){
            writer.writeStartDocument();
            writer.write(new EchoElement(stream));
            writer.writeEndDocument();
        }
        String result = resultStream.toString();
        
        // then
        assertThat("EchoElement handles InputStream", result, equalTo(xml));
    }

    private static String echoXml(String xml) throws XmlWriteException, XMLStreamException {
    
        final ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
    
        try (
            resultStream;
            XmlWriter writer = new XmlWriter(resultStream)
        ){
            writer.writeStartDocument();
            writer.write(new EchoElement(xml));
            writer.writeEndDocument();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return resultStream.toString();
    }
}
