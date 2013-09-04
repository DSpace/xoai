package com.lyncode.xoai.dataprovider.xml.oaipmh;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.lyncode.xoai.dataprovider.exceptions.WrittingXmlException;
import com.lyncode.xoai.dataprovider.xml.XMLWrittable;
import com.lyncode.xoai.dataprovider.xml.XSISchema;


public class OAIPMH implements XMLWrittable {
    public static final String NAMESPACE_URI = "http://www.openarchives.org/OAI/2.0/";
    public static final String SCHEMA_LOCATION = "http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd";
    
    private OAIPMHtype info;

    public OAIPMHtype getInfo() {

        return info;
    }

    public void setInfo(OAIPMHtype info) {

        this.info = info;
    }
    


    @Override
    public void write(XMLStreamWriter writter) throws WrittingXmlException {
        try {
            writter.writeStartDocument();
            writter.writeStartElement("OAI-PMH");
            writter.writeDefaultNamespace(NAMESPACE_URI);
            writter.writeAttribute(XSISchema.PREFIX,XSISchema.NAMESPACE_URI, "schemaLocation", 
                                   NAMESPACE_URI+" "+SCHEMA_LOCATION);
            if (this.info != null)
                info.write(writter);
            writter.writeEndElement();
            writter.writeEndDocument();
        } catch (XMLStreamException e) {
            throw new WrittingXmlException(e);
        }
    }

}
