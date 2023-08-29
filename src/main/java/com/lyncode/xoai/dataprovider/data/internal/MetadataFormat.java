/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.xoai.dataprovider.data.internal;

import com.lyncode.xoai.dataprovider.data.ItemIdentifier;
import com.lyncode.xoai.dataprovider.filter.conditions.Condition;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;

/**

 * @version 3.1.0
 */
public class MetadataFormat {
    private String prefix;
    private Templates xsltTemplates;
    private String namespace;
    private String schemaLocation;
    private Condition filter;

    public MetadataFormat(String prefix, Templates xsltTemplates, String namespace, String schemaLocation) {
        this.prefix = prefix;
        this.xsltTemplates = xsltTemplates;
        this.namespace = namespace;
        this.schemaLocation = schemaLocation;
    }

    public String getPrefix() {
        return prefix;
    }

    public Templates getXsltTemplates() {
        return xsltTemplates;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getSchemaLocation() {
        return schemaLocation;
    }

    public boolean isApplicable(ItemIdentifier item) {
        if (item.isDeleted()) return true;
        if (hasCondition()) return getCondition().getFilter().isItemShown(item);
        return true;
    }

    public Condition getCondition() {
        return filter;
    }

    public boolean hasCondition() {
        return filter != null;
    }

    public void setFilter(Condition filter) {
        this.filter = filter;
    }
}
