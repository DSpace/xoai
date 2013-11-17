package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.parameters;

import com.lyncode.xoai.dataprovider.xml.read.XmlReader;
import com.lyncode.xoai.dataprovider.xml.read.XmlReaderException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.SimpleType;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.ParseException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.Parser;

public abstract class SimpleTypeParser extends Parser<SimpleType> {
    @Override
    public SimpleType parse(XmlReader reader) throws ParseException {
        try {
            String name = reader.getAttribute("name");
            SimpleType simpleType = parse(name, reader.getText());
            reader.proceedToNextElement();
            return simpleType;
        } catch (XmlReaderException e) {
            throw new ParseException(e);
        }
    }

    protected abstract SimpleType parse(String name, String data);
}
