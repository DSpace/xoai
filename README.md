# XOAI

What is XOAI?

XOAI is the most powerful and flexible OAI-PMH Java Toolkit (developed by [Lyncode](http://www.lyncode.com "Lyncode Web and Mobile development")). XOAI contains common Java classes allowing to easily 
implement OAI-PMH data and service providers.

--- 
:warning: **XOAI 4.x IS NOT ACTIVELY MAINTAINED:**  The DSpace project adopted XOAI when the original creator had to abandon it. However, XOAI currently doesn't have a "product owner" or any funding for active support. DSpace itself still uses XOAI v3 (see [3.x branch](https://github.com/DSpace/xoai/tree/3.x)), and has yet to proritize an upgrade to the latest version of XOAI (code available on [`main` branch](https://github.com/DSpace/xoai/tree/main)). Therefore, the 4.x version of this project only receives attention/updates when someone from the DSpace team can prioritize it.

If you'd like to contribute or help maintain this project, please get in touch via an issue ticket.

_Until this notice is removed, those looking for a more updated XOAI 4.x may wish to consider using one of these forks:_
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


License
-------

As of August 2015, the XOAI codebase is now released under the [DSpace BSD License](https://raw.github.com/DSpace/DSpace/main/LICENSE), which is a standard [BSD 3-Clause license](https://opensource.org/licenses/BSD-3-Clause).

Prior 3.x releases of XOAI (<=3.2.9) were released under an [Apache 2 License](http://www.apache.org/licenses/LICENSE-2.0).
