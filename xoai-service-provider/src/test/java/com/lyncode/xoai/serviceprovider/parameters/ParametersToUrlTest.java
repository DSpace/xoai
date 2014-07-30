package com.lyncode.xoai.serviceprovider.parameters;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.lyncode.xoai.model.oaipmh.Verb.Type;

public class ParametersToUrlTest {
	
	private static final String BASE_URL = "http://base.org";

	@Test
	public void toUrl(){
		Parameters parameters = new Parameters().withVerb(Type.ListRecords);
		String url = parameters.toUrl(BASE_URL);
		assertEquals(BASE_URL+"?verb=ListRecords",url);
	}

}
