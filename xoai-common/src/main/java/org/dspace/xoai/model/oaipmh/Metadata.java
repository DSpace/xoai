/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.model.oaipmh;

import com.lyncode.xml.exceptions.XmlWriteException;
import org.apache.commons.io.IOUtils;
import org.dspace.xoai.model.xoai.XOAIMetadata;
import org.dspace.xoai.xml.EchoElement;
import org.dspace.xoai.xml.XmlWritable;
import org.dspace.xoai.xml.XmlWriter;

import javax.xml.bind.annotation.XmlValue;
import java.io.IOException;
import java.io.InputStream;

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
        this.string = IOUtils.toString(value);
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
