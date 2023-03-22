# XOAI

What is XOAI?

XOAI is the most powerful and flexible OAI-PMH Java Toolkit (initially developed by Lyncode). XOAI contains common Java classes allowing to easily 
implement OAI-PMH data and service providers.

--- 
:warning: **XOAI IS NOT ACTIVELY MAINTAINED:**  The DSpace project adopted XOAI when the original creator had to abandon it. However, XOAI currently doesn't have a "product owner" or any funding for active support. DSpace itself still uses XOAI v3 (see [3.x branch](https://github.com/DSpace/xoai/tree/3.x)), and has yet to proritize an upgrade to the latest version of XOAI. Therefore, this project only receives attention/updates when someone from the DSpace team can prioritize it.

If you'd like to contribute or help maintain this project, please get in touch via an issue ticket.

_Until this notice is removed, those looking for a more updated XOAI may wish to consider using one of these forks:_
* Dataverse project: https://github.com/gdcc/xoai

---

Maven
-----
XOAI has been released under several Maven central groupIDs:
* More recent releases can be found under
  ```
  <dependency>
      <groupId>org.dspace</groupId>
      <artifactId>xoai</artifactId>
  </dependency>
  ```
* Early releases (2014 and earlier) were under
  ```
  <dependency>
      <groupId>com.lyncode</groupId>
      <artifactId>xoai</artifactId>
  </dependency>
  ```
	
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

All DSpace source code is freely available under a standard [BSD 3-Clause license](https://opensource.org/licenses/BSD-3-Clause).
The full license is available in the [LICENSE](https://github.com/DSpace/DSpace/blob/main/LICENSE) file or online at http://www.dspace.org/license/ 
