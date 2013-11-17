package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.parameters;

import com.lyncode.xoai.dataprovider.xml.read.XmlReader;
import com.lyncode.xoai.dataprovider.xml.read.XmlReaderException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.ParameterList;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.ParseException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.Parser;

public class ParameterListParser extends Parser<ParameterList> {

    private FloatValueParser floatValueParser;
    private StringValueParser stringValueParser;
    private IntValueParser intValueParser;
    private DoubleValueParser doubleValueParser;
    private BooleanValueParser booleanValueParser;
    private ParameterMapParser parameterMapParser;

    public void load(FloatValueParser floatValueParser, BooleanValueParser booleanValueParser, StringValueParser stringValueParser, DoubleValueParser doubleValueParser, IntValueParser intValueParser, ParameterMapParser parameterMapParser) {
        this.floatValueParser = floatValueParser;
        this.booleanValueParser = booleanValueParser;
        this.stringValueParser = stringValueParser;
        this.doubleValueParser = doubleValueParser;
        this.intValueParser = intValueParser;
        this.parameterMapParser = parameterMapParser;
    }

    @Override
    public ParameterList parse(XmlReader reader) throws ParseException {
        try {
            String name = reader.getAttribute("name");
            ParameterList parameterList = (ParameterList) new ParameterList().withName(name);
            reader.proceedToNextElement();
            while (reader.isStart()) {
                if (reader.elementNameIs("list"))
                    parameterList.withValues(this.parse(reader));
                else if (reader.elementNameIs("int"))
                    parameterList.withValues(intValueParser.parse(reader));
                else if (reader.elementNameIs("double"))
                    parameterList.withValues(doubleValueParser.parse(reader));
                else if (reader.elementNameIs("float"))
                    parameterList.withValues(floatValueParser.parse(reader));
                else if (reader.elementNameIs("boolean"))
                    parameterList.withValues(booleanValueParser.parse(reader));
                else if (reader.elementNameIs("string"))
                    parameterList.withValues(stringValueParser.parse(reader));
                else if (reader.elementNameIs("map"))
                    parameterList.withValues(parameterMapParser.parse(reader));
                else throw new ParseException("Unknown configuration value " + reader.getName());
            }
            reader.proceedToNextElement();
            return parameterList;
        } catch (XmlReaderException e) {
            throw new ParseException(e);
        }
    }

}
