# XOAI

What is XOAI?

XOAI is the most powerful and flexible OAI-PMH Java Toolkit (initially developed by Lyncode). XOAI contains common Java classes allowing to easily 
implement OAI-PMH data and service providers.

- - - 

Maven
-----

XOAI could be integrated with maven support.

**Moving**: XOAI is now being moved to the DSpace umbrella
	
Changes
-------

**4.2.0**

Bug Fixes:
- Close stream in ListSetsHandler (see #52 and #53)
- Updated dependency Apache httpclient to 4.5.3 (see #63)

Enhancements:
- Add user agent to HttpOAIClient (see #61)
- Handle cases when unknown metadataPrefix is received (see #54)

**4.0.0 to 4.1.0**

- Several fixes on the Service provider
- Supporting granularity on parameters (service provider) 

**3.2.7 to 4.0.0**

- Centralized OAI-PMH model (for data and service provider)
- Data provider configuration simplified
- Service provider made lazy
- Service provider with pipeline processing (centralized xoai schema)


License
-------         

[DSpace BSD License](https://raw.github.com/DSpace/DSpace/master/LICENSE)
