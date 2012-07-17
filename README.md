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


License
-------

Copyright 2012 Lyncode

Licensed under the Apache License, Version 2.0 (the "License") you may not use this file except in compliance with the License.
You may obtain a copy of the License at 
	
[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

