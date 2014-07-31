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
	
	@Test
	public void normalParsing(){
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(
				"test/oai_dc/listsets/oai_dc-sets.xml");
		XmlReader reader;
		try {
			reader = new XmlReader(inputStream);
			ListSetsParser parser = new ListSetsParser(reader );
			List<Set> sets = parser.parse();
			assertEquals(2,sets.size());
			assertEquals("setOne",sets.get(0).getSpec());
			assertEquals("Set One",sets.get(0).getName());
			assertEquals("setTwo",sets.get(1).getSpec());
			assertEquals("Set Two",sets.get(1).getName());
			
		} catch (XmlReaderException e) {
			fail("unexpected exception reading the xml: "+ e.getCause().toString());
		}
	}
	
	@Test
	public void cdataIsParsed(){
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(
				"test/oai_dc/listsets/oai_dc-sets-CDATA.xml");
		XmlReader reader;
		try {
			reader = new XmlReader(inputStream);
			ListSetsParser parser = new ListSetsParser(reader );
			List<Set> sets = parser.parse();
			assertEquals("setOne",sets.get(0).getSpec());
			assertEquals("Set with CDATA",sets.get(0).getName());
			
		} catch (XmlReaderException e) {
			fail("unexpected exception reading the xml: "+ e.getCause().toString());
		}
	}
	
	

}
