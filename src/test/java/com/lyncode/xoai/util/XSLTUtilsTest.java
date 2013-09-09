package com.lyncode.xoai.util;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.lyncode.xoai.dataprovider.core.ItemMetadata;
import com.lyncode.xoai.dataprovider.data.AbstractItem;
import com.lyncode.xoai.dataprovider.exceptions.XSLTransformationException;

import static org.mockito.Mockito.*;


public class XSLTUtilsTest {
    public static String RESULT = "<dim:dimxmlns:dim=\"http://www.dspace.org/xmlns/dspace/dim\"xmlns:doc=\"http://www.lyncode.com/xoai\"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"xsi:schemaLocation=\"http://www.dspace.org/xmlns/dspace/dimhttp://www.dspace.org/schema/dim.xsd\"/>";
    public static String SCHEMA_XSL = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + 
    		"<!-- \r\n" + 
    		"\r\n" + 
    		"    The contents of this file are subject to the license and copyright detailed \r\n" + 
    		"    in the LICENSE and NOTICE files at the root of the source tree and available \r\n" + 
    		"    online at http://www.dspace.org/license/ \r\n" + 
    		"    \r\n" + 
    		"    Developed by DSpace @ Lyncode <dspace@lyncode.com> \r\n" + 
    		"    \r\n" + 
    		"-->\r\n" + 
    		"<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"\r\n" + 
    		"    xmlns:doc=\"http://www.lyncode.com/xoai\" version=\"1.0\">\r\n" + 
    		"    <xsl:output omit-xml-declaration=\"yes\" method=\"xml\" indent=\"yes\" />\r\n" + 
    		"\r\n" + 
    		"    <!-- An identity transformation to show the internal XOAI generated XML -->\r\n" + 
    		"    <xsl:template match=\"/\">\r\n" + 
    		"        <dim:dim xmlns:dim=\"http://www.dspace.org/xmlns/dspace/dim\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \r\n" + 
    		"            xsi:schemaLocation=\"http://www.dspace.org/xmlns/dspace/dim http://www.dspace.org/schema/dim.xsd\">\r\n" + 
    		"            <xsl:for-each select=\"doc:metadata/doc:element[@name='dc']/doc:element/doc:element\">\r\n" + 
    		"                <xsl:choose>\r\n" + 
    		"                    <xsl:when test=\"doc:element\">\r\n" + 
    		"                        <dim:field>\r\n" + 
    		"                            <xsl:attribute name=\"mdschema\">\r\n" + 
    		"                                <xsl:value-of select=\"../../@name\" />\r\n" + 
    		"                            </xsl:attribute>\r\n" + 
    		"                            <xsl:attribute name=\"element\">\r\n" + 
    		"                                <xsl:value-of select=\"../@name\" />\r\n" + 
    		"                            </xsl:attribute>\r\n" + 
    		"                            <xsl:attribute name=\"qualifier\">\r\n" + 
    		"                                <xsl:value-of select=\"@name\" />\r\n" + 
    		"                            </xsl:attribute>\r\n" + 
    		"                            <xsl:choose>\r\n" + 
    		"                                <xsl:when test=\"doc:element[@name='none']\"></xsl:when>\r\n" + 
    		"                                <xsl:otherwise>\r\n" + 
    		"                                    <xsl:attribute name=\"lang\">\r\n" + 
    		"                                        <xsl:value-of select=\"doc:element/@name\" />\r\n" + 
    		"                                    </xsl:attribute>\r\n" + 
    		"                                </xsl:otherwise>\r\n" + 
    		"                            </xsl:choose>\r\n" + 
    		"                            <xsl:if test=\"doc:element/doc:field[@name='authority']\">\r\n" + 
    		"                                <xsl:attribute name=\"authority\">\r\n" + 
    		"                                    <xsl:value-of select=\"doc:element/doc:field[@name='authority']/text()\" />\r\n" + 
    		"                                </xsl:attribute>\r\n" + 
    		"                            </xsl:if>\r\n" + 
    		"                            <xsl:if test=\"doc:element/doc:field[@name='confidence']\">\r\n" + 
    		"                                <xsl:attribute name=\"confidence\">\r\n" + 
    		"                                    <xsl:value-of select=\"doc:element/doc:field[@name='confidence']/text()\" />\r\n" + 
    		"                                </xsl:attribute>\r\n" + 
    		"                            </xsl:if>\r\n" + 
    		"                            <xsl:value-of select=\"doc:element/doc:field[@name='value']/text()\"></xsl:value-of>\r\n" + 
    		"                        </dim:field>\r\n" + 
    		"                    </xsl:when>\r\n" + 
    		"                    <xsl:otherwise>\r\n" + 
    		"                        <dim:field>\r\n" + 
    		"                            <xsl:attribute name=\"mdschema\">\r\n" + 
    		"                                <xsl:value-of select=\"../../@name\" />\r\n" + 
    		"                            </xsl:attribute>\r\n" + 
    		"                            <xsl:attribute name=\"element\">\r\n" + 
    		"                                <xsl:value-of select=\"../@name\" />\r\n" + 
    		"                            </xsl:attribute>\r\n" + 
    		"                            <xsl:choose>\r\n" + 
    		"                                <xsl:when test=\"@name='none'\"></xsl:when>\r\n" + 
    		"                                <xsl:otherwise>\r\n" + 
    		"                                    <xsl:attribute name=\"lang\">\r\n" + 
    		"                                        <xsl:value-of select=\"@name\" />\r\n" + 
    		"                                    </xsl:attribute>\r\n" + 
    		"                                </xsl:otherwise>\r\n" + 
    		"                            </xsl:choose>\r\n" + 
    		"                            <xsl:if test=\"doc:field[@name='authority']\">\r\n" + 
    		"                                <xsl:attribute name=\"authority\">\r\n" + 
    		"                                    <xsl:value-of select=\"doc:field[@name='authority']/text()\" />\r\n" + 
    		"                                </xsl:attribute>\r\n" + 
    		"                            </xsl:if>\r\n" + 
    		"                            <xsl:if test=\"doc:field[@name='confidence']\">\r\n" + 
    		"                                <xsl:attribute name=\"confidence\">\r\n" + 
    		"                                    <xsl:value-of select=\"doc:field[@name='confidence']/text()\" />\r\n" + 
    		"                                </xsl:attribute>\r\n" + 
    		"                            </xsl:if>\r\n" + 
    		"                            <xsl:value-of select=\"doc:field[@name='value']/text()\"></xsl:value-of>\r\n" + 
    		"                        </dim:field>\r\n" + 
    		"                    </xsl:otherwise>\r\n" + 
    		"                </xsl:choose>\r\n" + 
    		"            </xsl:for-each>\r\n" + 
    		"        </dim:dim>\r\n" + 
    		"    </xsl:template>\r\n" + 
    		"</xsl:stylesheet>";
    public static String METADATA_XSL = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + 
    		"<!-- \r\n" + 
    		"\r\n" + 
    		"    The contents of this file are subject to the license and copyright detailed \r\n" + 
    		"    in the LICENSE and NOTICE files at the root of the source tree and available \r\n" + 
    		"    online at http://www.dspace.org/license/ \r\n" + 
    		"    \r\n" + 
    		"    Developed by DSpace @ Lyncode <dspace@lyncode.com> \r\n" + 
    		"    \r\n" + 
    		"-->\r\n" + 
    		"<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"\r\n" + 
    		"    xmlns:doc=\"http://www.lyncode.com/xoai\" version=\"1.0\">\r\n" + 
    		"    <xsl:output omit-xml-declaration=\"yes\" method=\"xml\" indent=\"yes\" />\r\n" + 
    		"\r\n" + 
    		"    <!-- An identity transformation to show the internal XOAI generated XML -->\r\n" + 
    		"    <xsl:template match=\"/\">\r\n" + 
    		"        <dim:dim xmlns:dim=\"http://www.dspace.org/xmlns/dspace/dim\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \r\n" + 
    		"            xsi:schemaLocation=\"http://www.dspace.org/xmlns/dspace/dim http://www.dspace.org/schema/dim.xsd\">\r\n" + 
    		"            <xsl:for-each select=\"doc:metadata/doc:element[@name='dc']/doc:element/doc:element\">\r\n" + 
    		"                <xsl:choose>\r\n" + 
    		"                    <xsl:when test=\"doc:element\">\r\n" + 
    		"                        <dim:field>\r\n" + 
    		"                            <xsl:attribute name=\"mdschema\">\r\n" + 
    		"                                <xsl:value-of select=\"../../@name\" />\r\n" + 
    		"                            </xsl:attribute>\r\n" + 
    		"                            <xsl:attribute name=\"element\">\r\n" + 
    		"                                <xsl:value-of select=\"../@name\" />\r\n" + 
    		"                            </xsl:attribute>\r\n" + 
    		"                            <xsl:attribute name=\"qualifier\">\r\n" + 
    		"                                <xsl:value-of select=\"@name\" />\r\n" + 
    		"                            </xsl:attribute>\r\n" + 
    		"                            <xsl:choose>\r\n" + 
    		"                                <xsl:when test=\"doc:element[@name='none']\"></xsl:when>\r\n" + 
    		"                                <xsl:otherwise>\r\n" + 
    		"                                    <xsl:attribute name=\"lang\">\r\n" + 
    		"                                        <xsl:value-of select=\"doc:element/@name\" />\r\n" + 
    		"                                    </xsl:attribute>\r\n" + 
    		"                                </xsl:otherwise>\r\n" + 
    		"                            </xsl:choose>\r\n" + 
    		"                            <xsl:if test=\"doc:element/doc:field[@name='authority']\">\r\n" + 
    		"                                <xsl:attribute name=\"authority\">\r\n" + 
    		"                                    <xsl:value-of select=\"doc:element/doc:field[@name='authority']/text()\" />\r\n" + 
    		"                                </xsl:attribute>\r\n" + 
    		"                            </xsl:if>\r\n" + 
    		"                            <xsl:if test=\"doc:element/doc:field[@name='confidence']\">\r\n" + 
    		"                                <xsl:attribute name=\"confidence\">\r\n" + 
    		"                                    <xsl:value-of select=\"doc:element/doc:field[@name='confidence']/text()\" />\r\n" + 
    		"                                </xsl:attribute>\r\n" + 
    		"                            </xsl:if>\r\n" + 
    		"                            <xsl:value-of select=\"doc:element/doc:field[@name='value']/text()\"></xsl:value-of>\r\n" + 
    		"                        </dim:field>\r\n" + 
    		"                    </xsl:when>\r\n" + 
    		"                    <xsl:otherwise>\r\n" + 
    		"                        <dim:field>\r\n" + 
    		"                            <xsl:attribute name=\"mdschema\">\r\n" + 
    		"                                <xsl:value-of select=\"../../@name\" />\r\n" + 
    		"                            </xsl:attribute>\r\n" + 
    		"                            <xsl:attribute name=\"element\">\r\n" + 
    		"                                <xsl:value-of select=\"../@name\" />\r\n" + 
    		"                            </xsl:attribute>\r\n" + 
    		"                            <xsl:choose>\r\n" + 
    		"                                <xsl:when test=\"@name='none'\"></xsl:when>\r\n" + 
    		"                                <xsl:otherwise>\r\n" + 
    		"                                    <xsl:attribute name=\"lang\">\r\n" + 
    		"                                        <xsl:value-of select=\"@name\" />\r\n" + 
    		"                                    </xsl:attribute>\r\n" + 
    		"                                </xsl:otherwise>\r\n" + 
    		"                            </xsl:choose>\r\n" + 
    		"                            <xsl:if test=\"doc:field[@name='authority']\">\r\n" + 
    		"                                <xsl:attribute name=\"authority\">\r\n" + 
    		"                                    <xsl:value-of select=\"doc:field[@name='authority']/text()\" />\r\n" + 
    		"                                </xsl:attribute>\r\n" + 
    		"                            </xsl:if>\r\n" + 
    		"                            <xsl:if test=\"doc:field[@name='confidence']\">\r\n" + 
    		"                                <xsl:attribute name=\"confidence\">\r\n" + 
    		"                                    <xsl:value-of select=\"doc:field[@name='confidence']/text()\" />\r\n" + 
    		"                                </xsl:attribute>\r\n" + 
    		"                            </xsl:if>\r\n" + 
    		"                            <xsl:value-of select=\"doc:field[@name='value']/text()\"></xsl:value-of>\r\n" + 
    		"                        </dim:field>\r\n" + 
    		"                    </xsl:otherwise>\r\n" + 
    		"                </xsl:choose>\r\n" + 
    		"            </xsl:for-each>\r\n" + 
    		"        </dim:dim>\r\n" + 
    		"    </xsl:template>\r\n" + 
    		"</xsl:stylesheet>";
  
