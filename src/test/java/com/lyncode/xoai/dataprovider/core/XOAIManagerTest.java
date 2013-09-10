package com.lyncode.xoai.dataprovider.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.lyncode.xoai.dataprovider.configuration.ConfigurationManager;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration;


@PrepareForTest({ ConfigurationManager.class  })
@RunWith(PowerMockRunner.class)
public class XOAIManagerTest {
    static String FILEconfig = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
    		"<!-- \r\n" + 
    		"\r\n" + 
    		"    The contents of this file are subject to the license and copyright\r\n" + 
    		"    detailed in the LICENSE and NOTICE files at the root of the source\r\n" + 
    		"    tree and available online at\r\n" + 
    		"\r\n" + 
    		"    http://www.dspace.org/license/\r\n" + 
    		"\r\n" + 
    		"    Developed by DSpace @ Lyncode <dspace@lyncode.com>\r\n" + 
    		" -->\r\n" + 
    		"<Configuration indentation=\"false\" maxListIdentifiersSize=\"100\" maxListRecordsSize=\"100\"\r\n" + 
    		"    maxListSetsSize=\"100\" stylesheet=\"static/style.xsl\" \r\n" + 
    		"    >\r\n" + 
    		"\r\n" + 
    		"    <Contexts>\r\n" + 
    		"        <Context baseurl=\"request\">\r\n" + 
    		"            <Format refid=\"oaidc\" />\r\n" + 
    		"            <Format refid=\"mets\" />\r\n" + 
    		"            <Format refid=\"xoai\" />\r\n" + 
    		"            <Format refid=\"didl\" />\r\n" + 
    		"            <Format refid=\"dim\" />\r\n" + 
    		"            <Format refid=\"ore\" />\r\n" + 
    		"            <Format refid=\"rdf\" />\r\n" + 
    		"            <Format refid=\"etdms\" />\r\n" + 
    		"            <Format refid=\"mods\" />\r\n" + 
    		"            <Format refid=\"qdc\" />\r\n" + 
    		"            <Format refid=\"marc\" />\r\n" + 
    		"            <Format refid=\"uketd_dc\" />\r\n" + 
    		"        </Context> \r\n" + 
    		"        \r\n" + 
    		"        <!--\r\n" + 
    		"            Driver Guidelines:\r\n" + 
    		"         \r\n" + 
    		"            - http://www.driver-support.eu/documents/DRIVER_Guidelines_v2_Final_2008-11-13.pdf\r\n" + 
    		"            \r\n" + 
    		"            Page 57 - 58\r\n" + 
    		"         -->\r\n" + 
    		"        <Context baseurl=\"driver\">\r\n" + 
    		"            <!-- Date format, field prefixes, etc are ensured by the transformer -->\r\n" + 
    		"            <Transformer refid=\"driverTransformer\"/>\r\n" + 
    		"            <!-- Title : Mandatory -->\r\n" + 
    		"            <Filter refid=\"openairerelationFilter\" />\r\n" + 
    		"            <!-- Just an alias, if fact it returns all items within the driver context -->\r\n" + 
    		"            <Set refid=\"driverSet\" />\r\n" + 
    		"            <!-- Metadata Formats -->\r\n" + 
    		"            <Format refid=\"oaidc\"/>\r\n" + 
    		"            <Format refid=\"mets\" />\r\n" + 
    		"            <Format refid=\"didl\" />\r\n" + 
    		"        </Context>\r\n" + 
    		"        \r\n" + 
    		"        <!-- \r\n" + 
    		"            OpenAIRE Guidelines 1.1:\r\n" + 
    		"            \r\n" + 
    		"            - http://www.openaire.eu/en/component/attachments/download/79%E2%8C%A9=en\r\n" + 
    		"            \r\n" + 
    		"            There is a limitation over the embargoedEndDate parameter:\r\n" + 
    		"            \r\n" + 
    		"            - Predefined DSpace fields don't allow to set this up with a default.\r\n" + 
    		"         -->\r\n" + 
    		"        <Context baseurl=\"openaire\">\r\n" + 
    		"            <!-- Date format, field prefixes, etc are ensured by the transformer -->\r\n" + 
    		"            <Transformer refid=\"openaireTransformer\" />\r\n" + 
    		"            <!-- Title : Mandatory -->\r\n" + 
    		"            <Filter refid=\"openairerelationFilter\" />\r\n" + 
    		"            <!-- Just an alias, if fact it returns all items within the driver context -->\r\n" + 
    		"            <Set refid=\"openaireSet\" />\r\n" + 
    		"            <!-- Metadata Formats -->\r\n" + 
    		"            <Format refid=\"oaidc\" />\r\n" + 
    		"            <Format refid=\"mets\" />\r\n" + 
    		"        </Context>\r\n" + 
    		"    </Contexts>\r\n" + 
    		"    \r\n" + 
    		"    \r\n" + 
    		"    <Formats>\r\n" + 
    		"        <Format id=\"oaidc\">\r\n" + 
    		"            <Prefix>oai_dc</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/oai_dc.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://www.openarchives.org/OAI/2.0/oai_dc/</Namespace>\r\n" + 
    		"            <SchemaLocation>http://www.openarchives.org/OAI/2.0/oai_dc.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <Format id=\"mets\">\r\n" + 
    		"            <Prefix>mets</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/mets.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://www.loc.gov/METS/</Namespace>\r\n" + 
    		"            <SchemaLocation>http://www.loc.gov/standards/mets/mets.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <!-- Shows the XOAI internal generated XML -->\r\n" + 
    		"        <Format id=\"xoai\">\r\n" + 
    		"            <Prefix>xoai</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/xoai.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://www.lyncode.com/xoai</Namespace>\r\n" + 
    		"            <SchemaLocation>http://www.lyncode.com/schemas/xoai.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <Format id=\"didl\">\r\n" + 
    		"            <Prefix>didl</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/didl.xsl</XSLT>\r\n" + 
    		"            <Namespace>urn:mpeg:mpeg21:2002:02-DIDL-NS</Namespace>\r\n" + 
    		"            <SchemaLocation>http://standards.iso.org/ittf/PubliclyAvailableStandards/MPEG-21_schema_files/did/didl.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <Format id=\"dim\">\r\n" + 
    		"            <Prefix>dim</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/dim.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://www.dspace.org/xmlns/dspace/dim</Namespace>\r\n" + 
    		"            <SchemaLocation>http://www.dspace.org/schema/dim.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <Format id=\"ore\">\r\n" + 
    		"            <Prefix>ore</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/ore.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://www.w3.org/2005/Atom</Namespace>\r\n" + 
    		"            <SchemaLocation>http://tweety.lanl.gov/public/schemas/2008-06/atom-tron.sch</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <Format id=\"rdf\">\r\n" + 
    		"            <Prefix>rdf</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/rdf.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://www.openarchives.org/OAI/2.0/rdf/</Namespace>\r\n" + 
    		"            <SchemaLocation>http://www.openarchives.org/OAI/2.0/rdf.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <Format id=\"etdms\">\r\n" + 
    		"            <Prefix>etdms</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/etdms.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://www.ndltd.org/standards/metadata/etdms/1.0/</Namespace>\r\n" + 
    		"            <SchemaLocation>http://www.ndltd.org/standards/metadata/etdms/1.0/etdms.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <Format id=\"mods\">\r\n" + 
    		"            <Prefix>mods</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/mods.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://www.loc.gov/mods/v3</Namespace>\r\n" + 
    		"            <SchemaLocation>http://www.loc.gov/standards/mods/v3/mods-3-1.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <Format id=\"qdc\">\r\n" + 
    		"            <Prefix>qdc</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/qdc.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://purl.org/dc/terms/</Namespace>\r\n" + 
    		"            <SchemaLocation>http://dublincore.org/schemas/xmls/qdc/2006/01/06/dcterms.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <Format id=\"marc\">\r\n" + 
    		"            <Prefix>marc</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/marc.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://www.loc.gov/MARC21/slim</Namespace>\r\n" + 
    		"            <SchemaLocation>http://www.loc.gov/standards/marcxml/schema/MARC21slim.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <Format id=\"uketd_dc\">\r\n" + 
    		"            <Prefix>uketd_dc</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/uketd_dc.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://naca.central.cranfield.ac.uk/ethos-oai/2.0/</Namespace>\r\n" + 
    		"            <SchemaLocation>http://naca.central.cranfield.ac.uk/ethos-oai/2.0/uketd_dc.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"    </Formats>\r\n" + 
    		"    \r\n" + 
    		"    <Transformers>\r\n" + 
    		"        <Transformer id=\"driverTransformer\">\r\n" + 
    		"            <XSLT>transformers/driver.xsl</XSLT>\r\n" + 
    		"        </Transformer>\r\n" + 
    		"        <Transformer id=\"openaireTransformer\">\r\n" + 
    		"            <XSLT>transformers/openaire.xsl</XSLT>\r\n" + 
    		"        </Transformer>\r\n" + 
    		"    </Transformers>\r\n" + 
    		"    \r\n" + 
    		"\r\n" + 
    		"    <Filters>\r\n" + 
    		"        <CustomFilter id=\"authorexistsCondition\">\r\n" + 
    		"            <Class>com.lyncode.xoai.dataprovider.core.CustomFilterStub</Class>\r\n" +
    		"        </CustomFilter>\r\n" +
    		"        <Filter id=\"openairerelationFilter\">\r\n" + 
    		"            <definition>\r\n" + 
    		"                <Custom refid=\"authorexistsCondition\"/>\r\n" + 
    		"            </definition>\r\n" + 
    		"        </Filter>\r\n" + 
    		"    </Filters>\r\n" + 
    		"    \r\n" + 
    		"    <Sets>\r\n" + 
    		"        <Set id=\"driverSet\">\r\n" + 
    		"            <Pattern>driver</Pattern>\r\n" + 
    		"            <Name>Open Access DRIVERset</Name>\r\n" + 
    		"            <!-- Just an alias -->\r\n" + 
    		"        </Set>\r\n" + 
    		"        <Set id=\"openaireSet\">\r\n" + 
    		"            <Pattern>ec_fundedresources</Pattern>\r\n" + 
    		"            <Name>EC_fundedresources set</Name>\r\n" + 
    		"            <!-- Just an alias -->\r\n" + 
    		"        </Set>\r\n" + 
    		"    </Sets>\r\n" + 
    		"</Configuration>\r\n";
    		

    @Test
    public void shouldInitializeFilters() throws ConfigurationException {
        Configuration config = ConfigurationManager.readConfiguration(new ByteArrayInputStream(FILEconfig.getBytes()));
        
        PowerMockito.mockStatic(ConfigurationManager.class);
        PowerMockito.when(ConfigurationManager.readConfiguration(anyString())).thenReturn(config);
        PowerMockito.when(ConfigurationManager.readConfiguration(any(InputStream.class))).thenReturn(config);
        
        XOAIManager.initialize("USELESS");
        XOAIManager instance = XOAIManager.getManager();
        assertFalse(instance.getFilterManager().getFilters().isEmpty());
        assertFalse(instance.getContextManager().getOAIContext("driver").getFilters().isEmpty());
    }

}
