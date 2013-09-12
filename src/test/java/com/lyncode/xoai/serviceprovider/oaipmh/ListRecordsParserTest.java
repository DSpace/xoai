package com.lyncode.xoai.serviceprovider.oaipmh;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;
import org.mockito.Mockito;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.ListIdentifiersType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.ListRecordsType;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;


public class ListRecordsParserTest {
    static String XML = "<ListRecords>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10459.3/67</identifier>\r\n" + 
    		"                <datestamp>2013-07-29T11:25:13Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_10</setSpec>\r\n" + 
    		"                <setSpec>col_10673_13</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Lexicon hebraicum et chalcaicum in libros veteris testamenti ordine etymologico : compositum in usum scholarum</dc:title>\r\n" + 
    		"<dc:subject>Hebreu -- Diccionaris</dc:subject>\r\n" + 
    		"<dc:subject>Caldeu -- Diccionaris</dc:subject>\r\n" + 
    		"<dc:description>VI, 453 p.; 14 cm</dc:description>\r\n" + 
    		"<dc:date>2012-07-01T15:44:58Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-29T11:25:13Z</dc:date>\r\n" + 
    		"<dc:date>2012-07-01T15:44:58Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-29T11:25:13Z</dc:date>\r\n" + 
    		"<dc:date>2012</dc:date>\r\n" + 
    		"<dc:date>1850</dc:date>\r\n" + 
    		"<dc:type>Text</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10459.3/67</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10459</dc:identifier>\r\n" + 
    		"<dc:language>other</dc:language>\r\n" + 
    		"<dc:relation>http://cataleg.udl.cat/record=b1070140~S11*cat</dc:relation>\r\n" + 
    		"<dc:rights>Còpia permesa amb finalitat d'estudi o recerca, citant l'autor i la font: \"Fons Gili i Gaya / Universitat de Lleida\". Per a qualsevol altre ús cal demanar autorització.</dc:rights>\r\n" + 
    		"<dc:publisher>Universitat de Lleida</dc:publisher>\r\n" + 
    		"<dc:source>Publicació original: Lipsiae : Sumptibus et Typis Caroli Tauchnitii, 1850</dc:source>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <resumptionToken completeListSize=\"16816\" cursor=\"0\">MToxMDB8Mjp8Mzp8NDp8NTpvYWlfZGM=</resumptionToken>\r\n" + 
    		"    </ListRecords>";
    
    @Test
    public void testListRecords() throws XMLStreamException, ParseException {
        XMLInputFactory factory = XMLInputFactory2.newFactory();
        XMLEventReader reader = factory.createXMLEventReader(new ByteArrayInputStream(XML.getBytes()));

        reader.nextEvent();
        reader.peek();
        
        OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config = Mockito.mock(OAIServiceConfiguration.class);
        ListRecordsParser parser = new ListRecordsParser(config);
        
        //System.out.println(parser.parse(reader));
        ListRecordsType result = parser.parse(reader);
        

        assertEquals(1, result.getRecord().size());
        assertEquals("MToxMDB8Mjp8Mzp8NDp8NTpvYWlfZGM=", result.getResumptionToken().getValue());
    }
}
