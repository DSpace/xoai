package com.lyncode.xoai.serviceprovider.parser;

import com.lyncode.xoai.serviceprovider.exceptions.ParseException;

import javax.xml.stream.XMLEventReader;


public interface XMLParser {
    public Object parse(XMLEventReader xmlStreamReader) throws ParseException;
}
