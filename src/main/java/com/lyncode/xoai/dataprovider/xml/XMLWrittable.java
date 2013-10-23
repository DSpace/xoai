package com.lyncode.xoai.dataprovider.xml;

import javax.xml.stream.XMLStreamWriter;

import com.lyncode.xoai.dataprovider.exceptions.WrittingXmlException;


public interface XMLWrittable {
    void write (XMLStreamWriter writter) throws WrittingXmlException;
}
