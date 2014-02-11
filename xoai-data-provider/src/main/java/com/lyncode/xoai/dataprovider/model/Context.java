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

import com.lyncode.xoai.dataprovider.exceptions.InternalOAIException;
import com.lyncode.xoai.dataprovider.filter.FilterResolver;
import com.lyncode.xoai.dataprovider.model.conditions.Condition;

import javax.xml.transform.Transformer;
import java.util.ArrayList;
import java.util.List;

public class Context {
    public static Context context () {
        return new Context();
    }

    private Transformer metadataTransformer;
    private List<MetadataFormat> metadataFormats = new ArrayList<MetadataFormat>();
    private List<Set> sets = new ArrayList<Set>();
    private Condition condition;

    public List<Set> getSets() {
        return sets;
    }

    public Context withSet(Set set) {
        if (!set.hasCondition())
            throw new InternalOAIException("Context sets must have a condition");
        this.sets.add(set);
        return this;
    }

    public Transformer getTransformer() {
        return metadataTransformer;
    }

    public Context withTransformer(Transformer metadataTransformer) {
        this.metadataTransformer = metadataTransformer;
        return this;
    }

    public List<MetadataFormat> getMetadataFormats() {
        return metadataFormats;
    }

    public Context withMetadataFormat(MetadataFormat metadataFormat) {
        int remove = -1;
        for (int i = 0;i<metadataFormats.size();i++)
            if (metadataFormats.get(i).getPrefix().equals(metadataFormat.getPrefix()))
                remove = i;
        if (remove >= 0)
            this.metadataFormats.remove(remove);
        this.metadataFormats.add(metadataFormat);
        return this;
    }

    public Condition getCondition() {
        return condition;
    }

    public Context withCondition(Condition condition) {
        this.condition = condition;
        return this;
    }

    public MetadataFormat formatForPrefix(String metadataPrefix) {
        for (MetadataFormat format : this.metadataFormats)
            if (format.getPrefix().equals(metadataPrefix))
                return format;

        return null;
    }

    public boolean hasTransformer() {
        return metadataTransformer != null;
    }

    public boolean hasCondition() {
        return this.condition != null;
    }

    public boolean isStaticSet(String setSpec) {
        for (Set set : this.sets)
            if (set.getSpec().equals(setSpec))
                return true;

        return false;
    }

    public Set getSet(String setSpec) {
        for (Set set : this.sets)
            if (set.getSpec().equals(setSpec))
                return set;

        return null;
    }

    public boolean hasSet(String set) {
        return isStaticSet(set);
    }

    public Context withMetadataFormat(String prefix, Transformer transformer) {
        withMetadataFormat(new MetadataFormat().withNamespace(prefix).withPrefix(prefix).withSchemaLocation(prefix).withTransformer(transformer));
        return this;
    }

    public Context withMetadataFormat(String prefix, Transformer transformer, Condition condition) {
        withMetadataFormat(
                new MetadataFormat()
                        .withNamespace(prefix)
                        .withPrefix(prefix)
                        .withSchemaLocation(prefix)
                        .withTransformer(transformer)
                        .withCondition(condition)
        );
        return this;
    }

    public Context withoutMetadataFormats() {
        metadataFormats.clear();
        return this;
    }

    public List<MetadataFormat> formatFor(FilterResolver resolver, ItemIdentifier item) {
        List<MetadataFormat> result = new ArrayList<MetadataFormat>();
        for (MetadataFormat format : this.metadataFormats)
            if (!format.hasCondition() || format.getCondition().getFilter(resolver).isItemShown(item))
                result.add(format);
        return result;
    }
}
