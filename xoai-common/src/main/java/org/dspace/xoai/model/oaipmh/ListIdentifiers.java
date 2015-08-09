/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.model.oaipmh;

import com.lyncode.xml.exceptions.XmlWriteException;
import org.dspace.xoai.xml.XmlWriter;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.List;

public class ListIdentifiers implements Verb {

    protected List<Header> headers = new ArrayList<Header>();
    protected ResumptionToken resumptionToken;

    public List<Header> getHeaders() {
        return this.headers;
    }

    public ResumptionToken getResumptionToken() {
        return resumptionToken;
    }

    public ListIdentifiers withResumptionToken(ResumptionToken value) {
        this.resumptionToken = value;
        return this;
    }

    @Override
    public void write(XmlWriter writer) throws XmlWriteException {
        try {
            if (this.headers != null && !this.headers.isEmpty()) {
                for (Header header : this.headers) {
                    writer.writeStartElement("header");
                    header.write(writer);
                    writer.writeEndElement();
                }
            }

            if (this.resumptionToken != null) {
                writer.writeStartElement("resumptionToken");
                this.resumptionToken.write(writer);
                writer.writeEndElement();
            }
        } catch (XMLStreamException e) {
            throw new XmlWriteException(e);
        }
    }

    @Override
    public Type getType() {
        return Type.ListIdentifiers;
    }
}
