# XOAI

What is XOAI?

XOAI is the most powerful and flexible OAI-PMH Java Toolkit (developed by [Lyncode](http://www.lyncode.com "Lyncode Web and Mobile development")). XOAI contains common Java classes allowing to easily 
implement OAI-PMH data and service providers.

- - - 

Documentation
-------------

- [Wiki](https://github.com/lyncode/xoai/wiki  "XOAI Wiki")

Maven
-----

XOAI could be integrated with maven support.

	<dependency>
	    <groupId>com.lyncode</groupId>
	    <artifactId>xoai</artifactId>
	    <version>4.1.0</version>
	</dependency>
	
Changes
-------

**3.2.7 to 4.0.0**

- Centralized OAI-PMH model (for data and service provider)
- Data provider configuration simplified
- Service provider made lazy
- Service provider with pipeline processing (centralized xoai schema)

**4.0.0 to 4.1.0**

- Several fixes on the Service provider
- Supporting granularity on parameters (service provider) 

License
-------         

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)