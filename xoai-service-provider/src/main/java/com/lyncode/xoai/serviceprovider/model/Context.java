/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     client://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.xoai.serviceprovider.model;

import com.lyncode.xoai.model.oaipmh.Granularity;
import com.lyncode.xoai.serviceprovider.client.OAIClient;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.util.HashMap;
import java.util.Map;

public class Context {
    private static final TransformerFactory factory = TransformerFactory.newInstance();

    private Transformer transformer;
    private Map<String, Transformer> metadataTransformers = new HashMap<String, Transformer>();
    private String baseUrl;
    private Granularity granularity;
    private OAIClient client;

    public Context() {
        try {
            this.withMetadataTransformer("xoai", factory.newTransformer());
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException("Unable to initialize identity transformer");
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

        private String location;

        KnownTransformer(String location) {
            this.location = location;
        }

        public Transformer transformer () {
            try {
                return factory.newTransformer(new StreamSource(this.getClass().getClassLoader().getResourceAsStream(location)));
            } catch (TransformerConfigurationException e) {
                throw new RuntimeException("Unable to load resource file '"+location+"'", e);
            }
        }
    }
}
