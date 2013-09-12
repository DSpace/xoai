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
    		"                <identifier>oai:demo.dspace.org:10673/4</identifier>\r\n" + 
    		"                <datestamp>2013-09-10T20:40:03Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Test Webpage</dc:title>\r\n" + 
    		"<dc:subject>cat</dc:subject>\r\n" + 
    		"<dc:subject>calico</dc:subject>\r\n" + 
    		"<dc:description>This is a Sample HTML webpage including several images and styles (CSS).</dc:description>\r\n" + 
    		"<dc:date>1982-06-26T19:58:24Z</dc:date>\r\n" + 
    		"<dc:date>1982-06-26T19:58:24Z</dc:date>\r\n" + 
    		"<dc:date>1982-11-11</dc:date>\r\n" + 
    		"<dc:date>1982-11-11</dc:date>\r\n" + 
    		"<dc:type>text</dc:type>\r\n" + 
    		"<dc:identifier>123456789</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/4</dc:identifier>\r\n" + 
    		"<dc:rights>© EverCats.com</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/3</identifier>\r\n" + 
    		"                <datestamp>2013-09-08T00:01:15Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Test Excel Document</dc:title>\r\n" + 
    		"<dc:subject>keyword1</dc:subject>\r\n" + 
    		"<dc:subject>keyword2</dc:subject>\r\n" + 
    		"<dc:subject>keyword3</dc:subject>\r\n" + 
    		"<dc:description>This is really just a sample abstract.  But, Í’vé thrown ïn a cõuple of spëciâl charactèrs för êxtrå fuñ!</dc:description>\r\n" + 
    		"<dc:date>1992-06-26T19:58:24Z</dc:date>\r\n" + 
    		"<dc:date>1992-06-26T19:58:24Z</dc:date>\r\n" + 
    		"<dc:date>1992-06-26</dc:date>\r\n" + 
    		"<dc:type>text</dc:type>\r\n" + 
    		"<dc:identifier>123456789</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/3</dc:identifier>\r\n" + 
    		"<dc:rights>© John Doe</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/6</identifier>\r\n" + 
    		"                <datestamp>2013-09-08T02:00:36Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Test PowerPoint Document</dc:title>\r\n" + 
    		"<dc:subject>keyword1</dc:subject>\r\n" + 
    		"<dc:subject>keyword2</dc:subject>\r\n" + 
    		"<dc:subject>keyword3</dc:subject>\r\n" + 
    		"<dc:description>This is really just a sample abstract. If it was a real abstract it would contain useful information about this test document. Sorry though, nothing useful in this paragraph. You probably shouldn't have even bothered to read it!</dc:description>\r\n" + 
    		"<dc:date>1650-06-26T19:58:25Z</dc:date>\r\n" + 
    		"<dc:date>1650-06-26T19:58:25Z</dc:date>\r\n" + 
    		"<dc:date>1650-06-26</dc:date>\r\n" + 
    		"<dc:type>text</dc:type>\r\n" + 
    		"<dc:identifier>123456789</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/6</dc:identifier>\r\n" + 
    		"<dc:rights>© Jane Doe</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/5</identifier>\r\n" + 
    		"                <datestamp>2013-09-10T12:01:23Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Test PDF Document</dc:title>\r\n" + 
    		"<dc:subject>keyword1</dc:subject>\r\n" + 
    		"<dc:subject>keyword2</dc:subject>\r\n" + 
    		"<dc:subject>keyword3</dc:subject>\r\n" + 
    		"<dc:description>This abstract is really quite great. It grabs your attention at just the right spots, and holds it all the way until the end! Yep. Great abstracts do that.</dc:description>\r\n" + 
    		"<dc:date>2002-06-26T19:58:25Z</dc:date>\r\n" + 
    		"<dc:date>2002-06-26T19:58:25Z</dc:date>\r\n" + 
    		"<dc:date>2002-06-26</dc:date>\r\n" + 
    		"<dc:type>text</dc:type>\r\n" + 
    		"<dc:identifier>123456789</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/5</dc:identifier>\r\n" + 
    		"<dc:rights>© Sandra Nelson</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/7</identifier>\r\n" + 
    		"                <datestamp>2013-09-08T02:00:42Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Test Word Document</dc:title>\r\n" + 
    		"<dc:subject>keyword1</dc:subject>\r\n" + 
    		"<dc:subject>keyword2</dc:subject>\r\n" + 
    		"<dc:subject>keyword3</dc:subject>\r\n" + 
    		"<dc:description>This is a sample abstract.  But, to fill up some space, here's \"Hello\" in several different languages : (Chinese) 你好 , &#13;\r\n" + 
    		"(Georgian) გამარჯობა , (Greek) Γεια σας , (Hindi) नमस्ते , (Korean) 여보세요 , (Japanese) こんにちは , (Russian) привет , (Thai) สวัสดี</dc:description>\r\n" + 
    		"<dc:date>2012-06-26T20:02:09Z</dc:date>\r\n" + 
    		"<dc:date>2012-06-26T20:02:09Z</dc:date>\r\n" + 
    		"<dc:date>2012-06-26</dc:date>\r\n" + 
    		"<dc:type>text</dc:type>\r\n" + 
    		"<dc:identifier>123456789</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/7</dc:identifier>\r\n" + 
    		"<dc:rights>© Sam Smith</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/8</identifier>\r\n" + 
    		"                <datestamp>2013-05-02T14:28:36Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Metadata only item with CC</dc:title>\r\n" + 
    		"<dc:date>2013-05-02T09:04:28Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-02T09:04:28Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-02</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/8</dc:identifier>\r\n" + 
    		"<dc:rights>http://creativecommons.org/publicdomain/zero/1.0/</dc:rights>\r\n" + 
    		"<dc:rights>CC0 1.0 Universal</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/9</identifier>\r\n" + 
    		"                <datestamp>2013-07-15T11:26:04Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_8</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Himno</dc:title>\r\n" + 
    		"<dc:subject>Research Subject Categories::NATURAL SCIENCES</dc:subject>\r\n" + 
    		"<dc:date>2013-07-15T11:26:04Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-15T11:26:04Z</dc:date>\r\n" + 
    		"<dc:date>2018</dc:date>\r\n" + 
    		"<dc:type>Recording, oral</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/9</dc:identifier>\r\n" + 
    		"<dc:rights>http://creativecommons.org/publicdomain/zero/1.0/</dc:rights>\r\n" + 
    		"<dc:rights>CC0 1.0 Universal</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/10</identifier>\r\n" + 
    		"                <datestamp>2013-07-26T02:00:20Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>DAVIDSON'S PRINCIPLES AND PRACTICE OF MEDICINE</dc:title>\r\n" + 
    		"<dc:creator>Davidson's, John</dc:creator>\r\n" + 
    		"<dc:subject>Research Subject Categories::MEDICINE</dc:subject>\r\n" + 
    		"<dc:description>READING</dc:description>\r\n" + 
    		"<dc:description>provenance test</dc:description>\r\n" + 
    		"<dc:date>2013-07-22T07:07:01Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-22T07:07:01Z</dc:date>\r\n" + 
    		"<dc:date>1984</dc:date>\r\n" + 
    		"<dc:type>Book</dc:type>\r\n" + 
    		"<dc:identifier>22</dc:identifier>\r\n" + 
    		"<dc:identifier>0 443 03345 5</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/10</dc:identifier>\r\n" + 
    		"<dc:language>en</dc:language>\r\n" + 
    		"<dc:relation>BOOK;02</dc:relation>\r\n" + 
    		"<dc:rights>http://creativecommons.org/publicdomain/zero/1.0/</dc:rights>\r\n" + 
    		"<dc:rights>CC0 1.0 Universal</dc:rights>\r\n" + 
    		"<dc:publisher>ELBS</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/11</identifier>\r\n" + 
    		"                <datestamp>2013-08-25T02:12:53Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_9</setSpec>\r\n" + 
    		"                <setSpec>col_10673_10</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Maxwell e Eletromagnetismo - Parte 1</dc:title>\r\n" + 
    		"<dc:subject>Discutir e mostrar a natureza do conhecimento científico em comparação com o filosófico e o senso comum por intermédio da história das ciências e da epistemologia.</dc:subject>\r\n" + 
    		"<dc:description/>\r\n" + 
    		"<dc:date>2013-08-25T02:12:53Z</dc:date>\r\n" + 
    		"<dc:date>2013-08-25T02:12:53Z</dc:date>\r\n" + 
    		"<dc:date>2013-08-25</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/11</dc:identifier>\r\n" + 
    		"<dc:relation>http://eaulas.usp.br/portal/video.action?idItem=201</dc:relation>\r\n" + 
    		"<dc:relation>Evolução das Ciências: Natureza dos Conhecimentos Científico, Filosófico e do Senso Comum</dc:relation>\r\n" + 
    		"<dc:relation>PLC0007-1</dc:relation>\r\n" + 
    		"<dc:publisher>Escola Politécnica</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/12</identifier>\r\n" + 
    		"                <datestamp>2013-08-25T02:12:54Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_9</setSpec>\r\n" + 
    		"                <setSpec>col_10673_10</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Maxwell e Eletromagnetismo - Parte 2</dc:title>\r\n" + 
    		"<dc:subject>Discutir e mostrar a natureza do conhecimento científico em comparação com o filosófico e o senso comum por intermédio da história das ciências e da epistemologia.</dc:subject>\r\n" + 
    		"<dc:description/>\r\n" + 
    		"<dc:date>2013-08-25T02:12:54Z</dc:date>\r\n" + 
    		"<dc:date>2013-08-25T02:12:54Z</dc:date>\r\n" + 
    		"<dc:date>2013-08-25</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/12</dc:identifier>\r\n" + 
    		"<dc:relation>http://eaulas.usp.br/portal/video.action?idItem=200</dc:relation>\r\n" + 
    		"<dc:relation>Evolução das Ciências: Natureza dos Conhecimentos Científico, Filosófico e do Senso Comum</dc:relation>\r\n" + 
    		"<dc:relation>PLC0007-1</dc:relation>\r\n" + 
    		"<dc:publisher>Escola Politécnica</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/15</identifier>\r\n" + 
    		"                <datestamp>2013-09-04T02:00:21Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Testing cc lisence</dc:title>\r\n" + 
    		"<dc:creator>Aunet, Roger</dc:creator>\r\n" + 
    		"<dc:subject>Research Subject Categories::TECHNOLOGY::Information technology::Image analysis</dc:subject>\r\n" + 
    		"<dc:date>2013-09-03T11:12:58Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-03T11:12:58Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-03</dc:date>\r\n" + 
    		"<dc:type>Book</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/15</dc:identifier>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/us/</dc:rights>\r\n" + 
    		"<dc:rights>Attribution 3.0 United States</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/13</identifier>\r\n" + 
    		"                <datestamp>2013-08-25T02:12:54Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_9</setSpec>\r\n" + 
    		"                <setSpec>col_10673_10</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Panorama da Telemedicina</dc:title>\r\n" + 
    		"<dc:subject>Telemedicina, medicina, redes sociais, saúde</dc:subject>\r\n" + 
    		"<dc:description/>\r\n" + 
    		"<dc:date>2013-08-25T02:12:54Z</dc:date>\r\n" + 
    		"<dc:date>2013-08-25T02:12:54Z</dc:date>\r\n" + 
    		"<dc:date>2013-08-25</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/13</dc:identifier>\r\n" + 
    		"<dc:relation>http://eaulas.usp.br/portal/video.action?idItem=1809</dc:relation>\r\n" + 
    		"<dc:relation>Telemedicina</dc:relation>\r\n" + 
    		"<dc:relation>MPT1445-1</dc:relation>\r\n" + 
    		"<dc:publisher>Faculdade de Medicina</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/16</identifier>\r\n" + 
    		"                <datestamp>2013-09-04T02:00:22Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Testing embargo</dc:title>\r\n" + 
    		"<dc:creator>Aunet, Roger</dc:creator>\r\n" + 
    		"<dc:date>2013-09-03T12:35:11Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-03T12:35:11Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-03</dc:date>\r\n" + 
    		"<dc:type>Book</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/16</dc:identifier>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/us/</dc:rights>\r\n" + 
    		"<dc:rights>Attribution 3.0 United States</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:1842/1759</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:16:38Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>StORe online questionnaire</dc:title>\r\n" + 
    		"<dc:creator>Pryor, Graham</dc:creator>\r\n" + 
    		"<dc:subject>librarianship</dc:subject>\r\n" + 
    		"<dc:description>Project wiki:&#13;\r\n" + 
    		"http://jiscstore.jot.com/WikiHome</dc:description>\r\n" + 
    		"<dc:description>This is an anonymised file containing the responses to the StORe online questionnaire</dc:description>\r\n" + 
    		"<dc:date>2008-01-29T13:47:07Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:38Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:38Z</dc:date>\r\n" + 
    		"<dc:date>2006-12</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/1842/1759</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/7</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/1842/1759</dc:identifier>\r\n" + 
    		"<dc:language>en</dc:language>\r\n" + 
    		"<dc:publisher>Edinburgh Research Archive</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10459.3/13</identifier>\r\n" + 
    		"                <datestamp>2013-07-29T11:24:59Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_10</setSpec>\r\n" + 
    		"                <setSpec>col_10673_13</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Manuel de bibliothéconomie</dc:title>\r\n" + 
    		"<dc:subject>Biblioteconomia</dc:subject>\r\n" + 
    		"<dc:description>628 p. : il.; 21 cm</dc:description>\r\n" + 
    		"<dc:date>2012-06-23T15:28:11Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-29T11:24:59Z</dc:date>\r\n" + 
    		"<dc:date>2012-06-23T15:28:11Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-29T11:24:59Z</dc:date>\r\n" + 
    		"<dc:date>1897</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10459.3/13</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10459</dc:identifier>\r\n" + 
    		"<dc:language>fr</dc:language>\r\n" + 
    		"<dc:relation>http://cataleg.udl.cat/record=b1066979~S11*cat</dc:relation>\r\n" + 
    		"<dc:rights>Còpia permesa amb finalitat d'estudi o recerca, citant l'autor i la font: \"Fons Gili i Gaya / Universitat de Lleida\". Per a qualsevol altre ús cal demanar autorització.</dc:rights>\r\n" + 
    		"<dc:publisher>Universitat de Lleida</dc:publisher>\r\n" + 
    		"<dc:source>Publicació original: Paris : H. Welter, 1897</dc:source>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/14</identifier>\r\n" + 
    		"                <datestamp>2013-08-26T16:04:32Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_9</setSpec>\r\n" + 
    		"                <setSpec>col_10673_10</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Ética em Telemedicina</dc:title>\r\n" + 
    		"<dc:subject>Telemedicina, medicina, redes sociais, saúde</dc:subject>\r\n" + 
    		"<dc:date>2013-08-25T02:12:55Z</dc:date>\r\n" + 
    		"<dc:date>2013-08-25T02:12:55Z</dc:date>\r\n" + 
    		"<dc:date>2013-08-25</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/14</dc:identifier>\r\n" + 
    		"<dc:relation>http://eaulas.usp.br/portal/video.action?idItem=1803</dc:relation>\r\n" + 
    		"<dc:relation>Telemedicina</dc:relation>\r\n" + 
    		"<dc:relation>MPT1445-1</dc:relation>\r\n" + 
    		"<dc:publisher>Faculdade de Medicina</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/17</identifier>\r\n" + 
    		"                <datestamp>2013-09-04T02:00:18Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Testing embargo bitstream</dc:title>\r\n" + 
    		"<dc:creator>Aunet, Roger</dc:creator>\r\n" + 
    		"<dc:date>2013-09-03T13:13:03Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-03T13:13:03Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-03</dc:date>\r\n" + 
    		"<dc:type>Book</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/17</dc:identifier>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/us/</dc:rights>\r\n" + 
    		"<dc:rights>Attribution 3.0 United States</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/8</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:16:40Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>IRI-Scotland academic author survey</dc:title>\r\n" + 
    		"<dc:creator>Andrew, Theo</dc:creator>\r\n" + 
    		"<dc:subject>Scholarly Communication</dc:subject>\r\n" + 
    		"<dc:description>IRIS academic author survey pt1: Survey Overview.&#13;\r\n" + 
    		"Number of respondents: 488&#13;\r\n" + 
    		"Expected number of respondents: 500&#13;\r\n" + 
    		"Response rate: 97.6%&#13;\r\n" + 
    		"Launch date: 15 Mar 2006&#13;\r\n" + 
    		"Close date: 25 Apr 2006 &#13;\r\n" + 
    		"&#13;\r\n" + 
    		"IRIS academic author survey pt2: Survey Overview.&#13;\r\n" + 
    		"Number of respondents: 53&#13;\r\n" + 
    		"Expected number of respondents: 50&#13;\r\n" + 
    		"Response rate: 106.0%&#13;\r\n" + 
    		"Launch date: 19 May 2006&#13;\r\n" + 
    		"Close date: 28 May 2006</dc:description>\r\n" + 
    		"<dc:description>The IRI-Scotland project (http://www.iriscotland.lib.ed.ac.uk/) carried out a series of online questionnaires in 2006 to assess the attitudes towards open access and institutional repositories within the higher education community in Scotland.  In total, three questionnaires were targeted at different stakeholder groups within the community - academic authors, technical staff responsible for repository development, and senior management from academic libraries. &#13;\r\n" + 
    		"&#13;\r\n" + 
    		"For logistical reasons the first survey was undertaken during two different time periods and was aimed at academic authors from the following higher education institutions in Scotland; Abertay University, Dundee University , Edinburgh College of Art, Edinburgh University, Glasgow University, Glasgow Caledonian, Heriot Watt University, Robert Gordon University, St Andrews University, Stirling University, Strathclyde University, University of Aberdeen, University of Dundee, and University of Paisley. Library or Information services staff arranged for an email to be sent to as many research staff as possible from the target community.&#13;\r\n" + 
    		"&#13;\r\n" + 
    		"We present here anonymous data from the two parts of the academic author survey in comma separated value format.</dc:description>\r\n" + 
    		"<dc:date>2008-04-18T10:54:12Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:40Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:40Z</dc:date>\r\n" + 
    		"<dc:date>2008-04-18T10:54:12Z</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>Andrew, Theo; Greig, Morag; Ashworth, Susan. (2008). IRI-Scotland academic author survey [Dataset].</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/8</dc:identifier>\r\n" + 
    		"<dc:relation>yes</dc:relation>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/18</identifier>\r\n" + 
    		"                <datestamp>2013-09-05T02:00:35Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Test Item</dc:title>\r\n" + 
    		"<dc:creator>Surname, Name</dc:creator>\r\n" + 
    		"<dc:subject>Research Subject Categories::TECHNOLOGY</dc:subject>\r\n" + 
    		"<dc:description>Just an image</dc:description>\r\n" + 
    		"<dc:date>2013-09-04T11:53:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-04T11:53:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-04</dc:date>\r\n" + 
    		"<dc:type>Image</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/18</dc:identifier>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by-nc-nd/3.0/us/</dc:rights>\r\n" + 
    		"<dc:rights>Attribution-NonCommercial-NoDerivs 3.0 United States</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/9</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:16:41Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>IRI-Scotland Technical Staff survey</dc:title>\r\n" + 
    		"<dc:creator>Greig, Morag</dc:creator>\r\n" + 
    		"<dc:subject>Information Environment</dc:subject>\r\n" + 
    		"<dc:description>IRIS Technical Staff survey:Survey Overview.&#13;\r\n" + 
    		"Number of respondents: 12&#13;\r\n" + 
    		"Expected number of respondents: 20&#13;\r\n" + 
    		"Response rate: 60.0%&#13;\r\n" + 
    		"Launch date: 26 Apr 2006&#13;\r\n" + 
    		"Close date: 28 May 2006</dc:description>\r\n" + 
    		"<dc:description>The IRI-Scotland project (http://www.iriscotland.lib.ed.ac.uk/) carried out a series of online questionnaires in 2006 to assess the attitudes towards open access and institutional repositories within the higher education community in Scotland.  In total, three questionnaires were targeted at different stakeholder groups within the community  -  academic authors, technical staff responsible for repository development, and senior management from academic libraries. &#13;\r\n" + 
    		"&#13;\r\n" + 
    		"The second IRI-Scotland survey was targeted at technical staff, usually based in academic libraries or aligned information services support groups, who would be responsible for developing a digital repository in their institution. The questions were aimed to help determine the functional requirements needed to build a national hosted repository service that would be suitable for the current and future repository infrastructure in Scotland.&#13;\r\n" + 
    		"&#13;\r\n" + 
    		"We present here anonymous data from the technical staff survey in comma separated value format.</dc:description>\r\n" + 
    		"<dc:date>2008-04-18T10:54:22Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:41Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:41Z</dc:date>\r\n" + 
    		"<dc:date>2008-04-18T10:54:22Z</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>Andrew, Theo; Greig, Morag; Ashworth, Susan. (2008). IRI-Scotland Technical Staff survey [Dataset].</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/9</dc:identifier>\r\n" + 
    		"<dc:relation>yes</dc:relation>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/10</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:16:42Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>IRI-Scotland senior management survey</dc:title>\r\n" + 
    		"<dc:creator>Ashworth, Susan</dc:creator>\r\n" + 
    		"<dc:subject>Information Environment</dc:subject>\r\n" + 
    		"<dc:description>IRIS senior management survey: Survey Overview&#13;\r\n" + 
    		"Number of respondents: 25&#13;\r\n" + 
    		"Expected number of respondents: 35&#13;\r\n" + 
    		"Response rate: 71.4%&#13;\r\n" + 
    		"Launch date: 15 Mar 2006&#13;\r\n" + 
    		"Close date: 25 Apr 2006</dc:description>\r\n" + 
    		"<dc:description>The IRI-Scotland project (http://www.iriscotland.lib.ed.ac.uk/) carried out a series of online questionnaires in 2006 to assess the attitudes towards open access and institutional repositories within the higher education community in Scotland.  In total, three questionnaires were targeted at different stakeholder groups within the community – academic authors, technical staff responsible for repository development, and senior management from academic libraries. &#13;\r\n" + 
    		"&#13;\r\n" + 
    		"The third IRI-Scotland survey was targeted at senior management with the intent to identify the level of repository adoption in the community, assess policies relating to open access, and to identify suitable institutional contacts for the project.&#13;\r\n" + 
    		"&#13;\r\n" + 
    		"We present here anonymous data from the senior management survey in comma separated value format.</dc:description>\r\n" + 
    		"<dc:date>2008-04-18T10:54:27Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:42Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:42Z</dc:date>\r\n" + 
    		"<dc:date>2008-04-18T10:54:27Z</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>Andrew, Theo; Greig, Morag; Ashworth, Susan. (2008). IRI-Scotland senior management survey [Dataset].</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/10</dc:identifier>\r\n" + 
    		"<dc:relation>yes</dc:relation>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/20</identifier>\r\n" + 
    		"                <datestamp>2013-09-05T02:00:27Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>SWORD: Simple Web-service Offering Repository Deposit</dc:title>\r\n" + 
    		"<dc:creator>Allinson, Julie</dc:creator>\r\n" + 
    		"<dc:creator>François, Sebastien</dc:creator>\r\n" + 
    		"<dc:creator>Lewis, Stuart</dc:creator>\r\n" + 
    		"<dc:description>This article offers a twofold introduction to the JISC-funded SWORD Project which ran for eight months in mid-2007. Firstly it presents an overview of the methods and madness that led us to where we currently are, including a timeline of how this work moved through an informal working group to a lightweight, distributed project. Secondly, it offers an explanation of the outputs produced for the SWORD Project and their potential benefits for the repositories community. SWORD, which stands for Simple Web service Offering Repository Deposit, came into being in March 2007 but was preceded by a series of discussions and activities which have contributed much to the project, known as the 'Deposit API'. The project itself was funded under the JISC Repositories and Preservation Programme, Tools and Innovation strand, with the over-arching aim of scoping, defining, developing and testing a standard mechanism for depositing into repositories and other systems. The motivation was that there was no standard way of doing this currently and increasingly scenarios were arising that might usefully leverage such a standard.</dc:description>\r\n" + 
    		"<dc:date>2013-09-04T15:09:17Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-04T15:09:17Z</dc:date>\r\n" + 
    		"<dc:date>2008-01</dc:date>\r\n" + 
    		"<dc:date>2013-09-04T15:09:19Z</dc:date>\r\n" + 
    		"<dc:type>Journal Article</dc:type>\r\n" + 
    		"<dc:identifier>http://www.ariadne.ac.uk/issue54/allinson-et-al/</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/20</dc:identifier>\r\n" + 
    		"<dc:language>en</dc:language>\r\n" + 
    		"<dc:rights>Julie Allinson, Sebastien François, Stuart Lewis</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10986/2226</identifier>\r\n" + 
    		"                <datestamp>2013-05-06T16:54:49Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_18</setSpec>\r\n" + 
    		"                <setSpec>col_10673_19</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>The Health Workforce in Ethiopia :\r\n" + 
    		"            Addressing the Remaining Challenges</dc:title>\r\n" + 
    		"<dc:subject>ABORTION</dc:subject>\r\n" + 
    		"<dc:subject>ABORTION CARE</dc:subject>\r\n" + 
    		"<dc:subject>ACCESS TO CARE</dc:subject>\r\n" + 
    		"<dc:subject>ACCESS TO EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>ACCESS TO HEALTH CARE</dc:subject>\r\n" + 
    		"<dc:subject>ACCESS TO HEALTH SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>AGED</dc:subject>\r\n" + 
    		"<dc:subject>ANTENATAL CARE</dc:subject>\r\n" + 
    		"<dc:subject>BABIES</dc:subject>\r\n" + 
    		"<dc:subject>BASIC HEALTH</dc:subject>\r\n" + 
    		"<dc:subject>BASIC HEALTH SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>BULLETIN</dc:subject>\r\n" + 
    		"<dc:subject>CERTIFICATION</dc:subject>\r\n" + 
    		"<dc:subject>CHILD HEALTH</dc:subject>\r\n" + 
    		"<dc:subject>CITIES</dc:subject>\r\n" + 
    		"<dc:subject>CLINICS</dc:subject>\r\n" + 
    		"<dc:subject>COMMUNITY HEALTH</dc:subject>\r\n" + 
    		"<dc:subject>COUNTRY OF DESTINATION</dc:subject>\r\n" + 
    		"<dc:subject>DEATH RATE</dc:subject>\r\n" + 
    		"<dc:subject>DEATHS</dc:subject>\r\n" + 
    		"<dc:subject>DECISION MAKING</dc:subject>\r\n" + 
    		"<dc:subject>DEVELOPING COUNTRIES</dc:subject>\r\n" + 
    		"<dc:subject>DISSEMINATION</dc:subject>\r\n" + 
    		"<dc:subject>DOCTORS</dc:subject>\r\n" + 
    		"<dc:subject>DRUGS</dc:subject>\r\n" + 
    		"<dc:subject>EMERGENCY OBSTETRIC CARE</dc:subject>\r\n" + 
    		"<dc:subject>EMPLOYMENT</dc:subject>\r\n" + 
    		"<dc:subject>ENVIRONMENTAL HEALTH</dc:subject>\r\n" + 
    		"<dc:subject>EQUALITY OF OPPORTUNITY</dc:subject>\r\n" + 
    		"<dc:subject>EQUITABLE ACCESS</dc:subject>\r\n" + 
    		"<dc:subject>EYE CARE</dc:subject>\r\n" + 
    		"<dc:subject>FAMILIES</dc:subject>\r\n" + 
    		"<dc:subject>FAMILY PLANNING</dc:subject>\r\n" + 
    		"<dc:subject>FEMALE</dc:subject>\r\n" + 
    		"<dc:subject>FEMALES</dc:subject>\r\n" + 
    		"<dc:subject>GENDER</dc:subject>\r\n" + 
    		"<dc:subject>GENERAL PRACTITIONER</dc:subject>\r\n" + 
    		"<dc:subject>GENERAL PRACTITIONERS</dc:subject>\r\n" + 
    		"<dc:subject>GROSS DOMESTIC PRODUCT</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH CARE</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH CARE FACILITIES</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH CARE PROVIDER</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH CARE SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH CENTERS</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH CENTRE</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH EXPENDITURE</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH EXPENDITURES</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH EXTENSION</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH FACILITIES</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH INDICATORS</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH MANAGEMENT</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH MESSAGES</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH ORGANIZATION</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH OUTCOMES</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH POSTS</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH PROFESSIONAL</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH PROFESSIONALS</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH PROVIDERS</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH RESULTS</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH SERVICE</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH SYSTEMS</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH SYSTEMS STRENGTHENING</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH TRAINING</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH WORKFORCE</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH-SEEKING BEHAVIOR</dc:subject>\r\n" + 
    		"<dc:subject>HIV</dc:subject>\r\n" + 
    		"<dc:subject>HIV/AIDS</dc:subject>\r\n" + 
    		"<dc:subject>HOME VISITS</dc:subject>\r\n" + 
    		"<dc:subject>HOSPITAL</dc:subject>\r\n" + 
    		"<dc:subject>HOSPITALS</dc:subject>\r\n" + 
    		"<dc:subject>HOUSEHOLD INCOME</dc:subject>\r\n" + 
    		"<dc:subject>HR</dc:subject>\r\n" + 
    		"<dc:subject>HUMAN IMMUNODEFICIENCY VIRUS</dc:subject>\r\n" + 
    		"<dc:subject>HUMAN RESOURCE MANAGEMENT</dc:subject>\r\n" + 
    		"<dc:subject>HUMAN RESOURCES</dc:subject>\r\n" + 
    		"<dc:subject>IMMIGRATION</dc:subject>\r\n" + 
    		"<dc:subject>IMMUNIZATION</dc:subject>\r\n" + 
    		"<dc:subject>IMMUNODEFICIENCY</dc:subject>\r\n" + 
    		"<dc:subject>INCOME</dc:subject>\r\n" + 
    		"<dc:subject>INFANT</dc:subject>\r\n" + 
    		"<dc:subject>INFANT HEALTH</dc:subject>\r\n" + 
    		"<dc:subject>INFANT HEALTH CARE</dc:subject>\r\n" + 
    		"<dc:subject>INFORMATION SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>INTERVENTION</dc:subject>\r\n" + 
    		"<dc:subject>IRON</dc:subject>\r\n" + 
    		"<dc:subject>JOB SECURITY</dc:subject>\r\n" + 
    		"<dc:subject>LABOR FORCE</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MARKET</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MARKETS</dc:subject>\r\n" + 
    		"<dc:subject>LABORATORY WORKERS</dc:subject>\r\n" + 
    		"<dc:subject>LARGE POPULATION</dc:subject>\r\n" + 
    		"<dc:subject>LEGAL STATUS</dc:subject>\r\n" + 
    		"<dc:subject>LIVE BIRTHS</dc:subject>\r\n" + 
    		"<dc:subject>LIVING CONDITIONS</dc:subject>\r\n" + 
    		"<dc:subject>LOCAL COMMUNITY</dc:subject>\r\n" + 
    		"<dc:subject>MALARIA</dc:subject>\r\n" + 
    		"<dc:subject>MALARIA CASES</dc:subject>\r\n" + 
    		"<dc:subject>MATERNAL AND CHILD HEALTH</dc:subject>\r\n" + 
    		"<dc:subject>MATERNAL CARE</dc:subject>\r\n" + 
    		"<dc:subject>MATERNAL HEALTH</dc:subject>\r\n" + 
    		"<dc:subject>MATERNAL HEALTH SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>MATERNAL MORTALITY</dc:subject>\r\n" + 
    		"<dc:subject>MATERNAL MORTALITY RATIO</dc:subject>\r\n" + 
    		"<dc:subject>MATERNAL MORTALITY RATIOS</dc:subject>\r\n" + 
    		"<dc:subject>MATERNITY CARE</dc:subject>\r\n" + 
    		"<dc:subject>MATERNITY LEAVE</dc:subject>\r\n" + 
    		"<dc:subject>MEDICAL CARE</dc:subject>\r\n" + 
    		"<dc:subject>MEDICAL DOCTOR</dc:subject>\r\n" + 
    		"<dc:subject>MEDICAL DOCTORS</dc:subject>\r\n" + 
    		"<dc:subject>MEDICAL EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>MEDICAL SCHOOL</dc:subject>\r\n" + 
    		"<dc:subject>MEDICAL SCHOOLS</dc:subject>\r\n" + 
    		"<dc:subject>MEDICINE</dc:subject>\r\n" + 
    		"<dc:subject>MIDWIFE</dc:subject>\r\n" + 
    		"<dc:subject>MIDWIFERY</dc:subject>\r\n" + 
    		"<dc:subject>MIDWIVES</dc:subject>\r\n" + 
    		"<dc:subject>MIGRANTS</dc:subject>\r\n" + 
    		"<dc:subject>MIGRATION</dc:subject>\r\n" + 
    		"<dc:subject>MILLENNIUM DEVELOPMENT GOALS</dc:subject>\r\n" + 
    		"<dc:subject>MINISTRY OF EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>MINISTRY OF HEALTH</dc:subject>\r\n" + 
    		"<dc:subject>MINORITY</dc:subject>\r\n" + 
    		"<dc:subject>MORBIDITY</dc:subject>\r\n" + 
    		"<dc:subject>MORTALITY</dc:subject>\r\n" + 
    		"<dc:subject>MORTALITY RATE</dc:subject>\r\n" + 
    		"<dc:subject>MOTHER</dc:subject>\r\n" + 
    		"<dc:subject>MOTHERS</dc:subject>\r\n" + 
    		"<dc:subject>NEONATAL HEALTH</dc:subject>\r\n" + 
    		"<dc:subject>NEONATAL MORTALITY</dc:subject>\r\n" + 
    		"<dc:subject>NEWBORN</dc:subject>\r\n" + 
    		"<dc:subject>NEWBORN CARE</dc:subject>\r\n" + 
    		"<dc:subject>NURSE</dc:subject>\r\n" + 
    		"<dc:subject>NURSES</dc:subject>\r\n" + 
    		"<dc:subject>NURSING</dc:subject>\r\n" + 
    		"<dc:subject>OBSTETRICS</dc:subject>\r\n" + 
    		"<dc:subject>PARAMEDICS</dc:subject>\r\n" + 
    		"<dc:subject>PATIENT</dc:subject>\r\n" + 
    		"<dc:subject>PATIENT SATISFACTION</dc:subject>\r\n" + 
    		"<dc:subject>PATIENTS</dc:subject>\r\n" + 
    		"<dc:subject>PHARMACIST</dc:subject>\r\n" + 
    		"<dc:subject>PHARMACISTS</dc:subject>\r\n" + 
    		"<dc:subject>PHARMACY</dc:subject>\r\n" + 
    		"<dc:subject>PHYSICIAN</dc:subject>\r\n" + 
    		"<dc:subject>PHYSICIANS</dc:subject>\r\n" + 
    		"<dc:subject>POLICY DISCUSSIONS</dc:subject>\r\n" + 
    		"<dc:subject>POLICY IMPLICATIONS</dc:subject>\r\n" + 
    		"<dc:subject>POLICY-MAKING PROCESS</dc:subject>\r\n" + 
    		"<dc:subject>POPULATION DATA</dc:subject>\r\n" + 
    		"<dc:subject>POSTNATAL CARE</dc:subject>\r\n" + 
    		"<dc:subject>POTENTIAL MIGRANTS</dc:subject>\r\n" + 
    		"<dc:subject>POTENTIAL USERS</dc:subject>\r\n" + 
    		"<dc:subject>PREGNANCY</dc:subject>\r\n" + 
    		"<dc:subject>PREGNANT MOTHERS</dc:subject>\r\n" + 
    		"<dc:subject>PREMATURE DEATH</dc:subject>\r\n" + 
    		"<dc:subject>PREVALENCE</dc:subject>\r\n" + 
    		"<dc:subject>PROBABILITY</dc:subject>\r\n" + 
    		"<dc:subject>PROGRESS</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC HEALTH</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC HEALTH PROVISION</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC HEALTH WORKERS</dc:subject>\r\n" + 
    		"<dc:subject>PUSH FACTORS</dc:subject>\r\n" + 
    		"<dc:subject>QUALITY ASSURANCE</dc:subject>\r\n" + 
    		"<dc:subject>RURAL AREAS</dc:subject>\r\n" + 
    		"<dc:subject>RURAL COMMUNITIES</dc:subject>\r\n" + 
    		"<dc:subject>RURAL RESIDENTS</dc:subject>\r\n" + 
    		"<dc:subject>SANITATION</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOL CURRICULA</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOL HEALTH</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOL STUDENTS</dc:subject>\r\n" + 
    		"<dc:subject>SECONDARY EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>SECONDARY SCHOOL</dc:subject>\r\n" + 
    		"<dc:subject>SECONDARY SCHOOL EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>SEX</dc:subject>\r\n" + 
    		"<dc:subject>SEXUAL VIOLENCE</dc:subject>\r\n" + 
    		"<dc:subject>SHORT SUPPLY</dc:subject>\r\n" + 
    		"<dc:subject>SICK LEAVE</dc:subject>\r\n" + 
    		"<dc:subject>SOCIOECONOMIC DIFFERENCES</dc:subject>\r\n" + 
    		"<dc:subject>SPECIALIST</dc:subject>\r\n" + 
    		"<dc:subject>SPECIALISTS</dc:subject>\r\n" + 
    		"<dc:subject>SURGERY</dc:subject>\r\n" + 
    		"<dc:subject>TEACHING HOSPITALS</dc:subject>\r\n" + 
    		"<dc:subject>TECHNICAL ASSISTANCE</dc:subject>\r\n" + 
    		"<dc:subject>TETANUS</dc:subject>\r\n" + 
    		"<dc:subject>TRAINING OPPORTUNITIES</dc:subject>\r\n" + 
    		"<dc:subject>TREATMENT</dc:subject>\r\n" + 
    		"<dc:subject>URBAN AREAS</dc:subject>\r\n" + 
    		"<dc:subject>URBAN BIAS</dc:subject>\r\n" + 
    		"<dc:subject>URBAN POPULATIONS</dc:subject>\r\n" + 
    		"<dc:subject>VICTIMS</dc:subject>\r\n" + 
    		"<dc:subject>WORKERS</dc:subject>\r\n" + 
    		"<dc:subject>WORKING CONDITIONS</dc:subject>\r\n" + 
    		"<dc:subject>WORKPLACE</dc:subject>\r\n" + 
    		"<dc:subject>WORLD HEALTH ORGANIZATION</dc:subject>\r\n" + 
    		"<dc:subject>YOUNG WOMEN</dc:subject>\r\n" + 
    		"<dc:description>Health indicators in Ethiopia,\r\n" + 
    		"            particularly on child health and malaria, have improved\r\n" + 
    		"            significantly in recent years, with the next challenge now\r\n" + 
    		"            focused on improving maternal health indicators.\r\n" + 
    		"            Improvements in child health and malaria in particular can\r\n" + 
    		"            be attributed to strong government commitment towards health\r\n" + 
    		"            results, reflected in a number of notable policies and\r\n" + 
    		"            programs related to Human Resources for Health (HRH), in\r\n" + 
    		"            particular the health extension worker program. However,\r\n" + 
    		"            indicators related to maternal health remain problematic.\r\n" + 
    		"            Ethiopia has one of the lowest levels of assisted deliveries\r\n" + 
    		"            in the region. Although increases in the number of health\r\n" + 
    		"            workers particularly in rural areas may have contributed to\r\n" + 
    		"            improving access to some health services, it is in the\r\n" + 
    		"            government's interest to further improve the stock,\r\n" + 
    		"            distribution, and performance of relevant health workers in\r\n" + 
    		"            Ethiopia, particularly to bring about improvement in access\r\n" + 
    		"            to maternal health services for the poor. This document\r\n" + 
    		"            reviews the current HRH situation in Ethiopia, summarizes\r\n" + 
    		"            the evidence on population use of select health services,\r\n" + 
    		"            and offers relevant policy options to assist the government\r\n" + 
    		"            finalize its new human resources strategy and address\r\n" + 
    		"            remaining health challenges.</dc:description>\r\n" + 
    		"<dc:date>2013-05-06T16:54:48Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06T16:54:48Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10986/2226</dc:identifier>\r\n" + 
    		"<dc:relation>World Bank Study</dc:relation>\r\n" + 
    		"<dc:rights>CC BY 3.0 Unported</dc:rights>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/</dc:rights>\r\n" + 
    		"<dc:publisher>World Bank</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/16</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:16:45Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Database of Dedications to Saints in Medieval Scotland</dc:title>\r\n" + 
    		"<dc:contributor>ahrc</dc:contributor>\r\n" + 
    		"<dc:subject>Saints</dc:subject>\r\n" + 
    		"<dc:description>The Survey of Dedications to Saints in Medieval Scotland is the result of a three-year project funded by a Major Research Grant from the Arts and Humanities Research Council (AHRC). The project is based in the Scottish History subject area of the School of History, Classics and Archaeology at the University of Edinburgh. The project began in October 2004 and the final update of data from the AHRC-funded phase of the project was completed in December 2007. However, the contents of the database will continue to be updated annually (in December) to include material from sources outwith the scope of the original survey.</dc:description>\r\n" + 
    		"<dc:date>2008-09-05T16:38:30Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:45Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:45Z</dc:date>\r\n" + 
    		"<dc:date>2008-09-05T16:38:30Z</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>Davies, John; Williamson, Eila; Boardman, Steve. (2008). Database of dedications to Saints in Medieval Scotland [Dataset]. University of Edinburgh, School of History, Classics and Archaeology.</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/16</dc:identifier>\r\n" + 
    		"<dc:language>eng</dc:language>\r\n" + 
    		"<dc:relation>http://webdb.ucs.ed.ac.uk/saints/</dc:relation>\r\n" + 
    		"<dc:rights>Unless explicitly stated otherwise, all material is copyright © The University of Edinburgh</dc:rights>\r\n" + 
    		"<dc:publisher>University of Edinburgh, School of History, Classics and Archaeology</dc:publisher>\r\n" + 
    		"<dc:source>http://www.shc.ed.ac.uk/Research/saints/index.htm</dc:source>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/21</identifier>\r\n" + 
    		"                <datestamp>2013-09-05T02:00:33Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>SWORD: Simple Web-service Offering Repository Deposit</dc:title>\r\n" + 
    		"<dc:creator>Allinson, Julie</dc:creator>\r\n" + 
    		"<dc:creator>François, Sebastien</dc:creator>\r\n" + 
    		"<dc:creator>Lewis, Stuart</dc:creator>\r\n" + 
    		"<dc:description>This article offers a twofold introduction to the JISC-funded SWORD Project which ran for eight months in mid-2007. Firstly it presents an overview of the methods and madness that led us to where we currently are, including a timeline of how this work moved through an informal working group to a lightweight, distributed project. Secondly, it offers an explanation of the outputs produced for the SWORD Project and their potential benefits for the repositories community. SWORD, which stands for Simple Web service Offering Repository Deposit, came into being in March 2007 but was preceded by a series of discussions and activities which have contributed much to the project, known as the 'Deposit API'. The project itself was funded under the JISC Repositories and Preservation Programme, Tools and Innovation strand, with the over-arching aim of scoping, defining, developing and testing a standard mechanism for depositing into repositories and other systems. The motivation was that there was no standard way of doing this currently and increasingly scenarios were arising that might usefully leverage such a standard.</dc:description>\r\n" + 
    		"<dc:date>2013-09-04T17:36:51Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-04T17:36:51Z</dc:date>\r\n" + 
    		"<dc:date>2008-01</dc:date>\r\n" + 
    		"<dc:date>2013-09-04T17:36:53Z</dc:date>\r\n" + 
    		"<dc:type>Journal Article</dc:type>\r\n" + 
    		"<dc:identifier>http://www.ariadne.ac.uk/issue54/allinson-et-al/</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/21</dc:identifier>\r\n" + 
    		"<dc:language>en</dc:language>\r\n" + 
    		"<dc:rights>Julie Allinson, Sebastien François, Stuart Lewis</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10986/2228</identifier>\r\n" + 
    		"                <datestamp>2013-05-06T16:54:55Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_18</setSpec>\r\n" + 
    		"                <setSpec>col_10673_19</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>The Remittance Market in India :\r\n" + 
    		"            Opportunities, Challenges, and Policy Options</dc:title>\r\n" + 
    		"<dc:subject>ACCESS TO PAYMENT</dc:subject>\r\n" + 
    		"<dc:subject>ACCOUNT TRANSFERS</dc:subject>\r\n" + 
    		"<dc:subject>ACCOUNT-TO-ACCOUNT</dc:subject>\r\n" + 
    		"<dc:subject>ACROSS BORDERS</dc:subject>\r\n" + 
    		"<dc:subject>AMOUNT OF REMITTANCES</dc:subject>\r\n" + 
    		"<dc:subject>AMOUNTS OF REMITTANCES</dc:subject>\r\n" + 
    		"<dc:subject>ANTI-MONEY LAUNDERING</dc:subject>\r\n" + 
    		"<dc:subject>AVERAGE REMITTANCE</dc:subject>\r\n" + 
    		"<dc:subject>AVERAGE REMITTANCE SIZE</dc:subject>\r\n" + 
    		"<dc:subject>BALANCE OF PAYMENT</dc:subject>\r\n" + 
    		"<dc:subject>BALANCE OF PAYMENT CRISIS</dc:subject>\r\n" + 
    		"<dc:subject>BALANCE OF PAYMENTS</dc:subject>\r\n" + 
    		"<dc:subject>BANK ACCOUNT</dc:subject>\r\n" + 
    		"<dc:subject>BANK ACCOUNTS</dc:subject>\r\n" + 
    		"<dc:subject>BANK CUSTOMERS</dc:subject>\r\n" + 
    		"<dc:subject>BENEFITS OF SAVINGS</dc:subject>\r\n" + 
    		"<dc:subject>BORDER TRANSFERS</dc:subject>\r\n" + 
    		"<dc:subject>CAPITA INCOME</dc:subject>\r\n" + 
    		"<dc:subject>CAPITAL ACCOUNT</dc:subject>\r\n" + 
    		"<dc:subject>CAPITAL FLOWS</dc:subject>\r\n" + 
    		"<dc:subject>CASH PAYOUTS</dc:subject>\r\n" + 
    		"<dc:subject>CASH REMITTANCES</dc:subject>\r\n" + 
    		"<dc:subject>CASH TRANSFERS</dc:subject>\r\n" + 
    		"<dc:subject>CASH-TO-CASH</dc:subject>\r\n" + 
    		"<dc:subject>CENTER FOR IMMIGRATION STUDIES</dc:subject>\r\n" + 
    		"<dc:subject>CITIZENS</dc:subject>\r\n" + 
    		"<dc:subject>CLEARING HOUSE</dc:subject>\r\n" + 
    		"<dc:subject>COMMERCIAL BANKS</dc:subject>\r\n" + 
    		"<dc:subject>COMMUNITIES OF ORIGIN</dc:subject>\r\n" + 
    		"<dc:subject>CONSUMER PROTECTION</dc:subject>\r\n" + 
    		"<dc:subject>COST OF REMITTANCES</dc:subject>\r\n" + 
    		"<dc:subject>CREDIT CARDS</dc:subject>\r\n" + 
    		"<dc:subject>CREDIT CONSTRAINTS</dc:subject>\r\n" + 
    		"<dc:subject>DEBIT CARDS</dc:subject>\r\n" + 
    		"<dc:subject>DEPOSIT</dc:subject>\r\n" + 
    		"<dc:subject>DEPOSITORS</dc:subject>\r\n" + 
    		"<dc:subject>DEPOSITS</dc:subject>\r\n" + 
    		"<dc:subject>DETERMINANTS OF REMITTANCES</dc:subject>\r\n" + 
    		"<dc:subject>DEVELOPING COUNTRIES</dc:subject>\r\n" + 
    		"<dc:subject>DEVELOPMENT POTENTIAL OF REMITTANCES</dc:subject>\r\n" + 
    		"<dc:subject>DIASPORA</dc:subject>\r\n" + 
    		"<dc:subject>DISSEMINATION</dc:subject>\r\n" + 
    		"<dc:subject>DOMESTIC MIGRATION</dc:subject>\r\n" + 
    		"<dc:subject>DOMESTIC REMITTANCE</dc:subject>\r\n" + 
    		"<dc:subject>DOMESTIC REMITTANCES</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC UNCERTAINTY</dc:subject>\r\n" + 
    		"<dc:subject>ELECTRONIC FUNDS</dc:subject>\r\n" + 
    		"<dc:subject>EMERGENCIES</dc:subject>\r\n" + 
    		"<dc:subject>EMIGRATION</dc:subject>\r\n" + 
    		"<dc:subject>EMIGRATION COUNTRIES</dc:subject>\r\n" + 
    		"<dc:subject>EXCHANGE RATE</dc:subject>\r\n" + 
    		"<dc:subject>EXCHANGE RATES</dc:subject>\r\n" + 
    		"<dc:subject>EXTERNAL FINANCING</dc:subject>\r\n" + 
    		"<dc:subject>FAMILIES</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL ACCESS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL FLOWS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL INSTITUTION</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL PRODUCTS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL SERVICE PROVIDERS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN CURRENCY</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN DIRECT INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN EXCHANGE</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN EXCHANGE BUREAUS</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN EXCHANGE EARNINGS</dc:subject>\r\n" + 
    		"<dc:subject>FUTURE REMITTANCES</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL REMITTANCE</dc:subject>\r\n" + 
    		"<dc:subject>GROSS DOMESTIC PRODUCT</dc:subject>\r\n" + 
    		"<dc:subject>GROSS NATIONAL INCOME</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH CARE</dc:subject>\r\n" + 
    		"<dc:subject>HIGH SCHOOL EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>HOST COUNTRIES</dc:subject>\r\n" + 
    		"<dc:subject>HOUSEHOLD LEVEL</dc:subject>\r\n" + 
    		"<dc:subject>HUMAN DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>IMMIGRANT</dc:subject>\r\n" + 
    		"<dc:subject>IMMIGRANT POPULATION</dc:subject>\r\n" + 
    		"<dc:subject>IMMIGRANTS</dc:subject>\r\n" + 
    		"<dc:subject>IMMIGRATION</dc:subject>\r\n" + 
    		"<dc:subject>IMMIGRATION COUNTRIES</dc:subject>\r\n" + 
    		"<dc:subject>IMPORTANCE OF REMITTANCE FLOWS</dc:subject>\r\n" + 
    		"<dc:subject>IMPORTANCE OF REMITTANCES</dc:subject>\r\n" + 
    		"<dc:subject>INCOME</dc:subject>\r\n" + 
    		"<dc:subject>INCOMES</dc:subject>\r\n" + 
    		"<dc:subject>INFRASTRUCTURE DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>INTERNAL MIGRANTS</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL BANK</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL LAW</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL MIGRANT</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL MOBILITY</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL REMITTANCE</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL REMITTANCES</dc:subject>\r\n" + 
    		"<dc:subject>LABOR FORCE</dc:subject>\r\n" + 
    		"<dc:subject>LEGAL STATUS</dc:subject>\r\n" + 
    		"<dc:subject>LEVEL OF EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>LIQUIDITY</dc:subject>\r\n" + 
    		"<dc:subject>LIQUIDITY CONSTRAINTS</dc:subject>\r\n" + 
    		"<dc:subject>LOCAL CURRENCY</dc:subject>\r\n" + 
    		"<dc:subject>MACROECONOMIC DETERMINANTS</dc:subject>\r\n" + 
    		"<dc:subject>MARKET COMPETITION</dc:subject>\r\n" + 
    		"<dc:subject>MIGRANT</dc:subject>\r\n" + 
    		"<dc:subject>MIGRANT POPULATIONS</dc:subject>\r\n" + 
    		"<dc:subject>MIGRANT REMITTANCE</dc:subject>\r\n" + 
    		"<dc:subject>MIGRANT WORKERS</dc:subject>\r\n" + 
    		"<dc:subject>MIGRANTS</dc:subject>\r\n" + 
    		"<dc:subject>MIGRATION FLOWS</dc:subject>\r\n" + 
    		"<dc:subject>MIGRATION PATTERNS</dc:subject>\r\n" + 
    		"<dc:subject>MINIMUM BALANCE REQUIREMENTS</dc:subject>\r\n" + 
    		"<dc:subject>MONEY GRAM</dc:subject>\r\n" + 
    		"<dc:subject>MONEY LAUNDERING</dc:subject>\r\n" + 
    		"<dc:subject>MONEY ORDER</dc:subject>\r\n" + 
    		"<dc:subject>MONEY TRANSFER</dc:subject>\r\n" + 
    		"<dc:subject>MONEY TRANSFER OPERATOR</dc:subject>\r\n" + 
    		"<dc:subject>MONEY TRANSFER OPERATORS</dc:subject>\r\n" + 
    		"<dc:subject>MONEY TRANSFER SERVICE</dc:subject>\r\n" + 
    		"<dc:subject>MONEY TRANSFER SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>MONEY TRANSFERS</dc:subject>\r\n" + 
    		"<dc:subject>NATIONAL COUNCIL</dc:subject>\r\n" + 
    		"<dc:subject>NATIONALS</dc:subject>\r\n" + 
    		"<dc:subject>NATIVE POPULATION</dc:subject>\r\n" + 
    		"<dc:subject>PAYMENT SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>PAYMENTS INFRASTRUCTURES</dc:subject>\r\n" + 
    		"<dc:subject>POINT OF SALE</dc:subject>\r\n" + 
    		"<dc:subject>POLICY GOALS</dc:subject>\r\n" + 
    		"<dc:subject>POLICY GUIDANCE</dc:subject>\r\n" + 
    		"<dc:subject>POLICY MAKERS</dc:subject>\r\n" + 
    		"<dc:subject>POLICY RESEARCH</dc:subject>\r\n" + 
    		"<dc:subject>POLICY RESEARCH WORKING PAPER</dc:subject>\r\n" + 
    		"<dc:subject>POPULATION ESTIMATES</dc:subject>\r\n" + 
    		"<dc:subject>POST OFFICES</dc:subject>\r\n" + 
    		"<dc:subject>POSTAL MONEY ORDERS</dc:subject>\r\n" + 
    		"<dc:subject>PRACTITIONERS</dc:subject>\r\n" + 
    		"<dc:subject>PRIMARY EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>PROGRESS</dc:subject>\r\n" + 
    		"<dc:subject>PROOF OF RESIDENCE</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC POLICY</dc:subject>\r\n" + 
    		"<dc:subject>RECIPIENT COUNTRIES</dc:subject>\r\n" + 
    		"<dc:subject>RECIPIENT COUNTRY</dc:subject>\r\n" + 
    		"<dc:subject>RECIPIENTS OF REMITTANCES</dc:subject>\r\n" + 
    		"<dc:subject>REGULATORY FRAMEWORK</dc:subject>\r\n" + 
    		"<dc:subject>RELIGIOUS INSTITUTIONS</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE BUSINESS</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE CHANNELS</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE CORRIDORS</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE COSTS</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE FLOWS</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE INDUSTRY</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE INFLOWS</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE MARKET</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE OUTFLOWS</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE PAYMENT</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE PROCESS</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE PROVIDERS</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE RECEIPTS</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE RECIPIENTS</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE SENDER</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE SENDING</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE SENDING COUNTRIES</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE SERVICE</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE SERVICE PROVIDERS</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE TRANSFER</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE TRANSFERS</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCE-RECEIVING HOUSEHOLDS</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCES TRANSFERS</dc:subject>\r\n" + 
    		"<dc:subject>REMITTANCES · REMITTANCES</dc:subject>\r\n" + 
    		"<dc:subject>REMITTERS</dc:subject>\r\n" + 
    		"<dc:subject>RISING CONSUMPTION</dc:subject>\r\n" + 
    		"<dc:subject>RISK MANAGEMENT</dc:subject>\r\n" + 
    		"<dc:subject>RURAL AREAS</dc:subject>\r\n" + 
    		"<dc:subject>RURAL DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>SAVINGS ACCOUNTS</dc:subject>\r\n" + 
    		"<dc:subject>SAVINGS PRODUCTS</dc:subject>\r\n" + 
    		"<dc:subject>SAVINGS · SAVINGS</dc:subject>\r\n" + 
    		"<dc:subject>SECONDARY EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>SECURITIES</dc:subject>\r\n" + 
    		"<dc:subject>SEND REMITTANCES</dc:subject>\r\n" + 
    		"<dc:subject>SERVICE PROVIDER</dc:subject>\r\n" + 
    		"<dc:subject>SKILLED MIGRATION</dc:subject>\r\n" + 
    		"<dc:subject>SKILLED PROFESSIONALS</dc:subject>\r\n" + 
    		"<dc:subject>SKILLED WORKERS</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>SPECIAL REMITTANCE</dc:subject>\r\n" + 
    		"<dc:subject>SPEED OF DELIVERY</dc:subject>\r\n" + 
    		"<dc:subject>TECHNICAL ASSISTANCE</dc:subject>\r\n" + 
    		"<dc:subject>TERTIARY EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>TRANSFER METHOD</dc:subject>\r\n" + 
    		"<dc:subject>TRANSFER MONEY</dc:subject>\r\n" + 
    		"<dc:subject>TRANSFER OF FUNDS</dc:subject>\r\n" + 
    		"<dc:subject>TRANSPORTATION</dc:subject>\r\n" + 
    		"<dc:subject>UNEMPLOYMENT</dc:subject>\r\n" + 
    		"<dc:subject>UNSKILLED LABOR</dc:subject>\r\n" + 
    		"<dc:subject>URBAN AREAS</dc:subject>\r\n" + 
    		"<dc:subject>USING MONEY TRANSFER</dc:subject>\r\n" + 
    		"<dc:subject>WIRE TRANSFERS</dc:subject>\r\n" + 
    		"<dc:subject>WORKER REMITTANCES</dc:subject>\r\n" + 
    		"<dc:subject>WORLD POPULATION</dc:subject>\r\n" + 
    		"<dc:description>In chapter one, this report maps the\r\n" + 
    		"            patterns and characteristics of migration flows from India;\r\n" + 
    		"            in chapter two, it provides a detailed discussion of\r\n" + 
    		"            remittance flows to India in terms of their importance,\r\n" + 
    		"            sources, uses, trends, costs, and links to financial access.\r\n" + 
    		"            In chapter three, the report describes the remittance market\r\n" + 
    		"            (the players, the regulatory framework, as well as the\r\n" + 
    		"            existing operational schemes), setting the stage for chapter\r\n" + 
    		"            four, which presents a diagnostic of the remittance market\r\n" + 
    		"            based on the General Principles for International Remittance\r\n" + 
    		"            Services (GPs). The diagnostic covers the legal and\r\n" + 
    		"            regulatory framework, payment system infrastructure, market\r\n" + 
    		"            transparency and level of consumer protection, market\r\n" + 
    		"            structure, level of competition among remittance service\r\n" + 
    		"            providers, as well as market governance. It analyzes the\r\n" + 
    		"            existing situation in India and provides detailed\r\n" + 
    		"            recommendations (including lessons learned from\r\n" + 
    		"            international best practices) that are aimed at increasing\r\n" + 
    		"            competition in the remittance industry, providing broader\r\n" + 
    		"            access to payment system infrastructure, enhancing\r\n" + 
    		"            transparency, and ensuring a sound and predictable legal and\r\n" + 
    		"            regulatory framework. Several of the actions could set a\r\n" + 
    		"            basis for leveraging remittances to achieve other important\r\n" + 
    		"            public policy goals such as broadening financial access,\r\n" + 
    		"            expanding financial inclusion, and both strengthening and\r\n" + 
    		"            deepening the financial sector. The report was prepared\r\n" + 
    		"            through (a) background research (data research and mining,\r\n" + 
    		"            literature review, collection of relevant material and\r\n" + 
    		"            information, and background research), (b) a field visit in\r\n" + 
    		"            2009 (a team of experts visited India and conducted\r\n" + 
    		"            interviews and focus groups with all relevant stakeholders\r\n" + 
    		"            and major institutions active in the remittance market), and\r\n" + 
    		"            (c) surveys of both the authorities and the market players.</dc:description>\r\n" + 
    		"<dc:date>2013-05-06T16:54:55Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06T16:54:55Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10986/2228</dc:identifier>\r\n" + 
    		"<dc:relation>Directions in Development ; finance</dc:relation>\r\n" + 
    		"<dc:rights>CC BY 3.0 Unported</dc:rights>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/</dc:rights>\r\n" + 
    		"<dc:publisher>World Bank</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/17</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:16:46Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Refractive indices (500-3500 cm-) and emissivity (600-3350 cm-1) of pure water and seawater</dc:title>\r\n" + 
    		"<dc:contributor>nerc</dc:contributor>\r\n" + 
    		"<dc:subject>water; seawater; refractive index; emissivity; infrared; ATSR; AATSR</dc:subject>\r\n" + 
    		"<dc:description>Tables of infrared refractive indices have been compiled and calculations made of emissivity for pure water and seawater (35 PSU), suitable for radiative transfer simulation of observations of thermal imagers, such as the series of Along Track Scanning Radiometers.  The refractive indices are tabulated as a function of wave number (500–3500 cm-1) and temperature (274, 287, and 300 K).  The emissivities are tabulated as a function of wave number (600–3350 cm-1), view angle (0–85°), temperature (270–310 K), and wind speed (0–25 m s-1 at 12.5 m).</dc:description>\r\n" + 
    		"<dc:date>2008-09-22T12:42:03Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:46Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:46Z</dc:date>\r\n" + 
    		"<dc:date>2008-09-22T12:42:03Z</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>Filipiak, Mark. (2008). Refractive indices (500-3500 cm-) and emissivity (600-3350 cm-1) of pure water and seawater [Dataset].</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/17</dc:identifier>\r\n" + 
    		"<dc:language>eng</dc:language>\r\n" + 
    		"<dc:relation>International Journal of Remote Sensing, Vol. X, No. X, Month 2008, xxx–xxx</dc:relation>\r\n" + 
    		"<dc:rights>No IPR</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/19</identifier>\r\n" + 
    		"                <datestamp>2013-08-25T02:12:57Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_9</setSpec>\r\n" + 
    		"                <setSpec>col_10673_10</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Modelos Educacionais em Teleducação interativa e Media Training</dc:title>\r\n" + 
    		"<dc:subject>telemedicina</dc:subject>\r\n" + 
    		"<dc:description/>\r\n" + 
    		"<dc:date>2013-08-25T02:12:57Z</dc:date>\r\n" + 
    		"<dc:date>2013-08-25T02:12:57Z</dc:date>\r\n" + 
    		"<dc:date>2013-08-25</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/19</dc:identifier>\r\n" + 
    		"<dc:relation>http://eaulas.usp.br/portal/video.action?idItem=1758</dc:relation>\r\n" + 
    		"<dc:relation>Telemedicina</dc:relation>\r\n" + 
    		"<dc:relation>MPT1445-1</dc:relation>\r\n" + 
    		"<dc:publisher>Faculdade de Medicina</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/22</identifier>\r\n" + 
    		"                <datestamp>2013-09-05T02:00:37Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>SWORD: Simple Web-service Offering Repository Deposit</dc:title>\r\n" + 
    		"<dc:creator>Allinson, Julie</dc:creator>\r\n" + 
    		"<dc:creator>François, Sebastien</dc:creator>\r\n" + 
    		"<dc:creator>Lewis, Stuart</dc:creator>\r\n" + 
    		"<dc:description>This article offers a twofold introduction to the JISC-funded SWORD Project which ran for eight months in mid-2007. Firstly it presents an overview of the methods and madness that led us to where we currently are, including a timeline of how this work moved through an informal working group to a lightweight, distributed project. Secondly, it offers an explanation of the outputs produced for the SWORD Project and their potential benefits for the repositories community. SWORD, which stands for Simple Web service Offering Repository Deposit, came into being in March 2007 but was preceded by a series of discussions and activities which have contributed much to the project, known as the 'Deposit API'. The project itself was funded under the JISC Repositories and Preservation Programme, Tools and Innovation strand, with the over-arching aim of scoping, defining, developing and testing a standard mechanism for depositing into repositories and other systems. The motivation was that there was no standard way of doing this currently and increasingly scenarios were arising that might usefully leverage such a standard.</dc:description>\r\n" + 
    		"<dc:date>2013-09-04T17:41:57Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-04T17:41:57Z</dc:date>\r\n" + 
    		"<dc:date>2008-01</dc:date>\r\n" + 
    		"<dc:date>2013-09-04T17:41:59Z</dc:date>\r\n" + 
    		"<dc:type>Journal Article</dc:type>\r\n" + 
    		"<dc:identifier>http://www.ariadne.ac.uk/issue54/allinson-et-al/</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/22</dc:identifier>\r\n" + 
    		"<dc:language>en</dc:language>\r\n" + 
    		"<dc:rights>Julie Allinson, Sebastien François, Stuart Lewis</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10986/2229</identifier>\r\n" + 
    		"                <datestamp>2013-05-06T16:54:59Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_18</setSpec>\r\n" + 
    		"                <setSpec>col_10673_19</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Knowing, When You Do Not Know : Simulating the Poverty and Distributional Impacts of an Economic Crisis</dc:title>\r\n" + 
    		"<dc:subject>ACCOUNTING</dc:subject>\r\n" + 
    		"<dc:subject>AGGREGATE EMPLOYMENT</dc:subject>\r\n" + 
    		"<dc:subject>AGGREGATE INEQUALITY</dc:subject>\r\n" + 
    		"<dc:subject>AGGREGATE OUTPUT</dc:subject>\r\n" + 
    		"<dc:subject>ANTI-POVERTY</dc:subject>\r\n" + 
    		"<dc:subject>ANTI-POVERTY PROGRAMS</dc:subject>\r\n" + 
    		"<dc:subject>AVERAGE INCOME</dc:subject>\r\n" + 
    		"<dc:subject>BENEFICIARIES</dc:subject>\r\n" + 
    		"<dc:subject>CALORIE INTAKE</dc:subject>\r\n" + 
    		"<dc:subject>CONSTANT POVERTY LINE</dc:subject>\r\n" + 
    		"<dc:subject>COUNTERFACTUAL</dc:subject>\r\n" + 
    		"<dc:subject>CRISES</dc:subject>\r\n" + 
    		"<dc:subject>DATA REQUIREMENTS</dc:subject>\r\n" + 
    		"<dc:subject>DEMOGRAPHIC CHANGES</dc:subject>\r\n" + 
    		"<dc:subject>DEVELOPED COUNTRIES</dc:subject>\r\n" + 
    		"<dc:subject>DEVELOPED WORLD</dc:subject>\r\n" + 
    		"<dc:subject>DEVELOPING COUNTRIES</dc:subject>\r\n" + 
    		"<dc:subject>DEVELOPING WORLD</dc:subject>\r\n" + 
    		"<dc:subject>DEVELOPMENT INSTITUTIONS</dc:subject>\r\n" + 
    		"<dc:subject>DISTRIBUTIONAL EFFECTS</dc:subject>\r\n" + 
    		"<dc:subject>DISTRIBUTIONAL IMPACT</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC CONDITIONS</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>ELASTICITY</dc:subject>\r\n" + 
    		"<dc:subject>EMPIRICAL EVIDENCE</dc:subject>\r\n" + 
    		"<dc:subject>EMPIRICAL WORK</dc:subject>\r\n" + 
    		"<dc:subject>EMPLOYMENT IMPACTS</dc:subject>\r\n" + 
    		"<dc:subject>EMPLOYMENT STATUS</dc:subject>\r\n" + 
    		"<dc:subject>EXTREME POVERTY</dc:subject>\r\n" + 
    		"<dc:subject>EXTREME POVERTY LINES</dc:subject>\r\n" + 
    		"<dc:subject>FARMERS</dc:subject>\r\n" + 
    		"<dc:subject>FEMALE EMPLOYMENT</dc:subject>\r\n" + 
    		"<dc:subject>FEMALE PARTICIPATION</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL CRISIS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL MARKETS</dc:subject>\r\n" + 
    		"<dc:subject>FOOD BASKET</dc:subject>\r\n" + 
    		"<dc:subject>FOOD PRICE</dc:subject>\r\n" + 
    		"<dc:subject>FOOD PRICES</dc:subject>\r\n" + 
    		"<dc:subject>FOOD REQUIREMENTS</dc:subject>\r\n" + 
    		"<dc:subject>GENERAL EQUILIBRIUM</dc:subject>\r\n" + 
    		"<dc:subject>GENERAL EQUILIBRIUM MODELS</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL MARKETS</dc:subject>\r\n" + 
    		"<dc:subject>GROWTH RATES</dc:subject>\r\n" + 
    		"<dc:subject>HISTORICAL DATA</dc:subject>\r\n" + 
    		"<dc:subject>HOUSEHOLD DATA</dc:subject>\r\n" + 
    		"<dc:subject>HOUSEHOLD HEADS</dc:subject>\r\n" + 
    		"<dc:subject>HOUSEHOLD INCOME</dc:subject>\r\n" + 
    		"<dc:subject>HOUSEHOLD INCOMES</dc:subject>\r\n" + 
    		"<dc:subject>HOUSEHOLD MEMBERS</dc:subject>\r\n" + 
    		"<dc:subject>HOUSEHOLD SURVEY</dc:subject>\r\n" + 
    		"<dc:subject>IMPACT ON POVERTY</dc:subject>\r\n" + 
    		"<dc:subject>INCOME</dc:subject>\r\n" + 
    		"<dc:subject>INCOME DISTRIBUTION</dc:subject>\r\n" + 
    		"<dc:subject>INCOME DISTRIBUTIONS</dc:subject>\r\n" + 
    		"<dc:subject>INCOME GAINS</dc:subject>\r\n" + 
    		"<dc:subject>INCOME GROUPS</dc:subject>\r\n" + 
    		"<dc:subject>INCOME GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>INCOME LEVEL</dc:subject>\r\n" + 
    		"<dc:subject>INCOME LEVELS</dc:subject>\r\n" + 
    		"<dc:subject>INCOME SCALE</dc:subject>\r\n" + 
    		"<dc:subject>INCOME SHOCK</dc:subject>\r\n" + 
    		"<dc:subject>INCOME SHOCKS</dc:subject>\r\n" + 
    		"<dc:subject>INCOME SOURCE</dc:subject>\r\n" + 
    		"<dc:subject>INCOME SOURCES</dc:subject>\r\n" + 
    		"<dc:subject>INCOMES</dc:subject>\r\n" + 
    		"<dc:subject>INDIVIDUAL COUNTRIES</dc:subject>\r\n" + 
    		"<dc:subject>INEQUALITY</dc:subject>\r\n" + 
    		"<dc:subject>INEQUALITY MEASURES</dc:subject>\r\n" + 
    		"<dc:subject>INNOVATIONS</dc:subject>\r\n" + 
    		"<dc:subject>INSURANCE</dc:subject>\r\n" + 
    		"<dc:subject>LABOR FORCE</dc:subject>\r\n" + 
    		"<dc:subject>LABOR FORCE PARTICIPATION</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MARKET</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MARKETS</dc:subject>\r\n" + 
    		"<dc:subject>MACROECONOMIC MISMANAGEMENT</dc:subject>\r\n" + 
    		"<dc:subject>MACROECONOMIC SHOCKS</dc:subject>\r\n" + 
    		"<dc:subject>MIDDLE CLASS</dc:subject>\r\n" + 
    		"<dc:subject>NATIONAL POVERTY</dc:subject>\r\n" + 
    		"<dc:subject>NEW POOR</dc:subject>\r\n" + 
    		"<dc:subject>NOMINAL WAGES</dc:subject>\r\n" + 
    		"<dc:subject>NUTRITION</dc:subject>\r\n" + 
    		"<dc:subject>OCCUPATIONS</dc:subject>\r\n" + 
    		"<dc:subject>OUTPUTS</dc:subject>\r\n" + 
    		"<dc:subject>PARTICIPATION RATES</dc:subject>\r\n" + 
    		"<dc:subject>PER CAPITA INCOME</dc:subject>\r\n" + 
    		"<dc:subject>POLICY CHANGES</dc:subject>\r\n" + 
    		"<dc:subject>POLICY DECISIONS</dc:subject>\r\n" + 
    		"<dc:subject>POLICY DESIGN</dc:subject>\r\n" + 
    		"<dc:subject>POLICY INTERVENTIONS</dc:subject>\r\n" + 
    		"<dc:subject>POLICY MEASURES</dc:subject>\r\n" + 
    		"<dc:subject>POLITICAL ECONOMY</dc:subject>\r\n" + 
    		"<dc:subject>POOR</dc:subject>\r\n" + 
    		"<dc:subject>POOR HOUSEHOLDS</dc:subject>\r\n" + 
    		"<dc:subject>POOR RURAL HOUSEHOLDS</dc:subject>\r\n" + 
    		"<dc:subject>POPULATION GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>POVERTY ESTIMATES</dc:subject>\r\n" + 
    		"<dc:subject>POVERTY GAP</dc:subject>\r\n" + 
    		"<dc:subject>POVERTY HEADCOUNT</dc:subject>\r\n" + 
    		"<dc:subject>POVERTY HEADCOUNT RATE</dc:subject>\r\n" + 
    		"<dc:subject>POVERTY IMPACT</dc:subject>\r\n" + 
    		"<dc:subject>POVERTY IMPACTS</dc:subject>\r\n" + 
    		"<dc:subject>POVERTY INCREASE</dc:subject>\r\n" + 
    		"<dc:subject>POVERTY INDICES</dc:subject>\r\n" + 
    		"<dc:subject>POVERTY LINE</dc:subject>\r\n" + 
    		"<dc:subject>POVERTY LINES</dc:subject>\r\n" + 
    		"<dc:subject>POVERTY PROGRAMS</dc:subject>\r\n" + 
    		"<dc:subject>POVERTY RATE</dc:subject>\r\n" + 
    		"<dc:subject>POVERTY RATES</dc:subject>\r\n" + 
    		"<dc:subject>POVERTY REDUCTION</dc:subject>\r\n" + 
    		"<dc:subject>PRICE CHANGES</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE TRANSFERS</dc:subject>\r\n" + 
    		"<dc:subject>PRODUCTIVITY</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC POLICIES</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC TRANSFERS</dc:subject>\r\n" + 
    		"<dc:subject>REAL OUTPUT</dc:subject>\r\n" + 
    		"<dc:subject>RELATIVE IMPORTANCE</dc:subject>\r\n" + 
    		"<dc:subject>RELATIVE PRICES</dc:subject>\r\n" + 
    		"<dc:subject>RENTS</dc:subject>\r\n" + 
    		"<dc:subject>RURAL</dc:subject>\r\n" + 
    		"<dc:subject>RURAL AREAS</dc:subject>\r\n" + 
    		"<dc:subject>RURAL HOUSEHOLD</dc:subject>\r\n" + 
    		"<dc:subject>RURAL INCOME</dc:subject>\r\n" + 
    		"<dc:subject>SAFETY</dc:subject>\r\n" + 
    		"<dc:subject>SAFETY NET</dc:subject>\r\n" + 
    		"<dc:subject>SAFETY NET PROGRAMS</dc:subject>\r\n" + 
    		"<dc:subject>SAFETY NETS</dc:subject>\r\n" + 
    		"<dc:subject>SIGNIFICANT DIFFERENCES</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL ASSISTANCE</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL BENEFITS</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL POLICY</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL PROGRAMS</dc:subject>\r\n" + 
    		"<dc:subject>SUSTAINABLE DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>TARGETING</dc:subject>\r\n" + 
    		"<dc:subject>TRANSFER PROGRAMS</dc:subject>\r\n" + 
    		"<dc:subject>UNEMPLOYED</dc:subject>\r\n" + 
    		"<dc:subject>UNEMPLOYMENT</dc:subject>\r\n" + 
    		"<dc:subject>UNEMPLOYMENT BENEFITS</dc:subject>\r\n" + 
    		"<dc:subject>UNEMPLOYMENT RATE</dc:subject>\r\n" + 
    		"<dc:subject>WAGES</dc:subject>\r\n" + 
    		"<dc:subject>YOUNG WORKERS</dc:subject>\r\n" + 
    		"<dc:description>Economists have long sought to predict\r\n" + 
    		"            how macroeconomic shocks will affect individual welfare.\r\n" + 
    		"            Macroeconomic data and forecasts are easily available when\r\n" + 
    		"            crises strike. But policy action requires not only\r\n" + 
    		"            understanding the magnitude of a macro shock, but also\r\n" + 
    		"            identifying which households or individuals are being hurt\r\n" + 
    		"            by (or benefit from) the crisis. Moreover, in many cases,\r\n" + 
    		"            impacts on the ground might be already occurring as macro\r\n" + 
    		"            developments become known, while micro level evidence is\r\n" + 
    		"            still unavailable because of paucity of data. Because of\r\n" + 
    		"            these reasons, a comprehensive real-time understanding of\r\n" + 
    		"            how the aggregate changes will translate to impacts at the\r\n" + 
    		"            micro level remains elusive. This problem is particularly\r\n" + 
    		"            acute when dealing with developing countries where household\r\n" + 
    		"            data is sporadic or out of date. This volume outlines a more\r\n" + 
    		"            comprehensive approach to the problem, showcasing a micro\r\n" + 
    		"            simulation model, developed in response to demand from World\r\n" + 
    		"            Bank staff working in countries and country governments in\r\n" + 
    		"            the wake of the global financial crisis of 2008-09. During\r\n" + 
    		"            the growing catastrophe in a few industrialized countries,\r\n" + 
    		"            there was rising concern about how the crisis would affect\r\n" + 
    		"            the developing world and how to respond to it through public\r\n" + 
    		"            policies. World Bank staff s was scrambling to help\r\n" + 
    		"            countries design such policies; this in turn required\r\n" + 
    		"            information on which groups of the population, sectors and\r\n" + 
    		"            regions the crisis would likely affect and to what extent.\r\n" + 
    		"            The volume is organized as follows. Chapter 1 summarizes the\r\n" + 
    		"            methodology underlying the micro simulation model to predict\r\n" + 
    		"            distributional impacts of the crisis, along with several\r\n" + 
    		"            case studies that highlight how the model can be used in\r\n" + 
    		"            different country contexts. Chapters 2 to 4 are written by\r\n" + 
    		"            experts external to the Bank, two of whom participated as\r\n" + 
    		"            discussants at a workshop on the micro simulation work\r\n" + 
    		"            organized in May, 2010 at the World Bank headquarters.\r\n" + 
    		"            Chapter 2 comments on the broader implications and\r\n" + 
    		"            shortcomings of applying the technique described in Chapter\r\n" + 
    		"            1 and the ability or willingness of governments to respond\r\n" + 
    		"            adequately to its results. Chapter 3 draws parallels between\r\n" + 
    		"            the United States and developing countries to discuss the\r\n" + 
    		"            lessons that can be learned for mitigating the impacts of\r\n" + 
    		"            future crises. Chapter 4 discusses how the micro simulation\r\n" + 
    		"            approach can be sharpened to make it a better tool for\r\n" + 
    		"            distributional analysis moving forward.</dc:description>\r\n" + 
    		"<dc:date>2013-05-06T16:54:58Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06T16:54:58Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10986/2229</dc:identifier>\r\n" + 
    		"<dc:relation>World Bank Study</dc:relation>\r\n" + 
    		"<dc:rights>CC BY 3.0 Unported</dc:rights>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/</dc:rights>\r\n" + 
    		"<dc:publisher>World Bank</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/19</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:16:47Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Carstairs deprivation scores by CATT2, 1981, 1991, 2001</dc:title>\r\n" + 
    		"<dc:contributor>other</dc:contributor>\r\n" + 
    		"<dc:subject>overcrowding</dc:subject>\r\n" + 
    		"<dc:description>Carstairs deprivation scores were used to measure relative deprivation differences between small areas and over time, as the scores are widely used and recognised (Carstairs and Morris 1989; Carstairs and Morris 1991).  The Consistent Areas Through Time (CATTs, Exeter, Boyle et al. 2005) were used to provide a consistent geography between the 1981, 1991 and 2001 censuses, for ease of comparison.  Specifically the CATT2 small area geography was use, giving 10,058 individual areas with an average population in 2001 of approximately 500 persons.&#13;\r\n" + 
    		"&#13;\r\n" + 
    		"Carstairs deprivation scores were unavailable for the CATT2 geography, therefore were calculated from raw census data.  This also enabled a second set of scores to be calculated without the car ownership component (called Adjusted Carstairs hereafter), as car ownership is more of a necessity in rural areas compared with urban areas and can bias deprivation scores (Christie and Fonea 2003).  Although unadjusted Carstairs scores have been used for national studies this was considered a useful opportunity to investigate how the car ownership component affected scores in rural areas.  &#13;\r\n" + 
    		"&#13;\r\n" + 
    		"The Carstairs score is constructed from four components that have been shown to measure deprivation well (Carstairs and Morris 1989):&#13;\r\n" + 
    		"1.  Overcrowding: the percentage of all persons living in private households with a density of more than one person per room.&#13;\r\n" + 
    		"2.  Male Unemployment: the percentage of economically active males seeking or waiting to start work&#13;\r\n" + 
    		"3.  Low Social Class: the percentage of all persons in private households with an economically active head with head of household in social class IV or V. &#13;\r\n" + 
    		"4.  No Car: the percentage of all persons in private households which do not own a car.</dc:description>\r\n" + 
    		"<dc:date>2009-03-16T17:38:27Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:47Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:47Z</dc:date>\r\n" + 
    		"<dc:date>2009-03-16T17:38:27Z</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>Richardson, Elizabeth A. (2009). Carstairs deprivation scores by CATT2, 1981, 1991, 2001, 1981-2001 [Dataset]. University of Edinburgh. School of GeoSciences.</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/19</dc:identifier>\r\n" + 
    		"<dc:language>eng</dc:language>\r\n" + 
    		"<dc:relation>http://hdl.handle.net/1842/1868</dc:relation>\r\n" + 
    		"<dc:rights>Copyright, Elizabeth A. Richardson. Re-use permitted with attribution. See the suggested citation to this item.</dc:rights>\r\n" + 
    		"<dc:publisher>University of Edinburgh. School of GeoSciences</dc:publisher>\r\n" + 
    		"<dc:source>UKBORDERS: Consistent Areas Through Time for Scotland (1981-2001)</dc:source>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/23</identifier>\r\n" + 
    		"                <datestamp>2013-09-05T02:00:38Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>SWORD: Simple Web-service Offering Repository Deposit</dc:title>\r\n" + 
    		"<dc:creator>Allinson, Julie</dc:creator>\r\n" + 
    		"<dc:creator>François, Sebastien</dc:creator>\r\n" + 
    		"<dc:creator>Lewis, Stuart</dc:creator>\r\n" + 
    		"<dc:description>This article offers a twofold introduction to the JISC-funded SWORD Project which ran for eight months in mid-2007. Firstly it presents an overview of the methods and madness that led us to where we currently are, including a timeline of how this work moved through an informal working group to a lightweight, distributed project. Secondly, it offers an explanation of the outputs produced for the SWORD Project and their potential benefits for the repositories community. SWORD, which stands for Simple Web service Offering Repository Deposit, came into being in March 2007 but was preceded by a series of discussions and activities which have contributed much to the project, known as the 'Deposit API'. The project itself was funded under the JISC Repositories and Preservation Programme, Tools and Innovation strand, with the over-arching aim of scoping, defining, developing and testing a standard mechanism for depositing into repositories and other systems. The motivation was that there was no standard way of doing this currently and increasingly scenarios were arising that might usefully leverage such a standard.</dc:description>\r\n" + 
    		"<dc:date>2013-09-04T20:29:27Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-04T20:29:27Z</dc:date>\r\n" + 
    		"<dc:date>2008-01</dc:date>\r\n" + 
    		"<dc:date>2013-09-04T20:29:33Z</dc:date>\r\n" + 
    		"<dc:type>Journal Article</dc:type>\r\n" + 
    		"<dc:identifier>http://www.ariadne.ac.uk/issue54/allinson-et-al/</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/23</dc:identifier>\r\n" + 
    		"<dc:language>en</dc:language>\r\n" + 
    		"<dc:rights>Julie Allinson, Sebastien François, Stuart Lewis</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10986/2230</identifier>\r\n" + 
    		"                <datestamp>2013-05-06T16:55:04Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_18</setSpec>\r\n" + 
    		"                <setSpec>col_10673_19</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Out-of-Court Debt Restructuring</dc:title>\r\n" + 
    		"<dc:subject>ABSOLUTE PRIORITY RULES</dc:subject>\r\n" + 
    		"<dc:subject>ACCURATE FINANCIAL INFORMATION</dc:subject>\r\n" + 
    		"<dc:subject>ADMINISTRATION CONTRACTS</dc:subject>\r\n" + 
    		"<dc:subject>AMOUNT OF DEBT</dc:subject>\r\n" + 
    		"<dc:subject>ANTI-MONEY LAUNDERING</dc:subject>\r\n" + 
    		"<dc:subject>ASSET SALES</dc:subject>\r\n" + 
    		"<dc:subject>ASSETS</dc:subject>\r\n" + 
    		"<dc:subject>AUTOMATIC STAY</dc:subject>\r\n" + 
    		"<dc:subject>BANK DEBT</dc:subject>\r\n" + 
    		"<dc:subject>BANK INSOLVENCY</dc:subject>\r\n" + 
    		"<dc:subject>BANK INSOLVENCY INITIATIVE</dc:subject>\r\n" + 
    		"<dc:subject>BANKRUPTCY</dc:subject>\r\n" + 
    		"<dc:subject>BANKRUPTCY LAWS</dc:subject>\r\n" + 
    		"<dc:subject>BANKRUPTCY PROCEEDINGS</dc:subject>\r\n" + 
    		"<dc:subject>BANKRUPTCY SYSTEMS</dc:subject>\r\n" + 
    		"<dc:subject>BONDHOLDER</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESS PLAN</dc:subject>\r\n" + 
    		"<dc:subject>CAPITAL INJECTIONS</dc:subject>\r\n" + 
    		"<dc:subject>CODE OF CONDUCT</dc:subject>\r\n" + 
    		"<dc:subject>COLLATERAL</dc:subject>\r\n" + 
    		"<dc:subject>COLLECTION EFFORTS</dc:subject>\r\n" + 
    		"<dc:subject>COLLECTIVE ACTION</dc:subject>\r\n" + 
    		"<dc:subject>COLLECTIVE ACTION PROBLEMS</dc:subject>\r\n" + 
    		"<dc:subject>COMMERCIAL LAW</dc:subject>\r\n" + 
    		"<dc:subject>COMPANY LAW</dc:subject>\r\n" + 
    		"<dc:subject>COMPOSITION AGREEMENT</dc:subject>\r\n" + 
    		"<dc:subject>CONCESSION</dc:subject>\r\n" + 
    		"<dc:subject>CONFLICTS OF INTEREST</dc:subject>\r\n" + 
    		"<dc:subject>CONTRACTUAL ARRANGEMENTS</dc:subject>\r\n" + 
    		"<dc:subject>CONTRACTUAL PROVISIONS</dc:subject>\r\n" + 
    		"<dc:subject>CONTRACTUAL RELATIONSHIPS</dc:subject>\r\n" + 
    		"<dc:subject>CONVERSION OF DEBT</dc:subject>\r\n" + 
    		"<dc:subject>CONVERSION OF DEBT INTO EQUITY</dc:subject>\r\n" + 
    		"<dc:subject>CORPORATE DEBT</dc:subject>\r\n" + 
    		"<dc:subject>CORPORATE DISTRESS</dc:subject>\r\n" + 
    		"<dc:subject>CORPORATE FINANCIAL DIFFICULTY</dc:subject>\r\n" + 
    		"<dc:subject>CORPORATE FINANCIAL DISTRESS</dc:subject>\r\n" + 
    		"<dc:subject>CORPORATE GOVERNANCE</dc:subject>\r\n" + 
    		"<dc:subject>CORPORATE LAW</dc:subject>\r\n" + 
    		"<dc:subject>CORPORATE WORKOUTS</dc:subject>\r\n" + 
    		"<dc:subject>COURT DEBT</dc:subject>\r\n" + 
    		"<dc:subject>COURT INTERVENTION</dc:subject>\r\n" + 
    		"<dc:subject>COURT RESTRUCTURING</dc:subject>\r\n" + 
    		"<dc:subject>CREDITOR</dc:subject>\r\n" + 
    		"<dc:subject>CREDITOR ACTIONS</dc:subject>\r\n" + 
    		"<dc:subject>CREDITOR RIGHTS</dc:subject>\r\n" + 
    		"<dc:subject>CREDITOR RIGHTS SYSTEMS</dc:subject>\r\n" + 
    		"<dc:subject>CREDITORS</dc:subject>\r\n" + 
    		"<dc:subject>CREDITS</dc:subject>\r\n" + 
    		"<dc:subject>DEBT CONVERSIONS</dc:subject>\r\n" + 
    		"<dc:subject>DEBT INSTRUMENTS</dc:subject>\r\n" + 
    		"<dc:subject>DEBT PROBLEMS</dc:subject>\r\n" + 
    		"<dc:subject>DEBT RESCHEDULING</dc:subject>\r\n" + 
    		"<dc:subject>DEBT RESTRUCTURING</dc:subject>\r\n" + 
    		"<dc:subject>DEBT RESTRUCTURING MECHANISMS</dc:subject>\r\n" + 
    		"<dc:subject>DEBT SALES</dc:subject>\r\n" + 
    		"<dc:subject>DEBT SECURITIES</dc:subject>\r\n" + 
    		"<dc:subject>DEBTOR</dc:subject>\r\n" + 
    		"<dc:subject>DEBTOR COMPANY</dc:subject>\r\n" + 
    		"<dc:subject>DEBTOR ENTERPRISES</dc:subject>\r\n" + 
    		"<dc:subject>DEBTOR'S COMPANY</dc:subject>\r\n" + 
    		"<dc:subject>DEBTORS</dc:subject>\r\n" + 
    		"<dc:subject>DEBTS</dc:subject>\r\n" + 
    		"<dc:subject>DISTRESSED ASSETS</dc:subject>\r\n" + 
    		"<dc:subject>DISTRESSED COMPANIES</dc:subject>\r\n" + 
    		"<dc:subject>DISTRESSED COMPANY</dc:subject>\r\n" + 
    		"<dc:subject>DISTRESSED ENTERPRISE</dc:subject>\r\n" + 
    		"<dc:subject>DUE DILIGENCE</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC SITUATION</dc:subject>\r\n" + 
    		"<dc:subject>EFFECTIVE INSOLVENCY</dc:subject>\r\n" + 
    		"<dc:subject>EFFECTIVE INSOLVENCY SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>EFFICIENT INSOLVENCY</dc:subject>\r\n" + 
    		"<dc:subject>ENFORCEMENT MECHANISMS</dc:subject>\r\n" + 
    		"<dc:subject>ENTERPRISE REORGANIZATIONS</dc:subject>\r\n" + 
    		"<dc:subject>ENTERPRISE RESTRUCTURING</dc:subject>\r\n" + 
    		"<dc:subject>EQUITY CONVERSIONS</dc:subject>\r\n" + 
    		"<dc:subject>EQUITY EXCHANGES</dc:subject>\r\n" + 
    		"<dc:subject>EXECUTORY CONTRACTS</dc:subject>\r\n" + 
    		"<dc:subject>EXISTING CREDITORS</dc:subject>\r\n" + 
    		"<dc:subject>EXISTING DEBT</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL CREDITOR</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL CREDITORS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL CRISIS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL DIFFICULTIES</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL DIFFICULTY</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL INSTITUTION</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL INSTITUTIONS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL POSITION</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL PROBLEM</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL PROBLEMS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL SITUATION</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL STABILITY</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIALLY DISTRESSED ENTERPRISES</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN COURTS</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN PROCEEDINGS</dc:subject>\r\n" + 
    		"<dc:subject>FORMAL BANKRUPTCY</dc:subject>\r\n" + 
    		"<dc:subject>FORMAL INSOLVENCY</dc:subject>\r\n" + 
    		"<dc:subject>FORMAL INSOLVENCY PROCEEDING</dc:subject>\r\n" + 
    		"<dc:subject>FORMAL INSOLVENCY PROCEEDINGS</dc:subject>\r\n" + 
    		"<dc:subject>FORMAL LIQUIDATION</dc:subject>\r\n" + 
    		"<dc:subject>FORMAL REORGANIZATION</dc:subject>\r\n" + 
    		"<dc:subject>FRAUD</dc:subject>\r\n" + 
    		"<dc:subject>FRAUDULENT BEHAVIOR</dc:subject>\r\n" + 
    		"<dc:subject>GENERAL COUNSEL</dc:subject>\r\n" + 
    		"<dc:subject>GOOD FAITH</dc:subject>\r\n" + 
    		"<dc:subject>GROUP OF CREDITORS</dc:subject>\r\n" + 
    		"<dc:subject>ILLIQUIDITY</dc:subject>\r\n" + 
    		"<dc:subject>IMMEDIATE INSOLVENCY</dc:subject>\r\n" + 
    		"<dc:subject>INDEBTEDNESS</dc:subject>\r\n" + 
    		"<dc:subject>INFORMAL ARRANGEMENTS</dc:subject>\r\n" + 
    		"<dc:subject>INFORMAL PROCEDURES</dc:subject>\r\n" + 
    		"<dc:subject>INFORMAL WORKOUT</dc:subject>\r\n" + 
    		"<dc:subject>INFORMAL WORKOUT PROCEDURE</dc:subject>\r\n" + 
    		"<dc:subject>INFORMAL WORKOUTS</dc:subject>\r\n" + 
    		"<dc:subject>INSOLVENCY ASSESSMENTS</dc:subject>\r\n" + 
    		"<dc:subject>INSOLVENCY LAW</dc:subject>\r\n" + 
    		"<dc:subject>INSOLVENCY LAW REFORM</dc:subject>\r\n" + 
    		"<dc:subject>INSOLVENCY LAWS</dc:subject>\r\n" + 
    		"<dc:subject>INSOLVENCY LEGISLATION</dc:subject>\r\n" + 
    		"<dc:subject>INSOLVENCY POLICIES</dc:subject>\r\n" + 
    		"<dc:subject>INSOLVENCY POLICY</dc:subject>\r\n" + 
    		"<dc:subject>INSOLVENCY PROCEDURE</dc:subject>\r\n" + 
    		"<dc:subject>INSOLVENCY PROCEDURES</dc:subject>\r\n" + 
    		"<dc:subject>INSOLVENCY PROCESS</dc:subject>\r\n" + 
    		"<dc:subject>INSOLVENCY PROCESSES</dc:subject>\r\n" + 
    		"<dc:subject>INSOLVENCY SET</dc:subject>\r\n" + 
    		"<dc:subject>INSOLVENCY SYSTEMS</dc:subject>\r\n" + 
    		"<dc:subject>INSURANCE</dc:subject>\r\n" + 
    		"<dc:subject>INTEREST PAYMENTS</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL ACCOUNTING</dc:subject>\r\n" + 
    		"<dc:subject>JUDGES</dc:subject>\r\n" + 
    		"<dc:subject>JUDGMENT</dc:subject>\r\n" + 
    		"<dc:subject>JUDICIAL MECHANISMS</dc:subject>\r\n" + 
    		"<dc:subject>JUDICIAL SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>JUDICIARY</dc:subject>\r\n" + 
    		"<dc:subject>JURISDICTION</dc:subject>\r\n" + 
    		"<dc:subject>JURISDICTIONS</dc:subject>\r\n" + 
    		"<dc:subject>LARGE NUMBER OF CREDITORS</dc:subject>\r\n" + 
    		"<dc:subject>LEGAL FRAMEWORK</dc:subject>\r\n" + 
    		"<dc:subject>LEGAL SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>LEGAL SYSTEMS</dc:subject>\r\n" + 
    		"<dc:subject>LEGISLATIVE FRAMEWORK</dc:subject>\r\n" + 
    		"<dc:subject>LENDER</dc:subject>\r\n" + 
    		"<dc:subject>LIABILITY</dc:subject>\r\n" + 
    		"<dc:subject>LIQUIDATION</dc:subject>\r\n" + 
    		"<dc:subject>LIQUIDATION PROCEDURES</dc:subject>\r\n" + 
    		"<dc:subject>LIQUIDATION PROCEEDINGS</dc:subject>\r\n" + 
    		"<dc:subject>LIQUIDITY CRISIS</dc:subject>\r\n" + 
    		"<dc:subject>MAJORITY OF CREDITORS</dc:subject>\r\n" + 
    		"<dc:subject>MARKET VALUE</dc:subject>\r\n" + 
    		"<dc:subject>MINORITY CREDITORS</dc:subject>\r\n" + 
    		"<dc:subject>MORATORIUM ON CREDITOR</dc:subject>\r\n" + 
    		"<dc:subject>MULTIPLE CLAIMANTS</dc:subject>\r\n" + 
    		"<dc:subject>NEGOTIATION</dc:subject>\r\n" + 
    		"<dc:subject>NEGOTIATIONS</dc:subject>\r\n" + 
    		"<dc:subject>NONVIABLE COMPANIES</dc:subject>\r\n" + 
    		"<dc:subject>NUMBER OF BANKS</dc:subject>\r\n" + 
    		"<dc:subject>NUMBER OF CREDITORS</dc:subject>\r\n" + 
    		"<dc:subject>PAYMENT TERMS</dc:subject>\r\n" + 
    		"<dc:subject>PAYMENTS TO CREDITORS</dc:subject>\r\n" + 
    		"<dc:subject>POTENTIAL LIABILITIES</dc:subject>\r\n" + 
    		"<dc:subject>PROCEDURAL ISSUES</dc:subject>\r\n" + 
    		"<dc:subject>PROCUREMENT</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC ADMINISTRATION</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC DEBT</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC INFORMATION</dc:subject>\r\n" + 
    		"<dc:subject>RECAPITALIZATION</dc:subject>\r\n" + 
    		"<dc:subject>REGULATORY FRAMEWORK</dc:subject>\r\n" + 
    		"<dc:subject>REGULATORY SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>REMEDIES</dc:subject>\r\n" + 
    		"<dc:subject>REORGANIZATION PLAN</dc:subject>\r\n" + 
    		"<dc:subject>REORGANIZATION PROCEDURE</dc:subject>\r\n" + 
    		"<dc:subject>REORGANIZATION PROCEEDINGS</dc:subject>\r\n" + 
    		"<dc:subject>REORGANIZATION PROCESS</dc:subject>\r\n" + 
    		"<dc:subject>REPAYMENT</dc:subject>\r\n" + 
    		"<dc:subject>RESTRUCTURING PLAN</dc:subject>\r\n" + 
    		"<dc:subject>RESTRUCTURING PROCESSES</dc:subject>\r\n" + 
    		"<dc:subject>RESTRUCTURING TECHNIQUES</dc:subject>\r\n" + 
    		"<dc:subject>RIGHTS OF CREDITORS</dc:subject>\r\n" + 
    		"<dc:subject>SINGLE CREDITOR</dc:subject>\r\n" + 
    		"<dc:subject>STATUTORY PROVISIONS</dc:subject>\r\n" + 
    		"<dc:subject>STAY OF CREDITOR ACTIONS</dc:subject>\r\n" + 
    		"<dc:subject>STAY ON CREDITOR</dc:subject>\r\n" + 
    		"<dc:subject>STAY ON CREDITORS</dc:subject>\r\n" + 
    		"<dc:subject>SUBJECT TO INSOLVENCY</dc:subject>\r\n" + 
    		"<dc:subject>SYSTEMIC CRISES</dc:subject>\r\n" + 
    		"<dc:subject>SYSTEMIC CRISIS</dc:subject>\r\n" + 
    		"<dc:subject>TRADE CREDITORS</dc:subject>\r\n" + 
    		"<dc:subject>TRADE DEBT</dc:subject>\r\n" + 
    		"<dc:subject>TRADE DEBTS</dc:subject>\r\n" + 
    		"<dc:subject>TRUSTEE</dc:subject>\r\n" + 
    		"<dc:subject>TRUSTEE INDENTURE</dc:subject>\r\n" + 
    		"<dc:subject>VALUATION OF ASSETS</dc:subject>\r\n" + 
    		"<dc:subject>VIABLE BUSINESS</dc:subject>\r\n" + 
    		"<dc:subject>VIABLE BUSINESSES</dc:subject>\r\n" + 
    		"<dc:subject>WAGES</dc:subject>\r\n" + 
    		"<dc:subject>WORKOUT AGREEMENT</dc:subject>\r\n" + 
    		"<dc:subject>WORKOUTS</dc:subject>\r\n" + 
    		"<dc:description>This study provides a conceptual\r\n" + 
    		"            framework for the analysis of the questions of out-of-court\r\n" + 
    		"            debt restructuring from a policy-oriented perspective. The\r\n" + 
    		"            starting point of the analysis is given by the World Bank\r\n" + 
    		"            principles for effective insolvency and creditor rights\r\n" + 
    		"            systems. The study offers an overview of out-of-court\r\n" + 
    		"            restructuring, which is not seen as fundamentally opposed to\r\n" + 
    		"            formal insolvency procedures. Actually, the study\r\n" + 
    		"            contemplates different restructuring techniques as forming a\r\n" + 
    		"            continuum to the treatment of financial difficulties. The\r\n" + 
    		"            study discusses the advantages and disadvantages of all the\r\n" + 
    		"            debt restructuring techniques, and concludes, in this\r\n" + 
    		"            regard, that a legal system may contain a number of options\r\n" + 
    		"            a menu that can cover different sets of circumstances. In\r\n" + 
    		"            the end, the law may offer a toolbox with very different\r\n" + 
    		"            instruments that the parties may use depending on the\r\n" + 
    		"            specific facts of the case. The study also provides a\r\n" + 
    		"            checklist that can be used to examine the features of a\r\n" + 
    		"            legal system that bear a direct influence on debt\r\n" + 
    		"            restructuring activities.</dc:description>\r\n" + 
    		"<dc:date>2013-05-06T16:55:04Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06T16:55:04Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10986/2230</dc:identifier>\r\n" + 
    		"<dc:relation>World Bank Study</dc:relation>\r\n" + 
    		"<dc:rights>CC BY 3.0 Unported</dc:rights>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/</dc:rights>\r\n" + 
    		"<dc:publisher>World Bank</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/24</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:16:48Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Scottish Election Results 1997 - 2009</dc:title>\r\n" + 
    		"<dc:contributor>Other</dc:contributor>\r\n" + 
    		"<dc:subject>elections, election results</dc:subject>\r\n" + 
    		"<dc:description>The dataset contains all national, European and by-election results for Holyrood and Westminter elections in Scotland since 1997.  The results from each election are available in both CSV or XML format.  For national Holyrood elections the data includes both the constituency and list counts.  For national results, in addition to votes cast for each candidate or party, the XML format contains, where available, the name of each party leader.  The results were compiled from the following sources:&#13;\r\n" + 
    		"&#13;\r\n" + 
    		" * The British Broadcasting Corporation - http://bbc.co.uk&#13;\r\n" + 
    		"&#13;\r\n" + 
    		" * Scottish Politics - the almanac of Scottish elections and politics - http://www.alba.org.uk/&#13;\r\n" + 
    		"&#13;\r\n" + 
    		" * David Boothroyd's United Kingdom Election Results site - http://www.election.demon.co.uk/ (this continues the definitive work of Professor F. W. S. Craig, who compiled and analysed all British parliamentary election results from 1832 - 1979).</dc:description>\r\n" + 
    		"<dc:date>2009-10-06T16:18:06Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:48Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:48Z</dc:date>\r\n" + 
    		"<dc:date>2009-10-06T16:18:06Z</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>Hamilton, George. (2009). Scottish Election Results 1997 - 2009, 1997-2009 [Dataset]. University of Edinburgh. EDINA and University Data Library.</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/24</dc:identifier>\r\n" + 
    		"<dc:language>eng</dc:language>\r\n" + 
    		"<dc:rights>This data was freely obtained from http://bbc.co.uk, http://www.alba.org.uk/ , and http://www.election.demon.co.uk/</dc:rights>\r\n" + 
    		"<dc:publisher>University of Edinburgh. EDINA and University Data Library</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:2318/66</identifier>\r\n" + 
    		"                <datestamp>2013-07-09T22:59:18Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_16</setSpec>\r\n" + 
    		"                <setSpec>col_10673_21</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>On Fragility of Bubbles in Equilibrium Asset Pricing Models of Lucas-Type</dc:title>\r\n" + 
    		"<dc:subject>Optimization Techniques</dc:subject>\r\n" + 
    		"<dc:subject>Programming models</dc:subject>\r\n" + 
    		"<dc:subject>Dynamic Analysis</dc:subject>\r\n" + 
    		"<dc:subject>Existence and Stability Conditions of Equilibrium</dc:subject>\r\n" + 
    		"<dc:subject>Exchange and Production Economies</dc:subject>\r\n" + 
    		"<dc:subject>Asset Pricing</dc:subject>\r\n" + 
    		"<dc:description>In this paper we study the existence of bubbles for pricing equilibria in a pure exchange economy à la Lucas, with infinitely lived homogeneous agents. The model is analyzed under fairly general assumptions: no restrictions either on the stochastic process governing dividends' distribution or on the utilities (possibly unbounded) are required. We prove that the pricing equilibrium is unique as long as the agents exhibit uniformly bounded relative risk aversion. A generic uniqueness result is also given regardless of agent's preferences. A few “pathological” examples of economies exhibiting pricing equilibria with bubble components are constructed. Finally, a possible relationship between our approach and the theory developed by Santos and Woodford on ambiguous bubbles is investigated. The whole discussion sheds more insight on the common belief that bubbles are a marginal phenomenon in such models. Journal of Economic Literature Classification Numbers: C61, C62, D51, G12.</dc:description>\r\n" + 
    		"<dc:description>ICER - International Centre for Economic Research</dc:description>\r\n" + 
    		"<dc:date>2013-07-09T22:59:18Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09T22:59:18Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09</dc:date>\r\n" + 
    		"<dc:type>Articolo</dc:type>\r\n" + 
    		"<dc:type>Preprint</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/2318/66</dc:identifier>\r\n" + 
    		"<dc:relation>Applied Mathematics Working Papers Series</dc:relation>\r\n" + 
    		"<dc:relation>5/2001</dc:relation>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10459.3/52</identifier>\r\n" + 
    		"                <datestamp>2013-07-29T11:25:04Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_10</setSpec>\r\n" + 
    		"                <setSpec>col_10673_13</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Letteratura ebraica</dc:title>\r\n" + 
    		"<dc:subject>Literatura hebrea -- Història i crítica</dc:subject>\r\n" + 
    		"<dc:description>Manuali Hoepli. 2 v. ; 16 cm</dc:description>\r\n" + 
    		"<dc:date>2012-07-01T10:08:27Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-29T11:25:04Z</dc:date>\r\n" + 
    		"<dc:date>2012-07-01T10:08:27Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-29T11:25:04Z</dc:date>\r\n" + 
    		"<dc:date>2012</dc:date>\r\n" + 
    		"<dc:date>1888</dc:date>\r\n" + 
    		"<dc:type>Text</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10459.3/52</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10459</dc:identifier>\r\n" + 
    		"<dc:language>it</dc:language>\r\n" + 
    		"<dc:relation>http://cataleg.udl.cat/record=b1068859~S11*cat</dc:relation>\r\n" + 
    		"<dc:rights>Còpia permesa amb finalitat d'estudi o recerca, citant l'autor i la font: \"Fons Gili i Gaya / Universitat de Lleida\". Per a qualsevol altre ús cal demanar autorització.</dc:rights>\r\n" + 
    		"<dc:publisher>Universitat de Lleida</dc:publisher>\r\n" + 
    		"<dc:source>Publicació original: Milano : Ulrico Hoepli, 1888</dc:source>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10986/2231</identifier>\r\n" + 
    		"                <datestamp>2013-05-06T16:55:08Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_18</setSpec>\r\n" + 
    		"                <setSpec>col_10673_19</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>A Guide to Assessing Needs : Essential Tools for Collecting Information, Making Decisions, and Achieving Development Results</dc:title>\r\n" + 
    		"<dc:subject>AGRICULTURAL DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>AGRICULTURAL LABORATORIES</dc:subject>\r\n" + 
    		"<dc:subject>AGRICULTURAL PRACTICES</dc:subject>\r\n" + 
    		"<dc:subject>AGRICULTURAL PRODUCTION</dc:subject>\r\n" + 
    		"<dc:subject>AGRICULTURAL PRODUCTS</dc:subject>\r\n" + 
    		"<dc:subject>AGRICULTURE</dc:subject>\r\n" + 
    		"<dc:subject>ANALYTICAL LABORATORIES</dc:subject>\r\n" + 
    		"<dc:subject>ANIMAL DISEASES</dc:subject>\r\n" + 
    		"<dc:subject>ANIMAL HEALTH</dc:subject>\r\n" + 
    		"<dc:subject>ANIMAL PRODUCTS</dc:subject>\r\n" + 
    		"<dc:subject>AQUACULTURE</dc:subject>\r\n" + 
    		"<dc:subject>BANK LENDING</dc:subject>\r\n" + 
    		"<dc:subject>BOMBS</dc:subject>\r\n" + 
    		"<dc:subject>CAPACITY BUILDING</dc:subject>\r\n" + 
    		"<dc:subject>CASH MANAGEMENT</dc:subject>\r\n" + 
    		"<dc:subject>CHEMISTRY</dc:subject>\r\n" + 
    		"<dc:subject>CIVIL SERVICE STATUS</dc:subject>\r\n" + 
    		"<dc:subject>CONFLICT OF INTEREST</dc:subject>\r\n" + 
    		"<dc:subject>CONFLICTS OF INTEREST</dc:subject>\r\n" + 
    		"<dc:subject>CONTINGENCY PLANNING</dc:subject>\r\n" + 
    		"<dc:subject>COST EFFECTIVENESS</dc:subject>\r\n" + 
    		"<dc:subject>CROPS</dc:subject>\r\n" + 
    		"<dc:subject>CROSS CONTAMINATION</dc:subject>\r\n" + 
    		"<dc:subject>DAIRY</dc:subject>\r\n" + 
    		"<dc:subject>DAIRY PRODUCTS</dc:subject>\r\n" + 
    		"<dc:subject>DECISION MAKING</dc:subject>\r\n" + 
    		"<dc:subject>DEVELOPING COUNTRIES</dc:subject>\r\n" + 
    		"<dc:subject>DEVELOPING COUNTRY</dc:subject>\r\n" + 
    		"<dc:subject>DEVELOPMENT PROJECTS</dc:subject>\r\n" + 
    		"<dc:subject>DOMESTIC MARKET</dc:subject>\r\n" + 
    		"<dc:subject>DOMESTIC MARKETS</dc:subject>\r\n" + 
    		"<dc:subject>DRIED FRUIT</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC RISKS</dc:subject>\r\n" + 
    		"<dc:subject>EGG</dc:subject>\r\n" + 
    		"<dc:subject>EMERGENCY PLANS</dc:subject>\r\n" + 
    		"<dc:subject>EMERGENCY RESPONSE</dc:subject>\r\n" + 
    		"<dc:subject>ENVIRONMENTAL HEALTH</dc:subject>\r\n" + 
    		"<dc:subject>EPIDEMIOLOGY</dc:subject>\r\n" + 
    		"<dc:subject>EQUIPMENT</dc:subject>\r\n" + 
    		"<dc:subject>EQUIPMENTS</dc:subject>\r\n" + 
    		"<dc:subject>EXPENDITURES</dc:subject>\r\n" + 
    		"<dc:subject>EXPORT GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>EXPORT PERFORMANCE</dc:subject>\r\n" + 
    		"<dc:subject>EXPORTER</dc:subject>\r\n" + 
    		"<dc:subject>EXPORTERS</dc:subject>\r\n" + 
    		"<dc:subject>EXTENSION</dc:subject>\r\n" + 
    		"<dc:subject>EXTENSION SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>FARMERS</dc:subject>\r\n" + 
    		"<dc:subject>FARMS</dc:subject>\r\n" + 
    		"<dc:subject>FEE INCOME</dc:subject>\r\n" + 
    		"<dc:subject>FEED</dc:subject>\r\n" + 
    		"<dc:subject>FINANCES</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL MANAGEMENT</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL SUSTAINABILITY</dc:subject>\r\n" + 
    		"<dc:subject>FISCAL AUTONOMY</dc:subject>\r\n" + 
    		"<dc:subject>FISH</dc:subject>\r\n" + 
    		"<dc:subject>FISH PROCESSING</dc:subject>\r\n" + 
    		"<dc:subject>FISHERIES</dc:subject>\r\n" + 
    		"<dc:subject>FIXED ASSETS</dc:subject>\r\n" + 
    		"<dc:subject>FOOD CONTROL</dc:subject>\r\n" + 
    		"<dc:subject>FOOD HANDLING</dc:subject>\r\n" + 
    		"<dc:subject>FOOD INDUSTRIES</dc:subject>\r\n" + 
    		"<dc:subject>FOOD INDUSTRY</dc:subject>\r\n" + 
    		"<dc:subject>FOOD PROCESSING</dc:subject>\r\n" + 
    		"<dc:subject>FOOD PRODUCTION</dc:subject>\r\n" + 
    		"<dc:subject>FOOD PRODUCTS</dc:subject>\r\n" + 
    		"<dc:subject>FOOD REGULATIONS</dc:subject>\r\n" + 
    		"<dc:subject>FOOD SAFETY</dc:subject>\r\n" + 
    		"<dc:subject>FOOD TESTING</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN MARKET</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN MARKETS</dc:subject>\r\n" + 
    		"<dc:subject>FRUIT PRODUCTS</dc:subject>\r\n" + 
    		"<dc:subject>FUNDING MECHANISMS</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT FUNDING</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT POLICIES</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT REGULATIONS</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT SUPPORT</dc:subject>\r\n" + 
    		"<dc:subject>GRAPES</dc:subject>\r\n" + 
    		"<dc:subject>GROUNDNUTS</dc:subject>\r\n" + 
    		"<dc:subject>HAZARD</dc:subject>\r\n" + 
    		"<dc:subject>HAZARDS</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH CARE</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH MANAGEMENT</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH REGULATIONS</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>HONEY</dc:subject>\r\n" + 
    		"<dc:subject>HOUSING</dc:subject>\r\n" + 
    		"<dc:subject>HUMAN CAPITAL</dc:subject>\r\n" + 
    		"<dc:subject>HUMAN CAPITAL DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>HUMAN RESOURCES</dc:subject>\r\n" + 
    		"<dc:subject>HYGIENE</dc:subject>\r\n" + 
    		"<dc:subject>INCOMES</dc:subject>\r\n" + 
    		"<dc:subject>INFECTIOUS DISEASES</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL BANK</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL COOPERATION</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL FINANCIAL INSTITUTION</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL FINANCIAL INSTITUTIONS</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL ORGANIZATIONS</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL STANDARD</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL STANDARDS</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL SUPPORT</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL TRADE</dc:subject>\r\n" + 
    		"<dc:subject>INVESTING</dc:subject>\r\n" + 
    		"<dc:subject>INVESTMENT ALTERNATIVES</dc:subject>\r\n" + 
    		"<dc:subject>INVESTMENT CLIMATE</dc:subject>\r\n" + 
    		"<dc:subject>INVESTMENT DECISIONS</dc:subject>\r\n" + 
    		"<dc:subject>INVESTMENT PROJECTS</dc:subject>\r\n" + 
    		"<dc:subject>ISOLATION</dc:subject>\r\n" + 
    		"<dc:subject>ISSUANCE</dc:subject>\r\n" + 
    		"<dc:subject>LAB</dc:subject>\r\n" + 
    		"<dc:subject>LABORATORIES</dc:subject>\r\n" + 
    		"<dc:subject>LABORATORY</dc:subject>\r\n" + 
    		"<dc:subject>LABORATORY INFRASTRUCTURE</dc:subject>\r\n" + 
    		"<dc:subject>LABS</dc:subject>\r\n" + 
    		"<dc:subject>LAMB</dc:subject>\r\n" + 
    		"<dc:subject>LAWS</dc:subject>\r\n" + 
    		"<dc:subject>LEGAL SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>LEVEL PLAYING FIELD</dc:subject>\r\n" + 
    		"<dc:subject>LIVESTOCK</dc:subject>\r\n" + 
    		"<dc:subject>LIVESTOCK PRODUCTS</dc:subject>\r\n" + 
    		"<dc:subject>LOAN</dc:subject>\r\n" + 
    		"<dc:subject>MARKET ACCESS</dc:subject>\r\n" + 
    		"<dc:subject>MARKET COUNTRY</dc:subject>\r\n" + 
    		"<dc:subject>MARKETING</dc:subject>\r\n" + 
    		"<dc:subject>MEAT</dc:subject>\r\n" + 
    		"<dc:subject>MINISTRIES OF AGRICULTURE</dc:subject>\r\n" + 
    		"<dc:subject>NATIONAL LABORATORY</dc:subject>\r\n" + 
    		"<dc:subject>NEEDS ASSESSMENT</dc:subject>\r\n" + 
    		"<dc:subject>NUTS</dc:subject>\r\n" + 
    		"<dc:subject>OWNERSHIP STRUCTURE</dc:subject>\r\n" + 
    		"<dc:subject>PARASITES</dc:subject>\r\n" + 
    		"<dc:subject>PATHOGENS</dc:subject>\r\n" + 
    		"<dc:subject>PESTICIDE</dc:subject>\r\n" + 
    		"<dc:subject>PESTICIDES</dc:subject>\r\n" + 
    		"<dc:subject>PESTS</dc:subject>\r\n" + 
    		"<dc:subject>PLANT PRODUCTS</dc:subject>\r\n" + 
    		"<dc:subject>POLITICAL ECONOMY</dc:subject>\r\n" + 
    		"<dc:subject>POOR REPUTATION</dc:subject>\r\n" + 
    		"<dc:subject>POULTRY PRODUCTS</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE INVESTMENTS</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>PROFIT MARGIN</dc:subject>\r\n" + 
    		"<dc:subject>PROTEINS</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC BUDGET</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC BUDGETING</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC FUNDS</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC HEALTH</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>QUALITY CONTROL</dc:subject>\r\n" + 
    		"<dc:subject>REGULATION OF FOOD</dc:subject>\r\n" + 
    		"<dc:subject>REGULATORY CONSTRAINTS</dc:subject>\r\n" + 
    		"<dc:subject>REGULATORY FRAMEWORK</dc:subject>\r\n" + 
    		"<dc:subject>REGULATORY SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>RESERVE</dc:subject>\r\n" + 
    		"<dc:subject>RESERVES</dc:subject>\r\n" + 
    		"<dc:subject>RETURN</dc:subject>\r\n" + 
    		"<dc:subject>RETURNS</dc:subject>\r\n" + 
    		"<dc:subject>RISK MANAGEMENT</dc:subject>\r\n" + 
    		"<dc:subject>RURAL DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>SEAFOOD</dc:subject>\r\n" + 
    		"<dc:subject>SOILS</dc:subject>\r\n" + 
    		"<dc:subject>SPINACH</dc:subject>\r\n" + 
    		"<dc:subject>TRADING</dc:subject>\r\n" + 
    		"<dc:subject>TRANSITION COUNTRIES</dc:subject>\r\n" + 
    		"<dc:subject>TRANSITION ECONOMIES</dc:subject>\r\n" + 
    		"<dc:subject>TRANSPARENCY</dc:subject>\r\n" + 
    		"<dc:subject>TRUSTEES</dc:subject>\r\n" + 
    		"<dc:subject>TURNOVER</dc:subject>\r\n" + 
    		"<dc:subject>UNIVERSITIES</dc:subject>\r\n" + 
    		"<dc:subject>VETERINARY SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>WASTE</dc:subject>\r\n" + 
    		"<dc:subject>WASTE DISPOSAL</dc:subject>\r\n" + 
    		"<dc:subject>WORLD TRADE</dc:subject>\r\n" + 
    		"<dc:description>This book will benefit people and teams\r\n" + 
    		"            involved in planning and decision making. On the basis of\r\n" + 
    		"            their pragmatic value in guiding decisions, needs\r\n" + 
    		"            assessments are used in various professions and settings\r\n" + 
    		"            from emergency rooms to corporate boardrooms to guide\r\n" + 
    		"            decision making. Nonetheless, although needs assessments\r\n" + 
    		"            have many different applications, in this book on needs\r\n" + 
    		"            assessments as they are applied in organizations to\r\n" + 
    		"            accomplish results, as opposed to their use in personal\r\n" + 
    		"            decisions or medical triage. This book, in turn, is guide to\r\n" + 
    		"            assessing needs and then making essential decisions about\r\n" + 
    		"            what to do next. This book filled with practical strategies,\r\n" + 
    		"            tools, and guides covers both large-scale, formal needs\r\n" + 
    		"            assessments and less-formal assessments that guide daily\r\n" + 
    		"            decisions. Included in the book is a blend of rigorous\r\n" + 
    		"            methods and realistic tools that can help make informed and\r\n" + 
    		"            reasoned decisions. Together, these methods and tools offer\r\n" + 
    		"            a comprehensive, yet realistic, approach to identifying\r\n" + 
    		"            needs and selecting among alternative ways as go forward.\r\n" + 
    		"            Sections one and two offer quick, yet full, answers to many\r\n" + 
    		"            frequently asked questions regarding how to make justifiable\r\n" + 
    		"            decisions. Next, section three examines a variety of tools\r\n" + 
    		"            and techniques that can be used for both collecting\r\n" + 
    		"            information and making decisions. Appendix A then offers a\r\n" + 
    		"            number of checklists and guides for managing the systematic\r\n" + 
    		"            assessment processes that lead to quality decisions.\r\n" + 
    		"            Finally, the reference list at the end of the book is a\r\n" + 
    		"            valuable resource to research, tools, and discussions of\r\n" + 
    		"            needs assessment.</dc:description>\r\n" + 
    		"<dc:date>2013-05-06T16:55:08Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06T16:55:08Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10986/2231</dc:identifier>\r\n" + 
    		"<dc:rights>CC BY 3.0 Unported</dc:rights>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/</dc:rights>\r\n" + 
    		"<dc:publisher>World Bank</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/25</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:16:49Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>QUANGO Watch - Survey of non-elected public bodies in Scotland, 1995</dc:title>\r\n" + 
    		"<dc:contributor>Scottish Government</dc:contributor>\r\n" + 
    		"<dc:subject>QUANGO, non-governmental organisation</dc:subject>\r\n" + 
    		"<dc:description>Nearly 500 non-departmental public bodies in Scotland were surveyed in late 1995, having been identified from publications such as HMSO's 'Public Bodies' and 'Scottish Regions'. One organisation questionnaire was sent initially, then, if the organisation fitted the 'quango' profile, a further organisation questionnaire was sent together with board member questionnaires. A QUANGO is a Quasi-Autonomous Non-Governmental Organisation.  The replies were entered into two databases; one for organisations and one for board members. The databases contain details of over 250 organisations and approximately 600 individual members. The criteria for inclusion in the database are that the organisation receives public funds which it also disburses and that it's board members are not elected.</dc:description>\r\n" + 
    		"<dc:date>2009-10-15T12:05:56Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:49Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:49Z</dc:date>\r\n" + 
    		"<dc:date>2009-10-15T12:05:56Z</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>Brown, Alice; McCrone, David. (2009). QUANGO Watch - Survey of non-elected public bodies in Scotland, 1995, 1995 [Dataset]. University of Edinburgh.Institute of Governance (formerly Unit for the Study of Government in Scotland.</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/25</dc:identifier>\r\n" + 
    		"<dc:language>eng</dc:language>\r\n" + 
    		"<dc:rights>Copyright, University of Edinburgh.</dc:rights>\r\n" + 
    		"<dc:publisher>University of Edinburgh.Institute of Governance (formerly Unit for the Study of Government in Scotland</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:2318/62</identifier>\r\n" + 
    		"                <datestamp>2013-07-09T22:59:19Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_16</setSpec>\r\n" + 
    		"                <setSpec>col_10673_21</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Probabilistic Sophistication and Multiple Priors</dc:title>\r\n" + 
    		"<dc:description>We show that under fairly mild conditions, a maximin expected utility preference relation is probabilistically sophisticated if and only if it is subjective expected utility.</dc:description>\r\n" + 
    		"<dc:description>ICER - International Centre for Economic Research</dc:description>\r\n" + 
    		"<dc:date>2013-07-09T22:59:19Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09T22:59:19Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09</dc:date>\r\n" + 
    		"<dc:type>Articolo</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/2318/62</dc:identifier>\r\n" + 
    		"<dc:relation>Applied Mathematics Working Papers Series</dc:relation>\r\n" + 
    		"<dc:relation>8/2001</dc:relation>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10459.3/53</identifier>\r\n" + 
    		"                <datestamp>2013-07-29T11:25:05Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_10</setSpec>\r\n" + 
    		"                <setSpec>col_10673_13</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Vida y virtudes de la venerable virgen doña Luisa de Carvajal y Mendoza</dc:title>\r\n" + 
    		"<dc:subject>Carvajal Mendoza, Luisa de</dc:subject>\r\n" + 
    		"<dc:description>593 p.; 22 cm</dc:description>\r\n" + 
    		"<dc:date>2012-07-01T10:15:34Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-29T11:25:05Z</dc:date>\r\n" + 
    		"<dc:date>2012-07-01T10:15:34Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-29T11:25:05Z</dc:date>\r\n" + 
    		"<dc:date>2012</dc:date>\r\n" + 
    		"<dc:date>1897</dc:date>\r\n" + 
    		"<dc:type>Text</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10459.3/53</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10459</dc:identifier>\r\n" + 
    		"<dc:language>es</dc:language>\r\n" + 
    		"<dc:relation>http://cataleg.udl.cat/record=b1069304~S11*cat</dc:relation>\r\n" + 
    		"<dc:rights>Còpia permesa amb finalitat d'estudi o recerca, citant l'autor i la font: \"Fons Gili i Gaya / Universitat de Lleida\". Per a qualsevol altre ús cal demanar autorització.</dc:rights>\r\n" + 
    		"<dc:publisher>Universitat de Lleida</dc:publisher>\r\n" + 
    		"<dc:source>Publicació original: Madrid : Sucesores de Rivadeneyra, 1897</dc:source>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10986/2232</identifier>\r\n" + 
    		"                <datestamp>2013-05-06T16:55:14Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_18</setSpec>\r\n" + 
    		"                <setSpec>col_10673_19</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>New Structural Economics : A\r\n" + 
    		"            Framework for Rethinking Development and Policy</dc:title>\r\n" + 
    		"<dc:subject>Growth</dc:subject>\r\n" + 
    		"<dc:description>The new structural economics argues that\r\n" + 
    		"            the best way to upgrade a country's endowment structure\r\n" + 
    		"            is to develop its industries at any specific time according\r\n" + 
    		"            to the comparative advantages determined by its given\r\n" + 
    		"            endowment structure at that time. The economy will be most\r\n" + 
    		"            competitive, the economic surplus will be the largest, and\r\n" + 
    		"            the capital accumulation and the upgrading of factor\r\n" + 
    		"            endowment structure will be the fastest possible. The\r\n" + 
    		"            'New Structural Economics' presented in this book\r\n" + 
    		"            is an attempt to set out this third wave of development\r\n" + 
    		"            thinking. Taking into account the lessons learned from the\r\n" + 
    		"            growth successes and failures of the last decades, it\r\n" + 
    		"            advances a neoclassical approach to study the determinants\r\n" + 
    		"            and dynamics of economic structure. It postulates that the\r\n" + 
    		"            economic structure of an economy is endogenous to its factor\r\n" + 
    		"            endowment structure and that sustained economic development\r\n" + 
    		"            is driven by changes in factor endowments and continuous\r\n" + 
    		"            technological innovation. The paper also discusses binding\r\n" + 
    		"            constraints to growth in each of these industries'\r\n" + 
    		"            value chains as well as mechanisms through which\r\n" + 
    		"            governance-related issues in the implementation of\r\n" + 
    		"            industrial policy could be addressed.</dc:description>\r\n" + 
    		"<dc:date>2013-05-06T16:55:14Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06T16:55:14Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10986/2232</dc:identifier>\r\n" + 
    		"<dc:rights>CC BY 3.0 Unported</dc:rights>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/</dc:rights>\r\n" + 
    		"<dc:publisher>World Bank</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/45</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:16:51Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Survey of Scottish Witchcraft, 1563 - 1736</dc:title>\r\n" + 
    		"<dc:contributor>ESRC - Economic and Social Research Council</dc:contributor>\r\n" + 
    		"<dc:subject>Magic</dc:subject>\r\n" + 
    		"<dc:description>The Survey of Scottish Witchcraft is the result of a two-year project funded by the Economic and Social Research Council (ESRC), grant no. R000239234. The primary goal of the project was to create a database of people accused of witchcraft in Scotland between 1563 and 1736. The database was designed to enable the public and academic researchers to examine biographical and social information about accused witches; cultural and sociological patterns of witchcraft belief and accusation; community, ecclesiastical and legal procedures of investigation and trial, national and regional variations; and the chronology and geography of witchcraft accusation and prosecution.</dc:description>\r\n" + 
    		"<dc:date>2010-08-18T16:05:37Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:51Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:51Z</dc:date>\r\n" + 
    		"<dc:date>2010-08-18</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>Goodare, Julian; Yeoman, Louise; Martin, Lauren; Miller, Joyce. (2010). Survey of Scottish Witchcraft, 1563 - 1736, [Dataset]. University of Edinburgh. School of History, Classics and Archaeology..</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/45</dc:identifier>\r\n" + 
    		"<dc:language>eng</dc:language>\r\n" + 
    		"<dc:rights>Unless explicitly stated otherwise, all material is copyright © The University of Edinburgh</dc:rights>\r\n" + 
    		"<dc:publisher>University of Edinburgh. School of History, Classics and Archaeology.</dc:publisher>\r\n" + 
    		"<dc:source>http://webdb.ucs.ed.ac.uk/witches/</dc:source>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:2318/63</identifier>\r\n" + 
    		"                <datestamp>2013-07-09T22:59:19Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_16</setSpec>\r\n" + 
    		"                <setSpec>col_10673_21</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Subcalculus for Set Functions and Cores of TU Games</dc:title>\r\n" + 
    		"<dc:description>This paper introduces a subcalculus for general set functions and\r\n" + 
    		"uses this framework to study the core of TU games. After stating a\r\n" + 
    		"linearity theorem, we establish several theorems that characterize mea-\r\n" + 
    		"sure games having Þnite-dimensional cores. This is a very tractable\r\n" + 
    		"class of games relevant in many economic applications. Finally, we\r\n" + 
    		"show that exact games with Þnite dimensional cores are generalized\r\n" + 
    		"linear production games.</dc:description>\r\n" + 
    		"<dc:description>ICER - International Centre For Economic Research</dc:description>\r\n" + 
    		"<dc:date>2013-07-09T22:59:19Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09T22:59:19Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09</dc:date>\r\n" + 
    		"<dc:type>Articolo</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/2318/63</dc:identifier>\r\n" + 
    		"<dc:relation>Applied Mathematics Working Paper Series</dc:relation>\r\n" + 
    		"<dc:relation>09/2001</dc:relation>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/25</identifier>\r\n" + 
    		"                <datestamp>2013-09-05T13:53:51Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>test</dc:title>\r\n" + 
    		"<dc:date>2013-09-05T13:53:51Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-05T13:53:51Z</dc:date>\r\n" + 
    		"<dc:date>2000-01-01</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/25</dc:identifier>\r\n" + 
    		"<dc:rights>http://creativecommons.org/publicdomain/zero/1.0/</dc:rights>\r\n" + 
    		"<dc:rights>CC0 1.0 Universal</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/24</identifier>\r\n" + 
    		"                <datestamp>2013-09-11T02:00:54Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Collection Admin Test</dc:title>\r\n" + 
    		"<dc:creator>Collection, Admin</dc:creator>\r\n" + 
    		"<dc:description>Abstrct is missing</dc:description>\r\n" + 
    		"<dc:date>2013-09-10T08:09:16Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-10T08:09:16Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-10</dc:date>\r\n" + 
    		"<dc:type>Article</dc:type>\r\n" + 
    		"<dc:identifier>citation is missing.</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/24</dc:identifier>\r\n" + 
    		"<dc:language>de</dc:language>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/us/</dc:rights>\r\n" + 
    		"<dc:rights>Attribution 3.0 United States</dc:rights>\r\n" + 
    		"<dc:publisher>my private publisher</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10986/2233</identifier>\r\n" + 
    		"                <datestamp>2013-05-06T16:55:16Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_18</setSpec>\r\n" + 
    		"                <setSpec>col_10673_19</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Ascent after Decline : Regrowing\r\n" + 
    		"            Global Economies after the Great Recession</dc:title>\r\n" + 
    		"<dc:subject>ADVANCED COUNTRIES</dc:subject>\r\n" + 
    		"<dc:subject>ADVANCED ECONOMIES</dc:subject>\r\n" + 
    		"<dc:subject>AGGREGATE DEMAND</dc:subject>\r\n" + 
    		"<dc:subject>ARBITRAGE</dc:subject>\r\n" + 
    		"<dc:subject>ASSET PRICES</dc:subject>\r\n" + 
    		"<dc:subject>ASSETS</dc:subject>\r\n" + 
    		"<dc:subject>BALANCE SHEETS</dc:subject>\r\n" + 
    		"<dc:subject>BANK CREDIT</dc:subject>\r\n" + 
    		"<dc:subject>BANK GOVERNORS</dc:subject>\r\n" + 
    		"<dc:subject>BANKING SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>BANKING SYSTEMS</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESS CYCLE</dc:subject>\r\n" + 
    		"<dc:subject>CAPITAL ACCUMULATION</dc:subject>\r\n" + 
    		"<dc:subject>CAPITAL ADEQUACY</dc:subject>\r\n" + 
    		"<dc:subject>CAPITAL FLOW</dc:subject>\r\n" + 
    		"<dc:subject>CAPITAL FLOWS</dc:subject>\r\n" + 
    		"<dc:subject>CAPITAL INFLOWS</dc:subject>\r\n" + 
    		"<dc:subject>CENTRAL BANK</dc:subject>\r\n" + 
    		"<dc:subject>CENTRAL BANK POLICY</dc:subject>\r\n" + 
    		"<dc:subject>CENTRAL BANKS</dc:subject>\r\n" + 
    		"<dc:subject>CLIENT COUNTRIES</dc:subject>\r\n" + 
    		"<dc:subject>COLLATERAL</dc:subject>\r\n" + 
    		"<dc:subject>COMMON CURRENCY</dc:subject>\r\n" + 
    		"<dc:subject>COMMUNICATION TECHNOLOGY</dc:subject>\r\n" + 
    		"<dc:subject>COMPETITIVE EXCHANGE RATE</dc:subject>\r\n" + 
    		"<dc:subject>COMPETITIVENESS</dc:subject>\r\n" + 
    		"<dc:subject>CONSOLIDATION</dc:subject>\r\n" + 
    		"<dc:subject>CONSUMER DURABLES</dc:subject>\r\n" + 
    		"<dc:subject>COPYRIGHT CLEARANCE</dc:subject>\r\n" + 
    		"<dc:subject>COPYRIGHT CLEARANCE CENTER</dc:subject>\r\n" + 
    		"<dc:subject>CORPORATE TAX</dc:subject>\r\n" + 
    		"<dc:subject>CREDITWORTHINESS</dc:subject>\r\n" + 
    		"<dc:subject>CURRENT ACCOUNT</dc:subject>\r\n" + 
    		"<dc:subject>CURRENT ACCOUNT BALANCE</dc:subject>\r\n" + 
    		"<dc:subject>CURRENT ACCOUNT BALANCES</dc:subject>\r\n" + 
    		"<dc:subject>CURRENT ACCOUNT DEFICIT</dc:subject>\r\n" + 
    		"<dc:subject>CURRENT ACCOUNT DEFICITS</dc:subject>\r\n" + 
    		"<dc:subject>CURRENT ACCOUNT IMBALANCES</dc:subject>\r\n" + 
    		"<dc:subject>CURRENT ACCOUNT POSITIONS</dc:subject>\r\n" + 
    		"<dc:subject>CURRENT ACCOUNT SURPLUSES</dc:subject>\r\n" + 
    		"<dc:subject>DEBT CRISIS</dc:subject>\r\n" + 
    		"<dc:subject>DEBT SERVICE</dc:subject>\r\n" + 
    		"<dc:subject>DEBT SUSTAINABILITY</dc:subject>\r\n" + 
    		"<dc:subject>DEPENDENCY RATIO</dc:subject>\r\n" + 
    		"<dc:subject>DEPRESSION</dc:subject>\r\n" + 
    		"<dc:subject>DEREGULATION</dc:subject>\r\n" + 
    		"<dc:subject>DEVALUATION</dc:subject>\r\n" + 
    		"<dc:subject>DEVELOPING COUNTRIES</dc:subject>\r\n" + 
    		"<dc:subject>DEVELOPING ECONOMIES</dc:subject>\r\n" + 
    		"<dc:subject>DEVELOPING ECONOMY</dc:subject>\r\n" + 
    		"<dc:subject>DISCOUNT RATE</dc:subject>\r\n" + 
    		"<dc:subject>DOMESTIC CREDIT</dc:subject>\r\n" + 
    		"<dc:subject>DOMESTIC DEBT</dc:subject>\r\n" + 
    		"<dc:subject>DOMESTIC DEMAND</dc:subject>\r\n" + 
    		"<dc:subject>DOMESTIC REAL INTEREST RATES</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC ENVIRONMENT</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC POLICY</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIES OF SCALE</dc:subject>\r\n" + 
    		"<dc:subject>EMERGING ECONOMIES</dc:subject>\r\n" + 
    		"<dc:subject>EMERGING MARKET</dc:subject>\r\n" + 
    		"<dc:subject>EMERGING MARKET ECONOMIES</dc:subject>\r\n" + 
    		"<dc:subject>EMERGING-MARKET</dc:subject>\r\n" + 
    		"<dc:subject>EQUILIBRIUM</dc:subject>\r\n" + 
    		"<dc:subject>EQUITY MARKETS</dc:subject>\r\n" + 
    		"<dc:subject>EXCESS CAPACITY</dc:subject>\r\n" + 
    		"<dc:subject>EXCESS SUPPLY</dc:subject>\r\n" + 
    		"<dc:subject>EXCHANGE RATE</dc:subject>\r\n" + 
    		"<dc:subject>EXCHANGE RATE FLEXIBILITY</dc:subject>\r\n" + 
    		"<dc:subject>EXCHANGE RATE POLICIES</dc:subject>\r\n" + 
    		"<dc:subject>EXCHANGE RATES</dc:subject>\r\n" + 
    		"<dc:subject>EXOGENOUS SHOCKS</dc:subject>\r\n" + 
    		"<dc:subject>EXPORT MARKETS</dc:subject>\r\n" + 
    		"<dc:subject>EXPORT PERFORMANCE</dc:subject>\r\n" + 
    		"<dc:subject>EXPOSURE</dc:subject>\r\n" + 
    		"<dc:subject>EXTERNAL DEFICITS</dc:subject>\r\n" + 
    		"<dc:subject>EXTERNAL DEMAND</dc:subject>\r\n" + 
    		"<dc:subject>FEDERAL RESERVE</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL CONSTRAINTS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL CRISES</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL CRISIS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL INSTITUTIONS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL MARKETS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL REFORM</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL STABILITY</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL VOLATILITY</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL VULNERABILITY</dc:subject>\r\n" + 
    		"<dc:subject>FISCAL CONSOLIDATION</dc:subject>\r\n" + 
    		"<dc:subject>FISCAL DEFICITS</dc:subject>\r\n" + 
    		"<dc:subject>FISCAL IMBALANCES</dc:subject>\r\n" + 
    		"<dc:subject>FISCAL POLICIES</dc:subject>\r\n" + 
    		"<dc:subject>FISCAL POLICY</dc:subject>\r\n" + 
    		"<dc:subject>FLOWS OF CAPITAL</dc:subject>\r\n" + 
    		"<dc:subject>FOOD PRICES</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN DIRECT INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN RESERVES</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL DEMAND</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL ECONOMIES</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL ECONOMY</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL FINANCE</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL IMBALANCES</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL MARKET</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL MARKETS</dc:subject>\r\n" + 
    		"<dc:subject>GLOBALIZATION</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT BONDS</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT DEBT</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT SECURITIES</dc:subject>\r\n" + 
    		"<dc:subject>GROSS DOMESTIC PRODUCT</dc:subject>\r\n" + 
    		"<dc:subject>GROSS NATIONAL INCOME</dc:subject>\r\n" + 
    		"<dc:subject>GROWTH PERFORMANCE</dc:subject>\r\n" + 
    		"<dc:subject>GROWTH RATES</dc:subject>\r\n" + 
    		"<dc:subject>HARMONIZATION</dc:subject>\r\n" + 
    		"<dc:subject>HUMAN CAPITAL</dc:subject>\r\n" + 
    		"<dc:subject>IMBALANCE</dc:subject>\r\n" + 
    		"<dc:subject>IMPORT</dc:subject>\r\n" + 
    		"<dc:subject>IMPORT DEMAND</dc:subject>\r\n" + 
    		"<dc:subject>IMPORT PROTECTION</dc:subject>\r\n" + 
    		"<dc:subject>IMPORTS</dc:subject>\r\n" + 
    		"<dc:subject>INCOME TAX</dc:subject>\r\n" + 
    		"<dc:subject>INDEBTEDNESS</dc:subject>\r\n" + 
    		"<dc:subject>INDUSTRIAL COUNTRIES</dc:subject>\r\n" + 
    		"<dc:subject>INFLATION</dc:subject>\r\n" + 
    		"<dc:subject>INFLATIONARY PRESSURE</dc:subject>\r\n" + 
    		"<dc:subject>INFORMATION TECHNOLOGY</dc:subject>\r\n" + 
    		"<dc:subject>INFRASTRUCTURE INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>INFRASTRUCTURE PROJECTS</dc:subject>\r\n" + 
    		"<dc:subject>INTELLECTUAL PROPERTY</dc:subject>\r\n" + 
    		"<dc:subject>INTEREST RATE</dc:subject>\r\n" + 
    		"<dc:subject>INTEREST RATES</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL BUSINESS</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL ECONOMICS</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL FINANCIAL ARCHITECTURE</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL MONETARY SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL RESERVE</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL TRADE</dc:subject>\r\n" + 
    		"<dc:subject>INVESTMENT RATIOS</dc:subject>\r\n" + 
    		"<dc:subject>JOINT VENTURE</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MARKET</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MARKETS</dc:subject>\r\n" + 
    		"<dc:subject>LEVERAGE</dc:subject>\r\n" + 
    		"<dc:subject>LIBERALIZATION</dc:subject>\r\n" + 
    		"<dc:subject>LIBERALIZATION OF TRADE</dc:subject>\r\n" + 
    		"<dc:subject>LIQUIDITY</dc:subject>\r\n" + 
    		"<dc:subject>MACROECONOMIC POLICY</dc:subject>\r\n" + 
    		"<dc:subject>MACROECONOMIC STABILIZATION</dc:subject>\r\n" + 
    		"<dc:subject>MARKET SEGMENT</dc:subject>\r\n" + 
    		"<dc:subject>MARKET SEGMENTS</dc:subject>\r\n" + 
    		"<dc:subject>MARKET SHARES</dc:subject>\r\n" + 
    		"<dc:subject>MONETARY POLICIES</dc:subject>\r\n" + 
    		"<dc:subject>MONETARY POLICY</dc:subject>\r\n" + 
    		"<dc:subject>MONIES</dc:subject>\r\n" + 
    		"<dc:subject>MORTGAGES</dc:subject>\r\n" + 
    		"<dc:subject>NATIONAL SAVING</dc:subject>\r\n" + 
    		"<dc:subject>NATURAL RESOURCE</dc:subject>\r\n" + 
    		"<dc:subject>NATURAL RESOURCES</dc:subject>\r\n" + 
    		"<dc:subject>OIL EXPORTERS</dc:subject>\r\n" + 
    		"<dc:subject>OIL PRICE</dc:subject>\r\n" + 
    		"<dc:subject>OIL-EXPORTING COUNTRIES</dc:subject>\r\n" + 
    		"<dc:subject>OPEN ECONOMIES</dc:subject>\r\n" + 
    		"<dc:subject>OUTPUT</dc:subject>\r\n" + 
    		"<dc:subject>OUTPUT DECLINE</dc:subject>\r\n" + 
    		"<dc:subject>OUTPUT GAPS</dc:subject>\r\n" + 
    		"<dc:subject>OUTPUT RESPONSE</dc:subject>\r\n" + 
    		"<dc:subject>PENSION</dc:subject>\r\n" + 
    		"<dc:subject>PENSION LIABILITIES</dc:subject>\r\n" + 
    		"<dc:subject>PERSISTENT UNEMPLOYMENT</dc:subject>\r\n" + 
    		"<dc:subject>POVERTY REDUCTION</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE CONSUMPTION</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE CREDIT</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>PROTECTIONIST MEASURES</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC DEBT</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC EXPENDITURES</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC SPENDING</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC-PRIVATE PARTNERSHIP</dc:subject>\r\n" + 
    		"<dc:subject>REAL DEPRECIATION</dc:subject>\r\n" + 
    		"<dc:subject>REAL EFFECTIVE EXCHANGE RATES</dc:subject>\r\n" + 
    		"<dc:subject>RECESSION</dc:subject>\r\n" + 
    		"<dc:subject>RECESSIONS</dc:subject>\r\n" + 
    		"<dc:subject>REGIONAL MARKETS</dc:subject>\r\n" + 
    		"<dc:subject>REGULATORY REFORM</dc:subject>\r\n" + 
    		"<dc:subject>RESERVE CURRENCY</dc:subject>\r\n" + 
    		"<dc:subject>RISK CAPITAL</dc:subject>\r\n" + 
    		"<dc:subject>SECTOR LIBERALIZATION</dc:subject>\r\n" + 
    		"<dc:subject>SECTOR REFORM</dc:subject>\r\n" + 
    		"<dc:subject>SHORTFALLS</dc:subject>\r\n" + 
    		"<dc:subject>SLACK</dc:subject>\r\n" + 
    		"<dc:subject>SLOW GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>SLOWDOWN</dc:subject>\r\n" + 
    		"<dc:subject>SOVEREIGN DEBT</dc:subject>\r\n" + 
    		"<dc:subject>STEADY STATE</dc:subject>\r\n" + 
    		"<dc:subject>STOCK MARKET</dc:subject>\r\n" + 
    		"<dc:subject>STOCK MARKET CAPITALIZATION</dc:subject>\r\n" + 
    		"<dc:subject>STRUCTURAL REFORM</dc:subject>\r\n" + 
    		"<dc:subject>STRUCTURAL REFORMS</dc:subject>\r\n" + 
    		"<dc:subject>SURPLUS</dc:subject>\r\n" + 
    		"<dc:subject>SURPLUS COUNTRIES</dc:subject>\r\n" + 
    		"<dc:subject>TAX RATE</dc:subject>\r\n" + 
    		"<dc:subject>TAX REFORM</dc:subject>\r\n" + 
    		"<dc:subject>TAX REFORMS</dc:subject>\r\n" + 
    		"<dc:subject>TRADE DEFICITS</dc:subject>\r\n" + 
    		"<dc:subject>TRADE OPENNESS</dc:subject>\r\n" + 
    		"<dc:subject>TRADE PATTERNS</dc:subject>\r\n" + 
    		"<dc:subject>TRADING SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>UNCERTAINTY</dc:subject>\r\n" + 
    		"<dc:subject>UNEMPLOYMENT RATES</dc:subject>\r\n" + 
    		"<dc:subject>WORLD ECONOMY</dc:subject>\r\n" + 
    		"<dc:description>This volume combines the analyses of\r\n" + 
    		"            leading experts on the various elements affecting economic\r\n" + 
    		"            growth and the policies required to spur that growth. Ascent\r\n" + 
    		"            after Decline: Regrowing Global Economies after the Great\r\n" + 
    		"            Recession identifies the main challenges to the economic\r\n" + 
    		"            recovery, such as rising debt levels, reduced trade\r\n" + 
    		"            prospects, and global imbalances, as well as the obstacles\r\n" + 
    		"            to growth posed by fiscal conundrums and lagging\r\n" + 
    		"            infrastructure. It also examines the way forward, beginning\r\n" + 
    		"            with the role of the state and then covering labor markets,\r\n" + 
    		"            information technology, and innovation. The common thread\r\n" + 
    		"            throughout the book is the view that economic re-growth will\r\n" + 
    		"            depend in large measure on smart policy choices and that the\r\n" + 
    		"            role of government has never been more crucial than at any\r\n" + 
    		"            time since the great depression. As members of the World\r\n" + 
    		"            Bank community, these issues are of particular importance to\r\n" + 
    		"            us, since without a resurrection of strong economic growth\r\n" + 
    		"            in major economies, the likelihood of rapid economic\r\n" + 
    		"            development in poorer developing countries is dampened. This\r\n" + 
    		"            is troubling because we have seen progress in many parts of\r\n" + 
    		"            the globe in the past decade, including in Africa, and these\r\n" + 
    		"            gains will be arrested as long as the global economy is in\r\n" + 
    		"            disarray. Donors will withdraw, investment will retrench,\r\n" + 
    		"            and prospects will dim. This immiserizing welfare outcome is\r\n" + 
    		"            to be avoided. The volume is intended to shed light on those\r\n" + 
    		"            areas of policy that reduce the prospects of a prolonged\r\n" + 
    		"            period of stress and decline by 'regrowing growth.'</dc:description>\r\n" + 
    		"<dc:date>2013-05-06T16:55:16Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06T16:55:16Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10986/2233</dc:identifier>\r\n" + 
    		"<dc:rights>CC BY 3.0 Unported</dc:rights>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/</dc:rights>\r\n" + 
    		"<dc:publisher>World Bank</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/46</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:16:52Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>International Stroke Trial database (superseded)</dc:title>\r\n" + 
    		"<dc:contributor>MRC - Medical Research Council</dc:contributor>\r\n" + 
    		"<dc:subject>clinical trials, randomised controlled trials, RCTs,stroke,</dc:subject>\r\n" + 
    		"<dc:description>The International Stroke Trial (IST) was one the biggest randomised trials in acute stroke. Methods:  Available data  on variables assessed at  randomisation, at the early outcome point (14-days after randomisation or prior discharge) and at 6-months were extracted and made publically available. Results and Conclusions: The IST provides an excellent source of primary data easy-to-use for sample size calculations and preliminary analysis necessary for planning a good quality trial. This item has been superseded by new submission http://hdl.handle.net/10283/128.</dc:description>\r\n" + 
    		"<dc:date>2010-09-24T15:49:07Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:52Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:52Z</dc:date>\r\n" + 
    		"<dc:date>2010-09-24</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>Sandercock, Peter; Niewada, Maciej; Czlonkowska, Anna. (2010). International Stroke Trial database, [Dataset]. University of Edinburgh, Department of Clinical Neurosciences.</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/46</dc:identifier>\r\n" + 
    		"<dc:language>eng</dc:language>\r\n" + 
    		"<dc:relation/>\r\n" + 
    		"<dc:rights>Copyright, University of Edinburgh.</dc:rights>\r\n" + 
    		"<dc:publisher>University of Edinburgh, Department of Clinical Neurosciences</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:2318/64</identifier>\r\n" + 
    		"                <datestamp>2013-07-09T22:59:20Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_16</setSpec>\r\n" + 
    		"                <setSpec>col_10673_21</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Expected Utility Theory Without The Completeness Axiom</dc:title>\r\n" + 
    		"<dc:description>We study axiomatically the problem of obtaining an expected utility representation for a potentially incomplete preference relation over lotteries by means of a set of von Neumann-Morgenstern utility functions. It is shown that, when the prize space is a compact metric space, a preference relation admit such a multi-utility representation provided that it satisfies the standard axioms of expected utility theory. Moreover, the representing set of utilities is unique in a well-defined sens.</dc:description>\r\n" + 
    		"<dc:description>ICER - International Centre For Economic Research</dc:description>\r\n" + 
    		"<dc:date>2013-07-09T22:59:20Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09T22:59:20Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09</dc:date>\r\n" + 
    		"<dc:type>Articolo</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/2318/64</dc:identifier>\r\n" + 
    		"<dc:relation>Applied Mathematics Working Papers Series</dc:relation>\r\n" + 
    		"<dc:relation>11/2001</dc:relation>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/26</identifier>\r\n" + 
    		"                <datestamp>2013-09-06T02:00:28Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>SWORD: Simple Web-service Offering Repository Deposit</dc:title>\r\n" + 
    		"<dc:creator>Allinson, Julie</dc:creator>\r\n" + 
    		"<dc:creator>François, Sebastien</dc:creator>\r\n" + 
    		"<dc:creator>Lewis, Stuart</dc:creator>\r\n" + 
    		"<dc:description>This article offers a twofold introduction to the JISC-funded SWORD Project which ran for eight months in mid-2007. Firstly it presents an overview of the methods and madness that led us to where we currently are, including a timeline of how this work moved through an informal working group to a lightweight, distributed project. Secondly, it offers an explanation of the outputs produced for the SWORD Project and their potential benefits for the repositories community. SWORD, which stands for Simple Web service Offering Repository Deposit, came into being in March 2007 but was preceded by a series of discussions and activities which have contributed much to the project, known as the 'Deposit API'. The project itself was funded under the JISC Repositories and Preservation Programme, Tools and Innovation strand, with the over-arching aim of scoping, defining, developing and testing a standard mechanism for depositing into repositories and other systems. The motivation was that there was no standard way of doing this currently and increasingly scenarios were arising that might usefully leverage such a standard.</dc:description>\r\n" + 
    		"<dc:date>2013-09-05T14:59:23Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-05T14:59:23Z</dc:date>\r\n" + 
    		"<dc:date>2008-01</dc:date>\r\n" + 
    		"<dc:date>2013-09-05T14:59:26Z</dc:date>\r\n" + 
    		"<dc:type>Journal Article</dc:type>\r\n" + 
    		"<dc:identifier>http://www.ariadne.ac.uk/issue54/allinson-et-al/</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/26</dc:identifier>\r\n" + 
    		"<dc:language>en</dc:language>\r\n" + 
    		"<dc:rights>Julie Allinson, Sebastien François, Stuart Lewis</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10986/2234</identifier>\r\n" + 
    		"                <datestamp>2013-05-06T16:55:20Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_18</setSpec>\r\n" + 
    		"                <setSpec>col_10673_19</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Fighting Corruption in Public Services : Chronicling Georgia's Reforms</dc:title>\r\n" + 
    		"<dc:subject>ABUSE</dc:subject>\r\n" + 
    		"<dc:subject>ACCOUNTABILITIES</dc:subject>\r\n" + 
    		"<dc:subject>ACCOUNTABILITY</dc:subject>\r\n" + 
    		"<dc:subject>ACCOUNTING</dc:subject>\r\n" + 
    		"<dc:subject>AGRICULTURAL PRODUCTS</dc:subject>\r\n" + 
    		"<dc:subject>ANTICORRUPTION</dc:subject>\r\n" + 
    		"<dc:subject>ANTICORRUPTION BUREAU</dc:subject>\r\n" + 
    		"<dc:subject>ANTICORRUPTION EFFORTS</dc:subject>\r\n" + 
    		"<dc:subject>ANTICORRUPTION REFORM</dc:subject>\r\n" + 
    		"<dc:subject>ANTICORRUPTION REFORMS</dc:subject>\r\n" + 
    		"<dc:subject>ARREARS</dc:subject>\r\n" + 
    		"<dc:subject>ASSETS</dc:subject>\r\n" + 
    		"<dc:subject>AUDITORS</dc:subject>\r\n" + 
    		"<dc:subject>BANKS</dc:subject>\r\n" + 
    		"<dc:subject>BORDER CROSSINGS</dc:subject>\r\n" + 
    		"<dc:subject>BORDER MANAGEMENT</dc:subject>\r\n" + 
    		"<dc:subject>BRIBE</dc:subject>\r\n" + 
    		"<dc:subject>BRIBERY</dc:subject>\r\n" + 
    		"<dc:subject>BRIBES</dc:subject>\r\n" + 
    		"<dc:subject>BRIDGE</dc:subject>\r\n" + 
    		"<dc:subject>BRIDGE CROSSING</dc:subject>\r\n" + 
    		"<dc:subject>BROKERAGE</dc:subject>\r\n" + 
    		"<dc:subject>BROKERS</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESS ASSOCIATIONS</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESS COMMUNITY</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESS DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESS OWNERS</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESSMAN</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESSMEN</dc:subject>\r\n" + 
    		"<dc:subject>CAPACITY CONSTRAINTS</dc:subject>\r\n" + 
    		"<dc:subject>CAR</dc:subject>\r\n" + 
    		"<dc:subject>CAR MARKET</dc:subject>\r\n" + 
    		"<dc:subject>CARS</dc:subject>\r\n" + 
    		"<dc:subject>CHECKS</dc:subject>\r\n" + 
    		"<dc:subject>CIVIL SERVANTS</dc:subject>\r\n" + 
    		"<dc:subject>CIVIL SOCIETY</dc:subject>\r\n" + 
    		"<dc:subject>COLLAPSE</dc:subject>\r\n" + 
    		"<dc:subject>COMMERCIAL BANKS</dc:subject>\r\n" + 
    		"<dc:subject>COMPLAINT</dc:subject>\r\n" + 
    		"<dc:subject>COMPLAINTS</dc:subject>\r\n" + 
    		"<dc:subject>CONFIDENCE</dc:subject>\r\n" + 
    		"<dc:subject>CONSUMER PRICE INDEX</dc:subject>\r\n" + 
    		"<dc:subject>CORPORATE PROFIT TAX</dc:subject>\r\n" + 
    		"<dc:subject>CORRUPT</dc:subject>\r\n" + 
    		"<dc:subject>CORRUPT OFFICIALS</dc:subject>\r\n" + 
    		"<dc:subject>CORRUPTION</dc:subject>\r\n" + 
    		"<dc:subject>CORRUPTION FIGHTER</dc:subject>\r\n" + 
    		"<dc:subject>CORRUPTION PREVENTION</dc:subject>\r\n" + 
    		"<dc:subject>CREDIBILITY</dc:subject>\r\n" + 
    		"<dc:subject>CRIME</dc:subject>\r\n" + 
    		"<dc:subject>CRIMES</dc:subject>\r\n" + 
    		"<dc:subject>CRIMINAL</dc:subject>\r\n" + 
    		"<dc:subject>CRIMINALS</dc:subject>\r\n" + 
    		"<dc:subject>CURRENCY</dc:subject>\r\n" + 
    		"<dc:subject>CUSTOMER SATISFACTION</dc:subject>\r\n" + 
    		"<dc:subject>DEVELOPING COUNTRIES</dc:subject>\r\n" + 
    		"<dc:subject>DISCRETION</dc:subject>\r\n" + 
    		"<dc:subject>DISPUTE RESOLUTION</dc:subject>\r\n" + 
    		"<dc:subject>DOCUMENTATION REQUIREMENTS</dc:subject>\r\n" + 
    		"<dc:subject>DOMESTIC VIOLENCE</dc:subject>\r\n" + 
    		"<dc:subject>DRIVERS</dc:subject>\r\n" + 
    		"<dc:subject>DRIVING</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>ELECTRICITY</dc:subject>\r\n" + 
    		"<dc:subject>ELECTRONIC PAYMENT</dc:subject>\r\n" + 
    		"<dc:subject>ELECTRONIC PAYMENTS</dc:subject>\r\n" + 
    		"<dc:subject>ELECTRONIC SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>ELECTRONIC SYSTEMS</dc:subject>\r\n" + 
    		"<dc:subject>ENFORCEMENT AGENCIES</dc:subject>\r\n" + 
    		"<dc:subject>ENFORCEMENT SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>ENTERPRISE PERFORMANCE</dc:subject>\r\n" + 
    		"<dc:subject>EQUAL TREATMENT</dc:subject>\r\n" + 
    		"<dc:subject>EQUALIZATION</dc:subject>\r\n" + 
    		"<dc:subject>EQUIPMENT</dc:subject>\r\n" + 
    		"<dc:subject>EVASION</dc:subject>\r\n" + 
    		"<dc:subject>EXCISE TAX</dc:subject>\r\n" + 
    		"<dc:subject>EXCISE TAXES</dc:subject>\r\n" + 
    		"<dc:subject>EXPENDITURES</dc:subject>\r\n" + 
    		"<dc:subject>EXPORTER</dc:subject>\r\n" + 
    		"<dc:subject>EXPORTERS</dc:subject>\r\n" + 
    		"<dc:subject>EXTORTION</dc:subject>\r\n" + 
    		"<dc:subject>FACE-TO-FACE CONTACTS</dc:subject>\r\n" + 
    		"<dc:subject>FAMILIES</dc:subject>\r\n" + 
    		"<dc:subject>FARMERS</dc:subject>\r\n" + 
    		"<dc:subject>FIGHT AGAINST CORRUPTION</dc:subject>\r\n" + 
    		"<dc:subject>FINANCES</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL CRISIS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN DONOR</dc:subject>\r\n" + 
    		"<dc:subject>FRAUD</dc:subject>\r\n" + 
    		"<dc:subject>FRAUDULENT TRANSACTIONS</dc:subject>\r\n" + 
    		"<dc:subject>FREIGHT</dc:subject>\r\n" + 
    		"<dc:subject>FUEL</dc:subject>\r\n" + 
    		"<dc:subject>GANGS</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT BUDGETS</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT EXPENDITURE</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT OFFICIALS</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT POLICY</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT REVENUES</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT · POLICY</dc:subject>\r\n" + 
    		"<dc:subject>GRAY ECONOMY</dc:subject>\r\n" + 
    		"<dc:subject>GROSS DOMESTIC PRODUCT</dc:subject>\r\n" + 
    		"<dc:subject>HIGHWAYS</dc:subject>\r\n" + 
    		"<dc:subject>HOLDING</dc:subject>\r\n" + 
    		"<dc:subject>HOMOGENEOUS GOODS</dc:subject>\r\n" + 
    		"<dc:subject>HOUSEHOLDS</dc:subject>\r\n" + 
    		"<dc:subject>HUMAN RIGHTS</dc:subject>\r\n" + 
    		"<dc:subject>IMMUNIZATION</dc:subject>\r\n" + 
    		"<dc:subject>INCOME CATEGORY</dc:subject>\r\n" + 
    		"<dc:subject>INCOME GROUP</dc:subject>\r\n" + 
    		"<dc:subject>INCOME TAX</dc:subject>\r\n" + 
    		"<dc:subject>INFLATION</dc:subject>\r\n" + 
    		"<dc:subject>INFLUENCE PEDDLING</dc:subject>\r\n" + 
    		"<dc:subject>INSTITUTIONAL REFORMS</dc:subject>\r\n" + 
    		"<dc:subject>INTEGRITY</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL BANK</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>INVESTIGATIONS</dc:subject>\r\n" + 
    		"<dc:subject>ISSUANCE</dc:subject>\r\n" + 
    		"<dc:subject>JAIL</dc:subject>\r\n" + 
    		"<dc:subject>JUDICIAL SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>JUDICIARY</dc:subject>\r\n" + 
    		"<dc:subject>JUSTICE</dc:subject>\r\n" + 
    		"<dc:subject>LAW ENFORCEMENT</dc:subject>\r\n" + 
    		"<dc:subject>LAWS</dc:subject>\r\n" + 
    		"<dc:subject>LEADERSHIP</dc:subject>\r\n" + 
    		"<dc:subject>LEGAL FRAMEWORK</dc:subject>\r\n" + 
    		"<dc:subject>LIABILITY</dc:subject>\r\n" + 
    		"<dc:subject>LICENSES</dc:subject>\r\n" + 
    		"<dc:subject>LOBBYING</dc:subject>\r\n" + 
    		"<dc:subject>LOCAL GOVERNMENT</dc:subject>\r\n" + 
    		"<dc:subject>LOCAL GOVERNMENTS</dc:subject>\r\n" + 
    		"<dc:subject>LOCAL TAXES</dc:subject>\r\n" + 
    		"<dc:subject>MACROECONOMIC STABILITY</dc:subject>\r\n" + 
    		"<dc:subject>MEDIA</dc:subject>\r\n" + 
    		"<dc:subject>MINISTER</dc:subject>\r\n" + 
    		"<dc:subject>MINISTERS</dc:subject>\r\n" + 
    		"<dc:subject>MONEY SUPPLY</dc:subject>\r\n" + 
    		"<dc:subject>MOTORISTS</dc:subject>\r\n" + 
    		"<dc:subject>MUNICIPAL SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>NATIONAL SECURITY</dc:subject>\r\n" + 
    		"<dc:subject>NEW BUSINESS</dc:subject>\r\n" + 
    		"<dc:subject>NEW BUSINESSES</dc:subject>\r\n" + 
    		"<dc:subject>NONPAYMENTS</dc:subject>\r\n" + 
    		"<dc:subject>OFFENSES</dc:subject>\r\n" + 
    		"<dc:subject>PAYMENT SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>PEDESTRIANS</dc:subject>\r\n" + 
    		"<dc:subject>PENALTIES</dc:subject>\r\n" + 
    		"<dc:subject>PENSION</dc:subject>\r\n" + 
    		"<dc:subject>PENSIONERS</dc:subject>\r\n" + 
    		"<dc:subject>PENSIONS</dc:subject>\r\n" + 
    		"<dc:subject>PERSONAL INCOME</dc:subject>\r\n" + 
    		"<dc:subject>PETROLEUM PRODUCTS</dc:subject>\r\n" + 
    		"<dc:subject>PETTY CORRUPTION</dc:subject>\r\n" + 
    		"<dc:subject>POLICE</dc:subject>\r\n" + 
    		"<dc:subject>POLICE OFFICER</dc:subject>\r\n" + 
    		"<dc:subject>POLICE OFFICERS</dc:subject>\r\n" + 
    		"<dc:subject>POLICE STATION</dc:subject>\r\n" + 
    		"<dc:subject>POLICE STATIONS</dc:subject>\r\n" + 
    		"<dc:subject>POLITICAL ECONOMY</dc:subject>\r\n" + 
    		"<dc:subject>POLITICAL LEADERS</dc:subject>\r\n" + 
    		"<dc:subject>POLLUTION</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATIZATION</dc:subject>\r\n" + 
    		"<dc:subject>PROFESSIONAL DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>PROPERTY TAX</dc:subject>\r\n" + 
    		"<dc:subject>PROSECUTION</dc:subject>\r\n" + 
    		"<dc:subject>PROSECUTIONS</dc:subject>\r\n" + 
    		"<dc:subject>PROSECUTORS</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC ATTITUDES</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC ENTERPRISES</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC FINANCE</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC FUNDS</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC GOODS</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC OFFICIAL</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC OFFICIALS</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC REGISTRIES</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC REVENUES</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC SERVICE</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC WORKS</dc:subject>\r\n" + 
    		"<dc:subject>RAILWAY</dc:subject>\r\n" + 
    		"<dc:subject>RECEIPTS</dc:subject>\r\n" + 
    		"<dc:subject>RED TAPE</dc:subject>\r\n" + 
    		"<dc:subject>REGISTRATION PROCESS</dc:subject>\r\n" + 
    		"<dc:subject>REGISTRATION SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>REGULATORY FRAMEWORK</dc:subject>\r\n" + 
    		"<dc:subject>RESERVES</dc:subject>\r\n" + 
    		"<dc:subject>RETURNS</dc:subject>\r\n" + 
    		"<dc:subject>REVENUE COLLECTION</dc:subject>\r\n" + 
    		"<dc:subject>ROAD</dc:subject>\r\n" + 
    		"<dc:subject>ROAD CROSSING</dc:subject>\r\n" + 
    		"<dc:subject>ROADS</dc:subject>\r\n" + 
    		"<dc:subject>ROBBERIES</dc:subject>\r\n" + 
    		"<dc:subject>SENIOR GOVERNMENT OFFICIALS</dc:subject>\r\n" + 
    		"<dc:subject>SERVICE DELIVERY</dc:subject>\r\n" + 
    		"<dc:subject>SMALL-SCALE ENTERPRISES</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL SECURITY</dc:subject>\r\n" + 
    		"<dc:subject>SOURCE OF INCOME</dc:subject>\r\n" + 
    		"<dc:subject>SPECIAL FUND</dc:subject>\r\n" + 
    		"<dc:subject>START-UPS</dc:subject>\r\n" + 
    		"<dc:subject>STATE LIABILITIES</dc:subject>\r\n" + 
    		"<dc:subject>STREETS</dc:subject>\r\n" + 
    		"<dc:subject>STRICTER ENFORCEMENT</dc:subject>\r\n" + 
    		"<dc:subject>SUBWAY</dc:subject>\r\n" + 
    		"<dc:subject>SUBWAY SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>SUPERVISORY BOARD</dc:subject>\r\n" + 
    		"<dc:subject>TAX</dc:subject>\r\n" + 
    		"<dc:subject>TAX BURDEN</dc:subject>\r\n" + 
    		"<dc:subject>TAX CODE</dc:subject>\r\n" + 
    		"<dc:subject>TAX CODES</dc:subject>\r\n" + 
    		"<dc:subject>TAX COLLECTION</dc:subject>\r\n" + 
    		"<dc:subject>TAX COLLECTIONS</dc:subject>\r\n" + 
    		"<dc:subject>TAX CREDITS</dc:subject>\r\n" + 
    		"<dc:subject>TAX LEGISLATION</dc:subject>\r\n" + 
    		"<dc:subject>TAX LIABILITY</dc:subject>\r\n" + 
    		"<dc:subject>TAX RATE</dc:subject>\r\n" + 
    		"<dc:subject>TAX RATES</dc:subject>\r\n" + 
    		"<dc:subject>TAX REGIME</dc:subject>\r\n" + 
    		"<dc:subject>TAX RETURNS</dc:subject>\r\n" + 
    		"<dc:subject>TAX REVENUE</dc:subject>\r\n" + 
    		"<dc:subject>TAX REVENUES</dc:subject>\r\n" + 
    		"<dc:subject>TAX SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>TAX TREATMENT</dc:subject>\r\n" + 
    		"<dc:subject>TAXATION</dc:subject>\r\n" + 
    		"<dc:subject>THEFT</dc:subject>\r\n" + 
    		"<dc:subject>THIEF</dc:subject>\r\n" + 
    		"<dc:subject>THIEVES</dc:subject>\r\n" + 
    		"<dc:subject>TRADE REGIME</dc:subject>\r\n" + 
    		"<dc:subject>TRADING</dc:subject>\r\n" + 
    		"<dc:subject>TRAFFIC</dc:subject>\r\n" + 
    		"<dc:subject>TRAFFIC CAMERAS</dc:subject>\r\n" + 
    		"<dc:subject>TRAFFIC POLICE</dc:subject>\r\n" + 
    		"<dc:subject>TRAFFIC SAFETY</dc:subject>\r\n" + 
    		"<dc:subject>TRAFFIC VIOLATIONS</dc:subject>\r\n" + 
    		"<dc:subject>TRANSACTION</dc:subject>\r\n" + 
    		"<dc:subject>TRANSIT</dc:subject>\r\n" + 
    		"<dc:subject>TRANSPARENCY</dc:subject>\r\n" + 
    		"<dc:subject>TRANSPORT</dc:subject>\r\n" + 
    		"<dc:subject>TRANSPORTATION</dc:subject>\r\n" + 
    		"<dc:subject>TREASURY</dc:subject>\r\n" + 
    		"<dc:subject>TRUCKS</dc:subject>\r\n" + 
    		"<dc:subject>UNEMPLOYMENT</dc:subject>\r\n" + 
    		"<dc:subject>UNION</dc:subject>\r\n" + 
    		"<dc:subject>URBAN AREAS</dc:subject>\r\n" + 
    		"<dc:subject>UTILITIES</dc:subject>\r\n" + 
    		"<dc:subject>VALUATION</dc:subject>\r\n" + 
    		"<dc:subject>VEHICLE</dc:subject>\r\n" + 
    		"<dc:subject>VEHICLE REGISTRATION</dc:subject>\r\n" + 
    		"<dc:subject>VIOLENCE</dc:subject>\r\n" + 
    		"<dc:subject>WAREHOUSE</dc:subject>\r\n" + 
    		"<dc:subject>WOMAN</dc:subject>\r\n" + 
    		"<dc:subject>ZERO TOLERANCE</dc:subject>\r\n" + 
    		"<dc:description>This book chronicles the anticorruption\r\n" + 
    		"            reforms that have transformed public service in Georgia\r\n" + 
    		"            since the Rose Revolution in late 2003. The focus is on the\r\n" + 
    		"            'how' behind successful reforms of selected public\r\n" + 
    		"            services. This book tries to answer some of these questions.\r\n" + 
    		"            It is based largely on data and informed by interviews with\r\n" + 
    		"            past and current high-ranking government officials who\r\n" + 
    		"            provide insights from within government on the challenges\r\n" + 
    		"            and solutions, the decisions, and the trade-offs considered.\r\n" + 
    		"            This book does not assess Georgia's overall reforms\r\n" + 
    		"            since the rose revolution. It does not address efforts\r\n" + 
    		"            toward democratization, which were a key part of the rose\r\n" + 
    		"            revolution. The book focuses on how the state was able to\r\n" + 
    		"            root out corruption and eliminate red tape in selected\r\n" + 
    		"            public services. It does not analyze areas in which\r\n" + 
    		"            government efforts are still continuing or may have fallen\r\n" + 
    		"            short. Nor does it suggest any causality between\r\n" + 
    		"            anticorruption reforms and growth or social outcomes. From\r\n" + 
    		"            the case studies on each of these efforts, the book\r\n" + 
    		"            identifies a set of common factors that led to the success\r\n" + 
    		"            of the reforms.</dc:description>\r\n" + 
    		"<dc:date>2013-05-06T16:55:19Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06T16:55:19Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10986/2234</dc:identifier>\r\n" + 
    		"<dc:relation>Directions in Development : public sector governance</dc:relation>\r\n" + 
    		"<dc:rights>CC BY 3.0 Unported</dc:rights>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/</dc:rights>\r\n" + 
    		"<dc:publisher>World Bank</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/47</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:16:53Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Reseach computing consultation</dc:title>\r\n" + 
    		"<dc:contributor>Other</dc:contributor>\r\n" + 
    		"<dc:subject>research, computing, support service requirements</dc:subject>\r\n" + 
    		"<dc:description>The aim of this consultation is to identify and analyse, from the perspective of the staff, the research computing support service requirements across the University, and to inform the development of IS services in the research computing area.&#13;\r\n" + 
    		"The objectives of the consultation phase of the research computing are:&#13;\r\n" + 
    		"*   To ensure that Information Services has a good understanding of the research computing support service requirements across the University.&#13;\r\n" + 
    		"*   To identify the level of support required for existing services and the critical services which do not exist but should be established.&#13;\r\n" + 
    		"*   To carry out a full requirements gathering exercise across the University which will include:&#13;\r\n" + 
    		"    - computational infrastructure (including high performance throughout computing, storage and networking),&#13;\r\n" + 
    		"        - support for operating environments, middleware, virtual eResearch environments and portals being developed nationally,&#13;\r\n" + 
    		"        - data storage and management services, including assured backup, &#13;\r\n" + 
    		"        - support for collaborative research environment,&#13;\r\n" + 
    		"        - support for visualisation and information discovery,&#13;\r\n" + 
    		"        - support for research software,&#13;\r\n" + 
    		"        - other services which may arise as a result of consultations. &#13;\r\n" + 
    		"*   To inform the development of IS services in the research computing area.</dc:description>\r\n" + 
    		"<dc:date>2010-10-27T10:41:17Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:53Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:53Z</dc:date>\r\n" + 
    		"<dc:date>2010-10-27</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>Ekmekcioglu, Cuna. (2010). Reseach computing consultation, 2007 [Dataset]. University of Edinburgh. Information Services.</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/47</dc:identifier>\r\n" + 
    		"<dc:language>eng</dc:language>\r\n" + 
    		"<dc:publisher>University of Edinburgh. Information Services</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:2318/68</identifier>\r\n" + 
    		"                <datestamp>2013-07-09T22:59:20Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_16</setSpec>\r\n" + 
    		"                <setSpec>col_10673_21</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Random Correspondences as Bundles of Random Variables</dc:title>\r\n" + 
    		"<dc:description>We prove results that relate random correspondences with their\r\n" + 
    		"measurable selections, thus providing a foundation for viewing random\r\n" + 
    		"correspondences as  bundles  of random variables.</dc:description>\r\n" + 
    		"<dc:date>2013-07-09T22:59:20Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09T22:59:20Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09</dc:date>\r\n" + 
    		"<dc:type>Articolo</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/2318/68</dc:identifier>\r\n" + 
    		"<dc:relation>Applied Mathematics Working Paper Series</dc:relation>\r\n" + 
    		"<dc:relation>12/2001</dc:relation>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10986/2235</identifier>\r\n" + 
    		"                <datestamp>2013-05-06T16:55:21Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_18</setSpec>\r\n" + 
    		"                <setSpec>col_10673_19</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>A Workbook on Planning for Urban Resilience in the Face of Disasters : Adapting Experiences from Vietnam’s Cities to Other Cities</dc:title>\r\n" + 
    		"<dc:subject>ADAPTATION</dc:subject>\r\n" + 
    		"<dc:subject>ADAPTATION ACTIVITIES</dc:subject>\r\n" + 
    		"<dc:subject>ADAPTATION APPROACH</dc:subject>\r\n" + 
    		"<dc:subject>ADAPTATION CONSIDERATIONS</dc:subject>\r\n" + 
    		"<dc:subject>ADAPTATION MEASURE</dc:subject>\r\n" + 
    		"<dc:subject>ADAPTATION OPTIONS</dc:subject>\r\n" + 
    		"<dc:subject>ADAPTATION POLICIES</dc:subject>\r\n" + 
    		"<dc:subject>ADAPTATION PRIORITIES</dc:subject>\r\n" + 
    		"<dc:subject>ADAPTATION PROJECTS</dc:subject>\r\n" + 
    		"<dc:subject>ADAPTATION TO CLIMATE</dc:subject>\r\n" + 
    		"<dc:subject>ADAPTATION TO CLIMATE CHANGE</dc:subject>\r\n" + 
    		"<dc:subject>ADAPTING</dc:subject>\r\n" + 
    		"<dc:subject>ADAPTIVE CAPACITY</dc:subject>\r\n" + 
    		"<dc:subject>AGRICULTURAL PRODUCTION</dc:subject>\r\n" + 
    		"<dc:subject>AIR</dc:subject>\r\n" + 
    		"<dc:subject>AIR TEMPERATURE</dc:subject>\r\n" + 
    		"<dc:subject>ALTITUDE</dc:subject>\r\n" + 
    		"<dc:subject>ATMOSPHERE</dc:subject>\r\n" + 
    		"<dc:subject>BANKS</dc:subject>\r\n" + 
    		"<dc:subject>BUILDING CODES</dc:subject>\r\n" + 
    		"<dc:subject>CAPACITY BUILDING</dc:subject>\r\n" + 
    		"<dc:subject>CAPACITY STRENGTHENING</dc:subject>\r\n" + 
    		"<dc:subject>CARBON</dc:subject>\r\n" + 
    		"<dc:subject>CARBON DIOXIDE</dc:subject>\r\n" + 
    		"<dc:subject>CARBON FINANCE</dc:subject>\r\n" + 
    		"<dc:subject>CATASTROPHIC EVENTS</dc:subject>\r\n" + 
    		"<dc:subject>CENTRE FOR RESEARCH ON THE EPIDEMIOLOGY</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE ADAPTATION</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE CHANGE ADAPTATION</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE CHANGE ADAPTATION MEASURES</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE CHANGE ADAPTATION PROGRAM</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE CHANGE CONSEQUENCES</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE CHANGE IMPACTS</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE CHANGE MEASURES</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE CHANGE MITIGATION</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE CHANGE RESPONSES</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE CHANGE SCENARIOS</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE CHANGES</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE EXTREMES</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE FACTORS</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE FORECASTS</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE HAZARDS</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE IMPACTS</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE MITIGATION</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE PROJECTIONS</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE REFUGEES</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE RESILIENCE</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATE RISKS</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATES</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATIC STIMULI</dc:subject>\r\n" + 
    		"<dc:subject>CO</dc:subject>\r\n" + 
    		"<dc:subject>COLORS</dc:subject>\r\n" + 
    		"<dc:subject>CONSEQUENCES OF CLIMATE CHANGE</dc:subject>\r\n" + 
    		"<dc:subject>CONTINGENCY PLANNING</dc:subject>\r\n" + 
    		"<dc:subject>CRED</dc:subject>\r\n" + 
    		"<dc:subject>CYCLONES</dc:subject>\r\n" + 
    		"<dc:subject>DAMAGES</dc:subject>\r\n" + 
    		"<dc:subject>DEFORESTATION</dc:subject>\r\n" + 
    		"<dc:subject>DEVASTATION</dc:subject>\r\n" + 
    		"<dc:subject>DISASTER</dc:subject>\r\n" + 
    		"<dc:subject>DISASTER ASSISTANCE</dc:subject>\r\n" + 
    		"<dc:subject>DISASTER EMERGENCY</dc:subject>\r\n" + 
    		"<dc:subject>DISASTER EMERGENCY RESPONSE</dc:subject>\r\n" + 
    		"<dc:subject>DISASTER IMPACTS</dc:subject>\r\n" + 
    		"<dc:subject>DISASTER MANAGEMENT</dc:subject>\r\n" + 
    		"<dc:subject>DISASTER PREPAREDNESS</dc:subject>\r\n" + 
    		"<dc:subject>DISASTER PREVENTION</dc:subject>\r\n" + 
    		"<dc:subject>DISASTER REDUCTION</dc:subject>\r\n" + 
    		"<dc:subject>DISASTER RESPONSE</dc:subject>\r\n" + 
    		"<dc:subject>DISASTER RESPONSE SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>DISASTER RISK</dc:subject>\r\n" + 
    		"<dc:subject>DISASTER RISK MANAGEMENT</dc:subject>\r\n" + 
    		"<dc:subject>DISASTER RISK REDUCTION</dc:subject>\r\n" + 
    		"<dc:subject>DISASTER RISKS</dc:subject>\r\n" + 
    		"<dc:subject>DISASTERS</dc:subject>\r\n" + 
    		"<dc:subject>DOCUMENTS</dc:subject>\r\n" + 
    		"<dc:subject>DROUGHT</dc:subject>\r\n" + 
    		"<dc:subject>DROUGHT PERIODS</dc:subject>\r\n" + 
    		"<dc:subject>DROUGHTS</dc:subject>\r\n" + 
    		"<dc:subject>EARLY WARNING</dc:subject>\r\n" + 
    		"<dc:subject>EARLY WARNING SYSTEMS</dc:subject>\r\n" + 
    		"<dc:subject>EARTHQUAKE</dc:subject>\r\n" + 
    		"<dc:subject>EARTHQUAKES</dc:subject>\r\n" + 
    		"<dc:subject>ECOLOGICAL ZONES</dc:subject>\r\n" + 
    		"<dc:subject>ELECTRICITY</dc:subject>\r\n" + 
    		"<dc:subject>EMISSIONS</dc:subject>\r\n" + 
    		"<dc:subject>EPIDEMIOLOGY OF DISASTERS</dc:subject>\r\n" + 
    		"<dc:subject>EVACUATION</dc:subject>\r\n" + 
    		"<dc:subject>EXCESS RAINFALL</dc:subject>\r\n" + 
    		"<dc:subject>EXTREME EVENTS</dc:subject>\r\n" + 
    		"<dc:subject>EXTREME TEMPERATURES</dc:subject>\r\n" + 
    		"<dc:subject>EXTREME WEATHER</dc:subject>\r\n" + 
    		"<dc:subject>FAULT LINES</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL CAPACITY</dc:subject>\r\n" + 
    		"<dc:subject>FIRE</dc:subject>\r\n" + 
    		"<dc:subject>FLOOD</dc:subject>\r\n" + 
    		"<dc:subject>FLOOD CONTROL</dc:subject>\r\n" + 
    		"<dc:subject>FLOOD HAZARD</dc:subject>\r\n" + 
    		"<dc:subject>FLOODING</dc:subject>\r\n" + 
    		"<dc:subject>FLOODS</dc:subject>\r\n" + 
    		"<dc:subject>FOOD SECURITY</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN AFFAIRS</dc:subject>\r\n" + 
    		"<dc:subject>FORESTS</dc:subject>\r\n" + 
    		"<dc:subject>FOSSIL FUELS</dc:subject>\r\n" + 
    		"<dc:subject>FROST</dc:subject>\r\n" + 
    		"<dc:subject>FUTURE CLIMATE CHANGE</dc:subject>\r\n" + 
    		"<dc:subject>GHG</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL AIR TEMPERATURE</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL CLIMATE</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL CLIMATE CHANGE</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL EMISSIONS</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL FACILITY FOR DISASTER REDUCTION</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL GREENHOUSE GAS</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL TEMPERATURES</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL WARMING</dc:subject>\r\n" + 
    		"<dc:subject>GREENHOUSE</dc:subject>\r\n" + 
    		"<dc:subject>GREENHOUSE EFFECT</dc:subject>\r\n" + 
    		"<dc:subject>GREENHOUSE GAS</dc:subject>\r\n" + 
    		"<dc:subject>GREENHOUSE GAS EMISSIONS</dc:subject>\r\n" + 
    		"<dc:subject>GREENHOUSE GASES</dc:subject>\r\n" + 
    		"<dc:subject>HAIL</dc:subject>\r\n" + 
    		"<dc:subject>HEAT</dc:subject>\r\n" + 
    		"<dc:subject>HEAT WAVES</dc:subject>\r\n" + 
    		"<dc:subject>HEAVY RAINFALL</dc:subject>\r\n" + 
    		"<dc:subject>HEAVY RAINS</dc:subject>\r\n" + 
    		"<dc:subject>HYDROLOGY</dc:subject>\r\n" + 
    		"<dc:subject>ICE</dc:subject>\r\n" + 
    		"<dc:subject>ICE CAPS</dc:subject>\r\n" + 
    		"<dc:subject>ICE SHEETS</dc:subject>\r\n" + 
    		"<dc:subject>IMPACTS OF CLIMATE CHANGE</dc:subject>\r\n" + 
    		"<dc:subject>INFORMATION SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>INTERGOVERNMENTAL PANEL ON CLIMATE CHANGE</dc:subject>\r\n" + 
    		"<dc:subject>IPCC</dc:subject>\r\n" + 
    		"<dc:subject>LAND USE</dc:subject>\r\n" + 
    		"<dc:subject>LANDSLIDE</dc:subject>\r\n" + 
    		"<dc:subject>LANDSLIDES</dc:subject>\r\n" + 
    		"<dc:subject>LIGHTING</dc:subject>\r\n" + 
    		"<dc:subject>LOCAL IMPACTS OF CLIMATE CHANGE</dc:subject>\r\n" + 
    		"<dc:subject>LOW-CARBON</dc:subject>\r\n" + 
    		"<dc:subject>METEOROLOGY</dc:subject>\r\n" + 
    		"<dc:subject>METHANE</dc:subject>\r\n" + 
    		"<dc:subject>MITIGATION OF CLIMATE CHANGE</dc:subject>\r\n" + 
    		"<dc:subject>MONSOONS</dc:subject>\r\n" + 
    		"<dc:subject>MOUNTAIN GLACIERS</dc:subject>\r\n" + 
    		"<dc:subject>NATURAL DISASTER</dc:subject>\r\n" + 
    		"<dc:subject>NATURAL DISASTER MITIGATION</dc:subject>\r\n" + 
    		"<dc:subject>NATURAL DISASTER RISK MANAGEMENT</dc:subject>\r\n" + 
    		"<dc:subject>NATURAL DISASTERS</dc:subject>\r\n" + 
    		"<dc:subject>NATURAL HAZARDS</dc:subject>\r\n" + 
    		"<dc:subject>NONGOVERNMENTAL ORGANIZATION</dc:subject>\r\n" + 
    		"<dc:subject>NONGOVERNMENTAL ORGANIZATIONS</dc:subject>\r\n" + 
    		"<dc:subject>OCCURRENCE OF DISASTERS</dc:subject>\r\n" + 
    		"<dc:subject>PRECIPITATION</dc:subject>\r\n" + 
    		"<dc:subject>RAINFALL PATTERNS</dc:subject>\r\n" + 
    		"<dc:subject>RECONSTRUCTION</dc:subject>\r\n" + 
    		"<dc:subject>REDUCING POVERTY</dc:subject>\r\n" + 
    		"<dc:subject>RISK ASSESSMENT</dc:subject>\r\n" + 
    		"<dc:subject>SAFETY</dc:subject>\r\n" + 
    		"<dc:subject>SAVINGS</dc:subject>\r\n" + 
    		"<dc:subject>SEA LEVEL RISE</dc:subject>\r\n" + 
    		"<dc:subject>SEA LEVEL RISE SCENARIOS</dc:subject>\r\n" + 
    		"<dc:subject>SEA WALLS</dc:subject>\r\n" + 
    		"<dc:subject>SEARCH AND RESCUE</dc:subject>\r\n" + 
    		"<dc:subject>SEASON</dc:subject>\r\n" + 
    		"<dc:subject>SEAWATER</dc:subject>\r\n" + 
    		"<dc:subject>SLUM</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL AFFAIRS</dc:subject>\r\n" + 
    		"<dc:subject>STORM</dc:subject>\r\n" + 
    		"<dc:subject>STORM SURGE</dc:subject>\r\n" + 
    		"<dc:subject>STORM SURGES</dc:subject>\r\n" + 
    		"<dc:subject>TECTONIC PLATES</dc:subject>\r\n" + 
    		"<dc:subject>TEMPERATURE</dc:subject>\r\n" + 
    		"<dc:subject>TEMPERATURE CONTROL</dc:subject>\r\n" + 
    		"<dc:subject>TEMPERATURE INCREASES</dc:subject>\r\n" + 
    		"<dc:subject>THUNDERSTORMS</dc:subject>\r\n" + 
    		"<dc:subject>TORNADOS</dc:subject>\r\n" + 
    		"<dc:subject>TROPICAL STORMS</dc:subject>\r\n" + 
    		"<dc:subject>TROPICS</dc:subject>\r\n" + 
    		"<dc:subject>TSUNAMI</dc:subject>\r\n" + 
    		"<dc:subject>TSUNAMIS</dc:subject>\r\n" + 
    		"<dc:subject>TYPHOON</dc:subject>\r\n" + 
    		"<dc:subject>TYPHOONS</dc:subject>\r\n" + 
    		"<dc:subject>UPPER ATMOSPHERE</dc:subject>\r\n" + 
    		"<dc:subject>URBAN FORESTRY</dc:subject>\r\n" + 
    		"<dc:subject>VOLCANIC ERUPTIONS</dc:subject>\r\n" + 
    		"<dc:subject>VOLCANO</dc:subject>\r\n" + 
    		"<dc:subject>VOLCANOES</dc:subject>\r\n" + 
    		"<dc:description>This workbook is intended to help policy\r\n" + 
    		"            makers in developing countries plan for a safer future in\r\n" + 
    		"            urban areas in the face of natural disasters and the\r\n" + 
    		"            consequences of climate change. It is based on the\r\n" + 
    		"            experiences of three cities in Vietnam, Can Tho, Dong Hoi,\r\n" + 
    		"            and Hanoi, that worked with international and local experts\r\n" + 
    		"            under World Bank supervision to develop local resilience\r\n" + 
    		"            action plans (LRAPs) in 2009-10. An LRAP is a detailed\r\n" + 
    		"            planning document that reflects local concerns and\r\n" + 
    		"            priorities based on the experiences of the past and\r\n" + 
    		"            projections for the future. It is not a wish list of\r\n" + 
    		"            projects that may never be completed because they are too\r\n" + 
    		"            costly or lack political support. Rather, it should be a\r\n" + 
    		"            realistic document that describes and establishes priorities\r\n" + 
    		"            for specific steps that can be undertaken in the near term\r\n" + 
    		"            to adapt to both climate related and other hazards.\r\n" + 
    		"            Regardless of their size, location, political orientation,\r\n" + 
    		"            or technical capacity, other cities can learn from the\r\n" + 
    		"            experiences of these pilot cities to develop their own\r\n" + 
    		"            LRAPs. The purpose of this workbook is to adapt the initial\r\n" + 
    		"            experiences of Can Tho, Dong Hoi, and Hanoi to benefit the\r\n" + 
    		"            national government and other communities in Vietnam and\r\n" + 
    		"            beyond. Indeed, the process described in this workbook was\r\n" + 
    		"            later adopted in the cities of Iloilo, the Philippines;\r\n" + 
    		"            Ningbo, China; and Yogyakarta, Indonesia, and the concluding\r\n" + 
    		"            chapter of this workbook draws on some of the lessons\r\n" + 
    		"            learned in these cities. However, the workbook, while\r\n" + 
    		"            generalizable to other contexts, largely reflects the\r\n" + 
    		"            Vietnamese experience.</dc:description>\r\n" + 
    		"<dc:date>2013-05-06T16:55:21Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06T16:55:21Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10986/2235</dc:identifier>\r\n" + 
    		"<dc:rights>CC BY 3.0 Unported</dc:rights>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/</dc:rights>\r\n" + 
    		"<dc:publisher>World Bank</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/130</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:16:53Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>ARC-Lake v1.1 - Per-Lake</dc:title>\r\n" + 
    		"<dc:contributor>ESA - European Space Agency</dc:contributor>\r\n" + 
    		"<dc:subject>Temperature</dc:subject>\r\n" + 
    		"<dc:description>ARC-Lake v1.1 - Per-Lake contains data products on a lake-by-lake basis. These data products contain observations of Lake Surface Water Temperature (LSWT) and Lake Ice Cover (LIC) from the series of (Advanced) Along-Track Scanning Radiometers ((A)ATSRs). ARC-Lake v1.1 data products cover the period from 1st June 1995 to 31st December 2009.&#13;\r\n" + 
    		"&#13;\r\n" + 
    		"A number of different data products are available for each lake and are grouped together into a zip archive for each lake. A summary of the types of data product available is given on http://datashare.is.ed.ac.uk/handle/10283/88 and full details of the file naming convention and file contents are given in the ARC-Lake Data Product Description document (ARCLake_DPD_v1_1_2.pdf). Individual lake archives are grouped into larger zip archives by continent (with the exception of the Caspian Sea). A full listing of each zip archive is provided in the ARC-Lake Data Product File List (ARCLake_DPFL_v1_1_2.pdf). Details of the methods used and a list of all lakes and their locations are given in the ARC-Lake Algorithm Theoretical Basis Document (ARCLake_ATBD_v_1_1.pdf).&#13;\r\n" + 
    		"&#13;\r\n" + 
    		"Additional information about the ARC-Lake project and some basic data analysis tools can be found on the project website: http://www.geos.ed.ac.uk/arclake</dc:description>\r\n" + 
    		"<dc:date>2011-12-08T10:51:44Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:53Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:53Z</dc:date>\r\n" + 
    		"<dc:date>2011-12-08</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>MacCallum, Stuart N; Merchant, Christopher J. (2011). ARC-Lake v1.1 - Per-Lake, 1995-2009 [Dataset]. University of Edinburgh, School of GeoSciences / European Space Agency.</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/130</dc:identifier>\r\n" + 
    		"<dc:language>eng</dc:language>\r\n" + 
    		"<dc:publisher>University of Edinburgh, School of GeoSciences / European Space Agency</dc:publisher>\r\n" + 
    		"<dc:source>Along Track Scanning Radiometer observations: http://badc.nerc.ac.uk/view/neodc.nerc.ac.uk__ATOM__dataent_11954635582925507</dc:source>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:2318/70</identifier>\r\n" + 
    		"                <datestamp>2013-07-09T22:59:21Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_16</setSpec>\r\n" + 
    		"                <setSpec>col_10673_21</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>A Subjective Spin on Roulette Wheels</dc:title>\r\n" + 
    		"<dc:description>We provide a behavioral foundation to the notion of ‘mixture’ of acts, which is used\r\n" + 
    		"to great advantage in the decision setting introduced by Anscombe and Aumann.\r\n" + 
    		"Our construction allows one to formulate mixture-space axioms even in a fully subjective\r\n" + 
    		"setting, without assuming the existence of randomizing devices. This simplifies\r\n" + 
    		"the task of developing axiomatic models which only use behavioral data. Moreover, it\r\n" + 
    		"is immune from the di culty that agents may ‘distort’ the probabilities associated with\r\n" + 
    		"randomizing devices.\r\n" + 
    		"For illustration, we present simple subjective axiomatizations of some models of choice\r\n" + 
    		"under uncertainty, including the maxmin expected utility model of Gilboa and Schmeidler, and Bewley’s model of choice with incomplete preferences.</dc:description>\r\n" + 
    		"<dc:description>International Centre For Economic Research</dc:description>\r\n" + 
    		"<dc:date>2013-07-09T22:59:21Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09T22:59:21Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09</dc:date>\r\n" + 
    		"<dc:type>Articolo</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/2318/70</dc:identifier>\r\n" + 
    		"<dc:relation>Applied Mathematics Working Paper Series</dc:relation>\r\n" + 
    		"<dc:relation>17/2001</dc:relation>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/27</identifier>\r\n" + 
    		"                <datestamp>2013-09-07T02:00:31Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>അക്ബര്‍</dc:title>\r\n" + 
    		"<dc:creator>Test</dc:creator>\r\n" + 
    		"<dc:subject>ഹുമയൂണ്‍</dc:subject>\r\n" + 
    		"<dc:subject>ആഗ്ര</dc:subject>\r\n" + 
    		"<dc:subject>അമര്‍ക്കോട്</dc:subject>\r\n" + 
    		"<dc:description>മുഗള്‍ ചക്രവര്‍ത്തി. അബുല്‍ഫത്ത് ജലാലുദ്ദീന്‍ മുഹമ്മദ് അക്ബര്‍ എന്നായിരുന്നു പൂര്‍ണമായ പേര്. ഹുമയൂണിന്റെയും ഹമീദാബാനുവിന്റെയും പുത്രനായി 1542 ന. 23-ന് (ചൊവ്വാഴ്ച പൌര്‍ണമി രാത്രി) സിന്‍ഡ് മരുഭൂമിയുടെ കിഴക്കന്‍ പ്രാന്തത്തിലുള്ള അമര്‍ക്കോട് നഗരത്തില്‍ ജനിച്ചു. ഹുമയൂണ്‍ തന്റെ പുത്രന് ആദ്യം നല്കിയ പേര്‍ ബഹറുദ്ദീന്‍ (മതപൌര്‍ണമി) മുഹമ്മദ് അക്ബര്‍ എന്നായിരുന്നു. അക്ബര്‍ 'ജന്‍' പട്ടണത്തിലെ ഒരു കൊച്ചു വീട്ടില്‍ 1543 ജൂല. വരെ മാതാവിനോടൊത്തു താമസിച്ചു. കാന്തഹാറിലെത്തിയ ഹുമയൂണിന് അനുജനായ അസ്ക്കാരിയുടെ ശത്രുതമൂലം അക്ബറെ അവിടെ ഉപേക്ഷിച്ച് ഹമീദയോടൊപ്പം രക്ഷപ്പെടേണ്ടിവന്നു. പക്ഷേ അസ്ക്കാരിയുടെ കൊട്ടാരത്തില്‍ അദ്ദേഹത്തിന്റെ ഭാര്യയായ സുല്‍ത്താനാ ബീഗത്തിന്റെ വാത്സല്യപാത്രമാവാന്‍ അക്ബര്‍ക്കു കഴിഞ്ഞു. അടുത്ത കൊല്ലം അക്ബര്‍ മുത്തച്ഛന്റെ സഹോദരിയായ ഖല്‍സാദ് ബീഗത്തിന്റെ സംരക്ഷണയിലായി. അതേകൊല്ലം തന്നെ ഹുമയൂണ്‍ പുത്രസംരക്ഷണം വീണ്ടും ഏറ്റെടുത്തു. ഇതോടുകൂടി ബഹറുദ്ദീന്റെ പേര് 'ജലാലുദ്ദീന്‍' (മതതേജസ്സ്) എന്നുമാറ്റി. ഹുമയൂണിന് പെട്ടെന്നുണ്ടായ രോഗബാധ ശത്രുക്കള്‍ നല്ല ഒരവസരമായി കരുതി. സഹോദരനായ കംറാന്‍ 1546-ല്‍ കാബൂള്‍ പിടിച്ചെടുത്ത് ജലാലുദ്ദീന്‍ അക്ബറെ തടവിലാക്കി; എങ്കിലും 1550-ല്‍ ഹുമയൂണ്‍ പുത്രനെ വീണ്ടെടുത്തു. ഹുമയൂണ്‍ അപകടത്തില്‍പ്പെട്ടു മരിച്ചതോടെ ജലാലുദ്ദീന്‍ അക്ബര്‍ പതിനാലാമത്തെ വയസ്സില്‍ (1556 ഫെ. 14) ഡല്‍ഹി ചക്രവര്‍ത്തിയായി അധികാരമേറ്റു.</dc:description>\r\n" + 
    		"<dc:description>മുഗള്‍ ചക്രവര്‍ത്തി. അബുല്‍ഫത്ത് ജലാലുദ്ദീന്‍ മുഹമ്മദ് അക്ബര്‍( (Akbar) എന്നായിരുന്നു പൂര്‍ണമായ പേര്. ഹുമയൂണിന്റെയും ഹമീദാബാനുവിന്റെയും പുത്രനായി 1542 ന. 23-ന് (ചൊവ്വാഴ്ച പൌര്‍ണമി രാത്രി) സിന്‍ഡ് മരുഭൂമിയുടെ കിഴക്കന്‍ പ്രാന്തത്തിലുള്ള അമര്‍ക്കോട് നഗരത്തില്‍ ജനിച്ചു. ഹുമയൂണ്‍ തന്റെ പുത്രന് ആദ്യം നല്കിയ പേര്‍ ബഹറുദ്ദീന്‍ (മതപൌര്‍ണമി) മുഹമ്മദ് അക്ബര്‍ എന്നായിരുന്നു. അക്ബര്‍ 'ജന്‍' പട്ടണത്തിലെ ഒരു കൊച്ചു വീട്ടില്‍ 1543 ജൂല. വരെ മാതാവിനോടൊത്തു താമസിച്ചു. കാന്തഹാറിലെത്തിയ ഹുമയൂണിന് അനുജനായ അസ്ക്കാരിയുടെ ശത്രുതമൂലം അക്ബറെ അവിടെ ഉപേക്ഷിച്ച് ഹമീദയോടൊപ്പം രക്ഷപ്പെടേണ്ടിവന്നു. പക്ഷേ അസ്ക്കാരിയുടെ കൊട്ടാരത്തില്‍ അദ്ദേഹത്തിന്റെ ഭാര്യയായ സുല്‍ത്താനാ ബീഗത്തിന്റെ വാത്സല്യപാത്രമാവാന്‍ അക്ബര്‍ക്കു കഴിഞ്ഞു. അടുത്ത കൊല്ലം അക്ബര്‍ മുത്തച്ഛന്റെ സഹോദരിയായ ഖല്‍സാദ് ബീഗത്തിന്റെ സംരക്ഷണയിലായി. അതേകൊല്ലം തന്നെ ഹുമയൂണ്‍ പുത്രസംരക്ഷണം വീണ്ടും ഏറ്റെടുത്തു. ഇതോടുകൂടി ബഹറുദ്ദീന്റെ പേര് 'ജലാലുദ്ദീന്‍' (മതതേജസ്സ്) എന്നുമാറ്റി. ഹുമയൂണിന് പെട്ടെന്നുണ്ടായ രോഗബാധ ശത്രുക്കള്‍ നല്ല ഒരവസരമായി കരുതി. സഹോദരനായ കംറാന്‍ 1546-ല്‍ കാബൂള്‍ പിടിച്ചെടുത്ത് ജലാലുദ്ദീന്‍ അക്ബറെ തടവിലാക്കി; എങ്കിലും 1550-ല്‍ ഹുമയൂണ്‍ പുത്രനെ വീണ്ടെടുത്തു. ഹുമയൂണ്‍ അപകടത്തില്‍പ്പെട്ടു മരിച്ചതോടെ ജലാലുദ്ദീന്‍ അക്ബര്‍ പതിനാലാമത്തെ വയസ്സില്‍ (1556 ഫെ. 14) ഡല്‍ഹി ചക്രവര്‍ത്തിയായി അധികാരമേറ്റു.</dc:description>\r\n" + 
    		"<dc:date>2013-09-06T04:25:06Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-06T04:25:06Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-06</dc:date>\r\n" + 
    		"<dc:type>Article</dc:type>\r\n" + 
    		"<dc:identifier>1111</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/27</dc:identifier>\r\n" + 
    		"<dc:language>other</dc:language>\r\n" + 
    		"<dc:rights>http://creativecommons.org/publicdomain/zero/1.0/</dc:rights>\r\n" + 
    		"<dc:rights>CC0 1.0 Universal</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10986/2236</identifier>\r\n" + 
    		"                <datestamp>2013-05-06T16:55:24Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_18</setSpec>\r\n" + 
    		"                <setSpec>col_10673_19</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Skills for the 21st Century in Latin\r\n" + 
    		"            America and the Caribbean</dc:title>\r\n" + 
    		"<dc:subject>ABLE STUDENTS</dc:subject>\r\n" + 
    		"<dc:subject>ACQUISITION OF SKILLS</dc:subject>\r\n" + 
    		"<dc:subject>ADULT POPULATIONS</dc:subject>\r\n" + 
    		"<dc:subject>BASIC SKILLS</dc:subject>\r\n" + 
    		"<dc:subject>CALL</dc:subject>\r\n" + 
    		"<dc:subject>COGNITIVE SKILLS</dc:subject>\r\n" + 
    		"<dc:subject>COLLEGE GRADUATE</dc:subject>\r\n" + 
    		"<dc:subject>CURRICULA</dc:subject>\r\n" + 
    		"<dc:subject>DEMAND FOR EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>DROPOUT RATES</dc:subject>\r\n" + 
    		"<dc:subject>EARLY CHILDHOOD</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATED WORKERS</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATION ATTAINMENT</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATION INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATION LEVEL</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATION LEVELS</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATION OF CHILDREN</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATION PROGRAMS</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATION QUALITY</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATION SPENDING</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATION SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATION SYSTEMS</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL ATTAINMENT</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL ATTAINMENTS</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL CHOICES</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL DECISIONS</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL EXPANSION</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL HISTORY</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATORS</dc:subject>\r\n" + 
    		"<dc:subject>EFFECTS OF EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>ENROLLMENT</dc:subject>\r\n" + 
    		"<dc:subject>EXPANSION OF EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>FAMILY LIFE</dc:subject>\r\n" + 
    		"<dc:subject>FORMAL EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>GENERIC SKILLS</dc:subject>\r\n" + 
    		"<dc:subject>GRADE LEVELS</dc:subject>\r\n" + 
    		"<dc:subject>HIGH SCHOOL</dc:subject>\r\n" + 
    		"<dc:subject>HOUSEHOLD SURVEYS</dc:subject>\r\n" + 
    		"<dc:subject>HUMAN DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>INDEXES</dc:subject>\r\n" + 
    		"<dc:subject>INVESTMENT IN EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>LABOR FORCE</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MARKET</dc:subject>\r\n" + 
    		"<dc:subject>LEADERSHIP</dc:subject>\r\n" + 
    		"<dc:subject>LEARNING</dc:subject>\r\n" + 
    		"<dc:subject>LEARNING ACHIEVEMENT</dc:subject>\r\n" + 
    		"<dc:subject>LEARNING ACHIEVEMENTS</dc:subject>\r\n" + 
    		"<dc:subject>LEARNING NEEDS</dc:subject>\r\n" + 
    		"<dc:subject>LEARNING OUTCOMES</dc:subject>\r\n" + 
    		"<dc:subject>LET</dc:subject>\r\n" + 
    		"<dc:subject>LEVEL OF EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>LEVELS OF EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>LITERACY</dc:subject>\r\n" + 
    		"<dc:subject>LITERACY SKILLS</dc:subject>\r\n" + 
    		"<dc:subject>MATHEMATICS</dc:subject>\r\n" + 
    		"<dc:subject>NEGATIVE EFFECTS</dc:subject>\r\n" + 
    		"<dc:subject>NEW ENTRANTS</dc:subject>\r\n" + 
    		"<dc:subject>NUTRITION</dc:subject>\r\n" + 
    		"<dc:subject>OCCUPATIONS</dc:subject>\r\n" + 
    		"<dc:subject>PAPERS</dc:subject>\r\n" + 
    		"<dc:subject>PARENTAL EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>PARITY</dc:subject>\r\n" + 
    		"<dc:subject>PEER REVIEW</dc:subject>\r\n" + 
    		"<dc:subject>PERFORMANCE IN MATHEMATICS</dc:subject>\r\n" + 
    		"<dc:subject>PERSONALITY</dc:subject>\r\n" + 
    		"<dc:subject>POOR PERFORMANCE</dc:subject>\r\n" + 
    		"<dc:subject>PRIMARY EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>PRIMARY GRADUATES</dc:subject>\r\n" + 
    		"<dc:subject>QUALITY OF EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>READING</dc:subject>\r\n" + 
    		"<dc:subject>RETURNS TO EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>RIGOROUS ANALYSIS</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOL DROPOUT</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOL DROPOUTS</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOL EFFECTS</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOL GRADUATE</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOL GRADUATES</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOL LEVEL</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOLING</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOLS</dc:subject>\r\n" + 
    		"<dc:subject>SECONDARY EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>SECONDARY LEVEL</dc:subject>\r\n" + 
    		"<dc:subject>SECONDARY SCHOOL</dc:subject>\r\n" + 
    		"<dc:subject>SIGNIFICANT CHALLENGES</dc:subject>\r\n" + 
    		"<dc:subject>SKILL TRAINING</dc:subject>\r\n" + 
    		"<dc:subject>SKILLED WORKERS</dc:subject>\r\n" + 
    		"<dc:subject>SKILLED WORKFORCE</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL BEHAVIOR</dc:subject>\r\n" + 
    		"<dc:subject>STUDENT ACHIEVEMENT</dc:subject>\r\n" + 
    		"<dc:subject>STUDENT ASSESSMENT</dc:subject>\r\n" + 
    		"<dc:subject>TEACHERS</dc:subject>\r\n" + 
    		"<dc:subject>TEACHING</dc:subject>\r\n" + 
    		"<dc:subject>TECHNICAL EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>TERTIARY EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>TEST SCORES</dc:subject>\r\n" + 
    		"<dc:subject>TRAINING PROGRAMS</dc:subject>\r\n" + 
    		"<dc:subject>TYPES OF EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>UNIVERSITY EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>VOCATIONAL EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>YOUNG PEOPLE</dc:subject>\r\n" + 
    		"<dc:description>There is growing interest, worldwide, in\r\n" + 
    		"            the link between education systems and the production of\r\n" + 
    		"            skills that are valued in the labor market. With growth\r\n" + 
    		"            stagnating and unemployment soaring in much of the world,\r\n" + 
    		"            educators are being asked to focus more on producing skills\r\n" + 
    		"            that feed into labor productivity and support the\r\n" + 
    		"            sustainable growth of employment and incomes. This timely\r\n" + 
    		"            volume contributes important new findings on the dynamics of\r\n" + 
    		"            education systems and labor market outcomes in Latin America\r\n" + 
    		"            and the Caribbean (LAC). It analyzes an important recent\r\n" + 
    		"            shift in labor market trends in LAC: the first decade of the\r\n" + 
    		"            21st century has witnessed a marked decline in the earnings\r\n" + 
    		"            premia for university and secondary education. This, in\r\n" + 
    		"            turn, is contributing to reduced income inequality across\r\n" + 
    		"            the region. The recent trend contrasts with the sharp rise\r\n" + 
    		"            in tertiary earnings premia that was observed in the 1990s\r\n" + 
    		"            and that helped to reinforce high levels of income\r\n" + 
    		"            inequality in the region at that time. The authors recommend\r\n" + 
    		"            that, having achieved very large increases in secondary and\r\n" + 
    		"            tertiary enrollment, the region should now focus on\r\n" + 
    		"            improving the quality of its education systems and the\r\n" + 
    		"            pertinence of education curricula for the needs of the labor\r\n" + 
    		"            market. At age 15, the learning achievement of the average\r\n" + 
    		"            Latin American student still lags two years behind his or\r\n" + 
    		"            her Organization for Economic Co-operation and Development\r\n" + 
    		"            (OECD) contemporary. The study opens up an important agenda\r\n" + 
    		"            for future research. While the evidence presented on the\r\n" + 
    		"            trends in education earnings premia is clear, the\r\n" + 
    		"            conclusions about the causes and significance of those\r\n" + 
    		"            trends are largely based on suggestive evidence for a\r\n" + 
    		"            limited number of countries, and are not definitive because\r\n" + 
    		"            of data limitations. The findings call for further in-depth\r\n" + 
    		"            analysis of the nature of skill mismatches, to inform\r\n" + 
    		"            policies that can strengthen the region's future\r\n" + 
    		"            economic growth by enhancing the productivity and earnings\r\n" + 
    		"            potential of the workforce.</dc:description>\r\n" + 
    		"<dc:date>2013-05-06T16:55:24Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06T16:55:24Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10986/2236</dc:identifier>\r\n" + 
    		"<dc:relation>Directions in Development ; human development</dc:relation>\r\n" + 
    		"<dc:rights>CC BY 3.0 Unported</dc:rights>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/</dc:rights>\r\n" + 
    		"<dc:publisher>World Bank</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/131</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:16:54Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>ARC-Lake v1.1 - Global</dc:title>\r\n" + 
    		"<dc:contributor>ESA - European Space Agency</dc:contributor>\r\n" + 
    		"<dc:subject>Temperature</dc:subject>\r\n" + 
    		"<dc:description>ARC-Lake v1.1 - Global contains data products with global coverage, i.e. data for all (available) lakes are included in each product. These data products contain observations of Lake Surface Water Temperature (LSWT) and Lake Ice Cover (LIC) from the series of (Advanced) Along-Track Scanning Radiometers ((A)ATSRs). ARC-Lake v1.1 data products cover the period from 1st June 1995 to 31st December 2009.&#13;\r\n" + 
    		"&#13;\r\n" + 
    		"A number of different data products are available and are grouped together into six zip archives, by product type. A summary of the types of data product available is given on http://datashare.is.ed.ac.uk/handle/10283/88 and full details of the file naming convention and file contents are given in the ARC-Lake Data Product Description document (ARCLake_DPD_v1_1_2.pdf). Note that not all types of data product available on a per-lake basis are available as a global product. A full listing of each zip archive is provided in the ARC-Lake Data Product File List (ARCLake_DPFL_v1_1_2.pdf). Details of the methods used and a list of all lakes and their locations are given in the ARC-Lake Algorithm Theoretical Basis Document (ARCLake_ATBD_v_1_1.pdf).&#13;\r\n" + 
    		"&#13;\r\n" + 
    		"Additional information about the ARC-Lake project and some basic data analysis tools can be found on the project website: http://www.geos.ed.ac.uk/arclake/</dc:description>\r\n" + 
    		"<dc:date>2011-12-08T10:53:22Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:54Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:54Z</dc:date>\r\n" + 
    		"<dc:date>2011-12-08</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>MacCallum, Stuart N; Merchant, Christopher J. (2011). ARC-Lake v1.1 - Global, 1995-2009 [Dataset]. University of Edinburgh, School of GeoSciences / European Space Agency.</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/131</dc:identifier>\r\n" + 
    		"<dc:language>eng</dc:language>\r\n" + 
    		"<dc:publisher>University of Edinburgh, School of GeoSciences / European Space Agency</dc:publisher>\r\n" + 
    		"<dc:source>Along Track Scanning Radiometer observations: http://badc.nerc.ac.uk/view/neodc.nerc.ac.uk__ATOM__dataent_11954635582925507</dc:source>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:2318/74</identifier>\r\n" + 
    		"                <datestamp>2013-07-09T22:59:21Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_16</setSpec>\r\n" + 
    		"                <setSpec>col_10673_21</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Optimal Two-Object Auctions with Synergies</dc:title>\r\n" + 
    		"<dc:subject>Auctions</dc:subject>\r\n" + 
    		"<dc:subject>Multi-dimensional Screening</dc:subject>\r\n" + 
    		"<dc:description>We design the revenue-maximizing auction for two goods when each buyer\r\n" + 
    		"has bi-dimensional private information and a superadditive utility function\r\n" + 
    		"(i.e., a synergy is generated if a buyer wins both goods). In this setting the\r\n" + 
    		"seller is likely to allocate the goods ine ciently with respect to an environment\r\n" + 
    		"with no synergies [see Armstrong, RES (2000)]. In particular, if the\r\n" + 
    		"synergy is large then it may occur that a buyer’s valuations for the goods\r\n" + 
    		"weakly dominate the valuations of another buyer and the latter one receives\r\n" + 
    		"the bundle. We link this fact, which contrasts with the results for a setting\r\n" + 
    		"without synergies, to ”non-regular” one-good models.\r\n" + 
    		"Key words: Multiple-unit Auctions, Multi-dimensional Screening, Bundling.\r\n" + 
    		"JEL classification: D44.</dc:description>\r\n" + 
    		"<dc:description>ICER - International Centre For Economic Research</dc:description>\r\n" + 
    		"<dc:date>2013-07-09T22:59:21Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09T22:59:21Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09</dc:date>\r\n" + 
    		"<dc:type>Preprint</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/2318/74</dc:identifier>\r\n" + 
    		"<dc:relation>Applied Mathematics Working Paper Series</dc:relation>\r\n" + 
    		"<dc:relation>18/2001</dc:relation>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/28</identifier>\r\n" + 
    		"                <datestamp>2013-09-07T02:00:37Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>അക്വേറിയം</dc:title>\r\n" + 
    		"<dc:creator>test</dc:creator>\r\n" + 
    		"<dc:subject>ഓഷ്യനേറിയം</dc:subject>\r\n" + 
    		"<dc:subject>ജലപരിസഞ്ചരണം</dc:subject>\r\n" + 
    		"<dc:description>Aquarium&#13;\r\n" + 
    		"വിനോദാര്‍ഥമോ പഠന നിരീക്ഷണാര്‍ഥമോ ഒരു അലങ്കാര സംരംഭം എന്ന നിലയിലോ ജലജന്തുക്കളേയും സസ്യങ്ങളേയും പ്രദര്‍ശിപ്പിക്കുന്നതിന് പ്രത്യേകം സജ്ജമാക്കിയിട്ടുള്ള സംഭരണി/സ്ഥാപനം. അഴകും വര്‍ണവൈവിധ്യവും ആകാരഭംഗിയും ഒത്തിണങ്ങിയ അലങ്കാരമത്സ്യങ്ങളേയും മറ്റു ജലജീവികളേയും ആകര്‍ഷകമായ രീതിയില്‍ പ്രദര്‍ശിപ്പിച്ചു വളര്‍ത്തുന്ന കൃത്രിമസംവിധാനമാണ് ഇത്.</dc:description>\r\n" + 
    		"<dc:date>2013-09-06T09:13:29Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-06T09:13:29Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-06</dc:date>\r\n" + 
    		"<dc:type>Article</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/28</dc:identifier>\r\n" + 
    		"<dc:language>other</dc:language>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10986/2237</identifier>\r\n" + 
    		"                <datestamp>2013-05-06T16:55:27Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_18</setSpec>\r\n" + 
    		"                <setSpec>col_10673_19</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Implementing a National Assessment\r\n" + 
    		"            of Educational Achievement</dc:title>\r\n" + 
    		"<dc:subject>ACHIEVEMENT EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>ACHIEVEMENT IN MATHEMATICS</dc:subject>\r\n" + 
    		"<dc:subject>ACHIEVEMENT STANDARDS</dc:subject>\r\n" + 
    		"<dc:subject>ACHIEVEMENT TESTS</dc:subject>\r\n" + 
    		"<dc:subject>ACHIEVEMENTS</dc:subject>\r\n" + 
    		"<dc:subject>ASSESSMENT AGENCY</dc:subject>\r\n" + 
    		"<dc:subject>ASSESSMENT CENTER</dc:subject>\r\n" + 
    		"<dc:subject>ASSESSMENT DESIGN</dc:subject>\r\n" + 
    		"<dc:subject>ASSESSMENT EXERCISE</dc:subject>\r\n" + 
    		"<dc:subject>ASSESSMENT FRAMEWORK</dc:subject>\r\n" + 
    		"<dc:subject>ASSESSMENT INSTRUMENTS</dc:subject>\r\n" + 
    		"<dc:subject>ASSESSMENT REPORTS</dc:subject>\r\n" + 
    		"<dc:subject>ASSESSMENT TEAMS</dc:subject>\r\n" + 
    		"<dc:subject>BOOK DESIGN</dc:subject>\r\n" + 
    		"<dc:subject>CLASS TEACHER</dc:subject>\r\n" + 
    		"<dc:subject>CLASSROOM</dc:subject>\r\n" + 
    		"<dc:subject>CLASSROOM EXPERIENCE</dc:subject>\r\n" + 
    		"<dc:subject>CLASSROOM PRACTICE</dc:subject>\r\n" + 
    		"<dc:subject>CLASSROOM TEACHERS</dc:subject>\r\n" + 
    		"<dc:subject>CLASSROOMS</dc:subject>\r\n" + 
    		"<dc:subject>COMPLEX TASK</dc:subject>\r\n" + 
    		"<dc:subject>CURRICULUM</dc:subject>\r\n" + 
    		"<dc:subject>DATA ENTRY</dc:subject>\r\n" + 
    		"<dc:subject>DATA PROCESSING</dc:subject>\r\n" + 
    		"<dc:subject>DECISION MAKING</dc:subject>\r\n" + 
    		"<dc:subject>DEMONSTRATION</dc:subject>\r\n" + 
    		"<dc:subject>DISTRICT EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATION AID</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATION AUTHORITIES</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATION MANAGEMENT</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATION MANAGERS</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATION POLICIES</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATION POLICY</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATION QUALITY</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATION SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATION SYSTEMS</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL ACHIEVEMENT</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL ASSESSMENT</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL ASSESSMENTS</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL COMMUNITY</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL EVALUATION</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL FACTORS</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL MANAGEMENT</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL MEASUREMENT</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL POLICY</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL QUALITY</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL RESEARCH</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL STAKEHOLDERS</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL TESTING</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL TESTS</dc:subject>\r\n" + 
    		"<dc:subject>ELEMENTS</dc:subject>\r\n" + 
    		"<dc:subject>ENROLLMENT</dc:subject>\r\n" + 
    		"<dc:subject>ENROLLMENT DATA</dc:subject>\r\n" + 
    		"<dc:subject>ETHNIC GROUPS</dc:subject>\r\n" + 
    		"<dc:subject>EXAMINATION BOARD</dc:subject>\r\n" + 
    		"<dc:subject>EXAMINATION BOARDS</dc:subject>\r\n" + 
    		"<dc:subject>EXAMINATION OFFICIALS</dc:subject>\r\n" + 
    		"<dc:subject>EXAMS</dc:subject>\r\n" + 
    		"<dc:subject>FIELDS OF MATHS</dc:subject>\r\n" + 
    		"<dc:subject>FIRST LANGUAGE</dc:subject>\r\n" + 
    		"<dc:subject>FORMAL TRAINING</dc:subject>\r\n" + 
    		"<dc:subject>GENDER DIFFERENCES</dc:subject>\r\n" + 
    		"<dc:subject>HIGH-STAKES</dc:subject>\r\n" + 
    		"<dc:subject>HIGHER GRADES</dc:subject>\r\n" + 
    		"<dc:subject>HUMAN DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>INDIVIDUAL STUDENTS</dc:subject>\r\n" + 
    		"<dc:subject>INFORMATION TECHNOLOGY</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL STUDIES</dc:subject>\r\n" + 
    		"<dc:subject>LEARNING</dc:subject>\r\n" + 
    		"<dc:subject>LEARNING ACHIEVEMENT</dc:subject>\r\n" + 
    		"<dc:subject>LEARNING OBJECTIVES</dc:subject>\r\n" + 
    		"<dc:subject>LEARNING OUTCOMES</dc:subject>\r\n" + 
    		"<dc:subject>LEVEL OF STUDENT ACHIEVEMENT</dc:subject>\r\n" + 
    		"<dc:subject>LINGUISTIC GROUPS</dc:subject>\r\n" + 
    		"<dc:subject>LITERACY</dc:subject>\r\n" + 
    		"<dc:subject>LOCAL RADIO</dc:subject>\r\n" + 
    		"<dc:subject>MINISTRIES OF EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>MINISTRY OF EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>NATIONAL ASSESSMENT</dc:subject>\r\n" + 
    		"<dc:subject>NATIONAL ASSESSMENT DATA</dc:subject>\r\n" + 
    		"<dc:subject>NATIONAL ASSESSMENT FINDINGS</dc:subject>\r\n" + 
    		"<dc:subject>NATIONAL ASSESSMENTS</dc:subject>\r\n" + 
    		"<dc:subject>NATIONAL EXAMINATIONS</dc:subject>\r\n" + 
    		"<dc:subject>NUMBER OF SCHOOLS</dc:subject>\r\n" + 
    		"<dc:subject>NUMERACY</dc:subject>\r\n" + 
    		"<dc:subject>PRIMARY LEVEL</dc:subject>\r\n" + 
    		"<dc:subject>PRIMARY SCHOOL</dc:subject>\r\n" + 
    		"<dc:subject>PRIMARY SCHOOL TEACHERS</dc:subject>\r\n" + 
    		"<dc:subject>PRIMARY TEACHERS</dc:subject>\r\n" + 
    		"<dc:subject>PRINCIPALS</dc:subject>\r\n" + 
    		"<dc:subject>PRINTING</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE SCHOOLS</dc:subject>\r\n" + 
    		"<dc:subject>PROJECT PLANNING</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC EXAMINATION</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC EXAMINATIONS</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC EXAMS</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC SCHOOLS</dc:subject>\r\n" + 
    		"<dc:subject>QUALITY ASSURANCE</dc:subject>\r\n" + 
    		"<dc:subject>QUALITY CONTROL</dc:subject>\r\n" + 
    		"<dc:subject>QUALITY OF EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>READERS</dc:subject>\r\n" + 
    		"<dc:subject>READING</dc:subject>\r\n" + 
    		"<dc:subject>RECORD OF ATTENDANCE</dc:subject>\r\n" + 
    		"<dc:subject>RELIABILITY</dc:subject>\r\n" + 
    		"<dc:subject>REMOTE SCHOOLS</dc:subject>\r\n" + 
    		"<dc:subject>REPRESENTATIVE SAMPLE</dc:subject>\r\n" + 
    		"<dc:subject>RESEARCH INSTITUTIONS</dc:subject>\r\n" + 
    		"<dc:subject>RURAL POPULATION</dc:subject>\r\n" + 
    		"<dc:subject>RURAL STUDENTS</dc:subject>\r\n" + 
    		"<dc:subject>SALARY INCREASES</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOL DATA</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOL HOURS</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOL INSPECTORS</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOL PERSONNEL</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOL PRINCIPAL</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOL SIZE</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOL SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOL YEAR</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOLING</dc:subject>\r\n" + 
    		"<dc:subject>SCHOOLS</dc:subject>\r\n" + 
    		"<dc:subject>SCIENCE ASSESSMENT</dc:subject>\r\n" + 
    		"<dc:subject>SCIENCE STUDY</dc:subject>\r\n" + 
    		"<dc:subject>SCIENCE TEACHER</dc:subject>\r\n" + 
    		"<dc:subject>SCRIPT</dc:subject>\r\n" + 
    		"<dc:subject>SEATING ARRANGEMENT</dc:subject>\r\n" + 
    		"<dc:subject>SEATING ARRANGEMENTS</dc:subject>\r\n" + 
    		"<dc:subject>SECOND LANGUAGE</dc:subject>\r\n" + 
    		"<dc:subject>SECONDARY SCHOOL</dc:subject>\r\n" + 
    		"<dc:subject>SECONDARY SCHOOL SCIENCE</dc:subject>\r\n" + 
    		"<dc:subject>SECONDARY SCIENCE</dc:subject>\r\n" + 
    		"<dc:subject>SKILL LEVELS</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL COHESION</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL SCIENCES</dc:subject>\r\n" + 
    		"<dc:subject>STATISTICAL ANALYSES</dc:subject>\r\n" + 
    		"<dc:subject>STUDENT ACHIEVEMENT</dc:subject>\r\n" + 
    		"<dc:subject>STUDENT ASSESSMENT</dc:subject>\r\n" + 
    		"<dc:subject>STUDENT LEARNING</dc:subject>\r\n" + 
    		"<dc:subject>STUDENT LEARNING OUTCOMES</dc:subject>\r\n" + 
    		"<dc:subject>STUDENT PARTICIPATION</dc:subject>\r\n" + 
    		"<dc:subject>STUDENT PREPARATION</dc:subject>\r\n" + 
    		"<dc:subject>SUBJECT AREA</dc:subject>\r\n" + 
    		"<dc:subject>SUBJECT MATTER</dc:subject>\r\n" + 
    		"<dc:subject>TEACHER</dc:subject>\r\n" + 
    		"<dc:subject>TEACHER EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>TEACHER EDUCATION INSTITUTIONS</dc:subject>\r\n" + 
    		"<dc:subject>TEACHER EDUCATORS</dc:subject>\r\n" + 
    		"<dc:subject>TEACHER REPRESENTATIVES</dc:subject>\r\n" + 
    		"<dc:subject>TEACHER TRAINERS</dc:subject>\r\n" + 
    		"<dc:subject>TEACHERS</dc:subject>\r\n" + 
    		"<dc:subject>TEACHING</dc:subject>\r\n" + 
    		"<dc:subject>TECHNICAL ASSISTANCE</dc:subject>\r\n" + 
    		"<dc:subject>TECHNICAL SKILLS</dc:subject>\r\n" + 
    		"<dc:subject>TEST ADMINISTRATION</dc:subject>\r\n" + 
    		"<dc:subject>TEST ADMINISTRATORS</dc:subject>\r\n" + 
    		"<dc:subject>TEST DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>TEST RESULTS</dc:subject>\r\n" + 
    		"<dc:subject>TEST SCORES</dc:subject>\r\n" + 
    		"<dc:subject>TEST TAKING</dc:subject>\r\n" + 
    		"<dc:subject>TEXTBOOK</dc:subject>\r\n" + 
    		"<dc:subject>TYPES OF SCHOOLS</dc:subject>\r\n" + 
    		"<dc:subject>UNANNOUNCED VISITS</dc:subject>\r\n" + 
    		"<dc:subject>UNIVERSITIES</dc:subject>\r\n" + 
    		"<dc:subject>UNIVERSITY STUDENTS</dc:subject>\r\n" + 
    		"<dc:subject>VALIDITY</dc:subject>\r\n" + 
    		"<dc:subject>WORK PRACTICES</dc:subject>\r\n" + 
    		"<dc:description>This national assessment of educational\r\n" + 
    		"            achievement series of publications, of which this is the\r\n" + 
    		"            third volume, focuses on state-of-the-art procedures that\r\n" + 
    		"            need to be followed in order to ensure that the data (such\r\n" + 
    		"            as test scores and background information) produced by a\r\n" + 
    		"            large scale national assessment exercise are of high quality\r\n" + 
    		"            and address the concerns of policy makers, decision makers,\r\n" + 
    		"            and other stakeholders in the education system. Measuring\r\n" + 
    		"            student learning outcomes is necessary for monitoring a\r\n" + 
    		"            school system's success and for improving education\r\n" + 
    		"            quality. Student achievement information can be used to\r\n" + 
    		"            inform a wide variety of education policies and decisions,\r\n" + 
    		"            including the design and implementation of programs to\r\n" + 
    		"            improve teaching and learning in classrooms, the\r\n" + 
    		"            identification of lagging students so that they can get the\r\n" + 
    		"            support they need, and the provision of appropriate\r\n" + 
    		"            technical assistance and training where it is most needed.\r\n" + 
    		"            This volume in the national assessments of educational\r\n" + 
    		"            achievement series, it should become evident that the\r\n" + 
    		"            successful implementation of a national assessment exercise\r\n" + 
    		"            is a complex task that requires considerable knowledge,\r\n" + 
    		"            skill, and resources.</dc:description>\r\n" + 
    		"<dc:date>2013-05-06T16:55:27Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06T16:55:27Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10986/2237</dc:identifier>\r\n" + 
    		"<dc:relation>National Assessments of Educational Achievement, Volume 3</dc:relation>\r\n" + 
    		"<dc:rights>CC BY 3.0 Unported</dc:rights>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/</dc:rights>\r\n" + 
    		"<dc:publisher>World Bank</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/132</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:16:55Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>ARC-Lake v1.1 - Ancillary</dc:title>\r\n" + 
    		"<dc:contributor>ESA - European Space Agency</dc:contributor>\r\n" + 
    		"<dc:subject>Temperature</dc:subject>\r\n" + 
    		"<dc:description>ARC-Lake v1.1 - Ancillary contains ancillary data products. This consists of the full resolution land/water mask used to determined where observations of Lake Surface Water Temperature (LSWT) and Lake Ice Cover (LIC) are derived and a reduced resolution version of this mask, corresponding to the resolution of the spatially resolved data products. &#13;\r\n" + 
    		"&#13;\r\n" + 
    		"The methodology used to define this land/water mask is described in the ARC-Lake Algorithm Theoretical Basis Document (ARCLake_ATBD_v_1_1.pdf) and a description of the format of these data files is provided in the ARC-Lake Data Product Description document (ARCLake_DPD_v1_1_2.pdf).&#13;\r\n" + 
    		"&#13;\r\n" + 
    		"Additional information about the ARC-Lake project and some basic data analysis tools can be found on the project website: http://www.geos.ed.ac.uk/arclake/</dc:description>\r\n" + 
    		"<dc:date>2011-12-08T10:53:56Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:55Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:55Z</dc:date>\r\n" + 
    		"<dc:date>2011-12-08</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>MacCallum, Stuart N; Merchant, Christopher J. (2011). ARC-Lake v1.1 - Ancillary, 1995-2009 [Dataset]. University of Edinburgh, School of GeoSciences / European Space Agency.</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/132</dc:identifier>\r\n" + 
    		"<dc:language>eng</dc:language>\r\n" + 
    		"<dc:publisher>University of Edinburgh, School of GeoSciences / European Space Agency</dc:publisher>\r\n" + 
    		"<dc:source>Along Track Scanning Radiometer observations: http://badc.nerc.ac.uk/view/neodc.nerc.ac.uk__ATOM__dataent_11954635582925507</dc:source>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:2318/65</identifier>\r\n" + 
    		"                <datestamp>2013-07-09T22:59:22Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_16</setSpec>\r\n" + 
    		"                <setSpec>col_10673_21</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Bayesian inference via classes of normalized random measures</dc:title>\r\n" + 
    		"<dc:subject>Bayesian Nonparametrics</dc:subject>\r\n" + 
    		"<dc:subject>Chinese restaurant process</dc:subject>\r\n" + 
    		"<dc:subject>Generalized gamma convolutions</dc:subject>\r\n" + 
    		"<dc:subject>Gibbs partitions</dc:subject>\r\n" + 
    		"<dc:subject>Poisson random measure</dc:subject>\r\n" + 
    		"<dc:description>One of the main research areas in Bayesian Nonparametrics is the proposal and study of priors which generalize the Dirichlet process. Here we exploit theoretical properties of Poisson random measures in order to provide a comprehensive Bayesian analysis of random probabilities which are obtained by an appropriate normalization. Specifically we achieve explicit and tractable forms of the posterior and the marginal distributions, including an explicit and easily used description of generalizations of the important Blackwell-MacQueen P´olya urn distribution. Such simplifications are achieved by the use of a latent variable which admits quite interesting interpretations which allow to gain a better understanding of the behaviour of these random probability measures. It is noteworthy that these models are generalizations of models considered by Kingman (1975) in a non-Bayesian context. Such models are known to play a significant role in a variety of applications including genetics, physics, and work involving random mappings and assemblies. Hence our analysis is of utility in those contexts as well. We also show how our results may be applied to Bayesian mixture models and describe computational schemes which are generalizations of known efficient methods for the case of the Dirichlet process. We illustrate new examples of processes which can play the role of priors for Bayesian nonparametric inference and finally point out some interesting connections with the theory of generalized gamma convolutions initiated by Thorin and further developed by Bondesson.</dc:description>\r\n" + 
    		"<dc:description>ICER - International Center for Economic Research</dc:description>\r\n" + 
    		"<dc:date>2013-07-09T22:59:22Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09T22:59:22Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09</dc:date>\r\n" + 
    		"<dc:type>Preprint</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/2318/65</dc:identifier>\r\n" + 
    		"<dc:relation>Applied Mathematics Working Papers Series</dc:relation>\r\n" + 
    		"<dc:relation>5/2005</dc:relation>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10986/2238</identifier>\r\n" + 
    		"                <datestamp>2013-05-06T16:55:28Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_18</setSpec>\r\n" + 
    		"                <setSpec>col_10673_19</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>The Status of the Education Sector\r\n" + 
    		"            in Sudan</dc:title>\r\n" + 
    		"<dc:subject>Secondary eduction</dc:subject>\r\n" + 
    		"<dc:subject>Basic education</dc:subject>\r\n" + 
    		"<dc:subject>economic growth</dc:subject>\r\n" + 
    		"<dc:subject>education financing</dc:subject>\r\n" + 
    		"<dc:subject>education planning</dc:subject>\r\n" + 
    		"<dc:subject>higher education</dc:subject>\r\n" + 
    		"<dc:subject>human development</dc:subject>\r\n" + 
    		"<dc:subject>labor market</dc:subject>\r\n" + 
    		"<dc:subject>millennium development goals</dc:subject>\r\n" + 
    		"<dc:subject>school enrollment</dc:subject>\r\n" + 
    		"<dc:description>This publication is the first\r\n" + 
    		"            comprehensive overview of the education sector in Sudan. The\r\n" + 
    		"            challenge that remains is to design policy responses to the\r\n" + 
    		"            issues identified within the forthcoming education sector\r\n" + 
    		"            strategic plan. More important, these policies-already being\r\n" + 
    		"            discussed with the Ministry of General Education (MoGE) must\r\n" + 
    		"            be effectively implemented so that Sudan can make faster\r\n" + 
    		"            progress toward achieving the Education for All (EFA)\r\n" + 
    		"            targets and Millennium Development Goal's (MDGs). It is\r\n" + 
    		"            my hope that this report will serve as the basis for an\r\n" + 
    		"            evidence-based and equity oriented approach to education\r\n" + 
    		"            planning and investment. This approach will have positive\r\n" + 
    		"            repercussions for overall economic growth, poverty\r\n" + 
    		"            alleviation, and human development in 21st-century Sudan.\r\n" + 
    		"            This report was prepared in collaboration with a national\r\n" + 
    		"            team from the MoGE and partners active in the education\r\n" + 
    		"            sector in Sudan. Over a period of 18 months, this\r\n" + 
    		"            collaboration facilitated considerable capacity building in\r\n" + 
    		"            data collection and analysis, as well as regular\r\n" + 
    		"            dissemination of the analysis to a wider audience.</dc:description>\r\n" + 
    		"<dc:date>2013-05-06T16:55:28Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06T16:55:28Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10986/2238</dc:identifier>\r\n" + 
    		"<dc:relation>Africa Human Development</dc:relation>\r\n" + 
    		"<dc:rights>CC BY 3.0 Unported</dc:rights>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/</dc:rights>\r\n" + 
    		"<dc:publisher>World Bank</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/128</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:16:55Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>International Stroke Trial database (version 2)</dc:title>\r\n" + 
    		"<dc:contributor/>\r\n" + 
    		"<dc:contributor>MRC - Medical Research Council</dc:contributor>\r\n" + 
    		"<dc:subject>Clinical trials, stroke, RCTs, randomized controlled trials</dc:subject>\r\n" + 
    		"<dc:description>The International Stroke Trial (IST) was one the biggest randomised trials in acute stroke. Methods: Available data on variables assessed at randomisation, at the early outcome point (14-days after randomisation or prior discharge) and at 6-months were extracted and made publically available. Results and Conclusions: The IST provides an excellent source of primary data easy-to-use for sample size calculations and preliminary analysis necessary for planning a good quality trial.</dc:description>\r\n" + 
    		"<dc:date>2011-11-02T14:09:46Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:55Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:55Z</dc:date>\r\n" + 
    		"<dc:date>2011-11-02</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>Sandercock, Peter; Niewada, Maciej; Czlonkowska, Anna. (2011). International Stroke Trial database (version 2), [Dataset]. University of Edinburgh, Department of Clinical Neurosciences.</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/128</dc:identifier>\r\n" + 
    		"<dc:language>eng</dc:language>\r\n" + 
    		"<dc:relation>International Stroke Trial database</dc:relation>\r\n" + 
    		"<dc:relation>http://hdl.handle.net/10283/46</dc:relation>\r\n" + 
    		"<dc:publisher>University of Edinburgh, Department of Clinical Neurosciences</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:2318/67</identifier>\r\n" + 
    		"                <datestamp>2013-07-09T22:59:22Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_16</setSpec>\r\n" + 
    		"                <setSpec>col_10673_21</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>On rates of convergence for posterior distributions</dc:title>\r\n" + 
    		"<dc:subject>Hellinger consistency</dc:subject>\r\n" + 
    		"<dc:subject>Mixture of Dirichlet process</dc:subject>\r\n" + 
    		"<dc:subject>Posterior distribution</dc:subject>\r\n" + 
    		"<dc:subject>Rates of convergence</dc:subject>\r\n" + 
    		"<dc:description>This paper introduces a new approach to the study of rates of convergence for posterior distributions. It's a natural extension of a recent approach to the study of Bayesian consistency. Crucially, no sieve or entropy measures are required and so rates do not depend on the rate of convergence of the corresponding sieve maximum likelihood estimator. In particular, we improve on current rates for mixture models.</dc:description>\r\n" + 
    		"<dc:description>ICER - International Centre for Economic Research</dc:description>\r\n" + 
    		"<dc:date>2013-07-09T22:59:22Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09T22:59:22Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09</dc:date>\r\n" + 
    		"<dc:type>Preprint</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/2318/67</dc:identifier>\r\n" + 
    		"<dc:relation>Applied Mathematics Working Papers Series</dc:relation>\r\n" + 
    		"<dc:relation>24/2004</dc:relation>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10459.3/60</identifier>\r\n" + 
    		"                <datestamp>2013-07-29T11:25:08Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_10</setSpec>\r\n" + 
    		"                <setSpec>col_10673_13</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Historia literaria : ensayo</dc:title>\r\n" + 
    		"<dc:subject>Castellà -- Història</dc:subject>\r\n" + 
    		"<dc:description>2 v.; 19 cm -- Conté: 1. Literaturas extranjeras; 2. Literatura española</dc:description>\r\n" + 
    		"<dc:date>2012-07-01T14:53:46Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-29T11:25:08Z</dc:date>\r\n" + 
    		"<dc:date>2012-07-01T14:53:46Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-29T11:25:08Z</dc:date>\r\n" + 
    		"<dc:date>2012</dc:date>\r\n" + 
    		"<dc:date>1902</dc:date>\r\n" + 
    		"<dc:type>Text</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10459.3/60</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10459</dc:identifier>\r\n" + 
    		"<dc:language>es</dc:language>\r\n" + 
    		"<dc:relation>http://cataleg.udl.cat/record=b1066262~S11*cat</dc:relation>\r\n" + 
    		"<dc:rights>Còpia permesa amb finalitat d'estudi o recerca, citant l'autor i la font: \"Fons Gili i Gaya / Universitat de Lleida\". Per a qualsevol altre ús cal demanar autorització.</dc:rights>\r\n" + 
    		"<dc:publisher>Universitat de Lleida</dc:publisher>\r\n" + 
    		"<dc:source>Publicació original: Madrid : Antonio Marzo, 1902</dc:source>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10986/2240</identifier>\r\n" + 
    		"                <datestamp>2013-05-06T16:55:31Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_18</setSpec>\r\n" + 
    		"                <setSpec>col_10673_19</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Making Health Financing Work for\r\n" + 
    		"            Poor People in Tanzania</dc:title>\r\n" + 
    		"<dc:subject>ACCESS TO HEALTH CARE</dc:subject>\r\n" + 
    		"<dc:subject>ACCESS TO HEALTH CARE SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>ACCESS TO HEALTH SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>ACCESS TO SAFE WATER</dc:subject>\r\n" + 
    		"<dc:subject>ACCESS TO SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>AGED</dc:subject>\r\n" + 
    		"<dc:subject>ALLOCATION</dc:subject>\r\n" + 
    		"<dc:subject>AUDIT OFFICE</dc:subject>\r\n" + 
    		"<dc:subject>AUDITOR GENERAL</dc:subject>\r\n" + 
    		"<dc:subject>BASIC SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>BENEFICIARIES</dc:subject>\r\n" + 
    		"<dc:subject>BIRTH RATE</dc:subject>\r\n" + 
    		"<dc:subject>BUDGET BALANCE</dc:subject>\r\n" + 
    		"<dc:subject>BUDGET EXECUTION</dc:subject>\r\n" + 
    		"<dc:subject>BUDGET RESOURCES</dc:subject>\r\n" + 
    		"<dc:subject>CAPITATION</dc:subject>\r\n" + 
    		"<dc:subject>CENTRAL GOVERNMENT</dc:subject>\r\n" + 
    		"<dc:subject>CHILD HEALTH</dc:subject>\r\n" + 
    		"<dc:subject>CITIES</dc:subject>\r\n" + 
    		"<dc:subject>CIVIL SERVANTS</dc:subject>\r\n" + 
    		"<dc:subject>COMMODITY PRICES</dc:subject>\r\n" + 
    		"<dc:subject>COMMUNITY HEALTH</dc:subject>\r\n" + 
    		"<dc:subject>CONTRACTUAL OBLIGATIONS</dc:subject>\r\n" + 
    		"<dc:subject>CONTRIBUTION RATES</dc:subject>\r\n" + 
    		"<dc:subject>COST CONTROL</dc:subject>\r\n" + 
    		"<dc:subject>DEATH RATE</dc:subject>\r\n" + 
    		"<dc:subject>DEATHS</dc:subject>\r\n" + 
    		"<dc:subject>DEBT</dc:subject>\r\n" + 
    		"<dc:subject>DEBT RELIEF</dc:subject>\r\n" + 
    		"<dc:subject>DECENTRALIZATION</dc:subject>\r\n" + 
    		"<dc:subject>DEMAND FOR HEALTH</dc:subject>\r\n" + 
    		"<dc:subject>DEMAND FOR HEALTH SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>DISPENSARIES</dc:subject>\r\n" + 
    		"<dc:subject>DOMESTIC BORROWING</dc:subject>\r\n" + 
    		"<dc:subject>DOMESTIC REVENUE</dc:subject>\r\n" + 
    		"<dc:subject>DONOR FUNDS</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>EFFICIENCY GAINS</dc:subject>\r\n" + 
    		"<dc:subject>EFFICIENCY IMPROVEMENTS</dc:subject>\r\n" + 
    		"<dc:subject>EXCHANGE RATE</dc:subject>\r\n" + 
    		"<dc:subject>EXPENDITURE MANAGEMENT</dc:subject>\r\n" + 
    		"<dc:subject>EXTERNAL FINANCING</dc:subject>\r\n" + 
    		"<dc:subject>FEE-FOR-SERVICE</dc:subject>\r\n" + 
    		"<dc:subject>FEE-FOR-SERVICE BASIS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL CRISIS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL EFFECTS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL IMPACT</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL IMPLICATIONS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL INTERMEDIATION</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL RESOURCES</dc:subject>\r\n" + 
    		"<dc:subject>FINANCING OF HEALTH CARE</dc:subject>\r\n" + 
    		"<dc:subject>FISCAL POLICY</dc:subject>\r\n" + 
    		"<dc:subject>FISCAL TRANSFERS</dc:subject>\r\n" + 
    		"<dc:subject>GENERAL BUDGET SUPPORT</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT BUDGET</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT BUDGETS</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT EXPENDITURE</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT EXPENDITURES</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT SPENDING</dc:subject>\r\n" + 
    		"<dc:subject>GROSS DOMESTIC PRODUCT</dc:subject>\r\n" + 
    		"<dc:subject>GROWTH RATE</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH CARE</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH CARE CENTERS</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH CARE FACILITIES</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH CARE FINANCING</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH CARE PROVIDERS</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH CARE SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH CARE SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH CARE USE</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH CENTERS</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH EXPENDITURE</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH EXPENDITURES</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH FACILITIES</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH FINANCING</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH FINANCING REFORM</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH FINANCING SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH INSURANCE</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH INSURANCE COVERAGE</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH INSURANCE MARKET</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH INSURANCE SCHEME</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH INSURANCE SCHEMES</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH INTERVENTIONS</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH INVESTMENTS</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH MANAGEMENT</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH MINISTRY</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH ORGANIZATION</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH OUTCOMES</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH POLICY</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH REFORMS</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH SERVICE</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH SERVICE DELIVERY</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH STATUS</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH SYSTEM STRENGTHENING</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH SYSTEMS</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH SYSTEMS STRENGTHENING</dc:subject>\r\n" + 
    		"<dc:subject>HEALTH WORKERS</dc:subject>\r\n" + 
    		"<dc:subject>HIV/AIDS</dc:subject>\r\n" + 
    		"<dc:subject>HOSPITAL CARE</dc:subject>\r\n" + 
    		"<dc:subject>HOSPITAL SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>HOSPITALS</dc:subject>\r\n" + 
    		"<dc:subject>HOUSEHOLD EXPENDITURE</dc:subject>\r\n" + 
    		"<dc:subject>HUMAN DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>HUMAN RESOURCES</dc:subject>\r\n" + 
    		"<dc:subject>INCIDENCE ANALYSIS</dc:subject>\r\n" + 
    		"<dc:subject>INCOME</dc:subject>\r\n" + 
    		"<dc:subject>INCOME DISTRIBUTION</dc:subject>\r\n" + 
    		"<dc:subject>INCOME GROUPS</dc:subject>\r\n" + 
    		"<dc:subject>INFANT MORTALITY</dc:subject>\r\n" + 
    		"<dc:subject>INFANT MORTALITY RATE</dc:subject>\r\n" + 
    		"<dc:subject>INFLATION</dc:subject>\r\n" + 
    		"<dc:subject>INFLATIONARY PRESSURES</dc:subject>\r\n" + 
    		"<dc:subject>INFORMAL PAYMENTS</dc:subject>\r\n" + 
    		"<dc:subject>INFORMAL SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>INFORMAL SECTOR WORKERS</dc:subject>\r\n" + 
    		"<dc:subject>INFORMATION SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>INPATIENT CARE</dc:subject>\r\n" + 
    		"<dc:subject>INSURANCE</dc:subject>\r\n" + 
    		"<dc:subject>INSURANCE COVERAGE</dc:subject>\r\n" + 
    		"<dc:subject>INSURANCE PLAN</dc:subject>\r\n" + 
    		"<dc:subject>INSURERS</dc:subject>\r\n" + 
    		"<dc:subject>LIFE EXPECTANCY</dc:subject>\r\n" + 
    		"<dc:subject>LIQUIDITY</dc:subject>\r\n" + 
    		"<dc:subject>LIVING STANDARDS</dc:subject>\r\n" + 
    		"<dc:subject>LOCAL GOVERNMENT BUDGETS</dc:subject>\r\n" + 
    		"<dc:subject>MACROECONOMIC STABILITY</dc:subject>\r\n" + 
    		"<dc:subject>MEDICAL BENEFIT</dc:subject>\r\n" + 
    		"<dc:subject>MEDICAL EQUIPMENT</dc:subject>\r\n" + 
    		"<dc:subject>MEDICAL FACILITIES</dc:subject>\r\n" + 
    		"<dc:subject>MEDICAL INSURANCE</dc:subject>\r\n" + 
    		"<dc:subject>MEDICAL SUPPLIES</dc:subject>\r\n" + 
    		"<dc:subject>MEDICAL TREATMENT</dc:subject>\r\n" + 
    		"<dc:subject>MINISTRY OF FINANCE</dc:subject>\r\n" + 
    		"<dc:subject>MORTALITY</dc:subject>\r\n" + 
    		"<dc:subject>NATIONAL AUDIT</dc:subject>\r\n" + 
    		"<dc:subject>NATIONAL BUDGET</dc:subject>\r\n" + 
    		"<dc:subject>NATIONAL EXPENDITURE</dc:subject>\r\n" + 
    		"<dc:subject>NATIONAL HEALTH</dc:subject>\r\n" + 
    		"<dc:subject>NATIONAL HEALTH INSURANCE</dc:subject>\r\n" + 
    		"<dc:subject>NATIONAL HEALTH INSURANCE FUND</dc:subject>\r\n" + 
    		"<dc:subject>NATIONAL INCOME</dc:subject>\r\n" + 
    		"<dc:subject>NONGOVERNMENTAL ORGANIZATIONS</dc:subject>\r\n" + 
    		"<dc:subject>NURSES</dc:subject>\r\n" + 
    		"<dc:subject>OBSTETRICAL CARE</dc:subject>\r\n" + 
    		"<dc:subject>OIL PRICES</dc:subject>\r\n" + 
    		"<dc:subject>OUTPATIENT CARE</dc:subject>\r\n" + 
    		"<dc:subject>OUTPATIENT SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>PATIENTS</dc:subject>\r\n" + 
    		"<dc:subject>PERSONAL INCOME</dc:subject>\r\n" + 
    		"<dc:subject>PERSONAL INCOME TAX</dc:subject>\r\n" + 
    		"<dc:subject>PHYSICIANS</dc:subject>\r\n" + 
    		"<dc:subject>POCKET PAYMENTS</dc:subject>\r\n" + 
    		"<dc:subject>POVERTY IMPACT</dc:subject>\r\n" + 
    		"<dc:subject>POVERTY REDUCTION</dc:subject>\r\n" + 
    		"<dc:subject>PREPAYMENT MECHANISMS</dc:subject>\r\n" + 
    		"<dc:subject>PREPAYMENT SCHEMES</dc:subject>\r\n" + 
    		"<dc:subject>PRIMARY CARE</dc:subject>\r\n" + 
    		"<dc:subject>PRIMARY HEALTH CARE</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE HEALTH INSURANCE</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE INSURANCE</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE PHARMACIES</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE PROVIDERS</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE SPENDING</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC ADMINISTRATION</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC EXPENDITURE</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC FINANCING MECHANISMS</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC FUNDING</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC HEALTH</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC HEALTH EXPENDITURES</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC HEALTH SPENDING</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC PROVIDERS</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC SPENDING</dc:subject>\r\n" + 
    		"<dc:subject>QUALITY CONTROL</dc:subject>\r\n" + 
    		"<dc:subject>QUALITY OF HEALTH</dc:subject>\r\n" + 
    		"<dc:subject>QUALITY OF HEALTH CARE</dc:subject>\r\n" + 
    		"<dc:subject>REGIONAL ADMINISTRATION</dc:subject>\r\n" + 
    		"<dc:subject>REGULATORY AUTHORITY</dc:subject>\r\n" + 
    		"<dc:subject>REGULATORY FRAMEWORK</dc:subject>\r\n" + 
    		"<dc:subject>REGULATORY SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>SERVICE DEMAND</dc:subject>\r\n" + 
    		"<dc:subject>SERVICE PROVISION</dc:subject>\r\n" + 
    		"<dc:subject>SERVICE QUALITY</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL HEALTH INSURANCE</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL INDICATORS</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL POLICY</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL PROGRAMS</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL PROTECTION</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL SECURITY</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL WELFARE</dc:subject>\r\n" + 
    		"<dc:subject>STATED OBJECTIVE</dc:subject>\r\n" + 
    		"<dc:subject>TAX REVENUES</dc:subject>\r\n" + 
    		"<dc:subject>TUBERCULOSIS</dc:subject>\r\n" + 
    		"<dc:subject>UNDER-FIVE MORTALITY</dc:subject>\r\n" + 
    		"<dc:subject>UNEMPLOYMENT</dc:subject>\r\n" + 
    		"<dc:subject>USE OF HEALTH SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>WALKING</dc:subject>\r\n" + 
    		"<dc:subject>WORKERS</dc:subject>\r\n" + 
    		"<dc:description>This policy note is designed to support\r\n" + 
    		"            the development of the health financing strategy in\r\n" + 
    		"            Tanzania. It is directed at decision makers in the areas of\r\n" + 
    		"            health and social policy as well as the Ministry of Finance,\r\n" + 
    		"            which will play a crucial role in integrating the financial\r\n" + 
    		"            implications of this note into the overall fiscal situation\r\n" + 
    		"            in Tanzania. It is also hoped that this note will stimulate\r\n" + 
    		"            debate among interested stakeholders on the best funding\r\n" + 
    		"            modalities for health and the most appropriate ways to\r\n" + 
    		"            integrate those modalities. On the basis of the data and\r\n" + 
    		"            options described in this policy note, the World Bank will\r\n" + 
    		"            work with authorities and other interested stakeholders to\r\n" + 
    		"            develop a financing program to support the needed reforms in\r\n" + 
    		"            these sectors. This policy note provides background\r\n" + 
    		"            information, cross-country examples, and policy options,\r\n" + 
    		"            which can all be incorporated into the development of a\r\n" + 
    		"            comprehensive health financing strategy. It also provides a\r\n" + 
    		"            framework for looking at the various elements of the health\r\n" + 
    		"            financing system, and it explores the financial, economic,\r\n" + 
    		"            and health system implications of a number of the options.</dc:description>\r\n" + 
    		"<dc:date>2013-05-06T16:55:31Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06T16:55:31Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10986/2240</dc:identifier>\r\n" + 
    		"<dc:relation>World Bank Study</dc:relation>\r\n" + 
    		"<dc:rights>CC BY 3.0 Unported</dc:rights>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/</dc:rights>\r\n" + 
    		"<dc:publisher>World Bank</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/123</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:16:56Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>OpenUrl router requests by date</dc:title>\r\n" + 
    		"<dc:contributor>JISC - Joint Information Systems Committee</dc:contributor>\r\n" + 
    		"<dc:subject>openurl</dc:subject>\r\n" + 
    		"<dc:description>The data in this file was based on the OpenURL Router data available from http://openurl.ac.uk/doc/data/data.html. It was developed by EDINA as part of a JISC-funded project, more information on the project is available at http://edina.ac.uk/projects/Using_OpenURL_Activity_data_summary.html</dc:description>\r\n" + 
    		"<dc:date>2011-10-31T12:22:27Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:56Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:56Z</dc:date>\r\n" + 
    		"<dc:date>2011-10-31</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>Sferopoulos, Dimitrios. (2011). OpenUrl router requests by date, 2011 [Dataset]. Edina, University of Edinburgh.</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/123</dc:identifier>\r\n" + 
    		"<dc:language>eng</dc:language>\r\n" + 
    		"<dc:publisher>Edina, University of Edinburgh</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:2318/69</identifier>\r\n" + 
    		"                <datestamp>2013-07-09T22:59:22Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_16</setSpec>\r\n" + 
    		"                <setSpec>col_10673_21</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>On consistency of nonparametric normal mixtures</dc:title>\r\n" + 
    		"<dc:subject>Bayesian nonparametrics</dc:subject>\r\n" + 
    		"<dc:subject>Density estimation</dc:subject>\r\n" + 
    		"<dc:subject>Mixture of Dirichlet process</dc:subject>\r\n" + 
    		"<dc:subject>Normal mixture model</dc:subject>\r\n" + 
    		"<dc:subject>Random discrete distribution</dc:subject>\r\n" + 
    		"<dc:subject>Strong consistency</dc:subject>\r\n" + 
    		"<dc:description>The past decade has seen a remarkable development in the area of Bayesian nonparametric inference both from a theoretical and applied perspective. As for the latter, the celebrated Dirichlet process has been successfully exploited within Bayesian mixture models leading to many interesting applications. As for the former, some new discrete nonparametric priors have been recently proposed in the literature: their natural use is as alternatives to the Dirichlet process in a Bayesian hierarchical model for density estimation. When using such models for concrete applications, an investigation of their statistical properties is mandatory. Among them a prominent role is to be assigned to consistency. Indeed, strong consistency of Bayesian nonparametric procedures for density estimation has been the focus of a considerable amount of research and, in particular, much attention has been devoted to the normal mixture of Dirichlet process. In this paper we improve on previous contributions by establishing strong consistency of the mixture of Dirichlet process under fairly general conditions: besides the usual Kullback–Leibler support condition, consistency is achieved by finiteness of the mean of the base measure of the Dirichlet process and an exponential decay of the prior on the standard deviation. We show that the same conditions are sufficient for mixtures based on priors more general than the Dirichlet process as well. This leads to the easy establishment of consistency for many recently proposed mixture models.</dc:description>\r\n" + 
    		"<dc:description>ICER - International Centre for Economic Research</dc:description>\r\n" + 
    		"<dc:date>2013-07-09T22:59:22Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09T22:59:22Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09</dc:date>\r\n" + 
    		"<dc:type>Preprint</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/2318/69</dc:identifier>\r\n" + 
    		"<dc:relation>Applied Mathematics Working Papers Series</dc:relation>\r\n" + 
    		"<dc:relation>23/2004</dc:relation>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10459.3/61</identifier>\r\n" + 
    		"                <datestamp>2013-07-29T11:25:08Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_10</setSpec>\r\n" + 
    		"                <setSpec>col_10673_13</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Mots grecs : groupés d'après la forme et le sens, Les</dc:title>\r\n" + 
    		"<dc:subject>Grec -- Gramàtica</dc:subject>\r\n" + 
    		"<dc:description>95 p.; 18 cm</dc:description>\r\n" + 
    		"<dc:date>2012-07-01T15:01:08Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-29T11:25:08Z</dc:date>\r\n" + 
    		"<dc:date>2012-07-01T15:01:08Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-29T11:25:08Z</dc:date>\r\n" + 
    		"<dc:date>2012</dc:date>\r\n" + 
    		"<dc:date>1901</dc:date>\r\n" + 
    		"<dc:type>Text</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10459.3/61</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10459</dc:identifier>\r\n" + 
    		"<dc:language>fr</dc:language>\r\n" + 
    		"<dc:relation>http://cataleg.udl.cat/record=b1067065~S11*cat</dc:relation>\r\n" + 
    		"<dc:rights>Còpia permesa amb finalitat d'estudi o recerca, citant l'autor i la font: \"Fons Gili i Gaya / Universitat de Lleida\". Per a qualsevol altre ús cal demanar autorització.</dc:rights>\r\n" + 
    		"<dc:publisher>Universitat de Lleida</dc:publisher>\r\n" + 
    		"<dc:source>Publicació original: Paris : Librairie Hachette, 1901</dc:source>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/30</identifier>\r\n" + 
    		"                <datestamp>2013-08-25T02:13:03Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_9</setSpec>\r\n" + 
    		"                <setSpec>col_10673_10</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>O debate historiográfico em torno da “crise do Antigo Sistema Colonial” - parte 3</dc:title>\r\n" + 
    		"<dc:subject>história, história do brasil colônia, colonial, motim, sedição, contestação política</dc:subject>\r\n" + 
    		"<dc:description/>\r\n" + 
    		"<dc:date>2013-08-25T02:13:03Z</dc:date>\r\n" + 
    		"<dc:date>2013-08-25T02:13:03Z</dc:date>\r\n" + 
    		"<dc:date>2013-08-25</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/30</dc:identifier>\r\n" + 
    		"<dc:relation>http://eaulas.usp.br/portal/video.action?idItem=2772</dc:relation>\r\n" + 
    		"<dc:relation>História do Brasil Colonial II</dc:relation>\r\n" + 
    		"<dc:relation>FLH0242 -1</dc:relation>\r\n" + 
    		"<dc:publisher>Faculdade de Filosofia, Letras e Ciências Humanas</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/32</identifier>\r\n" + 
    		"                <datestamp>2013-09-07T02:00:39Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Hearing device</dc:title>\r\n" + 
    		"<dc:creator>unknown, Mr.</dc:creator>\r\n" + 
    		"<dc:description>Photograph of early hearing device.</dc:description>\r\n" + 
    		"<dc:description>Unknown image</dc:description>\r\n" + 
    		"<dc:date>2013-09-06T17:50:52Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-06T17:50:52Z</dc:date>\r\n" + 
    		"<dc:date>1943-04</dc:date>\r\n" + 
    		"<dc:type>Image</dc:type>\r\n" + 
    		"<dc:identifier>Army records</dc:identifier>\r\n" + 
    		"<dc:identifier>nc11.1893</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/32</dc:identifier>\r\n" + 
    		"<dc:language>en_US</dc:language>\r\n" + 
    		"<dc:relation>nc11.1893;</dc:relation>\r\n" + 
    		"<dc:rights>http://creativecommons.org/publicdomain/zero/1.0/</dc:rights>\r\n" + 
    		"<dc:rights>CC0 1.0 Universal</dc:rights>\r\n" + 
    		"<dc:publisher>unknown</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/29</identifier>\r\n" + 
    		"                <datestamp>2013-09-10T12:53:52Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Prueba versionado de items</dc:title>\r\n" + 
    		"<dc:creator>Apellido, Nombre</dc:creator>\r\n" + 
    		"<dc:description>Resumen 2</dc:description>\r\n" + 
    		"<dc:date>2013-09-10T12:50:27Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-10T12:46:50Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-10T12:50:27Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-10</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/123456789/29</dc:identifier>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10986/2241</identifier>\r\n" + 
    		"                <datestamp>2013-05-06T16:55:32Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_18</setSpec>\r\n" + 
    		"                <setSpec>col_10673_19</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Cities and Flooding : A Guide to\r\n" + 
    		"            Integrated Urban Flood Risk Management for the 21st Century</dc:title>\r\n" + 
    		"<dc:subject/>\r\n" + 
    		"<dc:description>The guide serves as a primer for\r\n" + 
    		"            decision and policy makers, technical specialists, central,\r\n" + 
    		"            regional and local government officials, and concerned\r\n" + 
    		"            stakeholders in the community sector, civil society and\r\n" + 
    		"            non-governmental organizations, and the private sector. The\r\n" + 
    		"            Guide embodies the state-of-the art on integrated urban\r\n" + 
    		"            flood risk management. The Guide starts with a summary for\r\n" + 
    		"            policy makers which outlines and describes the key areas\r\n" + 
    		"            which policy makers need to be knowledgeable about to create\r\n" + 
    		"            policy directions and an integrated strategic approach for\r\n" + 
    		"            urban flood risk management. The core of the Guide consists\r\n" + 
    		"            of seven chapters, organized as: understanding flood hazard;\r\n" + 
    		"            understanding flood impacts; integrated flood risk\r\n" + 
    		"            management (structural measures and non-structural\r\n" + 
    		"            measures); evaluating alternative flood risk management\r\n" + 
    		"            options: tools for decision makers; implementing integrated\r\n" + 
    		"            flood risk management; and conclusion. Each chapter starts\r\n" + 
    		"            with a full contents list and a summary of the chapter for\r\n" + 
    		"            quick reference.</dc:description>\r\n" + 
    		"<dc:date>2013-05-06T16:55:32Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06T16:55:32Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10986/2241</dc:identifier>\r\n" + 
    		"<dc:rights>CC BY 3.0 Unported</dc:rights>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/</dc:rights>\r\n" + 
    		"<dc:publisher>World Bank</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/141</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:16:58Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Collaboration Tools Survey</dc:title>\r\n" + 
    		"<dc:subject>research, teaching, learning, collaboration, tools, administration</dc:subject>\r\n" + 
    		"<dc:description>Report of the ITC short-life working group established to appraise the use of collaborative tools across the university in support of teaching and learning, research, and administration. The focus was on where, as a University, we are now, where we would like to be in an ideal world and how we should get there. https://www.wiki.ed.ac.uk/display/Web2wiki/Survey+Responses.</dc:description>\r\n" + 
    		"<dc:date>2012-02-13T14:55:29Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:57Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:57Z</dc:date>\r\n" + 
    		"<dc:date>2012-02-13</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>Dewhurst, David. (2012). Collaboration Tools Survey, 2009 [Dataset]. University of Edinburgh, Information Services.</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/141</dc:identifier>\r\n" + 
    		"<dc:language>eng</dc:language>\r\n" + 
    		"<dc:publisher>University of Edinburgh, Information Services</dc:publisher>\r\n" + 
    		"<dc:source>Information Services</dc:source>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:2318/73</identifier>\r\n" + 
    		"                <datestamp>2013-07-09T22:59:23Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_16</setSpec>\r\n" + 
    		"                <setSpec>col_10673_21</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Contributions to the understanding of Bayesian consistency</dc:title>\r\n" + 
    		"<dc:subject>Bayesian consistency</dc:subject>\r\n" + 
    		"<dc:subject>Density estimation</dc:subject>\r\n" + 
    		"<dc:subject>Hellinger distance</dc:subject>\r\n" + 
    		"<dc:subject>Weak neighborhood</dc:subject>\r\n" + 
    		"<dc:description>Consistency of Bayesian nonparametric procedures has been the focus of a considerable amount of research. Here we deal with strong consistency for Bayesian density estimation. An awkward consequence of inconsistency is pointed out. We investigate reasons for inconsistency and precisely identify the notion of “data tracking”. Specific examples in which this phenomenon can not occur are discussed. When it can happen, we show how and where things can go wrong, in particular the type of sets where the posterior can put mass.</dc:description>\r\n" + 
    		"<dc:description>ICER - International Centre for Economic Research</dc:description>\r\n" + 
    		"<dc:date>2013-07-09T22:59:23Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09T22:59:23Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09</dc:date>\r\n" + 
    		"<dc:type>Preprint</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/2318/73</dc:identifier>\r\n" + 
    		"<dc:relation>Applied Mathematics Working Papers Series</dc:relation>\r\n" + 
    		"<dc:relation>13/2004</dc:relation>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/31</identifier>\r\n" + 
    		"                <datestamp>2013-08-25T02:13:03Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_9</setSpec>\r\n" + 
    		"                <setSpec>col_10673_10</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>O debate historiográfico em torno da “crise do Antigo Sistema Colonial” - parte 2</dc:title>\r\n" + 
    		"<dc:subject>história, história do brasil colônia, colonial, motim, sedição, contestação política</dc:subject>\r\n" + 
    		"<dc:description/>\r\n" + 
    		"<dc:date>2013-08-25T02:13:03Z</dc:date>\r\n" + 
    		"<dc:date>2013-08-25T02:13:03Z</dc:date>\r\n" + 
    		"<dc:date>2013-08-25</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/31</dc:identifier>\r\n" + 
    		"<dc:relation>http://eaulas.usp.br/portal/video.action?idItem=2771</dc:relation>\r\n" + 
    		"<dc:relation>História do Brasil Colonial II</dc:relation>\r\n" + 
    		"<dc:relation>FLH0242 -1</dc:relation>\r\n" + 
    		"<dc:publisher>Faculdade de Filosofia, Letras e Ciências Humanas</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/33</identifier>\r\n" + 
    		"                <datestamp>2013-09-07T02:00:39Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>deck</dc:title>\r\n" + 
    		"<dc:creator>asdf, agagwergw4</dc:creator>\r\n" + 
    		"<dc:subject>Deck and garage</dc:subject>\r\n" + 
    		"<dc:description>asdf sadf</dc:description>\r\n" + 
    		"<dc:description>asdfdasb</dc:description>\r\n" + 
    		"<dc:date>2013-09-06T20:09:39Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-06T20:09:39Z</dc:date>\r\n" + 
    		"<dc:date>2012-04-23</dc:date>\r\n" + 
    		"<dc:type>Image</dc:type>\r\n" + 
    		"<dc:identifier>asder</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/33</dc:identifier>\r\n" + 
    		"<dc:language>en</dc:language>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by-nd/3.0/us/</dc:rights>\r\n" + 
    		"<dc:rights>Attribution-NoDerivs 3.0 United States</dc:rights>\r\n" + 
    		"<dc:publisher>MDPI</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/34</identifier>\r\n" + 
    		"                <datestamp>2013-09-11T02:00:49Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_9</setSpec>\r\n" + 
    		"                <setSpec>col_10673_32</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Die pastorale begeleiding van predikante van die Nederduitse Gereformeerde Kerk tydens die kerklike tughandeling</dc:title>\r\n" + 
    		"<dc:creator>Cornelis T. Kleynhans</dc:creator>\r\n" + 
    		"<dc:description>Pastoral guidance of ministers of the Dutch Reformed Church during ecclesiastical discipline. The process of ecclesiastical discipline evokes feelings of guilt and shame. Whilst literary study suggested this to be the case, the empirical research confirmed it. It is clear that the three-fold process was a traumatic and shocking experience for ministers. Most upsetting was the way that the process was handled. It was done in a non-professional way and without brotherly or sisterly love. The process triggered guilt and shame emotions in a number of ways, not least by the lack of support and guidance. Respondents indicated that they had positive and negative experiences of guilt and shame during the discipline process. Most respondents took action to amend their mistakes, and thereby used the guilt feeling functionally, whilst the use of defence mechanisms showed that they did not manage and process the feelings of shame. It is unsettling to realise that the Dutch Reformed Church fails her ministers in time of need. Only a few parishioners and ministers from other denominations provided some sort of comfort during the discipline process. The church gave no support and guidance in the processing of the feelings of guilt and shame. The church lacked in every aspect, even to show a basic understanding of the trauma, and none of the church councils offered any basic or interventive help. To remedy the situation, it is proposed that the church should take its task as caregiver during the ecclesiastical discipline of ministers very serious and give guidance in an official and professional way.</dc:description>\r\n" + 
    		"<dc:date>2013-09-10T13:17:27Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-10T13:17:27Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-10</dc:date>\r\n" + 
    		"<dc:date>2013-09-10T13:17:32Z</dc:date>\r\n" + 
    		"<dc:identifier/>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/34</dc:identifier>\r\n" + 
    		"<dc:language>en</dc:language>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10986/2242</identifier>\r\n" + 
    		"                <datestamp>2013-05-06T16:55:34Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_18</setSpec>\r\n" + 
    		"                <setSpec>col_10673_19</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Draining Development? Controlling\r\n" + 
    		"            Flows of Illicit Funds from Developing Countries</dc:title>\r\n" + 
    		"<dc:subject>Corruption</dc:subject>\r\n" + 
    		"<dc:subject>Illegal markets</dc:subject>\r\n" + 
    		"<dc:subject>Illicit financial flows</dc:subject>\r\n" + 
    		"<dc:subject>Money laundering</dc:subject>\r\n" + 
    		"<dc:subject>Stolen assets</dc:subject>\r\n" + 
    		"<dc:subject>Tax evasion</dc:subject>\r\n" + 
    		"<dc:subject>Trade mis-pricing</dc:subject>\r\n" + 
    		"<dc:subject>Transfer pricing</dc:subject>\r\n" + 
    		"<dc:description>The book provides the first collection\r\n" + 
    		"            of analytic contributions, as opposed to advocacy essays and\r\n" + 
    		"            black box estimates, on illicit financial flows (IFFs). Some\r\n" + 
    		"            of the chapter presents new empirical findings; others, new\r\n" + 
    		"            conceptual insights. All of them enrich the understanding of\r\n" + 
    		"            the dynamics of the illicit flows phenomenon. The book does\r\n" + 
    		"            not offer a new estimate of the global total of these flows\r\n" + 
    		"            because the phenomenon is too poorly understood. The\r\n" + 
    		"            chapters are based on papers first presented at a September\r\n" + 
    		"            2009 conference at the World Bank. Each paper had one or two\r\n" + 
    		"            assigned discussants, and the revisions reflect the often\r\n" + 
    		"            searching critiques of the discussants, as well as\r\n" + 
    		"            additional comments from the editor and from two external\r\n" + 
    		"            peer reviewers. The chapters have been written to be\r\n" + 
    		"            accessible to non-experts. Following this introduction, the\r\n" + 
    		"            book has five parts:  the political economy of illicit\r\n" + 
    		"            flows; illegal markets; to what extent do corporations\r\n" + 
    		"            facilitate illicit flows? Policy interventions; and\r\n" + 
    		"            conclusions and the path forward.</dc:description>\r\n" + 
    		"<dc:date>2013-05-06T16:55:33Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06T16:55:33Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10986/2242</dc:identifier>\r\n" + 
    		"<dc:rights>CC BY 3.0 Unported</dc:rights>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/</dc:rights>\r\n" + 
    		"<dc:publisher>World Bank</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/144</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:16:59Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>A survey of undergraduate technology use and attitudes</dc:title>\r\n" + 
    		"<dc:contributor>Other</dc:contributor>\r\n" + 
    		"<dc:subject>Technology</dc:subject>\r\n" + 
    		"<dc:description>An award from ELESIG (The Evaluation of Learners' Experience of eLearning Special Interest Group) provided an opportunity to continue the long and successful collection of information about how our students view ICT at the University of Edinburgh. The original work started in the 1990s but lapsed in recent years due to data collection issues. During the second semester in 2010-11, using a paper questionnaire, first and second years across the University were targetted. The findings raised a number of interesting points, for example: some students still find IT challenging; although laptop ownership is high, students do not seem to be planning to carry their laptops to &#13;\r\n" + 
    		"the campus; Facebook appears to be used significantly for academic &#13;\r\n" + 
    		"purposes.</dc:description>\r\n" + 
    		"<dc:date>2012-02-27T16:39:18Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:58Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:58Z</dc:date>\r\n" + 
    		"<dc:date>2012-02-27</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>Macleod, Hamish; Paterson, Jessie. (2012). A survey of undergraduate technology use and attitudes, 2011 [Dataset]. University of Edinburgh.</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/144</dc:identifier>\r\n" + 
    		"<dc:language>eng</dc:language>\r\n" + 
    		"<dc:publisher>University of Edinburgh</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:2318/72</identifier>\r\n" + 
    		"                <datestamp>2013-07-09T22:59:24Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_16</setSpec>\r\n" + 
    		"                <setSpec>col_10673_21</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Risk, Ambiguity, and the Separation of Utility and Beliefs</dc:title>\r\n" + 
    		"<dc:description>We introduce a general model of static choice under uncertainty, arguably the weakest\r\n" + 
    		"model achieving a separation of cardinal utility and a unique representation of beliefs.\r\n" + 
    		"Most of the non-expected utility models existing in the literature are special cases of it.\r\n" + 
    		"Such separation is motivated by the view that tastes are constant, whereas beliefs change\r\n" + 
    		"with new information. The model has a simple and natural axiomatization.\r\n" + 
    		"Elsewhere (forthcoming) we show that it can be very helpful in the characterization\r\n" + 
    		"of a notion of ambiguity aversion, as separating utility and beliefs allows to identify and\r\n" + 
    		"remove aspects of risk attitude from the decision maker’s behavior. Here we show that\r\n" + 
    		"the model allows to generalize several results on the characterization of risk aversion in\r\n" + 
    		"betting behavior. These generalizations are of independent interest, as they show that\r\n" + 
    		"some traditional results for subjective expected utility preferences can be formulated only\r\n" + 
    		"in terms of binary acts.</dc:description>\r\n" + 
    		"<dc:description>ICER - International Centre For Economic Research</dc:description>\r\n" + 
    		"<dc:date>2013-07-09T22:59:24Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09T22:59:24Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09</dc:date>\r\n" + 
    		"<dc:type>Articolo</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/2318/72</dc:identifier>\r\n" + 
    		"<dc:relation>Applied Mathematics Working Paper Series</dc:relation>\r\n" + 
    		"<dc:relation>21/2001</dc:relation>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/36</identifier>\r\n" + 
    		"                <datestamp>2013-09-07T02:00:36Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_34</setSpec>\r\n" + 
    		"                <setSpec>col_10673_35</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>asdg</dc:title>\r\n" + 
    		"<dc:date>2013-09-06T20:19:06Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-06T20:19:06Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-06</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/36</dc:identifier>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by-nc-sa/3.0/us/</dc:rights>\r\n" + 
    		"<dc:rights>Attribution-NonCommercial-ShareAlike 3.0 United States</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/35</identifier>\r\n" + 
    		"                <datestamp>2013-09-10T13:28:47Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_9</setSpec>\r\n" + 
    		"                <setSpec>col_10673_32</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>A pastoral evaluation of menopause in the African context</dc:title>\r\n" + 
    		"<dc:creator>Elijah Baloyi</dc:creator>\r\n" + 
    		"<dc:description>Menopause, with its physical and emotional changes, appears to be an inevitable road for  women to travel. The moment of choice for women at menopause involves not only whether  they will embrace the new self or try to cling to identities from earlier life but also how the society in which they live views women after menopause. Amongst other things, many  African marriages face difficulties when the moment of menopause arrives. This situation is  often characterised by a second marriage or a situation where husband and wife no longer  share a room. Whenever this happens, it testifies to the idea that the sole purpose of marriage amongst African people is procreation – hence, when the period for that is passed, the bedroom setup changes. This is one of the ways in which senior women are deemed unfit  for sexual encounters, a gender-equality concern. This article aims to unveil and discuss how  some Africans use menopause as an excuse to exclude women from sexual intercourse, and  how pastoral caregivers can help in such situations.</dc:description>\r\n" + 
    		"<dc:date>2013-09-10T13:28:46Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-10T13:28:46Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-10</dc:date>\r\n" + 
    		"<dc:date>2013-09-10T13:28:47Z</dc:date>\r\n" + 
    		"<dc:identifier/>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/35</dc:identifier>\r\n" + 
    		"<dc:language>en</dc:language>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10986/2243</identifier>\r\n" + 
    		"                <datestamp>2013-05-06T16:55:35Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_18</setSpec>\r\n" + 
    		"                <setSpec>col_10673_19</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Some Small Countries Do It Better :\r\n" + 
    		"            Rapid Growth and Its Causes in Singapore, Finland, and Ireland</dc:title>\r\n" + 
    		"<dc:subject>AGGREGATE DEMAND</dc:subject>\r\n" + 
    		"<dc:subject>AGRICULTURE</dc:subject>\r\n" + 
    		"<dc:subject>ALGORITHMS</dc:subject>\r\n" + 
    		"<dc:subject>ALLOCATIVE EFFICIENCY</dc:subject>\r\n" + 
    		"<dc:subject>ARTIFICIAL INTELLIGENCE</dc:subject>\r\n" + 
    		"<dc:subject>ATTRIBUTES</dc:subject>\r\n" + 
    		"<dc:subject>AUTOMATION</dc:subject>\r\n" + 
    		"<dc:subject>BACK UP</dc:subject>\r\n" + 
    		"<dc:subject>BANDWIDTH</dc:subject>\r\n" + 
    		"<dc:subject>BARRIERS TO ENTRY</dc:subject>\r\n" + 
    		"<dc:subject>BASIC</dc:subject>\r\n" + 
    		"<dc:subject>BENCHMARKS</dc:subject>\r\n" + 
    		"<dc:subject>BROADBAND</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESS ACTIVITY</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESS ASSOCIATIONS</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESS CLIMATE</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESS CLIMATES</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESS ENVIRONMENT</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESS MODELS</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESS PERFORMANCE</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESS SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESS SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESSES</dc:subject>\r\n" + 
    		"<dc:subject>CAPABILITIES</dc:subject>\r\n" + 
    		"<dc:subject>CAPABILITY</dc:subject>\r\n" + 
    		"<dc:subject>CAPITAL INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>CITIES</dc:subject>\r\n" + 
    		"<dc:subject>COMMODITIES</dc:subject>\r\n" + 
    		"<dc:subject>COMMUNICATION TECHNOLOGIES</dc:subject>\r\n" + 
    		"<dc:subject>COMMUNICATION TECHNOLOGY</dc:subject>\r\n" + 
    		"<dc:subject>COMMUNITIES</dc:subject>\r\n" + 
    		"<dc:subject>COMPETITIVE ENVIRONMENT</dc:subject>\r\n" + 
    		"<dc:subject>COMPETITIVE FORCES</dc:subject>\r\n" + 
    		"<dc:subject>COMPETITIVENESS</dc:subject>\r\n" + 
    		"<dc:subject>COMPONENTS</dc:subject>\r\n" + 
    		"<dc:subject>COMPUTER INDUSTRY</dc:subject>\r\n" + 
    		"<dc:subject>COMPUTER LITERACY</dc:subject>\r\n" + 
    		"<dc:subject>COMPUTERS</dc:subject>\r\n" + 
    		"<dc:subject>COMPUTING</dc:subject>\r\n" + 
    		"<dc:subject>CONNECTIVITY</dc:subject>\r\n" + 
    		"<dc:subject>CONSUMERS</dc:subject>\r\n" + 
    		"<dc:subject>COPYRIGHT</dc:subject>\r\n" + 
    		"<dc:subject>COUNTRY OF ORIGIN</dc:subject>\r\n" + 
    		"<dc:subject>DATA PROCESSING</dc:subject>\r\n" + 
    		"<dc:subject>DEMOCRACY</dc:subject>\r\n" + 
    		"<dc:subject>DEVELOPMENT ECONOMICS</dc:subject>\r\n" + 
    		"<dc:subject>DIGITAL</dc:subject>\r\n" + 
    		"<dc:subject>DISPOSABLE INCOME</dc:subject>\r\n" + 
    		"<dc:subject>E-MAIL</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMETRICS</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC ACTIVITY</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC CHANGE</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC DIVERSITY</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC HISTORY</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC OBJECTIVES</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC PERFORMANCE</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC RESEARCH</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC STRUCTURE</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC SURVEYS</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMICS OF EDUCATION</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMISTS</dc:subject>\r\n" + 
    		"<dc:subject>ELECTRIC MOTORS</dc:subject>\r\n" + 
    		"<dc:subject>ELECTRICITY</dc:subject>\r\n" + 
    		"<dc:subject>ELECTRONICS</dc:subject>\r\n" + 
    		"<dc:subject>ELITES</dc:subject>\r\n" + 
    		"<dc:subject>EMPIRICAL ANALYSIS</dc:subject>\r\n" + 
    		"<dc:subject>ENGINEERING</dc:subject>\r\n" + 
    		"<dc:subject>ENGINEERS</dc:subject>\r\n" + 
    		"<dc:subject>ENTREPRENEURIAL CULTURE</dc:subject>\r\n" + 
    		"<dc:subject>ENVIRONMENTAL</dc:subject>\r\n" + 
    		"<dc:subject>EXCHANGE RATE</dc:subject>\r\n" + 
    		"<dc:subject>EXPLOITATION</dc:subject>\r\n" + 
    		"<dc:subject>EXPORT GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>EXPORTS</dc:subject>\r\n" + 
    		"<dc:subject>EXTERNALITIES</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN DIRECT INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>FUNCTIONALITY</dc:subject>\r\n" + 
    		"<dc:subject>GDP</dc:subject>\r\n" + 
    		"<dc:subject>GDP PER CAPITA</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL ECONOMY</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL MARKETS</dc:subject>\r\n" + 
    		"<dc:subject>GLOBALIZATION</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>GROSS DOMESTIC PRODUCT</dc:subject>\r\n" + 
    		"<dc:subject>GROWTH MODELS</dc:subject>\r\n" + 
    		"<dc:subject>GROWTH POTENTIAL</dc:subject>\r\n" + 
    		"<dc:subject>GROWTH RATE</dc:subject>\r\n" + 
    		"<dc:subject>GROWTH STRATEGIES</dc:subject>\r\n" + 
    		"<dc:subject>GROWTH STRATEGY</dc:subject>\r\n" + 
    		"<dc:subject>GROWTH THEORY</dc:subject>\r\n" + 
    		"<dc:subject>HIGH TECHNOLOGY</dc:subject>\r\n" + 
    		"<dc:subject>HUMAN CAPITAL</dc:subject>\r\n" + 
    		"<dc:subject>HUMAN RESOURCES</dc:subject>\r\n" + 
    		"<dc:subject>ICT</dc:subject>\r\n" + 
    		"<dc:subject>INCOME</dc:subject>\r\n" + 
    		"<dc:subject>INDUSTRIAL DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>INDUSTRIALIZATION</dc:subject>\r\n" + 
    		"<dc:subject>INFORMATION SOCIETY</dc:subject>\r\n" + 
    		"<dc:subject>INFORMATION TECHNOLOGY</dc:subject>\r\n" + 
    		"<dc:subject>INNOVATION</dc:subject>\r\n" + 
    		"<dc:subject>INNOVATIONS</dc:subject>\r\n" + 
    		"<dc:subject>INSTITUTION</dc:subject>\r\n" + 
    		"<dc:subject>INTEGRATED CIRCUITS</dc:subject>\r\n" + 
    		"<dc:subject>INTELLECTUAL PROPERTY</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL BUSINESS</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL COMPARISON</dc:subject>\r\n" + 
    		"<dc:subject>INVENTION</dc:subject>\r\n" + 
    		"<dc:subject>INVENTIONS</dc:subject>\r\n" + 
    		"<dc:subject>KNOWLEDGE WORKER</dc:subject>\r\n" + 
    		"<dc:subject>LABOR FORCE</dc:subject>\r\n" + 
    		"<dc:subject>LABOR INPUTS</dc:subject>\r\n" + 
    		"<dc:subject>LAN</dc:subject>\r\n" + 
    		"<dc:subject>LEARNING</dc:subject>\r\n" + 
    		"<dc:subject>LICENSES</dc:subject>\r\n" + 
    		"<dc:subject>MACHINE INTELLIGENCE</dc:subject>\r\n" + 
    		"<dc:subject>MANAGERIAL SKILLS</dc:subject>\r\n" + 
    		"<dc:subject>MANUFACTURING</dc:subject>\r\n" + 
    		"<dc:subject>MARKET ECONOMY</dc:subject>\r\n" + 
    		"<dc:subject>MARKETING</dc:subject>\r\n" + 
    		"<dc:subject>MATERIAL</dc:subject>\r\n" + 
    		"<dc:subject>MICROELECTRONICS</dc:subject>\r\n" + 
    		"<dc:subject>MOBILE COMMUNICATIONS</dc:subject>\r\n" + 
    		"<dc:subject>NATIONAL INCOME</dc:subject>\r\n" + 
    		"<dc:subject>NATURAL MONOPOLIES</dc:subject>\r\n" + 
    		"<dc:subject>NATURAL RESOURCE</dc:subject>\r\n" + 
    		"<dc:subject>NATURAL RESOURCES</dc:subject>\r\n" + 
    		"<dc:subject>NET EXPORTS</dc:subject>\r\n" + 
    		"<dc:subject>NETWORKING</dc:subject>\r\n" + 
    		"<dc:subject>NETWORKS</dc:subject>\r\n" + 
    		"<dc:subject>NEW TECHNOLOGIES</dc:subject>\r\n" + 
    		"<dc:subject>NEWLY INDUSTRIALIZED COUNTRIES</dc:subject>\r\n" + 
    		"<dc:subject>ORGANIZATIONAL CAPABILITIES</dc:subject>\r\n" + 
    		"<dc:subject>ORGANIZATIONAL CAPABILITY</dc:subject>\r\n" + 
    		"<dc:subject>PATENTS</dc:subject>\r\n" + 
    		"<dc:subject>PER CAPITA INCOME</dc:subject>\r\n" + 
    		"<dc:subject>PER CAPITA INCOMES</dc:subject>\r\n" + 
    		"<dc:subject>PHOTOS</dc:subject>\r\n" + 
    		"<dc:subject>POLICY ENVIRONMENT</dc:subject>\r\n" + 
    		"<dc:subject>POLICY MAKERS</dc:subject>\r\n" + 
    		"<dc:subject>POLITICAL WILL</dc:subject>\r\n" + 
    		"<dc:subject>POPULATION GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>PROCUREMENT</dc:subject>\r\n" + 
    		"<dc:subject>PRODUCTIVE ASSETS</dc:subject>\r\n" + 
    		"<dc:subject>PRODUCTIVITY GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>PROGRAMS</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC ADMINISTRATION</dc:subject>\r\n" + 
    		"<dc:subject>PUBLISHING</dc:subject>\r\n" + 
    		"<dc:subject>QUERIES</dc:subject>\r\n" + 
    		"<dc:subject>R&amp;D</dc:subject>\r\n" + 
    		"<dc:subject>REGULATORY CAPTURE</dc:subject>\r\n" + 
    		"<dc:subject>REGULATORY MECHANISMS</dc:subject>\r\n" + 
    		"<dc:subject>RESEARCH AGENDA</dc:subject>\r\n" + 
    		"<dc:subject>RESOURCE ALLOCATION</dc:subject>\r\n" + 
    		"<dc:subject>RESULT</dc:subject>\r\n" + 
    		"<dc:subject>RESULTS</dc:subject>\r\n" + 
    		"<dc:subject>SCIENTISTS</dc:subject>\r\n" + 
    		"<dc:subject>SEMICONDUCTORS</dc:subject>\r\n" + 
    		"<dc:subject>SILICON</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL SCIENCES</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>STRUCTURAL CHANGE</dc:subject>\r\n" + 
    		"<dc:subject>SUSTAINABLE GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>TARGETS</dc:subject>\r\n" + 
    		"<dc:subject>TAX INCENTIVES</dc:subject>\r\n" + 
    		"<dc:subject>TECHNICAL COOPERATION</dc:subject>\r\n" + 
    		"<dc:subject>TECHNOLOGICAL ADVANCES</dc:subject>\r\n" + 
    		"<dc:subject>TECHNOLOGICAL CAPABILITY</dc:subject>\r\n" + 
    		"<dc:subject>TECHNOLOGICAL CHANGE</dc:subject>\r\n" + 
    		"<dc:subject>TECHNOLOGICAL INNOVATION</dc:subject>\r\n" + 
    		"<dc:subject>TECHNOLOGY TRANSFER</dc:subject>\r\n" + 
    		"<dc:subject>TELECOMMUNICATIONS</dc:subject>\r\n" + 
    		"<dc:subject>TELEPHONE</dc:subject>\r\n" + 
    		"<dc:subject>TOTAL FACTOR PRODUCTIVITY</dc:subject>\r\n" + 
    		"<dc:subject>TRANSACTION</dc:subject>\r\n" + 
    		"<dc:subject>TRANSACTION COSTS</dc:subject>\r\n" + 
    		"<dc:subject>TRANSLATION</dc:subject>\r\n" + 
    		"<dc:subject>USERS</dc:subject>\r\n" + 
    		"<dc:subject>USES</dc:subject>\r\n" + 
    		"<dc:subject>VALUE ADDED</dc:subject>\r\n" + 
    		"<dc:subject>VALUE CHAIN</dc:subject>\r\n" + 
    		"<dc:subject>VERIFICATION</dc:subject>\r\n" + 
    		"<dc:subject>WAGES</dc:subject>\r\n" + 
    		"<dc:subject>WEALTH</dc:subject>\r\n" + 
    		"<dc:subject>Finland</dc:subject>\r\n" + 
    		"<dc:subject>Ireland</dc:subject>\r\n" + 
    		"<dc:description>This book is an outcome of a series of\r\n" + 
    		"            study visits to Singapore for African policy makers\r\n" + 
    		"            initiated by Jee-Peng Tan in 2005 with support from Tommy\r\n" + 
    		"            Koh in Singapore and Birger Fredriksen, Yaw Ansu, and\r\n" + 
    		"            Dzingai Mutumbuka at the World Bank. Starting in the\r\n" + 
    		"            1960s-earlier if Japan is included-a number of East Asian\r\n" + 
    		"            economies began achieving growth rates well above the\r\n" + 
    		"            average and were able to maintain that pace until nearly the\r\n" + 
    		"            end of the 1990s. Countries, large and small, have struggled\r\n" + 
    		"            to imitate the industrial prowess of the East Asian\r\n" + 
    		"            pacesetters and to exploit the opportunities presented by\r\n" + 
    		"            globalization to expand exports. But approximating the East\r\n" + 
    		"            Asian benchmarks has proven difficult, and growth\r\n" + 
    		"            accelerations have tended to be remarkably transient.</dc:description>\r\n" + 
    		"<dc:date>2013-05-06T16:55:35Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06T16:55:35Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10986/2243</dc:identifier>\r\n" + 
    		"<dc:rights>CC BY 3.0 Unported</dc:rights>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/</dc:rights>\r\n" + 
    		"<dc:publisher>World Bank</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/163</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:16:59Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Acceptance and Commitment Therapy for Irritable Bowel Syndrome</dc:title>\r\n" + 
    		"<dc:contributor>Other</dc:contributor>\r\n" + 
    		"<dc:subject>ACT</dc:subject>\r\n" + 
    		"<dc:date>2012-09-04T11:20:43Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:59Z</dc:date>\r\n" + 
    		"<dc:date>10000-01-01</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:16:59Z</dc:date>\r\n" + 
    		"<dc:date>2012-09-04</dc:date>\r\n" + 
    		"<dc:type>Dataset</dc:type>\r\n" + 
    		"<dc:identifier>Morris, Paul; Gillanders, David; Eugenicos, Maria; Ferreira, Nuno. (2012). Acceptance and Commitment Therapy for Irritable Bowel Syndrome, 2009-2010 [Dataset]. UoE and NHS.</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/163</dc:identifier>\r\n" + 
    		"<dc:language>eng</dc:language>\r\n" + 
    		"<dc:rights>While this dataset is under embargo please contact the data creator for enquiries regarding the dataset</dc:rights>\r\n" + 
    		"<dc:publisher>UoE and NHS</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:2318/75</identifier>\r\n" + 
    		"                <datestamp>2013-07-09T22:59:24Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_16</setSpec>\r\n" + 
    		"                <setSpec>col_10673_21</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Change of Numéraire for Affine Arbitrage Pricing Models</dc:title>\r\n" + 
    		"<dc:description>We derive a general formula for the change of numéraire in multifactor affine arbitrage\r\n" + 
    		"free models driven by marked point processes. As a complement, we present both affine\r\n" + 
    		"structures and change of measures in the general setting of jump diffusions. This provides for\r\n" + 
    		"a comprehensive view on the subject.</dc:description>\r\n" + 
    		"<dc:description>ICER - International Centre For Economic Research</dc:description>\r\n" + 
    		"<dc:date>2013-07-09T22:59:24Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09T22:59:24Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09</dc:date>\r\n" + 
    		"<dc:type>Preprint</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/2318/75</dc:identifier>\r\n" + 
    		"<dc:relation>Applied Mathematics Working Paper Series</dc:relation>\r\n" + 
    		"<dc:relation>22/2001</dc:relation>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/38</identifier>\r\n" + 
    		"                <datestamp>2013-09-11T02:00:31Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Rycerz</dc:title>\r\n" + 
    		"<dc:creator>Anonim</dc:creator>\r\n" + 
    		"<dc:subject>rycerz</dc:subject>\r\n" + 
    		"<dc:subject>średniowiecze</dc:subject>\r\n" + 
    		"<dc:subject>zbroja</dc:subject>\r\n" + 
    		"<dc:subject>manuskrypt</dc:subject>\r\n" + 
    		"<dc:description>Przedstawienie średniowiecznego rycerza w zbroi.</dc:description>\r\n" + 
    		"<dc:date>2013-09-10T19:40:28Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-10T19:40:28Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-10</dc:date>\r\n" + 
    		"<dc:type>Image</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/38</dc:identifier>\r\n" + 
    		"<dc:language>other</dc:language>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by-nc-nd/3.0/us/</dc:rights>\r\n" + 
    		"<dc:rights>Attribution-NonCommercial-NoDerivs 3.0 United States</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10986/2244</identifier>\r\n" + 
    		"                <datestamp>2013-05-06T16:55:37Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_18</setSpec>\r\n" + 
    		"                <setSpec>col_10673_19</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Developing Public-Private\r\n" + 
    		"            Partnerships in Liberia</dc:title>\r\n" + 
    		"<dc:subject>AIRPORTS</dc:subject>\r\n" + 
    		"<dc:subject>BEST PRACTICES</dc:subject>\r\n" + 
    		"<dc:subject>BRIDGE</dc:subject>\r\n" + 
    		"<dc:subject>BULK WATER</dc:subject>\r\n" + 
    		"<dc:subject>BULK WATER SUPPLY</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESSES</dc:subject>\r\n" + 
    		"<dc:subject>CAPABILITY</dc:subject>\r\n" + 
    		"<dc:subject>CAPACITY BUILDING</dc:subject>\r\n" + 
    		"<dc:subject>CAPACITY-BUILDING</dc:subject>\r\n" + 
    		"<dc:subject>CAPITAL CONSTRAINTS</dc:subject>\r\n" + 
    		"<dc:subject>CAPITAL COSTS</dc:subject>\r\n" + 
    		"<dc:subject>CAPITAL INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>CARGO</dc:subject>\r\n" + 
    		"<dc:subject>CASH FLOW</dc:subject>\r\n" + 
    		"<dc:subject>CHEAPER POWER</dc:subject>\r\n" + 
    		"<dc:subject>COLLECTION EFFICIENCY</dc:subject>\r\n" + 
    		"<dc:subject>COMMERCIAL RISKS</dc:subject>\r\n" + 
    		"<dc:subject>COMMODITY</dc:subject>\r\n" + 
    		"<dc:subject>COMPETITIVE BIDDING</dc:subject>\r\n" + 
    		"<dc:subject>COMPETITIVE BIDDING PROCESS</dc:subject>\r\n" + 
    		"<dc:subject>COMPETITIVENESS</dc:subject>\r\n" + 
    		"<dc:subject>CONCESSION</dc:subject>\r\n" + 
    		"<dc:subject>CONCESSION AGREEMENTS</dc:subject>\r\n" + 
    		"<dc:subject>CONCESSION AREAS</dc:subject>\r\n" + 
    		"<dc:subject>CONCESSION ARRANGEMENTS</dc:subject>\r\n" + 
    		"<dc:subject>CONCESSION CONTRACT</dc:subject>\r\n" + 
    		"<dc:subject>CONCESSION CONTRACTS</dc:subject>\r\n" + 
    		"<dc:subject>CONCESSION MODEL</dc:subject>\r\n" + 
    		"<dc:subject>CONCESSION PROCESS</dc:subject>\r\n" + 
    		"<dc:subject>CONCESSIONAIRES</dc:subject>\r\n" + 
    		"<dc:subject>CONCESSIONS</dc:subject>\r\n" + 
    		"<dc:subject>CONTAINER PORT</dc:subject>\r\n" + 
    		"<dc:subject>CONTRACT PERFORMANCE</dc:subject>\r\n" + 
    		"<dc:subject>COPYRIGHT</dc:subject>\r\n" + 
    		"<dc:subject>CORRIDOR INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>CREDITWORTHINESS</dc:subject>\r\n" + 
    		"<dc:subject>CUSTOMER BASE</dc:subject>\r\n" + 
    		"<dc:subject>CUSTOMS</dc:subject>\r\n" + 
    		"<dc:subject>DEBT</dc:subject>\r\n" + 
    		"<dc:subject>DISTRIBUTION SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>DIVESTITURE</dc:subject>\r\n" + 
    		"<dc:subject>DRIVERS</dc:subject>\r\n" + 
    		"<dc:subject>DUE DILIGENCE</dc:subject>\r\n" + 
    		"<dc:subject>E-MAIL</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC BENEFITS</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC RISKS</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIES OF SCALE</dc:subject>\r\n" + 
    		"<dc:subject>ELECTRICITY</dc:subject>\r\n" + 
    		"<dc:subject>ENABLING ENVIRONMENT</dc:subject>\r\n" + 
    		"<dc:subject>ENABLING ENVIRONMENTS</dc:subject>\r\n" + 
    		"<dc:subject>ENTERPRISE DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>ENTRY POINT</dc:subject>\r\n" + 
    		"<dc:subject>ENVIRONMENTS</dc:subject>\r\n" + 
    		"<dc:subject>EXCESS CAPACITY</dc:subject>\r\n" + 
    		"<dc:subject>EXCESS POWER</dc:subject>\r\n" + 
    		"<dc:subject>EXPENDITURES</dc:subject>\r\n" + 
    		"<dc:subject>EXTERNAL CONSULTANTS</dc:subject>\r\n" + 
    		"<dc:subject>FEEDER ROADS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL ANALYSIS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL SYSTEM</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL TERMS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL VIABILITY</dc:subject>\r\n" + 
    		"<dc:subject>FISHING</dc:subject>\r\n" + 
    		"<dc:subject>FIXED FEE</dc:subject>\r\n" + 
    		"<dc:subject>FOOD PRODUCTION</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN DIRECT INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN FIRMS</dc:subject>\r\n" + 
    		"<dc:subject>FORESTRY</dc:subject>\r\n" + 
    		"<dc:subject>FREIGHT</dc:subject>\r\n" + 
    		"<dc:subject>FREIGHT COSTS</dc:subject>\r\n" + 
    		"<dc:subject>FREIGHT TRAFFIC</dc:subject>\r\n" + 
    		"<dc:subject>FUEL</dc:subject>\r\n" + 
    		"<dc:subject>FUEL OIL</dc:subject>\r\n" + 
    		"<dc:subject>FUNCTIONALITY</dc:subject>\r\n" + 
    		"<dc:subject>GENERATION</dc:subject>\r\n" + 
    		"<dc:subject>GENERATORS</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT APPROVAL</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT ENTITY</dc:subject>\r\n" + 
    		"<dc:subject>HEAVY GOODS VEHICLES</dc:subject>\r\n" + 
    		"<dc:subject>HUMAN CAPACITY</dc:subject>\r\n" + 
    		"<dc:subject>HUMAN CAPITAL</dc:subject>\r\n" + 
    		"<dc:subject>INDEXATION FORMULAS</dc:subject>\r\n" + 
    		"<dc:subject>INFRASTRUCTURE CAPACITY</dc:subject>\r\n" + 
    		"<dc:subject>INFRASTRUCTURE DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>INFRASTRUCTURE DEVELOPMENT PROJECTS</dc:subject>\r\n" + 
    		"<dc:subject>INFRASTRUCTURE INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>INFRASTRUCTURE INVESTMENTS</dc:subject>\r\n" + 
    		"<dc:subject>INFRASTRUCTURE PLANNING</dc:subject>\r\n" + 
    		"<dc:subject>INFRASTRUCTURE POLICY</dc:subject>\r\n" + 
    		"<dc:subject>INFRASTRUCTURE PROJECTS</dc:subject>\r\n" + 
    		"<dc:subject>INFRASTRUCTURE PROVISION</dc:subject>\r\n" + 
    		"<dc:subject>INFRASTRUCTURE REHABILITATION</dc:subject>\r\n" + 
    		"<dc:subject>INFRASTRUCTURE SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>INFRASTRUCTURE SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>INSTITUTION</dc:subject>\r\n" + 
    		"<dc:subject>INSTITUTIONAL CAPACITY</dc:subject>\r\n" + 
    		"<dc:subject>INSTITUTIONAL FRAMEWORK</dc:subject>\r\n" + 
    		"<dc:subject>INTERFACE</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL BEST PRACTICE</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL CONSULTANTS</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL FINANCE</dc:subject>\r\n" + 
    		"<dc:subject>INVESTMENT CLIMATE</dc:subject>\r\n" + 
    		"<dc:subject>INVESTMENT RISKS</dc:subject>\r\n" + 
    		"<dc:subject>LEASE CONTRACT</dc:subject>\r\n" + 
    		"<dc:subject>LEASE FEE</dc:subject>\r\n" + 
    		"<dc:subject>LEGAL FRAMEWORK</dc:subject>\r\n" + 
    		"<dc:subject>LICENSE</dc:subject>\r\n" + 
    		"<dc:subject>LICENSE FEE</dc:subject>\r\n" + 
    		"<dc:subject>LICENSES</dc:subject>\r\n" + 
    		"<dc:subject>MAINTENANCE CONTRACTS</dc:subject>\r\n" + 
    		"<dc:subject>MANAGEMENT CONTRACTS</dc:subject>\r\n" + 
    		"<dc:subject>MANAGEMENT FEE</dc:subject>\r\n" + 
    		"<dc:subject>MATERIAL</dc:subject>\r\n" + 
    		"<dc:subject>MINES</dc:subject>\r\n" + 
    		"<dc:subject>MOBILE TELEPHONY</dc:subject>\r\n" + 
    		"<dc:subject>NATIONAL ACCOUNTING</dc:subject>\r\n" + 
    		"<dc:subject>NATIONAL INFRASTRUCTURE</dc:subject>\r\n" + 
    		"<dc:subject>NATURAL RESOURCE</dc:subject>\r\n" + 
    		"<dc:subject>NATURAL RESOURCES</dc:subject>\r\n" + 
    		"<dc:subject>O&amp;M</dc:subject>\r\n" + 
    		"<dc:subject>OPERATIONAL EFFICIENCY</dc:subject>\r\n" + 
    		"<dc:subject>PDF</dc:subject>\r\n" + 
    		"<dc:subject>PERFORMANCE TARGETS</dc:subject>\r\n" + 
    		"<dc:subject>PERVERSE INCENTIVES</dc:subject>\r\n" + 
    		"<dc:subject>PIERS</dc:subject>\r\n" + 
    		"<dc:subject>POLICY FRAMEWORK</dc:subject>\r\n" + 
    		"<dc:subject>POLITICAL ECONOMY</dc:subject>\r\n" + 
    		"<dc:subject>PORT AUTHORITY</dc:subject>\r\n" + 
    		"<dc:subject>PORT CHARGES</dc:subject>\r\n" + 
    		"<dc:subject>PORT CONCESSIONS</dc:subject>\r\n" + 
    		"<dc:subject>PORT FACILITIES</dc:subject>\r\n" + 
    		"<dc:subject>PORT OPERATIONS</dc:subject>\r\n" + 
    		"<dc:subject>POWER</dc:subject>\r\n" + 
    		"<dc:subject>POWER DISTRIBUTION</dc:subject>\r\n" + 
    		"<dc:subject>POWER SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE OPERATORS</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE PARTNERSHIP</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE PARTNERSHIPS</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE SECTOR ACTIVITY</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE SECTOR DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE SECTOR ENGAGEMENT</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE SECTOR INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE SECTOR PARTICIPATION</dc:subject>\r\n" + 
    		"<dc:subject>PROCUREMENT PROCESS</dc:subject>\r\n" + 
    		"<dc:subject>PRODUCTIVITY</dc:subject>\r\n" + 
    		"<dc:subject>PROVISION OF INFRASTRUCTURE</dc:subject>\r\n" + 
    		"<dc:subject>PROVISIONS</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC DEBT</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC EXPENDITURES</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC INFRASTRUCTURE</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC MANAGEMENT</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC PROCUREMENT</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC PROPERTY</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC SECTOR INFRASTRUCTURE</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC SERVICE</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC SERVICE PROVIDER</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC WORKS</dc:subject>\r\n" + 
    		"<dc:subject>QUERIES</dc:subject>\r\n" + 
    		"<dc:subject>RAIL</dc:subject>\r\n" + 
    		"<dc:subject>RAIL FREIGHT</dc:subject>\r\n" + 
    		"<dc:subject>RAIL INFRASTRUCTURE</dc:subject>\r\n" + 
    		"<dc:subject>RAIL TRANSPORT</dc:subject>\r\n" + 
    		"<dc:subject>RAILROADS</dc:subject>\r\n" + 
    		"<dc:subject>RAILWAY</dc:subject>\r\n" + 
    		"<dc:subject>RAILWAY LINE</dc:subject>\r\n" + 
    		"<dc:subject>RAILWAYS</dc:subject>\r\n" + 
    		"<dc:subject>REGULATORY FRAMEWORK</dc:subject>\r\n" + 
    		"<dc:subject>RESULT</dc:subject>\r\n" + 
    		"<dc:subject>RESULTS</dc:subject>\r\n" + 
    		"<dc:subject>RIGHTS OF ACCESS</dc:subject>\r\n" + 
    		"<dc:subject>RISK ALLOCATION</dc:subject>\r\n" + 
    		"<dc:subject>ROAD</dc:subject>\r\n" + 
    		"<dc:subject>ROAD DAMAGE</dc:subject>\r\n" + 
    		"<dc:subject>ROAD INFRASTRUCTURE</dc:subject>\r\n" + 
    		"<dc:subject>ROAD NETWORKS</dc:subject>\r\n" + 
    		"<dc:subject>ROAD PROJECTS</dc:subject>\r\n" + 
    		"<dc:subject>ROADS</dc:subject>\r\n" + 
    		"<dc:subject>RURAL ROADS</dc:subject>\r\n" + 
    		"<dc:subject>SANITATION</dc:subject>\r\n" + 
    		"<dc:subject>SANITATION SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>SAVINGS</dc:subject>\r\n" + 
    		"<dc:subject>SERVICE CONTRACTS</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL SERVICE</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>SOLID WASTE COLLECTION</dc:subject>\r\n" + 
    		"<dc:subject>TAX</dc:subject>\r\n" + 
    		"<dc:subject>TAX RATES</dc:subject>\r\n" + 
    		"<dc:subject>TECHNICAL ASSISTANCE</dc:subject>\r\n" + 
    		"<dc:subject>TECHNICAL EXPERTS</dc:subject>\r\n" + 
    		"<dc:subject>TELECOM</dc:subject>\r\n" + 
    		"<dc:subject>TELEPHONE</dc:subject>\r\n" + 
    		"<dc:subject>TERMINAL OPERATORS</dc:subject>\r\n" + 
    		"<dc:subject>TIMBER</dc:subject>\r\n" + 
    		"<dc:subject>TOLL</dc:subject>\r\n" + 
    		"<dc:subject>TRAINS</dc:subject>\r\n" + 
    		"<dc:subject>TRANSACTION</dc:subject>\r\n" + 
    		"<dc:subject>TRANSACTION COSTS</dc:subject>\r\n" + 
    		"<dc:subject>TRANSMISSION</dc:subject>\r\n" + 
    		"<dc:subject>TRANSMISSION LINES</dc:subject>\r\n" + 
    		"<dc:subject>TRANSPARENCY</dc:subject>\r\n" + 
    		"<dc:subject>TRANSPORT</dc:subject>\r\n" + 
    		"<dc:subject>TRANSPORT SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>TRANSPORTATION</dc:subject>\r\n" + 
    		"<dc:subject>TRANSPORTATION NETWORK</dc:subject>\r\n" + 
    		"<dc:subject>TREASURY</dc:subject>\r\n" + 
    		"<dc:subject>USER</dc:subject>\r\n" + 
    		"<dc:subject>USER CHARGES</dc:subject>\r\n" + 
    		"<dc:subject>USES</dc:subject>\r\n" + 
    		"<dc:subject>VALUE CHAINS</dc:subject>\r\n" + 
    		"<dc:subject>WATER DISTRIBUTION</dc:subject>\r\n" + 
    		"<dc:subject>WATER TARIFFS</dc:subject>\r\n" + 
    		"<dc:description>The Government of Liberia is in the\r\n" + 
    		"            process of developing a new Poverty Reduction Strategy (PRS)\r\n" + 
    		"            that is intended to determine its path toward middle-income\r\n" + 
    		"            status. One central aspect of the strategy is likely to be a\r\n" + 
    		"            stronger focus on inclusive growth. This will mean that\r\n" + 
    		"            higher priority will be placed on growing the local private\r\n" + 
    		"            sector, and broadening the base of the economy.\r\n" + 
    		"            Public-private partnerships (PPPs) in infrastructure and\r\n" + 
    		"            services can be a key instrument for achieving these goals\r\n" + 
    		"            especially in an economy like Liberia. The analysis\r\n" + 
    		"            contained in this study identifies the steps toward\r\n" + 
    		"            establishing PPPs as both a policy instrument and method for\r\n" + 
    		"            deepening private sector investment in Liberia.\r\n" + 
    		"            Liberia's rich natural resource endowments have played\r\n" + 
    		"            a fundamental role in the way in which the economy has\r\n" + 
    		"            developed, and in the way in which Government manages\r\n" + 
    		"            private investment in extractive industries. The Government\r\n" + 
    		"            itself has a long history of entering into concession\r\n" + 
    		"            contracts with private investors and operators. Firestone\r\n" + 
    		"            rubber first signed a concession agreement in 1926, and\r\n" + 
    		"            re-signed their concession to last until 2041. More\r\n" + 
    		"            recently, the Government of Liberia has entered into several\r\n" + 
    		"            large natural resource and mining concession contracts that\r\n" + 
    		"            will see large sums of private sector capital invested\r\n" + 
    		"            onshore. This study is one element of a multi-faceted effort\r\n" + 
    		"            to support local private sector and financial sector\r\n" + 
    		"            development in Liberia. It takes into close account the\r\n" + 
    		"            Government's focus on job-creation, the post-conflict\r\n" + 
    		"            dynamics in the country, and Liberia's reliance on\r\n" + 
    		"            extractive industries as a primary source of revenue. The\r\n" + 
    		"            analysis also builds on previous economic sector work that\r\n" + 
    		"            has looked closely at how to stimulate private sector growth\r\n" + 
    		"            and investment, how to support small and medium-size\r\n" + 
    		"            enterprise (SME), and how to leverage existing private\r\n" + 
    		"            sector investment to generate deeper local markets and\r\n" + 
    		"            create new jobs.</dc:description>\r\n" + 
    		"<dc:date>2013-05-06T16:55:37Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06T16:55:37Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10986/2244</dc:identifier>\r\n" + 
    		"<dc:rights>CC BY 3.0 Unported</dc:rights>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/</dc:rights>\r\n" + 
    		"<dc:publisher>World Bank</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/182</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:17:00Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Ading Wiu</dc:title>\r\n" + 
    		"<dc:contributor>AHRC - Arts and Humanities Research Council</dc:contributor>\r\n" + 
    		"<dc:subject>songs</dc:subject>\r\n" + 
    		"<dc:description>The songs in this collection were recorded and annotated as part of the project ‘Metre and Melody in Dinka Speech and Song’, a project carried out by researchers from the University of Edinburgh and the School of Oriental and African Studies in London, and funded by the UK Arts and Humanities Research Council as part of their ‘Beyond Text’ programme. The project aimed to understand the interplay between traditional Dinka musical forms and the Dinka language (which distinguishes words not just by different consonants and vowels but also by means of rhythm, pitch and voice quality), and to learn more about the way the song tradition responded to the disruptions of the long Sudanese civil war. In this context, we aimed to record a large collection of Dinka songs for preservation in a long-term sound archive. This collection is the result of that effort. It presents song material from 36 Dinka singers and groups of singers. Further details can be found in the readme file. The collection is accompanied by an index, which is explained in the readme file.</dc:description>\r\n" + 
    		"<dc:date>2012-09-24T09:21:53Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:17:00Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:17:00Z</dc:date>\r\n" + 
    		"<dc:date>2012-09-24</dc:date>\r\n" + 
    		"<dc:type>sound</dc:type>\r\n" + 
    		"<dc:identifier>Remijsen, Bert; Impey, Angela; Ajuet Deng, Elizabeth Achol; Deng Yak, Simon Yak; Ayuel Ring, Peter Malek; Penn de Ngong, John; Reid, Tatiana; Ladd, D. Robert; Meyerhoff, Miriam. (2012). Ading Wiu, 2009-2012 [sound]. University of Edinburgh, School of Philosophy, Psychology and Language Sciences.</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/182</dc:identifier>\r\n" + 
    		"<dc:language>eng</dc:language>\r\n" + 
    		"<dc:publisher>University of Edinburgh, School of Philosophy, Psychology and Language Sciences</dc:publisher>\r\n" + 
    		"<dc:source>\\\\vienna.ling.ed.ac.uk\\webdata\\nilotic\\songs</dc:source>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:2318/76</identifier>\r\n" + 
    		"                <datestamp>2013-07-09T22:59:24Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_16</setSpec>\r\n" + 
    		"                <setSpec>col_10673_21</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Credit Rationing, Wealth Inequality, and Allocation of Talent</dc:title>\r\n" + 
    		"<dc:subject>Occupational Choice</dc:subject>\r\n" + 
    		"<dc:subject>Adverse Selection</dc:subject>\r\n" + 
    		"<dc:subject>Wealth Distribution</dc:subject>\r\n" + 
    		"<dc:subject>Credit Rationing</dc:subject>\r\n" + 
    		"<dc:description>We study an economy where agents are heterogeneous in terms of observable wealth and unobservable talent. Adverse selection forces creditors to ask for collateral. We study the two-way interaction between rationing in the credit market and the wages offered in the labour market. Both pooling and separating credit contracts can be offered in equilibrium. The minimum wealth needed to obtain a separating contract is decreasing in the wage, whereas the minimum wealth needed for a pooling contract is increasing in the wage. If the first effect dominates, the derived labour demand can be upward sloping, resulting in the possibility of multiple equilibria.</dc:description>\r\n" + 
    		"<dc:description>ICER - International Centre For Economic Research</dc:description>\r\n" + 
    		"<dc:date>2013-07-09T22:59:24Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09T22:59:24Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09</dc:date>\r\n" + 
    		"<dc:type>Preprint</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/2318/76</dc:identifier>\r\n" + 
    		"<dc:relation>Applied Mathematics Working Paper Series</dc:relation>\r\n" + 
    		"<dc:relation>23/2001</dc:relation>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10673/39</identifier>\r\n" + 
    		"                <datestamp>2013-09-10T20:36:27Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_2</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>bnkjgh</dc:title>\r\n" + 
    		"<dc:date>2013-09-10T20:36:27Z</dc:date>\r\n" + 
    		"<dc:date>2013-09-10T20:36:27Z</dc:date>\r\n" + 
    		"<dc:date>1222</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10673/39</dc:identifier>\r\n" + 
    		"<dc:rights>http://creativecommons.org/publicdomain/zero/1.0/</dc:rights>\r\n" + 
    		"<dc:rights>CC0 1.0 Universal</dc:rights>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10986/2245</identifier>\r\n" + 
    		"                <datestamp>2013-05-06T16:55:39Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_18</setSpec>\r\n" + 
    		"                <setSpec>col_10673_19</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Light Manufacturing in Africa :\r\n" + 
    		"            Targeted Policies to Enhance Private Investment and Create Jobs</dc:title>\r\n" + 
    		"<dc:subject>ACCOUNTABILITY</dc:subject>\r\n" + 
    		"<dc:subject>AGRICULTURE</dc:subject>\r\n" + 
    		"<dc:subject>ANALYTICAL APPROACH</dc:subject>\r\n" + 
    		"<dc:subject>ARABLE LAND</dc:subject>\r\n" + 
    		"<dc:subject>AUTOMOBILES</dc:subject>\r\n" + 
    		"<dc:subject>BANK LOANS</dc:subject>\r\n" + 
    		"<dc:subject>BANKS</dc:subject>\r\n" + 
    		"<dc:subject>BARGAINING</dc:subject>\r\n" + 
    		"<dc:subject>BARGAINING POWER</dc:subject>\r\n" + 
    		"<dc:subject>BENCHMARK</dc:subject>\r\n" + 
    		"<dc:subject>BENCHMARKING</dc:subject>\r\n" + 
    		"<dc:subject>BENCHMARKS</dc:subject>\r\n" + 
    		"<dc:subject>BENEFIT ANALYSIS</dc:subject>\r\n" + 
    		"<dc:subject>BORROWING</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESS ENVIRONMENT</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESS ENVIRONMENTS</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESS REGULATIONS</dc:subject>\r\n" + 
    		"<dc:subject>BUSINESS STRATEGY</dc:subject>\r\n" + 
    		"<dc:subject>CAPITAL REQUIREMENTS</dc:subject>\r\n" + 
    		"<dc:subject>CARBON</dc:subject>\r\n" + 
    		"<dc:subject>CERTIFICATE</dc:subject>\r\n" + 
    		"<dc:subject>CHAMBER OF COMMERCE</dc:subject>\r\n" + 
    		"<dc:subject>CLIMATIC CONDITIONS</dc:subject>\r\n" + 
    		"<dc:subject>COMMODITIES</dc:subject>\r\n" + 
    		"<dc:subject>COMMODITY</dc:subject>\r\n" + 
    		"<dc:subject>COMPARATIVE ADVANTAGE</dc:subject>\r\n" + 
    		"<dc:subject>COMPETITIVENESS</dc:subject>\r\n" + 
    		"<dc:subject>CONNECTIVITY</dc:subject>\r\n" + 
    		"<dc:subject>COPYRIGHT</dc:subject>\r\n" + 
    		"<dc:subject>COST OF LIVING</dc:subject>\r\n" + 
    		"<dc:subject>COUNTRY COMPARISONS</dc:subject>\r\n" + 
    		"<dc:subject>CUSTOMS</dc:subject>\r\n" + 
    		"<dc:subject>DEBT</dc:subject>\r\n" + 
    		"<dc:subject>DEMONSTRATION EFFECTS</dc:subject>\r\n" + 
    		"<dc:subject>DEPOSITS</dc:subject>\r\n" + 
    		"<dc:subject>DEVELOPMENT ECONOMICS</dc:subject>\r\n" + 
    		"<dc:subject>E-MAIL</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC ACTIVITY</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC CONDITIONS</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC PERFORMANCE</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC STRUCTURE</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIES OF SCALE</dc:subject>\r\n" + 
    		"<dc:subject>EMPLOYMENT OPPORTUNITIES</dc:subject>\r\n" + 
    		"<dc:subject>ENTERPRISE SURVEY</dc:subject>\r\n" + 
    		"<dc:subject>ENTERPRISE SURVEYS</dc:subject>\r\n" + 
    		"<dc:subject>ENTREPRENEURIAL SKILLS</dc:subject>\r\n" + 
    		"<dc:subject>ENVIRONMENTS</dc:subject>\r\n" + 
    		"<dc:subject>EQUIPMENT</dc:subject>\r\n" + 
    		"<dc:subject>EXPLOITATION</dc:subject>\r\n" + 
    		"<dc:subject>EXPORT GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>EXPORT MARKET</dc:subject>\r\n" + 
    		"<dc:subject>EXPORT MARKETS</dc:subject>\r\n" + 
    		"<dc:subject>EXPORT OPPORTUNITIES</dc:subject>\r\n" + 
    		"<dc:subject>EXPORTS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL INSTITUTIONS</dc:subject>\r\n" + 
    		"<dc:subject>FINANCIAL SUPPORT</dc:subject>\r\n" + 
    		"<dc:subject>FISCAL POLICIES</dc:subject>\r\n" + 
    		"<dc:subject>FISHING</dc:subject>\r\n" + 
    		"<dc:subject>FIXED PRICES</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN DIRECT INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN EXCHANGE</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN INVESTMENTS</dc:subject>\r\n" + 
    		"<dc:subject>FUTURE STUDIES</dc:subject>\r\n" + 
    		"<dc:subject>GDP</dc:subject>\r\n" + 
    		"<dc:subject>GDP PER CAPITA</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL ECONOMY</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL MARKET</dc:subject>\r\n" + 
    		"<dc:subject>GLOBAL MARKETS</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT INTERVENTION</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT POLICY</dc:subject>\r\n" + 
    		"<dc:subject>GROSS DOMESTIC PRODUCT</dc:subject>\r\n" + 
    		"<dc:subject>HOUSING</dc:subject>\r\n" + 
    		"<dc:subject>HUMAN RESOURCE</dc:subject>\r\n" + 
    		"<dc:subject>IMPORT TARIFFS</dc:subject>\r\n" + 
    		"<dc:subject>INCOME</dc:subject>\r\n" + 
    		"<dc:subject>INDUSTRIAL BASE</dc:subject>\r\n" + 
    		"<dc:subject>INDUSTRIAL DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>INDUSTRIAL ECONOMY</dc:subject>\r\n" + 
    		"<dc:subject>INDUSTRIAL STRUCTURE</dc:subject>\r\n" + 
    		"<dc:subject>INDUSTRIALIZATION</dc:subject>\r\n" + 
    		"<dc:subject>INEXPERIENCED WORKERS</dc:subject>\r\n" + 
    		"<dc:subject>INFLATION</dc:subject>\r\n" + 
    		"<dc:subject>INFORMATION TECHNOLOGY</dc:subject>\r\n" + 
    		"<dc:subject>INNOVATION</dc:subject>\r\n" + 
    		"<dc:subject>INPUT PRICES</dc:subject>\r\n" + 
    		"<dc:subject>INSPECTION</dc:subject>\r\n" + 
    		"<dc:subject>INSTITUTION</dc:subject>\r\n" + 
    		"<dc:subject>INSURANCE</dc:subject>\r\n" + 
    		"<dc:subject>JOB CREATION</dc:subject>\r\n" + 
    		"<dc:subject>LABOR COSTS</dc:subject>\r\n" + 
    		"<dc:subject>LABOR DISPUTES</dc:subject>\r\n" + 
    		"<dc:subject>LABOR EFFICIENCY</dc:subject>\r\n" + 
    		"<dc:subject>LABOR FORCE</dc:subject>\r\n" + 
    		"<dc:subject>LABOR LEGISLATION</dc:subject>\r\n" + 
    		"<dc:subject>LABOR PRODUCTIVITY</dc:subject>\r\n" + 
    		"<dc:subject>LABOR RELATIONS</dc:subject>\r\n" + 
    		"<dc:subject>LAND PRICES</dc:subject>\r\n" + 
    		"<dc:subject>LAND USE</dc:subject>\r\n" + 
    		"<dc:subject>LAWS</dc:subject>\r\n" + 
    		"<dc:subject>LEGISLATION</dc:subject>\r\n" + 
    		"<dc:subject>LICENSES</dc:subject>\r\n" + 
    		"<dc:subject>LIMITED ACCESS</dc:subject>\r\n" + 
    		"<dc:subject>LOCAL GOVERNMENT</dc:subject>\r\n" + 
    		"<dc:subject>LOCAL GOVERNMENTS</dc:subject>\r\n" + 
    		"<dc:subject>LOGGING</dc:subject>\r\n" + 
    		"<dc:subject>MACROECONOMIC STABILITY</dc:subject>\r\n" + 
    		"<dc:subject>MANAGERIAL SKILLS</dc:subject>\r\n" + 
    		"<dc:subject>MANUFACTURING</dc:subject>\r\n" + 
    		"<dc:subject>MANUFACTURING INDUSTRIES</dc:subject>\r\n" + 
    		"<dc:subject>MARKET INFORMATION</dc:subject>\r\n" + 
    		"<dc:subject>MARKET LIBERALIZATION</dc:subject>\r\n" + 
    		"<dc:subject>MARKET SHARE</dc:subject>\r\n" + 
    		"<dc:subject>MARKETING</dc:subject>\r\n" + 
    		"<dc:subject>MATERIAL</dc:subject>\r\n" + 
    		"<dc:subject>MEDIUM ENTERPRISES</dc:subject>\r\n" + 
    		"<dc:subject>METALS</dc:subject>\r\n" + 
    		"<dc:subject>MIGRATION</dc:subject>\r\n" + 
    		"<dc:subject>MONOPOLIES</dc:subject>\r\n" + 
    		"<dc:subject>NATURAL RESOURCES</dc:subject>\r\n" + 
    		"<dc:subject>NEW ENTRANTS</dc:subject>\r\n" + 
    		"<dc:subject>OCCUPATIONS</dc:subject>\r\n" + 
    		"<dc:subject>OIL</dc:subject>\r\n" + 
    		"<dc:subject>ONE-STOP SHOPS</dc:subject>\r\n" + 
    		"<dc:subject>OPEN ECONOMIES</dc:subject>\r\n" + 
    		"<dc:subject>OUTPUTS</dc:subject>\r\n" + 
    		"<dc:subject>PAYOUTS</dc:subject>\r\n" + 
    		"<dc:subject>PDF</dc:subject>\r\n" + 
    		"<dc:subject>PENALTIES</dc:subject>\r\n" + 
    		"<dc:subject>PENSION PLANS</dc:subject>\r\n" + 
    		"<dc:subject>PER CAPITA INCOME</dc:subject>\r\n" + 
    		"<dc:subject>PHOTO</dc:subject>\r\n" + 
    		"<dc:subject>PHYSICAL INFRASTRUCTURE</dc:subject>\r\n" + 
    		"<dc:subject>POLICY MAKERS</dc:subject>\r\n" + 
    		"<dc:subject>POLITICAL ECONOMY</dc:subject>\r\n" + 
    		"<dc:subject>PRICE CONTROLS</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE PARTNERSHIPS</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE SECTOR DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE SECTOR INVESTMENTS</dc:subject>\r\n" + 
    		"<dc:subject>PRODUCERS</dc:subject>\r\n" + 
    		"<dc:subject>PRODUCTION COSTS</dc:subject>\r\n" + 
    		"<dc:subject>PRODUCTIVITY</dc:subject>\r\n" + 
    		"<dc:subject>PRODUCTIVITY GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>PROVEN RESERVES</dc:subject>\r\n" + 
    		"<dc:subject>PURCHASING POWER</dc:subject>\r\n" + 
    		"<dc:subject>QUALITY MANAGEMENT</dc:subject>\r\n" + 
    		"<dc:subject>QUERIES</dc:subject>\r\n" + 
    		"<dc:subject>RENT SEEKING</dc:subject>\r\n" + 
    		"<dc:subject>RESULT</dc:subject>\r\n" + 
    		"<dc:subject>RESULTS</dc:subject>\r\n" + 
    		"<dc:subject>RETAIL TRADE</dc:subject>\r\n" + 
    		"<dc:subject>SAFETY</dc:subject>\r\n" + 
    		"<dc:subject>SAFETY NET</dc:subject>\r\n" + 
    		"<dc:subject>SAFETY NETS</dc:subject>\r\n" + 
    		"<dc:subject>SAVINGS</dc:subject>\r\n" + 
    		"<dc:subject>SEARCH</dc:subject>\r\n" + 
    		"<dc:subject>SKILLED LABOR</dc:subject>\r\n" + 
    		"<dc:subject>SKILLED WORKERS</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>STRUCTURAL CHANGE</dc:subject>\r\n" + 
    		"<dc:subject>SUBSIDIARY</dc:subject>\r\n" + 
    		"<dc:subject>SUPERVISION</dc:subject>\r\n" + 
    		"<dc:subject>SUPPLIERS</dc:subject>\r\n" + 
    		"<dc:subject>SUPPLY CHAIN</dc:subject>\r\n" + 
    		"<dc:subject>SUSTAINABLE GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>TAX REVENUE</dc:subject>\r\n" + 
    		"<dc:subject>TAX REVENUES</dc:subject>\r\n" + 
    		"<dc:subject>TECHNICAL ASSISTANCE</dc:subject>\r\n" + 
    		"<dc:subject>TECHNICAL SKILLS</dc:subject>\r\n" + 
    		"<dc:subject>TECHNICAL SUPPORT</dc:subject>\r\n" + 
    		"<dc:subject>TECHNICAL TRAINING</dc:subject>\r\n" + 
    		"<dc:subject>TELECOMMUNICATIONS</dc:subject>\r\n" + 
    		"<dc:subject>TELEPHONE</dc:subject>\r\n" + 
    		"<dc:subject>TIMBER</dc:subject>\r\n" + 
    		"<dc:subject>TIME FRAME</dc:subject>\r\n" + 
    		"<dc:subject>TRADE LIBERALIZATION</dc:subject>\r\n" + 
    		"<dc:subject>TRAINING COSTS</dc:subject>\r\n" + 
    		"<dc:subject>TRANSACTION</dc:subject>\r\n" + 
    		"<dc:subject>TRANSACTION COSTS</dc:subject>\r\n" + 
    		"<dc:subject>TRANSPORT</dc:subject>\r\n" + 
    		"<dc:subject>UNEMPLOYMENT</dc:subject>\r\n" + 
    		"<dc:subject>UNSKILLED WORKERS</dc:subject>\r\n" + 
    		"<dc:subject>USES</dc:subject>\r\n" + 
    		"<dc:subject>VALUE ADDED</dc:subject>\r\n" + 
    		"<dc:subject>VALUE CHAIN</dc:subject>\r\n" + 
    		"<dc:subject>VALUE CHAINS</dc:subject>\r\n" + 
    		"<dc:subject>WAGE RATES</dc:subject>\r\n" + 
    		"<dc:subject>WAGES</dc:subject>\r\n" + 
    		"<dc:subject>WAN</dc:subject>\r\n" + 
    		"<dc:subject>WEB</dc:subject>\r\n" + 
    		"<dc:subject>WILLINGNESS TO PAY</dc:subject>\r\n" + 
    		"<dc:description>The World Bank's strategy for\r\n" + 
    		"            Africa's future recognizes the central importance of\r\n" + 
    		"            industrialization in Sub-Saharan Africa, and the consequent\r\n" + 
    		"            creation of productive jobs for Africans, which have long\r\n" + 
    		"            been a preoccupation of African leaders and policy makers.\r\n" + 
    		"            This book represents an attempt to address these issues. The\r\n" + 
    		"            book stresses that, while the recent turnaround in\r\n" + 
    		"            Africa's economic growth is encouraging, this growth\r\n" + 
    		"            must be accompanied by structural transformation to be\r\n" + 
    		"            sustainable and to create productive employment for its\r\n" + 
    		"            people. For many African countries, this transformation\r\n" + 
    		"            involves lifting workers from low-productivity agriculture\r\n" + 
    		"            and informal sectors into higher productivity activities.\r\n" + 
    		"            Light manufacturing can offer a viable solution for\r\n" + 
    		"            Sub-Saharan Africa, given its potential competitiveness that\r\n" + 
    		"            is based on low wage costs and abundance of natural\r\n" + 
    		"            resources that supply raw materials needed for industries.\r\n" + 
    		"            This study has five features that distinguish it from\r\n" + 
    		"            previous studies. First, the detailed studies on light\r\n" + 
    		"            manufacturing at the subsector and product levels in five\r\n" + 
    		"            countries provide in-depth cost comparisons between Asia and\r\n" + 
    		"            Africa. Second, building on a growing body of work, the\r\n" + 
    		"            report uses a wide array of quantitative and qualitative\r\n" + 
    		"            techniques, including quantitative surveys and value chain\r\n" + 
    		"            analysis, to identify key constraints to enterprises and to\r\n" + 
    		"            evaluate differences in firm performance across countries.\r\n" + 
    		"            Third, the findings that firm constraints vary by country,\r\n" + 
    		"            sector, and firm size led us to adopt a targeted approach to\r\n" + 
    		"            identifying constraints and combining market-based measures\r\n" + 
    		"            and selected government interventions to remove them.\r\n" + 
    		"            Fourth, the solution to light manufacturing problems cuts\r\n" + 
    		"            across many sectors and does not lie only in manufacturing\r\n" + 
    		"            alone. Solving the problem of manufacturing inputs requires\r\n" + 
    		"            solving specific issues in agriculture, education, and\r\n" + 
    		"            infrastructure. Fifth, the report draws on experiences and\r\n" + 
    		"            solutions from other developing countries to inform its\r\n" + 
    		"            recommendations. The report's goal is to find practical\r\n" + 
    		"            ways to increase employment and spur job creation in\r\n" + 
    		"            Sub-Saharan Africa.</dc:description>\r\n" + 
    		"<dc:date>2013-05-06T16:55:39Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06T16:55:39Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10986/2245</dc:identifier>\r\n" + 
    		"<dc:relation>Africa Development Forum</dc:relation>\r\n" + 
    		"<dc:rights>CC BY 3.0 Unported</dc:rights>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/</dc:rights>\r\n" + 
    		"<dc:publisher>World Bank</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/184</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:17:00Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Ageer Padang Men</dc:title>\r\n" + 
    		"<dc:contributor>AHRC - Arts and Humanities Research Council</dc:contributor>\r\n" + 
    		"<dc:subject>songs</dc:subject>\r\n" + 
    		"<dc:description>The songs in this collection were recorded and annotated as part of the project ‘Metre and Melody in Dinka Speech and Song’, a project carried out by researchers from the University of Edinburgh and the School of Oriental and African Studies in London, and funded by the UK Arts and Humanities Research Council as part of their ‘Beyond Text’ programme. The project aimed to understand the interplay between traditional Dinka musical forms and the Dinka language (which distinguishes words not just by different consonants and vowels but also by means of rhythm, pitch and voice quality), and to learn more about the way the song tradition responded to the disruptions of the long Sudanese civil war. In this context, we aimed to record a large collection of Dinka songs for preservation in a long-term sound archive. This collection is the result of that effort. It presents song material from 36 Dinka singers and groups of singers. Further details can be found in the readme file. The collection is accompanied by an index, which is explained in the readme file.</dc:description>\r\n" + 
    		"<dc:date>2012-09-24T09:33:16Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:17:00Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:17:00Z</dc:date>\r\n" + 
    		"<dc:date>2012-09-24</dc:date>\r\n" + 
    		"<dc:type>sound</dc:type>\r\n" + 
    		"<dc:identifier>Remijsen, Bert; Impey, Angela; Ajuet Deng, Elizabeth Achol; Deng Yak, Simon Yak; Ayuel Ring, Peter Malek; Penn de Ngong, John; Reid, Tatiana; Ladd, D. Robert; Meyerhoff, Miriam. (2012). Ageer Padang Men, 2009-2012 [sound]. University of Edinburgh, School of Philosophy, Psychology and Language Sciences.</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/184</dc:identifier>\r\n" + 
    		"<dc:language>eng</dc:language>\r\n" + 
    		"<dc:publisher>University of Edinburgh, School of Philosophy, Psychology and Language Sciences</dc:publisher>\r\n" + 
    		"<dc:source>\\\\vienna.ling.ed.ac.uk\\webdata\\nilotic\\songs</dc:source>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:2318/77</identifier>\r\n" + 
    		"                <datestamp>2013-07-09T22:59:25Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_16</setSpec>\r\n" + 
    		"                <setSpec>col_10673_21</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>BV As A Dual Space</dc:title>\r\n" + 
    		"<dc:subject>Set functions</dc:subject>\r\n" + 
    		"<dc:subject>Duality</dc:subject>\r\n" + 
    		"<dc:subject>Compactness</dc:subject>\r\n" + 
    		"<dc:subject>Coalitional games</dc:subject>\r\n" + 
    		"<dc:description>Let C be a field of subsets of a set I. It is well known that the space FA of all the finitely\r\n" + 
    		"additive games of bounded variation on C is the norm dual of the space of all simple functions\r\n" + 
    		"on C. In this paper we prove that the space BV of all the games of bounded variation on\r\n" + 
    		"C is the norm dual of the space of all simple games on C. This result is equivalent to the\r\n" + 
    		"compactness of the unit ball in BV with respect to the vague topology.</dc:description>\r\n" + 
    		"<dc:description>ICER - International Centre For Economic Research</dc:description>\r\n" + 
    		"<dc:date>2013-07-09T22:59:25Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09T22:59:25Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09</dc:date>\r\n" + 
    		"<dc:type>Articolo</dc:type>\r\n" + 
    		"<dc:type>Preprint</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/2318/77</dc:identifier>\r\n" + 
    		"<dc:relation>Applied Mathematics Working Paper Series</dc:relation>\r\n" + 
    		"<dc:relation>29/2001</dc:relation>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10986/2246</identifier>\r\n" + 
    		"                <datestamp>2013-05-06T16:55:41Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_18</setSpec>\r\n" + 
    		"                <setSpec>col_10673_19</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>The Cash Dividend : The Rise of Cash\r\n" + 
    		"            Transfer Programs in Sub-Saharan Africa</dc:title>\r\n" + 
    		"<dc:subject>ACCESS TO FINANCING</dc:subject>\r\n" + 
    		"<dc:subject>ACCOUNTING</dc:subject>\r\n" + 
    		"<dc:subject>ACTIVE LABOR</dc:subject>\r\n" + 
    		"<dc:subject>ACTIVE LABOR MARKET</dc:subject>\r\n" + 
    		"<dc:subject>ACTIVE LABOR MARKET POLICIES</dc:subject>\r\n" + 
    		"<dc:subject>ACTIVE LABOR MARKET POLICY</dc:subject>\r\n" + 
    		"<dc:subject>ADVANCED ECONOMIES</dc:subject>\r\n" + 
    		"<dc:subject>ADVERSE EFFECTS</dc:subject>\r\n" + 
    		"<dc:subject>AGGREGATE DEMAND</dc:subject>\r\n" + 
    		"<dc:subject>AGRICULTURAL SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>AVERAGE WAGES</dc:subject>\r\n" + 
    		"<dc:subject>BANK FINANCING</dc:subject>\r\n" + 
    		"<dc:subject>BRIBES</dc:subject>\r\n" + 
    		"<dc:subject>CAPITAL FLOWS</dc:subject>\r\n" + 
    		"<dc:subject>CAPITAL INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>CAPITAL MARKETS</dc:subject>\r\n" + 
    		"<dc:subject>CENTRAL BANKS</dc:subject>\r\n" + 
    		"<dc:subject>COLLECTIVE BARGAINING</dc:subject>\r\n" + 
    		"<dc:subject>COMPETITIVE PRESSURE</dc:subject>\r\n" + 
    		"<dc:subject>COMPETITIVE PRESSURES</dc:subject>\r\n" + 
    		"<dc:subject>CONTRACT ENFORCEMENT</dc:subject>\r\n" + 
    		"<dc:subject>CORRUPTION</dc:subject>\r\n" + 
    		"<dc:subject>CREDIT ACCESS</dc:subject>\r\n" + 
    		"<dc:subject>DEBT</dc:subject>\r\n" + 
    		"<dc:subject>DEVELOPMENT BANK</dc:subject>\r\n" + 
    		"<dc:subject>DEVELOPMENT ECONOMICS</dc:subject>\r\n" + 
    		"<dc:subject>DISADVANTAGED GROUPS</dc:subject>\r\n" + 
    		"<dc:subject>DISTRIBUTION OF INCOME</dc:subject>\r\n" + 
    		"<dc:subject>DOMESTIC MARKET</dc:subject>\r\n" + 
    		"<dc:subject>DOWNSIDE RISKS</dc:subject>\r\n" + 
    		"<dc:subject>EARNINGS</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC DOWNTURNS</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIC RECOVERY</dc:subject>\r\n" + 
    		"<dc:subject>ECONOMIES OF SCALE</dc:subject>\r\n" + 
    		"<dc:subject>EDUCATIONAL ATTAINMENTS</dc:subject>\r\n" + 
    		"<dc:subject>EMERGING MARKET ECONOMIES</dc:subject>\r\n" + 
    		"<dc:subject>EMERGING MARKETS</dc:subject>\r\n" + 
    		"<dc:subject>EMPLOYABILITY</dc:subject>\r\n" + 
    		"<dc:subject>EMPLOYEE</dc:subject>\r\n" + 
    		"<dc:subject>EMPLOYMENT</dc:subject>\r\n" + 
    		"<dc:subject>EMPLOYMENT GENERATION</dc:subject>\r\n" + 
    		"<dc:subject>EMPLOYMENT GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>EMPLOYMENT IMPACT</dc:subject>\r\n" + 
    		"<dc:subject>EMPLOYMENT POLICIES</dc:subject>\r\n" + 
    		"<dc:subject>EMPLOYMENT PROGRAMS</dc:subject>\r\n" + 
    		"<dc:subject>EMPLOYMENT PROTECTION LEGISLATION</dc:subject>\r\n" + 
    		"<dc:subject>EMPLOYMENT RATE</dc:subject>\r\n" + 
    		"<dc:subject>EMPLOYMENT SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>EMPLOYMENT SHARE</dc:subject>\r\n" + 
    		"<dc:subject>EMPLOYMENT SUBSIDIES</dc:subject>\r\n" + 
    		"<dc:subject>EMPLOYMENT TRENDS</dc:subject>\r\n" + 
    		"<dc:subject>ENTREPRENEURIAL ACTIVITY</dc:subject>\r\n" + 
    		"<dc:subject>ENTREPRENEURSHIP</dc:subject>\r\n" + 
    		"<dc:subject>EXTERNALITIES</dc:subject>\r\n" + 
    		"<dc:subject>EXTREME POVERTY</dc:subject>\r\n" + 
    		"<dc:subject>FEMALE ENTREPRENEURS</dc:subject>\r\n" + 
    		"<dc:subject>FEMALE ENTREPRENEURSHIP</dc:subject>\r\n" + 
    		"<dc:subject>FEMALE LABOR</dc:subject>\r\n" + 
    		"<dc:subject>FEMALE LABOR FORCE</dc:subject>\r\n" + 
    		"<dc:subject>FIRM PERFORMANCE</dc:subject>\r\n" + 
    		"<dc:subject>FISCAL POLICY</dc:subject>\r\n" + 
    		"<dc:subject>FOREIGN DIRECT INVESTMENT</dc:subject>\r\n" + 
    		"<dc:subject>GENDER</dc:subject>\r\n" + 
    		"<dc:subject>GENDER DIFFERENCES</dc:subject>\r\n" + 
    		"<dc:subject>GENDER GAP</dc:subject>\r\n" + 
    		"<dc:subject>GENDER INEQUALITIES</dc:subject>\r\n" + 
    		"<dc:subject>GENDER ROLES</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT INTERVENTION</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT POLICIES</dc:subject>\r\n" + 
    		"<dc:subject>GOVERNMENT POLICY</dc:subject>\r\n" + 
    		"<dc:subject>HIGH UNEMPLOYMENT</dc:subject>\r\n" + 
    		"<dc:subject>HOUSEHOLD DEBT</dc:subject>\r\n" + 
    		"<dc:subject>HOUSEHOLD INCOME</dc:subject>\r\n" + 
    		"<dc:subject>HOUSING</dc:subject>\r\n" + 
    		"<dc:subject>HUMAN CAPITAL</dc:subject>\r\n" + 
    		"<dc:subject>ID</dc:subject>\r\n" + 
    		"<dc:subject>INCOME DISTRIBUTION</dc:subject>\r\n" + 
    		"<dc:subject>INCOME GROUP</dc:subject>\r\n" + 
    		"<dc:subject>INCOME INEQUALITIES</dc:subject>\r\n" + 
    		"<dc:subject>INCOME INEQUALITY</dc:subject>\r\n" + 
    		"<dc:subject>INDUSTRIAL WAGE</dc:subject>\r\n" + 
    		"<dc:subject>INFLATION</dc:subject>\r\n" + 
    		"<dc:subject>INFORMAL ECONOMY</dc:subject>\r\n" + 
    		"<dc:subject>INFORMAL EMPLOYMENT</dc:subject>\r\n" + 
    		"<dc:subject>INFORMAL LABOR MARKET</dc:subject>\r\n" + 
    		"<dc:subject>INFORMAL SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>INNOVATION</dc:subject>\r\n" + 
    		"<dc:subject>INTERNATIONAL BANK</dc:subject>\r\n" + 
    		"<dc:subject>JOB CREATION</dc:subject>\r\n" + 
    		"<dc:subject>JOB MATCH</dc:subject>\r\n" + 
    		"<dc:subject>JOB MATCHES</dc:subject>\r\n" + 
    		"<dc:subject>JOB OPPORTUNITIES</dc:subject>\r\n" + 
    		"<dc:subject>JOB SEEKERS</dc:subject>\r\n" + 
    		"<dc:subject>JOB TRAINING</dc:subject>\r\n" + 
    		"<dc:subject>JOBS</dc:subject>\r\n" + 
    		"<dc:subject>KEY CHALLENGES</dc:subject>\r\n" + 
    		"<dc:subject>KNOWLEDGE ECONOMY</dc:subject>\r\n" + 
    		"<dc:subject>LABOR DEMAND</dc:subject>\r\n" + 
    		"<dc:subject>LABOR FORCE</dc:subject>\r\n" + 
    		"<dc:subject>LABOR FORCE PARTICIPATION</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MARKET</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MARKET CONDITIONS</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MARKET INDICATORS</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MARKET INSTITUTIONS</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MARKET ISSUES</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MARKET OUTCOME</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MARKET OUTCOMES</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MARKET PERFORMANCE</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MARKET POLICIES</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MARKET REGULATION</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MARKET REGULATIONS</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MARKET SITUATION</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MARKETS</dc:subject>\r\n" + 
    		"<dc:subject>LABOR MOBILITY</dc:subject>\r\n" + 
    		"<dc:subject>LABOR POLICIES</dc:subject>\r\n" + 
    		"<dc:subject>LABOR PRODUCTIVITY</dc:subject>\r\n" + 
    		"<dc:subject>LABOR PRODUCTIVITY GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>LABOR REGULATION</dc:subject>\r\n" + 
    		"<dc:subject>LABOR REGULATIONS</dc:subject>\r\n" + 
    		"<dc:subject>LABOR SUPPLY</dc:subject>\r\n" + 
    		"<dc:subject>LABOUR</dc:subject>\r\n" + 
    		"<dc:subject>LABOUR TURNOVER</dc:subject>\r\n" + 
    		"<dc:subject>LAWS</dc:subject>\r\n" + 
    		"<dc:subject>LAYOFFS</dc:subject>\r\n" + 
    		"<dc:subject>LIVING STANDARDS</dc:subject>\r\n" + 
    		"<dc:subject>LOAN</dc:subject>\r\n" + 
    		"<dc:subject>LONG-TERM UNEMPLOYMENT</dc:subject>\r\n" + 
    		"<dc:subject>MACROECONOMIC STABILITY</dc:subject>\r\n" + 
    		"<dc:subject>MACROECONOMICS</dc:subject>\r\n" + 
    		"<dc:subject>MANPOWER</dc:subject>\r\n" + 
    		"<dc:subject>MANPOWER POLICY</dc:subject>\r\n" + 
    		"<dc:subject>MARKET ECONOMIES</dc:subject>\r\n" + 
    		"<dc:subject>MARKET ENTRY</dc:subject>\r\n" + 
    		"<dc:subject>MARKET FAILURES</dc:subject>\r\n" + 
    		"<dc:subject>MIGRATION</dc:subject>\r\n" + 
    		"<dc:subject>MINIMUM WAGE</dc:subject>\r\n" + 
    		"<dc:subject>MINIMUM WAGES</dc:subject>\r\n" + 
    		"<dc:subject>MONETARY POLICIES</dc:subject>\r\n" + 
    		"<dc:subject>MONETARY POLICY</dc:subject>\r\n" + 
    		"<dc:subject>MORAL HAZARD</dc:subject>\r\n" + 
    		"<dc:subject>NATURAL RESOURCES</dc:subject>\r\n" + 
    		"<dc:subject>OCCUPATIONS</dc:subject>\r\n" + 
    		"<dc:subject>OPERATING PERMITS</dc:subject>\r\n" + 
    		"<dc:subject>OUTREACH</dc:subject>\r\n" + 
    		"<dc:subject>PASSIVE LABOR</dc:subject>\r\n" + 
    		"<dc:subject>POLITICAL ECONOMY</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE EMPLOYMENT</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE PARTNERSHIPS</dc:subject>\r\n" + 
    		"<dc:subject>PRIVATE SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>PRODUCTIVE WORK</dc:subject>\r\n" + 
    		"<dc:subject>PRODUCTIVITY GROWTH</dc:subject>\r\n" + 
    		"<dc:subject>PROGRAM DESIGN</dc:subject>\r\n" + 
    		"<dc:subject>PROPERTY RIGHTS</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC EMPLOYMENT</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC POLICY</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC SERVICES</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC WORKS</dc:subject>\r\n" + 
    		"<dc:subject>PUBLIC-PRIVATE PARTNERSHIPS</dc:subject>\r\n" + 
    		"<dc:subject>PURCHASING POWER</dc:subject>\r\n" + 
    		"<dc:subject>REAL INTEREST RATES</dc:subject>\r\n" + 
    		"<dc:subject>REAL WAGE</dc:subject>\r\n" + 
    		"<dc:subject>REAL WAGES</dc:subject>\r\n" + 
    		"<dc:subject>RECESSION</dc:subject>\r\n" + 
    		"<dc:subject>REGULATORY POLICY</dc:subject>\r\n" + 
    		"<dc:subject>RENTS</dc:subject>\r\n" + 
    		"<dc:subject>SAFETY</dc:subject>\r\n" + 
    		"<dc:subject>SAFETY NET</dc:subject>\r\n" + 
    		"<dc:subject>SAFETY NETS</dc:subject>\r\n" + 
    		"<dc:subject>SAVINGS</dc:subject>\r\n" + 
    		"<dc:subject>SERVICE EMPLOYMENT</dc:subject>\r\n" + 
    		"<dc:subject>SERVICE INDUSTRIES</dc:subject>\r\n" + 
    		"<dc:subject>SERVICE SECTOR</dc:subject>\r\n" + 
    		"<dc:subject>SKILLS DEVELOPMENT</dc:subject>\r\n" + 
    		"<dc:subject>SMALL BUSINESS</dc:subject>\r\n" + 
    		"<dc:subject>SMALL BUSINESSES</dc:subject>\r\n" + 
    		"<dc:subject>SMALL ENTERPRISES</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL COHESION</dc:subject>\r\n" + 
    		"<dc:subject>SOCIAL SECURITY</dc:subject>\r\n" + 
    		"<dc:subject>SUBSIDIARY</dc:subject>\r\n" + 
    		"<dc:subject>TAX ADMINISTRATION</dc:subject>\r\n" + 
    		"<dc:subject>TAXATION</dc:subject>\r\n" + 
    		"<dc:subject>TEMPORARY WORKERS</dc:subject>\r\n" + 
    		"<dc:subject>TOTAL EMPLOYMENT</dc:subject>\r\n" + 
    		"<dc:subject>TRADE LIBERALIZATION</dc:subject>\r\n" + 
    		"<dc:subject>TRADE UNIONS</dc:subject>\r\n" + 
    		"<dc:subject>TRAINING POLICIES</dc:subject>\r\n" + 
    		"<dc:subject>TRAINING PROGRAMS</dc:subject>\r\n" + 
    		"<dc:subject>TRANSITION ECONOMIES</dc:subject>\r\n" + 
    		"<dc:subject>TRANSPORT</dc:subject>\r\n" + 
    		"<dc:subject>UNEMPLOYED</dc:subject>\r\n" + 
    		"<dc:subject>UNEMPLOYMENT</dc:subject>\r\n" + 
    		"<dc:subject>UNEMPLOYMENT ASSISTANCE</dc:subject>\r\n" + 
    		"<dc:subject>UNEMPLOYMENT BENEFITS</dc:subject>\r\n" + 
    		"<dc:subject>UNEMPLOYMENT INSURANCE</dc:subject>\r\n" + 
    		"<dc:subject>UNEMPLOYMENT RATE</dc:subject>\r\n" + 
    		"<dc:subject>UNEMPLOYMENT RATES</dc:subject>\r\n" + 
    		"<dc:subject>UNION</dc:subject>\r\n" + 
    		"<dc:subject>UNPAID FAMILY WORKERS</dc:subject>\r\n" + 
    		"<dc:subject>VENTURE CREATION</dc:subject>\r\n" + 
    		"<dc:subject>WAGE DETERMINATION</dc:subject>\r\n" + 
    		"<dc:subject>WAGE INEQUALITY</dc:subject>\r\n" + 
    		"<dc:subject>WAGE LEVEL</dc:subject>\r\n" + 
    		"<dc:subject>WAGE PREMIUM</dc:subject>\r\n" + 
    		"<dc:subject>WAGE RATE</dc:subject>\r\n" + 
    		"<dc:subject>WAGE RATES</dc:subject>\r\n" + 
    		"<dc:subject>WOMEN ENTREPRENEURS</dc:subject>\r\n" + 
    		"<dc:subject>WORKER</dc:subject>\r\n" + 
    		"<dc:subject>WORKERS</dc:subject>\r\n" + 
    		"<dc:subject>WORKING CONDITIONS</dc:subject>\r\n" + 
    		"<dc:subject>WORKING HOURS</dc:subject>\r\n" + 
    		"<dc:subject>WORKING POOR</dc:subject>\r\n" + 
    		"<dc:subject>YOUTH EMPLOYMENT</dc:subject>\r\n" + 
    		"<dc:subject>YOUTH UNEMPLOYMENT</dc:subject>\r\n" + 
    		"<dc:subject>YOUTH UNEMPLOYMENT RATE</dc:subject>\r\n" + 
    		"<dc:description>The results of the review do not\r\n" + 
    		"            disappoint. The authors identified more than 120 cash\r\n" + 
    		"            transfer programs that were implemented between 2000 and\r\n" + 
    		"            mid-2009 in Sub-Saharan Africa. These programs have varying\r\n" + 
    		"            objectives, targeting, scale, conditions, technologies, and\r\n" + 
    		"            more. A sizable number of these programs conducted robust\r\n" + 
    		"            impact evaluations that provide important information,\r\n" + 
    		"            presented here, on the merits of cash transfer programs and\r\n" + 
    		"            their specific design features in the African context. The\r\n" + 
    		"            authors present summary information on programs, often in\r\n" + 
    		"            useful graphs, and provide detailed reference material in\r\n" + 
    		"            the appendixes. They highlight how many of the cash transfer\r\n" + 
    		"            programs in Africa that had not yet begun implementation at\r\n" + 
    		"            the time of writing will continue to provide important\r\n" + 
    		"            evaluation results that will guide the design of cash\r\n" + 
    		"            transfer programs in the region. In addition to presenting\r\n" + 
    		"            data and analysis on the mechanics of the programs, the\r\n" + 
    		"            authors discuss issues related to political economy. They\r\n" + 
    		"            highlight the importance of addressing key tradeoffs in cash\r\n" + 
    		"            transfers, political will, and buy-in, and they emphasize\r\n" + 
    		"            the need to build evidence-based debates on cash transfer\r\n" + 
    		"            programs. Useful anecdotes and discussion illustrate how\r\n" + 
    		"            some programs have dealt with these issues with varying\r\n" + 
    		"            degrees of success. This text will serve as a useful\r\n" + 
    		"            reference for years to come for those interested in large-\r\n" + 
    		"            and small-scale issues of cash transfer implementation, both\r\n" + 
    		"            in Africa and beyond. However, the book is not an end in\r\n" + 
    		"            itself. It also raises important questions that must be\r\n" + 
    		"            addressed and knowledge gaps that must be filled. Therefore,\r\n" + 
    		"            it is useful both in the information it provides and in the\r\n" + 
    		"            issues and questions it raises.</dc:description>\r\n" + 
    		"<dc:date>2013-05-06T16:55:41Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06T16:55:41Z</dc:date>\r\n" + 
    		"<dc:date>2013-05-06</dc:date>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10986/2246</dc:identifier>\r\n" + 
    		"<dc:relation>Directions in Development ; human development</dc:relation>\r\n" + 
    		"<dc:rights>CC BY 3.0 Unported</dc:rights>\r\n" + 
    		"<dc:rights>http://creativecommons.org/licenses/by/3.0/</dc:rights>\r\n" + 
    		"<dc:publisher>World Bank</dc:publisher>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:10283/185</identifier>\r\n" + 
    		"                <datestamp>2013-06-17T07:17:01Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_1</setSpec>\r\n" + 
    		"                <setSpec>col_10673_11</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Ageer Padang Women</dc:title>\r\n" + 
    		"<dc:contributor>AHRC - Arts and Humanities Research Council</dc:contributor>\r\n" + 
    		"<dc:subject>songs</dc:subject>\r\n" + 
    		"<dc:description>The songs in this collection were recorded and annotated as part of the project ‘Metre and Melody in Dinka Speech and Song’, a project carried out by researchers from the University of Edinburgh and the School of Oriental and African Studies in London, and funded by the UK Arts and Humanities Research Council as part of their ‘Beyond Text’ programme. The project aimed to understand the interplay between traditional Dinka musical forms and the Dinka language (which distinguishes words not just by different consonants and vowels but also by means of rhythm, pitch and voice quality), and to learn more about the way the song tradition responded to the disruptions of the long Sudanese civil war. In this context, we aimed to record a large collection of Dinka songs for preservation in a long-term sound archive. This collection is the result of that effort. It presents song material from 36 Dinka singers and groups of singers. Further details can be found in the readme file. The collection is accompanied by an index, which is explained in the readme file.</dc:description>\r\n" + 
    		"<dc:date>2012-09-24T09:34:00Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:17:01Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T00:15:05Z</dc:date>\r\n" + 
    		"<dc:date>2013-06-17T07:17:01Z</dc:date>\r\n" + 
    		"<dc:date>2012-09-24</dc:date>\r\n" + 
    		"<dc:type>sound</dc:type>\r\n" + 
    		"<dc:identifier>Remijsen, Bert; Impey, Angela; Ajuet Deng, Elizabeth Achol; Deng Yak, Simon Yak; Ayuel Ring, Peter Malek; Penn de Ngong, John; Reid, Tatiana; Ladd, D. Robert; Meyerhoff, Miriam. (2012). Ageer Padang Women, 2009-2012 [sound]. University of Edinburgh, School of Philosophy, Psychology and Language Sciences.</dc:identifier>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/10283/185</dc:identifier>\r\n" + 
    		"<dc:language>eng</dc:language>\r\n" + 
    		"<dc:publisher>University of Edinburgh, School of Philosophy, Psychology and Language Sciences</dc:publisher>\r\n" + 
    		"<dc:source>\\\\vienna.ling.ed.ac.uk\\webdata\\nilotic\\songs</dc:source>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
    		"        <record>\r\n" + 
    		"            <header>\r\n" + 
    		"                <identifier>oai:demo.dspace.org:2318/78</identifier>\r\n" + 
    		"                <datestamp>2013-07-09T22:59:26Z</datestamp>\r\n" + 
    		"                <setSpec>com_10673_16</setSpec>\r\n" + 
    		"                <setSpec>col_10673_21</setSpec>\r\n" + 
    		"            </header>\r\n" + 
    		"            <metadata><oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\r\n" + 
    		"<dc:title>Yaari dual theory without the completeness axiom</dc:title>\r\n" + 
    		"<dc:subject>Yaari dual theory</dc:subject>\r\n" + 
    		"<dc:subject>Incomplete preferences</dc:subject>\r\n" + 
    		"<dc:subject>Stochastic orders</dc:subject>\r\n" + 
    		"<dc:description>This note shows how Yaari’s dual theory of choice under risk naturally extends to the\r\n" + 
    		"case of incomplete preferences. This also provides an axiomatic characterization of a large\r\n" + 
    		"and widely studied class of stochastic orders used to rank the riskiness of random variables\r\n" + 
    		"or the dispersion of income distributions (including, e.g., second order stochastic dominance,\r\n" + 
    		"dispersion, location independent riskiness).</dc:description>\r\n" + 
    		"<dc:description>ICER - International Centre For Economic Research</dc:description>\r\n" + 
    		"<dc:date>2013-07-09T22:59:26Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09T22:59:26Z</dc:date>\r\n" + 
    		"<dc:date>2013-07-09</dc:date>\r\n" + 
    		"<dc:type>Preprint</dc:type>\r\n" + 
    		"<dc:identifier>http://hdl.handle.net/2318/78</dc:identifier>\r\n" + 
    		"<dc:relation>Applied Mathematics Working Paper Series</dc:relation>\r\n" + 
    		"<dc:relation>30/2001</dc:relation>\r\n" + 
    		"</oai_dc:dc>\r\n" + 
    		"</metadata>\r\n" + 
    		"        </record>\r\n" + 
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
        

        assertEquals(100, result.getRecord().size());
        assertEquals("MToxMDB8Mjp8Mzp8NDp8NTpvYWlfZGM=", result.getResumptionToken().getValue());
    }
}
