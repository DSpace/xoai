/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.parsers;

import com.lyncode.xml.XmlReader;
import org.dspace.xoai.model.oaipmh.Record;
import org.dspace.xoai.serviceprovider.model.Context;
import org.dspace.xoai.serviceprovider.model.Context.KnownTransformer;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class RecordParserTest {
	
	private InputStream input;
	private Context context;
	private RecordParser parser;

	@Before
	public void setUp(){
		input = getClass().getClassLoader().getResourceAsStream(
				"test/oai_dc-valid.xml");

		context = new Context().withMetadataTransformer("oai_dc",
				KnownTransformer.OAI_DC);
	}

	@Test
	public void multipleElementsFound() throws Exception {
		
		parser = new RecordParser(context, "oai_dc");
		XmlReader reader = new XmlReader(input);
		Record record = parser.parse(reader);
		assertEquals(2,record.getMetadata().getValue().searcher().findAll("dc.rights").size());
	}
	
	@Test
	public void xmlLangIsParsed() throws Exception {
		
		parser = new RecordParser(context, "oai_dc");
		XmlReader reader = new XmlReader(input);
		Record record = parser.parse(reader);
		assertEquals(2,record.getMetadata().getValue().searcher().findAll("dc.rights:xml:lang").size());
	}
	
	@Test
	public void cdataParsing() throws Exception {
		input = getClass().getClassLoader().getResourceAsStream(
				"test/oai_dc-CDATA.xml");
		
		parser = new RecordParser(context, "oai_dc");
		XmlReader reader = new XmlReader(input);
		Record record = parser.parse(reader);
		assertEquals(1,record.getMetadata().getValue().searcher().findAll("dc.title").size());
		assertEquals("Article Title-additional CDATA",record.getMetadata().getValue().searcher().findOne("dc.title"));
	}

}
