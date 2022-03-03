/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.model.oaipmh;

import io.gdcc.xoai.xmlio.exceptions.XmlWriteException;
import io.gdcc.xoai.model.xoai.XOAIMetadata;
import io.gdcc.xoai.xml.EchoElement;
import io.gdcc.xoai.xml.XmlWritable;
import io.gdcc.xoai.xml.XmlWriter;

import javax.xml.bind.annotation.XmlValue;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Metadata implements XmlWritable {
    private String string;

    @XmlValue
    protected XOAIMetadata value;

    public Metadata(XOAIMetadata value) {
        this.value = value;
    }

    public Metadata(String value) {
        this.string = value;
    }

    public Metadata(InputStream value) throws IOException {
        this.string = new String(value.readAllBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public void write(XmlWriter writer) throws XmlWriteException {
        if (this.value != null)
            this.value.write(writer);
        else {
            EchoElement elem = new EchoElement(string);
            elem.write(writer);
        }
    }

    public XOAIMetadata getValue () {
        return value;
    }

}
