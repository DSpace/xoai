package com.lyncode.xoai.serviceprovider.parameters;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.lyncode.xoai.model.oaipmh.Granularity;

public class ParametersIncludesGranularityTest {
	
	@Test
	public void listRecordsParametersGranularity(){
		Parameters parameters = new Parameters();
		ListRecordsParameters listRecordParameters = new ListRecordsParameters();
		listRecordParameters.withGranularity(Granularity.Day.toString());
		
		parameters.include(listRecordParameters);
		assertEquals(Granularity.Day.toString(),parameters.getGranularity());
	}

	@Test
	public void listIdentifiersParametersGranularity(){
		Parameters parameters = new Parameters();
		ListIdentifiersParameters listIdentifiersParameters = new ListIdentifiersParameters();
		listIdentifiersParameters.withGranularity(Granularity.Day.toString());
		
		parameters.include(listIdentifiersParameters);
		assertEquals(Granularity.Day.toString(),parameters.getGranularity());
	}
}
