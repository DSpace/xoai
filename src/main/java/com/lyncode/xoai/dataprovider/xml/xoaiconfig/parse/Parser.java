package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse;

import com.lyncode.xoai.dataprovider.xml.read.XmlReader;

public abstract class Parser<T> {
    public abstract T parse(XmlReader reader) throws ParseException;
}
