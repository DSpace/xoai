/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider.parameters;

import io.gdcc.xoai.model.oaipmh.Verb.Type;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParametersToUrlTest {
	
	private static final String BASE_URL = "http://base.org";
	
	@Test
	public void toUrl(){
		Parameters parameters = new Parameters().withVerb(Type.ListRecords);
		String url = parameters.toUrl(BASE_URL);
		assertEquals(BASE_URL+"?verb=ListRecords",url);
	}
	
	@Test
	public void fromUntilDatesPassed(){
		Instant date = LocalDateTime.of(2011, 4, 7, 9, 30, 0).toInstant(ZoneOffset.UTC);
		
		Parameters parameters = new Parameters()
			.withVerb(Type.ListRecords)
			.withFrom(date)
			.withUntil(date);
		
		String url = parameters.toUrl(BASE_URL);
		assertEquals(BASE_URL+"?verb=ListRecords"+"&from=2011-04-07T09%3A30%3A00Z"+"&until=2011-04-07T09%3A30%3A00Z",url);
	}
	
	@Test
	public void differentGranularityIsRespected(){
		Instant date = LocalDateTime.of(2011, 4, 7, 9, 30, 0).toInstant(ZoneOffset.UTC);
		
		Parameters parameters = new Parameters()
			.withVerb(Type.ListRecords)
			.withFrom(date)
			.withUntil(date)
			.withGranularity("YYYY-MM-DD");
		
		String url = parameters.toUrl(BASE_URL);
		assertEquals(BASE_URL+"?verb=ListRecords"+"&from=2011-04-07"+"&until=2011-04-07", url);
	}
	
	

}
