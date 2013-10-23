package com.lyncode.xoai.dataprovider.xml.oaipmh;

import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XMLWrittable;
import com.lyncode.xoai.dataprovider.xml.XSISchema;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;


public class OAIPMH implements XMLWrittable {
    public static final String NAMESPACE_URI = "http://www.openarchives.org/OAI/2.0/";
    public static final String SCHEMA_LOCATION = "http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd";
    private final XOAIManager manager;

    public OAIPMH(XOAIManager manager) {
        this.manager = manager;
    }

    private OAIPMHtype info;

    public OAIPMHtype getInfo() {
        return info;
    }

    public void setInfo(OAIPMHtype info) {
        this.info = info;
    }


    @Override
    public void write(XMLStreamWriter writter) throws WritingXmlException {
        try {
            if (manager.hasStyleSheet())
                writter.writeProcessingInstruction("xml-stylesheet href='" + manager.getStyleSheet() + "' type='text/xsl'");
            writter.writeStartElement("OAI-PMH");
            writter.writeDefaultNamespace(NAMESPACE_URI);
            writter.writeNamespace(XSISchema.PREFIX, XSISchema.NAMESPACE_URI);
            writter.writeAttribute(XSISchema.PREFIX, XSISchema.NAMESPACE_URI, "schemaLocation",
                    NAMESPACE_URI + " " + SCHEMA_LOCATION);
            if (this.info != null)
                info.write(writter);
            writter.writeEndElement();
        } catch (XMLStreamException e) {
            throw new WritingXmlException(e);
        }
    }

}
