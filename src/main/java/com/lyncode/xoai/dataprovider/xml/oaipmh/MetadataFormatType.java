//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.01.13 at 08:24:23 PM WET 
//

package com.lyncode.xoai.dataprovider.xml.oaipmh;

import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XMLWritable;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;

import javax.xml.bind.annotation.*;
import javax.xml.stream.XMLStreamException;

import static com.lyncode.xoai.util.XmlIOUtils.writeValue;

/**
 *
 * Java class for metadataFormatType complex type.
 *
 *
 * The following schema fragment specifies the expected content contained within
 * this class.
 *













 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "metadataFormatType", propOrder = {"metadataPrefix", "schema",
        "metadataNamespace"})
public class MetadataFormatType implements XMLWritable {

    @XmlElement(required = true)
    protected String metadataPrefix;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String schema;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String metadataNamespace;

    /**
     * Gets the value of the metadataPrefix property.
     *
     * @return possible object is ;
     */
    public String getMetadataPrefix() {
        return metadataPrefix;
    }

    /**
     * Sets the value of the metadataPrefix property.
     *
     * @param value allowed object is ;
     */
    public void setMetadataPrefix(String value) {
        this.metadataPrefix = value;
    }

    /**
     * Gets the value of the schema property.
     *
     * @return possible object is ;
     */
    public String getSchema() {
        return schema;
    }

    /**
     * Sets the value of the schema property.
     *
     * @param value allowed object is ;
     */
    public void setSchema(String value) {
        this.schema = value;
    }

    /**
     * Gets the value of the metadataNamespace property.
     *
     * @return possible object is ;
     */
    public String getMetadataNamespace() {
        return metadataNamespace;
    }

    /**
     * Sets the value of the metadataNamespace property.
     *
     * @param value allowed object is ;
     */
    public void setMetadataNamespace(String value) {
        this.metadataNamespace = value;
    }

    /*
     *



     */
    @Override
    public void write(XmlOutputContext context) throws WritingXmlException {
        try {
            if (metadataPrefix != null)
                writeValue(context.getWriter(), "metadataPrefix", metadataPrefix);
            if (schema != null)
                writeValue(context.getWriter(), "schema", schema);
            if (metadataNamespace != null)
                writeValue(context.getWriter(), "metadataNamespace", metadataNamespace);
        } catch (XMLStreamException e) {
            throw new WritingXmlException(e);
        }
    }

}
