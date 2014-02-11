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

package com.lyncode.xoai.dataprovider.model;

import com.lyncode.xoai.dataprovider.model.conditions.Condition;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

public class MetadataFormat {
    public static Transformer identity () {
        try {
            return TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static MetadataFormat metadataFormat (String prefix) {
        return new MetadataFormat().withPrefix(prefix);
    }

    private Condition condition;
    private String prefix;
    private Transformer transformer;
    private String namespace;
    private String schemaLocation;

    public String getPrefix() {
        return prefix;
    }

    public MetadataFormat withPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public Transformer getTransformer() {
        return transformer;
    }

    public MetadataFormat withTransformer(Transformer transformer) {
        this.transformer = transformer;
        return this;
    }

    public String getNamespace() {
        return namespace;
    }

    public MetadataFormat withNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public String getSchemaLocation() {
        return schemaLocation;
    }

    public MetadataFormat withSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
        return this;
    }

    public MetadataFormat withCondition(Condition filter) {
        this.condition = filter;
        return this;
    }

    public Condition getCondition() {
        return condition;
    }

    public boolean hasCondition() {
        return condition != null;
    }
}
