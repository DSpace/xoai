/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.model.oaipmh;

import com.lyncode.xml.exceptions.XmlWriteException;
import org.dspace.xoai.model.xoai.XOAIMetadata;
import org.dspace.xoai.xml.EchoElement;
import org.dspace.xoai.xml.XmlWritable;
import org.dspace.xoai.xml.XmlWriter;

public class Description implements XmlWritable {
    public static Description description (XOAIMetadata metadata) {
        return new Description(metadata);
    }

    protected String value;
    private XOAIMetadata XOAIMetadata;

    public Description() {}

    public Description(XOAIMetadata XOAIMetadata) {
        this.XOAIMetadata = XOAIMetadata;
    }

    public Description(String compiledMetadata) {
        value = compiledMetadata;
    }

    public Description withMetadata(XOAIMetadata XOAIMetadata) {
        this.XOAIMetadata = XOAIMetadata;
        return this;
    }
    public Description withMetadata(String metadata) {
        this.value = metadata;
        return this;
    }

    @Override
    public void write(XmlWriter writer) throws XmlWriteException {
        if (XOAIMetadata != null) {
            this.XOAIMetadata.write(writer);
        } else if (this.value != null) {
            EchoElement echo = new EchoElement(value);
            echo.write(writer);
        }
    }

}
