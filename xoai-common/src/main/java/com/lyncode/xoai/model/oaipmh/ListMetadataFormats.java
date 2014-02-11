package com.lyncode.xoai.model.oaipmh;

import com.lyncode.xml.exceptions.XmlWriteException;
import com.lyncode.xoai.xml.XmlWriter;

import java.util.ArrayList;
import java.util.List;

public class ListMetadataFormats implements Verb {
    protected List<MetadataFormat> metadataFormats = new ArrayList<MetadataFormat>();

    public List<MetadataFormat> getMetadataFormats() {
        return this.metadataFormats;
    }

    public ListMetadataFormats withMetadataFormat (MetadataFormat mdf) {
        metadataFormats.add(mdf);
        return this;
    }

    @Override
    public void write(XmlWriter writer) throws XmlWriteException {
        if (!this.metadataFormats.isEmpty())
            for (MetadataFormat format : this.metadataFormats)
                writer.writeElement("metadataFormat", format);
    }

    @Override
    public Type getType() {
        return Type.ListMetadataFormats;
    }
}
