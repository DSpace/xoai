package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse;

import com.lyncode.xoai.dataprovider.xml.read.XmlReader;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.BundleReference;

public class BundleReferenceParser extends Parser<BundleReference> {
    @Override
    public BundleReference parse(XmlReader reader) throws com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse.ParseException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
//    @Override
//    public BundleReference parse(XMLEventReader reader) throws ParseException {
//        try {
//            return new BundleReference(getAttribute(reader, "ref"));
//        } catch (XMLStreamException e) {
//            throw new ParseException(e.getMessage(), e);
//        }
//    }
}
