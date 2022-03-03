Forked from https://github.com/lyncode/xml-io, licensed under Apache 2.0, last release to Maven Central was 2014.
This library is used to wrap around Java XML libraries like StAX etc to extract XML data from streams and events.

It has been altered

- to avoid collisions with others and 
- as it hasn't been changed for a long time and 
- uses stable native Java APIs and
- isn't currently used by any other (public) project, 

has been drawn into this complete XOAI package as a submodule.

### lyncode/test-support

This library also includes a few classes from lyncode/test-support (namely the package `test.matchers.extractor`), 
which is not available on GitHub anymore. The sources have been extracted from the source JAR at 
https://search.maven.org/artifact/com.lyncode/test-support/1.1.0/jar. 

This package is also licensed under Apache 2.0 and hasn't been released in a long time. 
The classes have been adapted to not rely on Google Guava but use native JDK only.

----

Original content as follows:

xml-io
======

XML IO Library