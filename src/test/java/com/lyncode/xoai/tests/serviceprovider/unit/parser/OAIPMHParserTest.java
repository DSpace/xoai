package com.lyncode.xoai.tests.serviceprovider.unit.parser;

import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.OAIPMHParser;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.OAIPMHtype;
import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import static com.lyncode.xoai.tests.SyntacticSugar.given;
import static com.lyncode.xoai.tests.SyntacticSugar.with;
import static org.junit.Assert.assertEquals;


public class OAIPMHParserTest extends AbstractParseTest<OAIPMHtype> {
    static String XML = "<OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd\"><responseDate>2013-09-12T21:15:35Z</responseDate>\r\n" +
            "    <request verb=\"Identify\">http://demo.dspace.org/oai/request</request>\r\n" +
            "    <Identify>\r\n" +
            "        <repositoryName>DSpace Demo Repository</repositoryName>\r\n" +
            "        <baseURL>http://demo.dspace.org/oai/request</baseURL>\r\n" +
            "        <protocolVersion>2.0</protocolVersion>\r\n" +
            "        <adminEmail>dspacedemo@gmail.com</adminEmail>\r\n" +
            "        <earliestDatestamp>1650-06-26T19:58:25Z</earliestDatestamp>\r\n" +
            "        <deletedRecord>persistent</deletedRecord>\r\n" +
            "        <granularity>YYYY-MM-DDThh:mm:ssZ</granularity>\r\n" +
            "        <description>\r\n" +
            "<XOAIDescription xmlns=\"http://www.lyncode.com/XOAIConfiguration\">XOAI: OAI-PMH Java Toolkit</XOAIDescription>\r\n" +
            "</description>\r\n" +
            "    </Identify>\r\n" +
            "</OAI-PMH>";

    @Test
    public void test() throws XMLStreamException, ParseException {
        given(aXmlEventReader(with(XML)));
        inPosition();

        afterParsingTheGivenContent();

        assertEquals("DSpace Demo Repository", theResult().getIdentify().getRepositoryName());
    }

    @Override
    protected OAIPMHtype parse(XMLEventReader reader) throws ParseException {
        return new OAIPMHParser(theConfiguration()).parse(reader);
    }
}
