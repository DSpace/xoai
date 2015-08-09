/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.parameters;

import org.dspace.xoai.model.oaipmh.Granularity;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
