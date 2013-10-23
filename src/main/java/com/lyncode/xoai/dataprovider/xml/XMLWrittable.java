package com.lyncode.xoai.dataprovider.xml;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;

import javax.xml.stream.XMLStreamWriter;


public interface XMLWrittable {
    void write(XMLStreamWriter writter) throws WritingXmlException;
}
