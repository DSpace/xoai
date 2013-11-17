package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse;

import com.lyncode.xoai.dataprovider.xml.read.XmlReader;
import com.lyncode.xoai.dataprovider.xml.read.XmlReaderException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.ConditionConfiguration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.parameters.*;

public class ConditionConfigurationParser extends Parser<ConditionConfiguration> {
    private RootParameterMapParser rootParameterMapParser;

    public ConditionConfigurationParser() {
        this.rootParameterMapParser = new RootParameterMapParser();
        FloatValueParser floatValueParser = new FloatValueParser();
        BooleanValueParser booleanValueParser = new BooleanValueParser();
        StringValueParser stringValueParser = new StringValueParser();
        DoubleValueParser doubleValueParser = new DoubleValueParser();
        IntValueParser intValueParser = new IntValueParser();

        ParameterListParser parameterListParser = new ParameterListParser();
        ParameterMapParser parameterMapParser = new ParameterMapParser();

        parameterListParser.load(floatValueParser, booleanValueParser, stringValueParser, doubleValueParser, intValueParser, parameterMapParser);
        parameterMapParser.load(floatValueParser, booleanValueParser, stringValueParser, parameterListParser, doubleValueParser, intValueParser);

        rootParameterMapParser.load(floatValueParser, booleanValueParser, stringValueParser,
                parameterListParser, parameterMapParser, doubleValueParser, intValueParser);
    }

    @Override
    public ConditionConfiguration parse(XmlReader reader) throws ParseException {
        try {
            ConditionConfiguration conditionConfiguration = new ConditionConfiguration(reader.getAttribute("id"));
            conditionConfiguration.withClazz(reader.getNextElementText("Class"));

            reader.proceedToNextElement();
            if (reader.isStart()) {
                conditionConfiguration.withConfiguration(rootParameterMapParser.parse(reader));
                reader.proceedToNextElement();
            }

            return conditionConfiguration;
        } catch (XmlReaderException e) {
            throw new ParseException(e);
        }
    }
}
