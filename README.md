# XOAI 2.2.1

What is XOAI?

XOAI is an OAI-PMH Java Toolkit developed by Lyncode. XOAI contais common Java classes allowing to easily 
implement OAI-PMH data and service providers.

- - - 

Service Provider
-----------------

	package test.com.lyncode.xoai.serviceprovider;

	import com.lyncode.xoai.serviceprovider.HarvesterManager;
	import com.lyncode.xoai.serviceprovider.configuration.Configuration;
	import com.lyncode.xoai.serviceprovider.data.Record;
	import com.lyncode.xoai.serviceprovider.exceptions.HarvestException;
	import com.lyncode.xoai.serviceprovider.iterators.RecordIterator;


	public class SimpleRecordListing {
		public static void main (String... args) {
			Configuration config = new Configuration();
		    config.setResumptionInterval(1000); // 1 second

		    String baseUrl = "http://localhost:8080/xoai/request";
		    String metadataPrefix = "oai_dc";

		    HarvesterManager harvester = new HarvesterManager(config, baseUrl);
		    
		    RecordIterator it = harvester.listRecords(metadataPrefix).iterator();
		    try {
				while (it.hasNext()) {
					Record r = it.next();
					System.out.println(r.getHeader().getIdentifier());
				}
			} catch (HarvestException e) {
				System.out.println(e.getClass().getName());
				System.out.println(e.getMessage());
			} 
		}
	}

Maven
-----

XOAI could be integrated with maven support.

	<dependency>
	    <groupId>com.lyncode</groupId>
	    <artifactId>xoai</artifactId>
	    <version>2.2.1</version>
	</dependency>

License
-------

Copyright 2012 Lyncode

Licensed under the Apache License, Version 2.0 (the "License") you may not use this file except in compliance with the License.
You may obtain a copy of the License at 
	
[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
