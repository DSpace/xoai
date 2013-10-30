package com.lyncode.xoai.tests.serviceprovider.unit.parser;

import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.IdentifyParser;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.IdentifyType;
import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import static com.lyncode.xoai.tests.syntax.SyntacticSugar.given;
import static com.lyncode.xoai.tests.syntax.SyntacticSugar.with;
import static org.junit.Assert.assertEquals;


public class IdentifyParserTest extends AbstractParseTest<IdentifyType> {
    static String XML = "<Identify>\r\n" +
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
            "    </Identify>";

    @Test
    public void testIdentify() throws XMLStreamException, ParseException {
        given(aXmlEventReader(with(XML)));
        inPosition();

        afterParsingTheGivenContent();

        assertEquals("DSpace Demo Repository", theResult().getRepositoryName());
    }

    @Override
    protected IdentifyType parse(XMLEventReader reader) throws ParseException {
        return new IdentifyParser(theConfiguration()).parse(reader);
    }
}
