package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;


public class RootParameterMap extends ParameterMap {
    @Override
    public void write(XmlOutputContext writer) throws WritingXmlException {
        for (ParameterValue value : getValues())
            value.write(writer);
    }
}
