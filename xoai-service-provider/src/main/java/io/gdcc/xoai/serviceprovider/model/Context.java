/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider.model;

import io.gdcc.xoai.model.oaipmh.Granularity;
import io.gdcc.xoai.serviceprovider.client.OAIClient;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.util.HashMap;
import java.util.Map;

public class Context {
    private static final TransformerFactory factory = TransformerFactory.newInstance();

    private Transformer transformer;
    private final Map<String, Transformer> metadataTransformers = new HashMap<>();
    private String baseUrl;
    private Granularity granularity;
    private OAIClient client;

    public Context() {
        try {
            this.withMetadataTransformer("xoai", Context.factory.newTransformer());
        } catch (TransformerConfigurationException e) {
            throw new IllegalStateException("Unable to initialize identity transformer", e);
        }
    }

    public Context withTransformer (Transformer transformer) {
        this.transformer = transformer;
        return this;
    }

    public boolean hasTransformer () {
        return transformer != null;
    }

    public Transformer getTransformer () {
        return transformer;
    }

    public boolean hasMetadataTransformerForPrefix (String prefix) {
        return metadataTransformers.containsKey(prefix);
    }

    public Context withMetadataTransformer (String prefix, Transformer transformer) {
        metadataTransformers.put(prefix, transformer);
        return this;
    }

    public Context withMetadataTransformer (String prefix, KnownTransformer knownTransformer) {
        return withMetadataTransformer(prefix, knownTransformer.transformer());
    }

    public Transformer getMetadataTransformer (String prefix) {
        return metadataTransformers.get(prefix);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Context withBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public Granularity getGranularity() {
        return granularity;
    }

    public Context withGranularity(Granularity granularity) {
        this.granularity = granularity;
        return this;
    }

    public Context withOAIClient (OAIClient client) {
        this.client = client;
        return this;
    }

    public OAIClient getClient () {
        return client;
    }

    public enum KnownTransformer {
        OAI_DC("to_xoai/oai_dc.xsl");

        private final String location;

        KnownTransformer(String location) {
            this.location = location;
        }

        public Transformer transformer () {
            try {
                return Context.factory.newTransformer(new StreamSource(this.getClass().getClassLoader().getResourceAsStream(location)));
            } catch (TransformerConfigurationException e) {
                throw new IllegalStateException("Unable to load resource file '" + location + "'", e);
            }
        }
    }
}
