<?xml version="1.0"?>
<!--
  ~ The contents of this file are subject to the license and copyright
  ~ detailed in the LICENSE and NOTICE files at the root of the source
  ~ tree and available online at
  ~
  ~ http://www.dspace.org/license/
  -->

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
                xmlns:dc="http://purl.org/dc/elements/1.1/"
                exclude-result-prefixes="dc oai_dc"
        >

    <xsl:output method="xml" omit-xml-declaration="yes" />

    <xsl:template match="/">
        <metadata xmlns="http://www.lyncode.com/xoai">
            <element name="dc">
                <xsl:for-each select="/oai_dc:dc/dc:*">
                    <element>
                        <xsl:attribute name="name">
                            <xsl:value-of select="local-name(.)" />
                        </xsl:attribute>
                        <field name="value">
                            <xsl:value-of select="text()" />
                        </field>
                        <xsl:choose>
							<xsl:when test="@xml:lang">
								<field name="xml:lang">
	                        		<xsl:value-of select="@xml:lang" />
	                        	</field>
	                        </xsl:when>
                        </xsl:choose>
                        
                    </element>
                </xsl:for-each>
            </element>
        </metadata>
    </xsl:template>
</xsl:stylesheet>