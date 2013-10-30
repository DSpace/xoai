package com.lyncode.xoai.tests.serviceprovider.unit.parser;

import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.ErrorParser;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.OAIPMHerrorType;
import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import static com.lyncode.xoai.tests.syntax.SyntacticSugar.given;
import static com.lyncode.xoai.tests.syntax.SyntacticSugar.with;
import static org.junit.Assert.assertEquals;


public class ErrorParserTest extends AbstractParseTest<OAIPMHerrorType> {
    static String XML = "<error code=\"badVerb\">Illegal verb</error>";

    @Test
    public void testError() throws XMLStreamException, ParseException {
        given(aXmlEventReader(with(XML)));
        inPosition();

        afterParsingTheGivenContent();

        assertEquals("badVerb", theResult().getCode().value());
    }

    @Override
    protected OAIPMHerrorType parse(XMLEventReader reader) throws ParseException {
        return new ErrorParser(theConfiguration()).parse(reader);
    }
}
