package com.lyncode.xoai.dataprovider.xml.oaipmh;

import com.lyncode.xoai.dataprovider.xml.XMLWritable;
import com.lyncode.xoai.dataprovider.xml.XSISchema;
import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;


public class OAIPMH implements XMLWritable {
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
    public void write(XmlOutputContext context) throws WritingXmlException {
        try {
            if (manager.hasStyleSheet())
                context.getWriter().writeProcessingInstruction("xml-stylesheet href='" + manager.getStyleSheet() + "' type='text/xsl'");
            context.getWriter().writeStartElement("OAI-PMH");
            context.getWriter().writeDefaultNamespace(NAMESPACE_URI);
            context.getWriter().writeNamespace(XSISchema.PREFIX, XSISchema.NAMESPACE_URI);
            context.getWriter().writeAttribute(XSISchema.PREFIX, XSISchema.NAMESPACE_URI, "schemaLocation",
                    NAMESPACE_URI + " " + SCHEMA_LOCATION);
            if (this.info != null)
                info.write(context);
            context.getWriter().writeEndElement();
        } catch (XMLStreamException e) {
            throw new WritingXmlException(e);
        }
    }

}
