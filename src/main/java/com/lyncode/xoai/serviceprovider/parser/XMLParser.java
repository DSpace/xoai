package com.lyncode.xoai.serviceprovider.parser;

import javax.xml.stream.XMLEventReader;

import com.lyncode.xoai.serviceprovider.exceptions.ParseException;


public interface XMLParser {
    public Object parse (XMLEventReader xmlStreamReader) throws ParseException;
}
