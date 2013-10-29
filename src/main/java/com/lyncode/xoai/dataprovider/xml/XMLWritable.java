package com.lyncode.xoai.dataprovider.xml;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;


public interface XMLWritable {
    void write(XmlOutputContext writer) throws WritingXmlException;
}
