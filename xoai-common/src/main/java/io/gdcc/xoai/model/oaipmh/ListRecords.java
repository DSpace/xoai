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

import java.util.ArrayList;
import java.util.List;

public class ListRecords implements Verb {

    protected final List<Record> records = new ArrayList<>();
    protected ResumptionToken resumptionToken;

    public List<Record> getRecords() {
        return this.records;
    }

    public ResumptionToken getResumptionToken() {
        return resumptionToken;
    }

    public ListRecords withResumptionToken(ResumptionToken value) {
        this.resumptionToken = value;
        return this;
    }

    public ListRecords withRecord(Record record) {
        this.records.add(record);
        return this;
    }

    @Override
    public void write(XmlWriter writer) throws XmlWriteException {
        if (!this.records.isEmpty())
            for (Record record : this.records)
                writer.writeElement("record", record);
        writer.writeElement("resumptionToken", resumptionToken);
    }

    @Override
    public Type getType() {
        return Type.ListRecords;
    }
}
