package com.lyncode.xoai.model.oaipmh;

import com.lyncode.xml.exceptions.XmlWriteException;
import com.lyncode.xoai.xml.XmlWriter;

import java.util.ArrayList;
import java.util.List;

public class ListRecords implements Verb {

    protected List<Record> records = new ArrayList<Record>();
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
