package com.lyncode.xoai.tests.dataprovider.unit.xml.xoaiconfig;

import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.xml.read.XmlReader;
import com.lyncode.xoai.dataprovider.xml.read.XmlReaderException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.FormatConfiguration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.TransformerConfiguration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.conditions.AndConditionConfiguration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.conditions.FilterConditionConfiguration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.conditions.NotConditionConfiguration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.conditions.OrConditionConfiguration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.RootParameterMap;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.FormatConfigurationParser;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.TransformerConfigurationParser;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.conditions.ConditionParser;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.parameters.*;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration.readConfiguration;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ConfigurationReadTest {
    private static final String FILTER_ID = "filter";
    private static final String ID = "test";
    private static final String XSLT = "test";
    private static final String SCHEMA_LOCATION = "test";
    private static final String NAMESPACE = "test";
    private static final String PREFIX = "test";
    private static final String CUSTOM_REFERENCE_1 = "a";
    private static final String CUSTOM_REFERENCE_2 = "b";
    private static final String CUSTOM_REFERENCE_3 = "c";
    private static final String INTEGER_PARAM = "hello";
    private static final int INTEGER_VALUE = 123;
    private static final String STRING_PARAM = "String";
    private static final String STRING_VALUE = "Hello";
    private static final String LIST_PARAM = "list";
    private static final String MAP_PARAM = "map";
    private static final String FLOAT_PARAM = "float";
    private static final String DOUBLE_PARAM = "double";

    private Configuration configuration;

    @Test
    public void readConfigurationFromInputStream() throws ConfigurationException {
        afterReadConfiguration(aSampleConfiguration());

        assertThat(theConfiguration().getContexts().size(), is(3));
        assertThat(theConfiguration().getConditions().size(), is(1));
        assertThat(theConfiguration().getFilters().size(), is(1));
        assertThat(theConfiguration().getSets().size(), is(2));
        assertThat(theConfiguration().getTransformers().size(), is(2));
        assertThat(theConfiguration().getFormats().size(), is(12));
    }

    private Configuration theConfiguration() {
        return this.configuration;
    }

    @Test
    public void readTransformerConfiguration() throws Exception {
        TransformerConfigurationParser underTest = new TransformerConfigurationParser();
        String ID = "test";
        String XSTL = "test";
        String DESC = "Ola";

        TransformerConfiguration result = underTest.parse(aReader("<Transformer id=\"" + ID + "\"><XSLT>" + XSTL + "</XSLT><Description>" + DESC + "</Description></Transformer>"));

        assertThat(result.getId(), is(ID));
        assertThat(result.getXslt(), is(XSTL));
        assertThat(result.getDescription(), is(DESC));
    }

    @Test
    public void readTransformerConfigurationWithoutDescription() throws Exception {
        TransformerConfigurationParser underTest = new TransformerConfigurationParser();
        TransformerConfiguration result = underTest.parse(aReader("<Transformer id=\"" + ID + "\"><XSLT>" + XSLT + "</XSLT></Transformer>"));

        assertThat(result.getId(), is(ID));
        assertThat(result.getXslt(), is(XSLT));
        assertNull(result.getDescription());
    }


    @Test
    public void readFormatConfiguration() throws Exception {
        FormatConfigurationParser underTest = new FormatConfigurationParser();

        FormatConfiguration result = underTest.parse(aReader("<Format id=\"" + ID + "\">" +
                "<Prefix>" + PREFIX + "</Prefix>" +
                "<XSLT>" + XSLT + "</XSLT>" +
                "<Namespace>" + NAMESPACE + "</Namespace>" +
                "<SchemaLocation>" + SCHEMA_LOCATION + "</SchemaLocation>" +
                "<Filter ref=\"" + FILTER_ID + "\" />" +
                "</Format>"));

        assertThat(result.getId(), is(ID));
        assertThat(result.getXslt(), is(XSLT));
        assertThat(result.getPrefix(), is(PREFIX));
        assertThat(result.getNamespace(), is(NAMESPACE));
        assertThat(result.getSchemaLocation(), is(SCHEMA_LOCATION));
        assertThat(result.getFilter().getReference(), is(FILTER_ID));
    }

    @Test
    public void readFormatConfigurationWithoutFilter() throws Exception {
        FormatConfigurationParser underTest = new FormatConfigurationParser();

        FormatConfiguration result = underTest.parse(aReader("<Format id=\"" + ID + "\">" +
                "<Prefix>" + PREFIX + "</Prefix>" +
                "<XSLT>" + XSLT + "</XSLT>" +
                "<Namespace>" + NAMESPACE + "</Namespace>" +
                "<SchemaLocation>" + SCHEMA_LOCATION + "</SchemaLocation>" +
                "</Format>"));

        assertThat(result.getId(), is(ID));
        assertThat(result.getXslt(), is(XSLT));
        assertThat(result.getPrefix(), is(PREFIX));
        assertThat(result.getNamespace(), is(NAMESPACE));
        assertThat(result.getSchemaLocation(), is(SCHEMA_LOCATION));
        assertNull(result.getFilter());
    }

    @Test
    public void readConditions() throws Exception {
        ConditionParser underTest = new ConditionParser();

        FilterConditionConfiguration result = underTest.parse(aReader("<And>" +
                "<LeftCondition>" +
                "<Not>" +
                "<Condition>" +
                "<Custom ref='" + CUSTOM_REFERENCE_1 + "' />" +
                "</Condition>" +
                "</Not>" +
                "</LeftCondition>" +
                "<RightCondition>" +
                "<Or>" +
                "<LeftCondition>" +
                "<Not><Condition><Custom ref='" + CUSTOM_REFERENCE_2 + "' /></Condition></Not>" +
                "</LeftCondition>" +
                "<RightCondition>" +
                "<Custom ref='" + CUSTOM_REFERENCE_3 + "' />" +
                "</RightCondition>" +
                "</Or>" +
                "</RightCondition>" +
                "</And>"));

        assertTrue(result.is(AndConditionConfiguration.class));
        assertTrue(result.as(AndConditionConfiguration.class).getLeft().is(NotConditionConfiguration.class));
        assertTrue(result.as(AndConditionConfiguration.class).getRight().is(OrConditionConfiguration.class));
    }

    @Test
    public void readParameters() throws Exception {
        RootParameterMapParser underTest = new RootParameterMapParser();
        FloatValueParser floatValueParser = new FloatValueParser();
        BooleanValueParser booleanValueParser = new BooleanValueParser();
        StringValueParser stringValueParser = new StringValueParser();
        DoubleValueParser doubleValueParser = new DoubleValueParser();
        IntValueParser intValueParser = new IntValueParser();

        ParameterListParser parameterListParser = new ParameterListParser();
        ParameterMapParser parameterMapParser = new ParameterMapParser();

        parameterListParser.load(floatValueParser, booleanValueParser, stringValueParser, doubleValueParser, intValueParser, parameterMapParser);
        parameterMapParser.load(floatValueParser, booleanValueParser, stringValueParser, parameterListParser, doubleValueParser, intValueParser);

        underTest.load(floatValueParser, booleanValueParser, stringValueParser,
                parameterListParser, parameterMapParser, doubleValueParser, intValueParser);

        RootParameterMap result = underTest.parse(aReader("<map name='test'>" +
                "<int name='" + INTEGER_PARAM + "'>" + INTEGER_VALUE + "</int>" +
                "<string name='" + STRING_PARAM + "'>" + STRING_VALUE + "</string>" +
                "<list name='" + LIST_PARAM + "'>" +
                "<int>1</int>" +
                "<int>2</int>" +
                "</list>" +
                "<map name='" + MAP_PARAM + "'>" +
                "<float name='" + FLOAT_PARAM + "'>12.1</float>" +
                "<double name='" + DOUBLE_PARAM + "'>12.12</double>" +
                "</map>" +
                "</map>"));

        assertThat(result.get(INTEGER_PARAM).asSimpleType().asInteger(), is(INTEGER_VALUE));
        assertThat(result.get(STRING_PARAM).asSimpleType().asString(), is(STRING_VALUE));
        assertThat(result.get(LIST_PARAM).asParameterList().size(), is(2));
        assertThat(result.get(MAP_PARAM).asParameterMap().get(FLOAT_PARAM).asSimpleType().asFloat(), is(12.1F));
        assertThat(result.get(MAP_PARAM).asParameterMap().get(DOUBLE_PARAM).asSimpleType().asDouble(), is(12.12));
    }


    private XmlReader aReader(String xml) throws XMLStreamException, XmlReaderException {
        XmlReader xmlReader = new XmlReader(new ByteArrayInputStream(("<xml>" + xml + "</xml>").getBytes()));
        xmlReader.proceedToTheNextStartElement();
        xmlReader.proceedToNextElement();
        return xmlReader;
    }

    private void afterReadConfiguration(InputStream intput) throws ConfigurationException {
        this.configuration = readConfiguration(intput);
    }

    private InputStream aSampleConfiguration() {
        return this.getClass().getClassLoader().getResourceAsStream("xoai.xml");
    }
}
