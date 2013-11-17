package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.parameters;

import com.lyncode.xoai.dataprovider.xml.read.XmlReader;
import com.lyncode.xoai.dataprovider.xml.read.XmlReaderException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.ParameterMap;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.ParseException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.Parser;

public class ParameterMapParser extends Parser<ParameterMap> {

    private FloatValueParser floatValueParser;
    private StringValueParser stringValueParser;
    private ParameterListParser parameterListParser;
    private IntValueParser intValueParser;
    private DoubleValueParser doubleValueParser;
    private BooleanValueParser booleanValueParser;

    public void load(FloatValueParser floatValueParser, BooleanValueParser booleanValueParser, StringValueParser stringValueParser, ParameterListParser parameterListParser, DoubleValueParser doubleValueParser, IntValueParser intValueParser) {
        this.floatValueParser = floatValueParser;
        this.booleanValueParser = booleanValueParser;
        this.stringValueParser = stringValueParser;
        this.parameterListParser = parameterListParser;
        this.doubleValueParser = doubleValueParser;
        this.intValueParser = intValueParser;
    }

    @Override
    public ParameterMap parse(XmlReader reader) throws ParseException {
        try {
            ParameterMap parameterMap = new ParameterMap();
            parameterMap.withName(reader.getAttribute("name"));

            reader.proceedToNextElement();
            while (reader.isStart()) {
                if (reader.elementNameIs("list"))
                    parameterMap.withValues(parameterListParser.parse(reader));
                else if (reader.elementNameIs("int"))
                    parameterMap.withValues(intValueParser.parse(reader));
                else if (reader.elementNameIs("double"))
                    parameterMap.withValues(doubleValueParser.parse(reader));
                else if (reader.elementNameIs("float"))
                    parameterMap.withValues(floatValueParser.parse(reader));
                else if (reader.elementNameIs("boolean"))
                    parameterMap.withValues(booleanValueParser.parse(reader));
                else if (reader.elementNameIs("string"))
                    parameterMap.withValues(stringValueParser.parse(reader));
                else if (reader.elementNameIs("map"))
                    parameterMap.withValues(this.parse(reader));
                else throw new ParseException("Unknown configuration value " + reader.getName());
            }
            reader.proceedToNextElement();
            return parameterMap;
        } catch (XmlReaderException e) {
            throw new ParseException(e);
        }
    }
}
