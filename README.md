# XOAI 2.2.0

What is XOAI?

XOAI is an OAI-PMH Java Toolkit developed by Lyncode. XOAI contais common Java classes allowing to easily 
implement OAI-PMH data and service providers.

- - - 

Service Provider
-----------------

	package com.lyncode.test;
	
	import com.lyncode.xoai.serviceprovider.HarvesterManager;
	import com.lyncode.xoai.serviceprovider.configuration.Configuration;
	import com.lyncode.xoai.serviceprovider.data.Record;
	
	public class App 
	{
	    public static void main( String[] args )
	    {
	        Configuration config = new Configuration();
	        config.setResumptionInterval(1000); // 1 second
	        
	        String baseUrl = "http://lyncode-dev.dtdns.net/xoai/request";
	        String metadataPrefix = "oai_dc";
	        
	        HarvesterManager harvester = new HarvesterManager(config, baseUrl);
	        
	        for (Record record : harvester.listRecords(metadataPrefix)) {
	            System.out.println(record.getHeader().getIdentifier());
	        }
	        
	    }
	}



