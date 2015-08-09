/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.parameters;

import org.dspace.xoai.model.oaipmh.Verb.Type;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class ParametersToUrlTest {
	
	private static final String BASE_URL = "http://base.org";
	private Calendar calendar;
	@Test
	public void toUrl(){
		Parameters parameters = new Parameters().withVerb(Type.ListRecords);
		String url = parameters.toUrl(BASE_URL);
		assertEquals(BASE_URL+"?verb=ListRecords",url);
	}
	
	@Before
	public void setUp(){
		calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	@Test
	public void fromUntilDatesPassed(){
		Parameters parameters = new Parameters().withVerb(Type.ListRecords);
		calendar.set(2011, Calendar.APRIL, 07,9,30,0);
		Date date = calendar.getTime();
		
		parameters.withFrom(date).withUntil(date);
		
		String url = parameters.toUrl(BASE_URL);
		assertEquals(BASE_URL+"?verb=ListRecords"+"&from=2011-04-07T09%3A30%3A00Z"+"&until=2011-04-07T09%3A30%3A00Z",url);
	}
	
	@Test
	public void differentGranularityIsRespected(){
		Parameters parameters = new Parameters().withVerb(Type.ListRecords);
		calendar.set(2013, Calendar.JANUARY, 9);
		Date date = calendar.getTime();
		
		parameters.withFrom(date).withUntil(date);
		parameters.withGranularity("YYYY-MM-DD");
		
		String url = parameters.toUrl(BASE_URL);
		assertEquals(BASE_URL+"?verb=ListRecords"+"&from=2013-01-09"+"&until=2013-01-09",url);
	}
	
	

}
