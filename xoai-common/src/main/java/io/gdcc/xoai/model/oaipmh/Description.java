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