    public static String SAMPLE_DATA = "<metadata xmlns=\"http://www.lyncode.com/xoai\">\r\n" + 
    		"    <element name=\"dc\">\r\n" + 
    		"        <element name=\"creator\">\r\n" + 
    		"            <element name=\"en_US\">\r\n" + 
    		"                <field name=\"value\">Cat, Lily</field>\r\n" + 
    		"            </element>\r\n" + 
    		"        </element>\r\n" + 
    		"        <element name=\"date\">\r\n" + 
    		"            <element name=\"accessioned\">\r\n" + 
    		"                <element name=\"none\">\r\n" + 
    		"                    <field name=\"value\">1982-06-26T19:58:24Z</field>\r\n" + 
    		"                </element>\r\n" + 
    		"            </element>\r\n" + 
    		"            <element name=\"available\">\r\n" + 
    		"                <element name=\"none\">\r\n" + 
    		"                    <field name=\"value\">1982-06-26T19:58:24Z</field>\r\n" + 
    		"                </element>\r\n" + 
    		"            </element>\r\n" + 
    		"            <element name=\"created\">\r\n" + 
    		"                <element name=\"en_US\">\r\n" + 
    		"                    <field name=\"value\">1982-11-11</field>\r\n" + 
    		"                </element>\r\n" + 
    		"            </element>\r\n" + 
    		"            <element name=\"issued\">\r\n" + 
    		"                <element name=\"none\">\r\n" + 
    		"                    <field name=\"value\">1982-11-11</field>\r\n" + 
    		"                </element>\r\n" + 
    		"            </element>\r\n" + 
    		"        </element>\r\n" + 
    		"        <element name=\"identifier\">\r\n" + 
    		"            <element name=\"issn\">\r\n" + 
    		"                <element name=\"en_US\">\r\n" + 
    		"                    <field name=\"value\">123456789</field>\r\n" + 
    		"                </element>\r\n" + 
    		"            </element>\r\n" + 
    		"            <element name=\"uri\">\r\n" + 
    		"                <element name=\"none\">\r\n" + 
    		"                    <field name=\"value\">http://hdl.handle.net/10673/4</field>\r\n" + 
    		"                </element>\r\n" + 
    		"            </element>\r\n" + 
    		"        </element>\r\n" + 
    		"        <element name=\"description\">\r\n" + 
    		"            <element name=\"abstract\">\r\n" + 
    		"                <element name=\"en_US\">\r\n" + 
    		"                    <field name=\"value\">This is a Sample HTML webpage including several images and styles (CSS).</field>\r\n" + 
    		"                </element>\r\n" + 
    		"            </element>\r\n" + 
    		"            <element name=\"provenance\">\r\n" + 
    		"                <element name=\"en\">\r\n" + 
    		"                    <field name=\"value\">Made available in DSpace on 2012-06-26T19:58:24Z (GMT). No. of bitstreams: 7&#13;\r\n" + 
    		"Lily-cat-of-day.htm: 39970 bytes, checksum: aae901336b56ae14070fdec8c79dd48e (MD5)&#13;\r\n" + 
    		"cl_style.css: 1181 bytes, checksum: 58178d41c221520d88333b9d482b31b8 (MD5)&#13;\r\n" + 
    		"kitty-1257950690.jpg: 83390 bytes, checksum: 729596bb3ad09ebe958a150787418212 (MD5)&#13;\r\n" + 
    		"kitty-1257950690_002.jpg: 86057 bytes, checksum: 37c07c8488042d064bd945765d9e2f98 (MD5)&#13;\r\n" + 
    		"logo.png: 4157 bytes, checksum: d64fb5f69c7820685d5ed3037b9670ed (MD5)&#13;\r\n" + 
    		"style.css: 49623 bytes, checksum: 89c2a0557993a8665574c0b52fc1582d (MD5)&#13;\r\n" + 
    		"stylesheet.css: 877 bytes, checksum: d2b580ac1c89ae88dc05dcd9108b002e (MD5)</field>\r\n" + 
    		"                    <field name=\"value\">Restored into DSpace on 2013-06-13T09:17:32Z (GMT).</field>\r\n" + 
    		"                    <field name=\"value\">Restored into DSpace on 2013-06-13T11:04:13Z (GMT).</field>\r\n" + 
    		"                    <field name=\"value\">Restored into DSpace on 2013-09-01T00:01:40Z (GMT).</field>\r\n" + 
    		"                </element>\r\n" + 
    		"            </element>\r\n" + 
    		"        </element>\r\n" + 
    		"        <element name=\"language\">\r\n" + 
    		"            <element name=\"en_US\">\r\n" + 
    		"                <field name=\"value\">en</field>\r\n" + 
    		"            </element>\r\n" + 
    		"        </element>\r\n" + 
    		"        <element name=\"rights\">\r\n" + 
    		"            <element name=\"en_US\">\r\n" + 
    		"                <field name=\"value\">© EverCats.com</field>\r\n" + 
    		"            </element>\r\n" + 
    		"        </element>\r\n" + 
    		"        <element name=\"subject\">\r\n" + 
    		"            <element name=\"en_US\">\r\n" + 
    		"                <field name=\"value\">cat</field>\r\n" + 
    		"                <field name=\"value\">calico</field>\r\n" + 
    		"            </element>\r\n" + 
    		"        </element>\r\n" + 
    		"        <element name=\"title\">\r\n" + 
    		"            <element name=\"en_US\">\r\n" + 
    		"                <field name=\"value\">Test Webpage</field>\r\n" + 
    		"            </element>\r\n" + 
    		"        </element>\r\n" + 
    		"        <element name=\"type\">\r\n" + 
    		"            <element name=\"en_US\">\r\n" + 
    		"                <field name=\"value\">text</field>\r\n" + 
    		"            </element>\r\n" + 
    		"        </element>\r\n" + 
    		"    </element>\r\n" + 
    		"    <element name=\"bundles\">\r\n" + 
    		"        <element name=\"bundle\">\r\n" + 
    		"            <field name=\"name\">ORIGINAL</field>\r\n" + 
    		"            <element name=\"bitstreams\">\r\n" + 
    		"                <element name=\"bitstream\">\r\n" + 
    		"                    <field name=\"name\">Lily-cat-of-day.htm</field>\r\n" + 
    		"                    <field name=\"format\">text/html</field>\r\n" + 
    		"                    <field name=\"size\">39970</field>\r\n" + 
    		"                    <field name=\"url\">http://demo.dspace.org/xmlui/bitstream/10673%2F4/1/bitstream</field>\r\n" + 
    		"                    <field name=\"checksum\">aae901336b56ae14070fdec8c79dd48e</field>\r\n" + 
    		"                    <field name=\"checksumAlgorithm\">MD5</field>\r\n" + 
    		"                    <field name=\"sid\">1</field>\r\n" + 
    		"                </element>\r\n" + 
    		"                <element name=\"bitstream\">\r\n" + 
    		"                    <field name=\"name\">cl_style.css</field>\r\n" + 
    		"                    <field name=\"format\">text/css</field>\r\n" + 
    		"                    <field name=\"size\">1181</field>\r\n" + 
    		"                    <field name=\"url\">http://demo.dspace.org/xmlui/bitstream/10673%2F4/2/bitstream</field>\r\n" + 
    		"                    <field name=\"checksum\">58178d41c221520d88333b9d482b31b8</field>\r\n" + 
    		"                    <field name=\"checksumAlgorithm\">MD5</field>\r\n" + 
    		"                    <field name=\"sid\">2</field>\r\n" + 
    		"                </element>\r\n" + 
    		"                <element name=\"bitstream\">\r\n" + 
    		"                    <field name=\"name\">kitty-1257950690.jpg</field>\r\n" + 
    		"                    <field name=\"format\">image/jpeg</field>\r\n" + 
    		"                    <field name=\"size\">83390</field>\r\n" + 
    		"                    <field name=\"url\">http://demo.dspace.org/xmlui/bitstream/10673%2F4/3/bitstream</field>\r\n" + 
    		"                    <field name=\"checksum\">729596bb3ad09ebe958a150787418212</field>\r\n" + 
    		"                    <field name=\"checksumAlgorithm\">MD5</field>\r\n" + 
    		"                    <field name=\"sid\">3</field>\r\n" + 
    		"                </element>\r\n" + 
    		"                <element name=\"bitstream\">\r\n" + 
    		"                    <field name=\"name\">kitty-1257950690_002.jpg</field>\r\n" + 
    		"                    <field name=\"format\">image/jpeg</field>\r\n" + 
    		"                    <field name=\"size\">86057</field>\r\n" + 
    		"                    <field name=\"url\">http://demo.dspace.org/xmlui/bitstream/10673%2F4/4/bitstream</field>\r\n" + 
    		"                    <field name=\"checksum\">37c07c8488042d064bd945765d9e2f98</field>\r\n" + 
    		"                    <field name=\"checksumAlgorithm\">MD5</field>\r\n" + 
    		"                    <field name=\"sid\">4</field>\r\n" + 
    		"                </element>\r\n" + 
    		"                <element name=\"bitstream\">\r\n" + 
    		"                    <field name=\"name\">logo.png</field>\r\n" + 
    		"                    <field name=\"format\">image/png</field>\r\n" + 
    		"                    <field name=\"size\">4157</field>\r\n" + 
    		"                    <field name=\"url\">http://demo.dspace.org/xmlui/bitstream/10673%2F4/5/bitstream</field>\r\n" + 
    		"                    <field name=\"checksum\">d64fb5f69c7820685d5ed3037b9670ed</field>\r\n" + 
    		"                    <field name=\"checksumAlgorithm\">MD5</field>\r\n" + 
    		"                    <field name=\"sid\">5</field>\r\n" + 
    		"                </element>\r\n" + 
    		"                <element name=\"bitstream\">\r\n" + 
    		"                    <field name=\"name\">style.css</field>\r\n" + 
    		"                    <field name=\"format\">text/css</field>\r\n" + 
    		"                    <field name=\"size\">49623</field>\r\n" + 
    		"                    <field name=\"url\">http://demo.dspace.org/xmlui/bitstream/10673%2F4/6/bitstream</field>\r\n" + 
    		"                    <field name=\"checksum\">89c2a0557993a8665574c0b52fc1582d</field>\r\n" + 
    		"                    <field name=\"checksumAlgorithm\">MD5</field>\r\n" + 
    		"                    <field name=\"sid\">6</field>\r\n" + 
    		"                </element>\r\n" + 
    		"                <element name=\"bitstream\">\r\n" + 
    		"                    <field name=\"name\">stylesheet.css</field>\r\n" + 
    		"                    <field name=\"format\">text/css</field>\r\n" + 
    		"                    <field name=\"size\">877</field>\r\n" + 
    		"                    <field name=\"url\">http://demo.dspace.org/xmlui/bitstream/10673%2F4/7/bitstream</field>\r\n" + 
    		"                    <field name=\"checksum\">d2b580ac1c89ae88dc05dcd9108b002e</field>\r\n" + 
    		"                    <field name=\"checksumAlgorithm\">MD5</field>\r\n" + 
    		"                    <field name=\"sid\">7</field>\r\n" + 
    		"                </element>\r\n" + 
    		"            </element>\r\n" + 
    		"        </element>\r\n" + 
    		"        <element name=\"bundle\">\r\n" + 
    		"            <field name=\"name\">LICENSE</field>\r\n" + 
    		"            <element name=\"bitstreams\">\r\n" + 
    		"                <element name=\"bitstream\">\r\n" + 
    		"                    <field name=\"name\">license.txt</field>\r\n" + 
    		"                    <field name=\"originalName\">license.txt</field>\r\n" + 
    		"                    <field name=\"format\">text/plain; charset=utf-8</field>\r\n" + 
    		"                    <field name=\"size\">1748</field>\r\n" + 
    		"                    <field name=\"url\">http://demo.dspace.org/xmlui/bitstream/10673%2F4/8/bitstream</field>\r\n" + 
    		"                    <field name=\"checksum\">8a4605be74aa9ea9d79846c1fba20a33</field>\r\n" + 
    		"                    <field name=\"checksumAlgorithm\">MD5</field>\r\n" + 
    		"                    <field name=\"sid\">8</field>\r\n" + 
    		"                </element>\r\n" + 
    		"            </element>\r\n" + 
    		"        </element>\r\n" + 
    		"        <element name=\"bundle\">\r\n" + 
    		"            <field name=\"name\">TEXT</field>\r\n" + 
    		"            <element name=\"bitstreams\">\r\n" + 
    		"                <element name=\"bitstream\">\r\n" + 
    		"                    <field name=\"name\">Lily-cat-of-day.htm.txt</field>\r\n" + 
    		"                    <field name=\"originalName\">Lily-cat-of-day.htm.txt</field>\r\n" + 
    		"                    <field name=\"format\">text/plain</field>\r\n" + 
    		"                    <field name=\"size\">5026</field>\r\n" + 
    		"                    <field name=\"url\">http://demo.dspace.org/xmlui/bitstream/10673%2F4/9/bitstream</field>\r\n" + 
    		"                    <field name=\"checksum\">fe0041b3efc9c21308752f4e51dbf4f9</field>\r\n" + 
    		"                    <field name=\"checksumAlgorithm\">MD5</field>\r\n" + 
    		"                    <field name=\"sid\">9</field>\r\n" + 
    		"                </element>\r\n" + 
    		"            </element>\r\n" + 
    		"        </element>\r\n" + 
    		"        <element name=\"bundle\">\r\n" + 
    		"            <field name=\"name\">THUMBNAIL</field>\r\n" + 
    		"            <element name=\"bitstreams\">\r\n" + 
    		"                <element name=\"bitstream\">\r\n" + 
    		"                    <field name=\"name\">kitty-1257950690.jpg.jpg</field>\r\n" + 
    		"                    <field name=\"originalName\">kitty-1257950690.jpg.jpg</field>\r\n" + 
    		"                    <field name=\"format\">image/jpeg</field>\r\n" + 
    		"                    <field name=\"size\">2271</field>\r\n" + 
    		"                    <field name=\"url\">http://demo.dspace.org/xmlui/bitstream/10673%2F4/10/bitstream</field>\r\n" + 
    		"                    <field name=\"checksum\">ca818144119599efeab8915b406c5584</field>\r\n" + 
    		"                    <field name=\"checksumAlgorithm\">MD5</field>\r\n" + 
    		"                    <field name=\"sid\">10</field>\r\n" + 
    		"                </element>\r\n" + 
    		"                <element name=\"bitstream\">\r\n" + 
    		"                    <field name=\"name\">kitty-1257950690_002.jpg.jpg</field>\r\n" + 
    		"                    <field name=\"originalName\">kitty-1257950690_002.jpg.jpg</field>\r\n" + 
    		"                    <field name=\"format\">image/jpeg</field>\r\n" + 
    		"                    <field name=\"size\">1772</field>\r\n" + 
    		"                    <field name=\"url\">http://demo.dspace.org/xmlui/bitstream/10673%2F4/11/bitstream</field>\r\n" + 
    		"                    <field name=\"checksum\">40d67d58a7cc47c3e37c56a5c2e48479</field>\r\n" + 
    		"                    <field name=\"checksumAlgorithm\">MD5</field>\r\n" + 
    		"                    <field name=\"sid\">11</field>\r\n" + 
    		"                </element>\r\n" + 
    		"                <element name=\"bitstream\">\r\n" + 
    		"                    <field name=\"name\">logo.png.jpg</field>\r\n" + 
    		"                    <field name=\"originalName\">logo.png.jpg</field>\r\n" + 
    		"                    <field name=\"format\">image/jpeg</field>\r\n" + 
    		"                    <field name=\"size\">1279</field>\r\n" + 
    		"                    <field name=\"url\">http://demo.dspace.org/xmlui/bitstream/10673%2F4/12/bitstream</field>\r\n" + 
    		"                    <field name=\"checksum\">4a88efd091aaa0b4619011bd0dff5f00</field>\r\n" + 
    		"                    <field name=\"checksumAlgorithm\">MD5</field>\r\n" + 
    		"                    <field name=\"sid\">12</field>\r\n" + 
    		"                </element>\r\n" + 
    		"            </element>\r\n" + 
    		"        </element>\r\n" + 
    		"    </element>\r\n" + 
    		"    <element name=\"others\">\r\n" + 
    		"        <field name=\"handle\">10673/4</field>\r\n" + 
    		"        <field name=\"identifier\">oai:demo.dspace.org:10673/4</field>\r\n" + 
    		"        <field name=\"lastModifyDate\">2013-09-01 02:00:26.763</field>\r\n" + 
    		"    </element>\r\n" + 
    		"    <element name=\"repository\">\r\n" + 
    		"        <field name=\"name\">DSpace Demo Repository</field>\r\n" + 
    		"        <field name=\"mail\">dspacedemo@gmail.com</field>\r\n" + 
    		"    </element>\r\n" + 
    		"    <element name=\"license\">\r\n" + 
    		"        <field name=\"bin\">Tk9URTogUExBQ0UgWU9VUiBPV04gTElDRU5TRSBIRVJFClRoaXMgc2FtcGxlIGxpY2Vuc2UgaXMgcHJvdmlkZWQgZm9yIGluZm9ybWF0aW9uYWwgcHVycG9zZXMgb25seS4KCk5PTi1FWENMVVNJVkUgRElTVFJJQlVUSU9OIExJQ0VOU0UKCkJ5IHNpZ25pbmcgYW5kIHN1Ym1pdHRpbmcgdGhpcyBsaWNlbnNlLCB5b3UgKHRoZSBhdXRob3Iocykgb3IgY29weXJpZ2h0Cm93bmVyKSBncmFudHMgdG8gRFNwYWNlIFVuaXZlcnNpdHkgKERTVSkgdGhlIG5vbi1leGNsdXNpdmUgcmlnaHQgdG8gcmVwcm9kdWNlLAp0cmFuc2xhdGUgKGFzIGRlZmluZWQgYmVsb3cpLCBhbmQvb3IgZGlzdHJpYnV0ZSB5b3VyIHN1Ym1pc3Npb24gKGluY2x1ZGluZwp0aGUgYWJzdHJhY3QpIHdvcmxkd2lkZSBpbiBwcmludCBhbmQgZWxlY3Ryb25pYyBmb3JtYXQgYW5kIGluIGFueSBtZWRpdW0sCmluY2x1ZGluZyBidXQgbm90IGxpbWl0ZWQgdG8gYXVkaW8gb3IgdmlkZW8uCgpZb3UgYWdyZWUgdGhhdCBEU1UgbWF5LCB3aXRob3V0IGNoYW5naW5nIHRoZSBjb250ZW50LCB0cmFuc2xhdGUgdGhlCnN1Ym1pc3Npb24gdG8gYW55IG1lZGl1bSBvciBmb3JtYXQgZm9yIHRoZSBwdXJwb3NlIG9mIHByZXNlcnZhdGlvbi4KCllvdSBhbHNvIGFncmVlIHRoYXQgRFNVIG1heSBrZWVwIG1vcmUgdGhhbiBvbmUgY29weSBvZiB0aGlzIHN1Ym1pc3Npb24gZm9yCnB1cnBvc2VzIG9mIHNlY3VyaXR5LCBiYWNrLXVwIGFuZCBwcmVzZXJ2YXRpb24uCgpZb3UgcmVwcmVzZW50IHRoYXQgdGhlIHN1Ym1pc3Npb24gaXMgeW91ciBvcmlnaW5hbCB3b3JrLCBhbmQgdGhhdCB5b3UgaGF2ZQp0aGUgcmlnaHQgdG8gZ3JhbnQgdGhlIHJpZ2h0cyBjb250YWluZWQgaW4gdGhpcyBsaWNlbnNlLiBZb3UgYWxzbyByZXByZXNlbnQKdGhhdCB5b3VyIHN1Ym1pc3Npb24gZG9lcyBub3QsIHRvIHRoZSBiZXN0IG9mIHlvdXIga25vd2xlZGdlLCBpbmZyaW5nZSB1cG9uCmFueW9uZSdzIGNvcHlyaWdodC4KCklmIHRoZSBzdWJtaXNzaW9uIGNvbnRhaW5zIG1hdGVyaWFsIGZvciB3aGljaCB5b3UgZG8gbm90IGhvbGQgY29weXJpZ2h0LAp5b3UgcmVwcmVzZW50IHRoYXQgeW91IGhhdmUgb2J0YWluZWQgdGhlIHVucmVzdHJpY3RlZCBwZXJtaXNzaW9uIG9mIHRoZQpjb3B5cmlnaHQgb3duZXIgdG8gZ3JhbnQgRFNVIHRoZSByaWdodHMgcmVxdWlyZWQgYnkgdGhpcyBsaWNlbnNlLCBhbmQgdGhhdApzdWNoIHRoaXJkLXBhcnR5IG93bmVkIG1hdGVyaWFsIGlzIGNsZWFybHkgaWRlbnRpZmllZCBhbmQgYWNrbm93bGVkZ2VkCndpdGhpbiB0aGUgdGV4dCBvciBjb250ZW50IG9mIHRoZSBzdWJtaXNzaW9uLgoKSUYgVEhFIFNVQk1JU1NJT04gSVMgQkFTRUQgVVBPTiBXT1JLIFRIQVQgSEFTIEJFRU4gU1BPTlNPUkVEIE9SIFNVUFBPUlRFRApCWSBBTiBBR0VOQ1kgT1IgT1JHQU5JWkFUSU9OIE9USEVSIFRIQU4gRFNVLCBZT1UgUkVQUkVTRU5UIFRIQVQgWU9VIEhBVkUKRlVMRklMTEVEIEFOWSBSSUdIVCBPRiBSRVZJRVcgT1IgT1RIRVIgT0JMSUdBVElPTlMgUkVRVUlSRUQgQlkgU1VDSApDT05UUkFDVCBPUiBBR1JFRU1FTlQuCgpEU1Ugd2lsbCBjbGVhcmx5IGlkZW50aWZ5IHlvdXIgbmFtZShzKSBhcyB0aGUgYXV0aG9yKHMpIG9yIG93bmVyKHMpIG9mIHRoZQpzdWJtaXNzaW9uLCBhbmQgd2lsbCBub3QgbWFrZSBhbnkgYWx0ZXJhdGlvbiwgb3RoZXIgdGhhbiBhcyBhbGxvd2VkIGJ5IHRoaXMKbGljZW5zZSwgdG8geW91ciBzdWJtaXNzaW9uLgo=</field>\r\n" + 
    		"    </element>\r\n" + 
    		"</metadata>";
    public static String RESULT2 = "<dim:dim xmlns:dim = \"http://www.dspace.org/xmlns/dspace/dim\" xmlns:doc=\"http://www.lyncode.com/xoai\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n" + 
    		"    xsi:schemaLocation=\"http://www.dspace.org/xmlns/dspace/dimhttp://www.dspace.org/schema/dim.xsd\">\r\n" + 
    		"    <dim:field mdschema = \"dc \" element=\"creator\" lang=\"en_US\">\r\n" + 
    		"        Cat,Lily\r\n" + 
    		"    </dim:field>\r\n" + 
    		"        <dim:field mdschema = \"dc \" element=\"date\" qualifier=\"accessioned\">\r\n" + 
    		"            1982-06-26T19:58:24Z\r\n" + 
    		"        </dim:field>\r\n" + 
    		"            <dim:field mdschema = \"dc \" element=\"date\" qualifier=\"available\">\r\n" + 
    		"                1982-06-26T19:58:24Z\r\n" + 
    		"            </dim:field>\r\n" + 
    		"                <dim:field mdschema =\" dc \" element=\"date\"\r\n" + 
    		"                    qualifier=\"created\" lang=\"en_US\">\r\n" + 
    		"                    1982-11-11\r\n" + 
    		"                </dim:field>\r\n" + 
    		"                    <dim:field mdschema = \"dc \" element=\"date\"\r\n" + 
    		"                        qualifier=\"issued\">\r\n" + 
    		"                        1982-11-11\r\n" + 
    		"                    </dim:field>\r\n" + 
    		"                        <dim:field mdschema = \"dc \" element=\"identifier\"\r\n" + 
    		"                            qualifier=\"issn\" lang=\"en_US\">\r\n" + 
    		"                            123456789\r\n" + 
    		"                        </dim:field>\r\n" + 
    		"                            <dim:field mdschema = \"dc \" element=\"identifier\"\r\n" + 
    		"                                qualifier=\"uri\">\r\n" + 
    		"                                http://hdl.handle.net/10673/4\r\n" + 
    		"                            </dim:field>\r\n" + 
    		"                                <dim:field mdschema =\" dc \" element=\"description\"\r\n" + 
    		"                                    qualifier=\"abstract\" lang=\"en_US\">\r\n" + 
    		"                                    ThisisaSampleHTMLwebpageincludingseveralimagesandstyles(CSS).\r\n" + 
    		"                                </dim:field>\r\n" + 
    		"                                    <dim:field mdschema = \"dc \" element=\"description\"\r\n" + 
    		"                                        qualifier=\"provenance\" lang=\"en\">\r\n" + 
    		"                                        MadeavailableinDSpaceon2012-06-26T19:58:24Z(GMT).No.ofbitstreams:7&#13;Lily-cat-of-day.htm:39970bytes,checksum:aae901336b56ae14070fdec8c79dd48e(MD5)&#13;cl_style.css:1181bytes,checksum:58178d41c221520d88333b9d482b31b8(MD5)&#13;kitty-1257950690.jpg:83390bytes,checksum:729596bb3ad09ebe958a150787418212(MD5)&#13;kitty-1257950690_002.jpg:86057bytes,checksum:37c07c8488042d064bd945765d9e2f98(MD5)&#13;logo.png:4157bytes,checksum:d64fb5f69c7820685d5ed3037b9670ed(MD5)&#13;style.css:49623bytes,checksum:89c2a0557993a8665574c0b52fc1582d(MD5)&#13;stylesheet.css:877bytes,checksum:d2b580ac1c89ae88dc05dcd9108b002e(MD5)\r\n" + 
    		"                                    </dim:field>\r\n" + 
    		"                                        <dim:field mdschema = \"dc \" element=\"language\"\r\n" + 
    		"                                            lang=\"en_US\">\r\n" + 
    		"                                            en\r\n" + 
    		"                                        </dim:field>\r\n" + 
    		"                                            <dim:field mdschema = \"dc \" element=\"rights\"\r\n" + 
    		"                                                lang=\"en_US\">\r\n" + 
    		"                                                ©EverCats.com\r\n" + 
    		"                                            </dim:field>\r\n" + 
    		"                                                <dim:field mdschema = \"dc \" element=\"subject\"\r\n" + 
    		"                                                    lang=\"en_US\">\r\n" + 
    		"                                                    cat\r\n" + 
    		"                                                </dim:field>\r\n" + 
    		"                                                    <dim:field mdschema = \"dc \" element=\"title\"\r\n" + 
    		"                                                        lang=\"en_US\">\r\n" + 
    		"                                                        TestWebpage\r\n" + 
    		"                                                    </dim:field>\r\n" + 
    		"                                                        <dim:field mdschema = \"dc \" element=\"type\"\r\n" + 
    		"                                                            lang=\"en_US\">\r\n" + 
    		"                                                            text\r\n" + 
    		"                                                        </dim:field>\r\n" + 
    		"                                                        </dim:dim>";
    
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void shouldTransformItRight() throws IOException, XSLTransformationException {
        InputStream xmlSchemaTransform = new ByteArrayInputStream(SCHEMA_XSL.getBytes());
        InputStream xmlMetadataTransform = new ByteArrayInputStream(METADATA_XSL.getBytes());
        
        AbstractItem item = mock(AbstractItem.class);
        ItemMetadata metadata = new ItemMetadata(SAMPLE_DATA);
        
        when(item.getMetadata()).thenReturn(metadata);
        
        String result = XSLTUtils.transform(xmlMetadataTransform, xmlSchemaTransform, item).replaceAll("\\s", "");
       
        assertEquals(RESULT.replaceAll("\\s", ""), result);
    }

    @Test
    public void shouldTransformItRight2() throws IOException, XSLTransformationException {
        InputStream xmlSchemaTransform = new ByteArrayInputStream(SCHEMA_XSL.getBytes());
        
        AbstractItem item = mock(AbstractItem.class);
        ItemMetadata metadata = new ItemMetadata(SAMPLE_DATA);
        
        when(item.getMetadata()).thenReturn(metadata);
        
        String result = XSLTUtils.transform(xmlSchemaTransform, item).replaceAll("\\s", "");
        assertEquals(RESULT2.replaceAll("\\s", ""), result);
    }

}
