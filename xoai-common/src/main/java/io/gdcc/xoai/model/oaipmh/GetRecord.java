/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.model.oaipmh;

import io.gdcc.xoai.xmlio.exceptions.XmlWriteException;
import io.gdcc.xoai.xml.XmlWriter;

import javax.xml.stream.XMLStreamException;

public class GetRecord implements Verb {
    private final Record record;

    public GetRecord(Record record) {
        this.record = record;
    }

    @Override
    public void write(XmlWriter writer) throws XmlWriteException {
        try {
            writer.writeStartElement("record");
            writer.write(record);
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            throw new XmlWriteException(e);
        }
    }

    @Override
    public Type getType() {
        return Type.GetRecord;
    }
}
