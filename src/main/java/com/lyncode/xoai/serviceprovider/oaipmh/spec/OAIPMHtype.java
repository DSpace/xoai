//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.01.13 at 08:24:23 PM WET 
//

package com.lyncode.xoai.serviceprovider.oaipmh.spec;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Java class for OAI-PMHtype complex type.
 *
 *
 * The following schema fragment specifies the expected content contained within
 * this class.
 *





















 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OAI-PMHtype", propOrder = {"responseDate", "request",
        "error", "identify", "listMetadataFormats", "listSets", "getRecord",
        "listIdentifiers", "listRecords"})
public class OAIPMHtype {

    @XmlElement(required = true)
    protected Date responseDate;
    @XmlElement(required = true)
    protected RequestType request;
    protected List<OAIPMHerrorType> error;
    @XmlElement(name = "Identify")
    protected IdentifyType identify;
    @XmlElement(name = "ListMetadataFormats")
    protected ListMetadataFormatsType listMetadataFormats;
    @XmlElement(name = "ListSets")
    protected ListSetsType listSets;
    @XmlElement(name = "GetRecord")
    protected GetRecordType getRecord;
    @XmlElement(name = "ListIdentifiers")
    protected ListIdentifiersType listIdentifiers;
    @XmlElement(name = "ListRecords")
    protected ListRecordsType listRecords;

    /**
     * Gets the value of the responseDate property.
     *
     * @return possible object is
     */
    public Date getResponseDate() {
        return responseDate;
    }

    /**
     * Sets the value of the responseDate property.
     *
     * @param value allowed object is
     */
    public void setResponseDate(Date value) {
        this.responseDate = value;
    }

    /**
     * Gets the value of the request property.
     *
     * @return possible object is ;
     */
    public RequestType getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     *
     * @param value allowed object is ;
     */
    public void setRequest(RequestType value) {
        this.request = value;
    }

    /**
     * Gets the value of the error property.
     *
     *
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
      method for the error property.
     *
     *
     * For example, to add a new item, do as follows:
     *

     * getError().add(newItem);

     *
     *
     *
     * Objects of the following type(s) are allowed in the list
     * ;
     */
    public List<OAIPMHerrorType> getError() {
        if (error == null) {
            error = new ArrayList<OAIPMHerrorType>();
        }
        return this.error;
    }

    /**
     * Gets the value of the identify property.
     *
     * @return possible object is ;
     */
    public IdentifyType getIdentify() {
        return identify;
    }

    /**
     * Sets the value of the identify property.
     *
     * @param value allowed object is ;
     */
    public void setIdentify(IdentifyType value) {
        this.identify = value;
    }

    /**
     * Gets the value of the listMetadataFormats property.
     *
     * @return possible object is ;
     */
    public ListMetadataFormatsType getListMetadataFormats() {
        return listMetadataFormats;
    }

    /**
     * Sets the value of the listMetadataFormats property.
     *
     * @param value allowed object is ;
     */
    public void setListMetadataFormats(ListMetadataFormatsType value) {
        this.listMetadataFormats = value;
    }

    /**
     * Gets the value of the listSets property.
     *
     * @return possible object is ;
     */
    public ListSetsType getListSets() {
        return listSets;
    }

    /**
     * Sets the value of the listSets property.
     *
     * @param value allowed object is ;
     */
    public void setListSets(ListSetsType value) {
        this.listSets = value;
    }

    /**
     * Gets the value of the getRecord property.
     *
     * @return possible object is ;
     */
    public GetRecordType getGetRecord() {
        return getRecord;
    }

    /**
     * Sets the value of the getRecord property.
     *
     * @param value allowed object is ;
     */
    public void setGetRecord(GetRecordType value) {
        this.getRecord = value;
    }

    /**
     * Gets the value of the listIdentifiers property.
     *
     * @return possible object is ;
     */
    public ListIdentifiersType getListIdentifiers() {
        return listIdentifiers;
    }

    /**
     * Sets the value of the listIdentifiers property.
     *
     * @param value allowed object is ;
     */
    public void setListIdentifiers(ListIdentifiersType value) {
        this.listIdentifiers = value;
    }

    /**
     * Gets the value of the listRecords property.
     *
     * @return possible object is ;
     */
    public ListRecordsType getListRecords() {
        return listRecords;
    }

    /**
     * Sets the value of the listRecords property.
     *
     * @param value allowed object is ;
     */
    public void setListRecords(ListRecordsType value) {
        this.listRecords = value;
    }

}
