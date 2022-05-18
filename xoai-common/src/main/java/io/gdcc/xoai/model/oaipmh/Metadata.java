/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.model.oaipmh;

import io.gdcc.xoai.xml.CopyElement;
import io.gdcc.xoai.xmlio.exceptions.XmlWriteException;
import io.gdcc.xoai.model.xoai.XOAIMetadata;
import io.gdcc.xoai.xml.EchoElement;
import io.gdcc.xoai.xml.XmlWritable;
import io.gdcc.xoai.xml.XmlWriter;

import java.io.InputStream;

/**
 * Model class representing <OAI-PMH><ListRecords|GetRecord><record><metadata> elements.
 * The element holds the actual metadata as XML structure serialization, e.g. using OAI Dublin Core
 */
public class Metadata implements XmlWritable {
    
    private final XmlWritable element;
    
    public Metadata(final XOAIMetadata value) {
        this.element = value;
    }

    public Metadata(final String value) {
        this.element = new EchoElement(value);
    }

    public Metadata(final EchoElement value) {
        this.element = value;
    }
    
    public Metadata(final CopyElement value) {
        this.element = value;
    }
    
    /**
     * If this metadata element needs to be passed through an {@link io.gdcc.xoai.xml.XSLPipeline},
     * this is indicated by "true". When this metadata element consists of pregenerated data, which
     * shall not be processed again, this is indicated by "false".
     *
     * @return true - needs a processing pipeline, false - use as is
     */
    public boolean needsProcessing() {
        return !(this.element instanceof CopyElement);
    }
    
    /**
     * Create a metadata element from an associated {@link InputStream} which will
     * copy the streams content to the OAI-PMH response when the metadata gets written.
     * @param stream The input stream containing the metadata
     * @return A Metadata item to store away, e. g. in an {@link io.gdcc.xoai.dataprovider.model.Item}
     */
    public static Metadata copyFromStream(InputStream stream) {
        return new Metadata(new CopyElement(stream));
    }
    
    @Override
    public void write(final XmlWriter writer) throws XmlWriteException {
        writer.write(element);
    }

    public XOAIMetadata getXoaiMetadata() {
        if (element instanceof XOAIMetadata)
            return (XOAIMetadata) element;
        else
            return null;
    }
}
