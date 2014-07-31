package com.lyncode.xoai.serviceprovider.parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.lyncode.xml.XmlReader;
import com.lyncode.xml.exceptions.XmlReaderException;
import com.lyncode.xoai.model.oaipmh.Set;

public class ListSetsParserTest {
	
	private static final String OAI_DC_SETS_CDATA_XML = "test/oai_dc/listsets/oai_dc-sets-CDATA.xml";

	@Test
	public void normalParsing(){
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(
				"test/oai_dc/listsets/oai_dc-sets.xml");
		
		List<Set> sets = parseXML(inputStream);
		assertEquals(2,sets.size());
		assertEquals("setOne",sets.get(0).getSpec());
		assertEquals("Set One",sets.get(0).getName());
		assertEquals("setTwo",sets.get(1).getSpec());
		assertEquals("Set Two",sets.get(1).getName());
	}
	
	@Test
	public void cdataIsParsed(){
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(
				OAI_DC_SETS_CDATA_XML);
		
		List<Set> sets = parseXML(inputStream);
		assertEquals("cdataSPEC",sets.get(0).getSpec());
		assertEquals("Set with CDATA",sets.get(0).getName());
		
	}
	
	@Test
	public void multipleCDATAParsed(){
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(
				OAI_DC_SETS_CDATA_XML);
		
		List<Set> sets = parseXML(inputStream);
		assertEquals(2,sets.size());
		assertEquals("First CDATA, Set 2 with CDATA",sets.get(1).getName());
	}
	
	
	private List<Set> parseXML(InputStream inputStream){
		XmlReader reader;
		try {
			reader = new XmlReader(inputStream);
			ListSetsParser parser = new ListSetsParser(reader );
			List<Set> sets = parser.parse();
			return sets;
			
		} catch (XmlReaderException e) {
			fail("unexpected exception reading the xml: "+ e.getCause().toString());
		}
		return null;
		
	}

}
