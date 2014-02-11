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
import com.lyncode.xoai.model.xoai.XOAIMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 3.1.0
 */
public class Set {
    public static Set set (String spec) {
        return new Set(spec);
    }

    private final String spec;
    private String name;
    private List<XOAIMetadata> descriptions = new ArrayList<XOAIMetadata>();
    private Condition condition;

    public Set(String spec) {
        this.spec = spec;
    }

    public String getName() {
        return name;
    }

    public Set withName(String name) {
        this.name = name;
        return this;
    }

    public List<XOAIMetadata> getDescriptions() {
        return descriptions;
    }

    public Set withDescription(XOAIMetadata description) {
        descriptions.add(description);
        return this;
    }

    public boolean hasDescription() {
        return (!this.descriptions.isEmpty());
    }

    public Condition getCondition() {
        return condition;
    }

    public boolean hasCondition() {
        return condition != null;
    }

    public Set withCondition(Condition condition) {
        this.condition = condition;
        return this;
    }

    public String getSpec() {
        return spec;
    }

    public com.lyncode.xoai.model.oaipmh.Set toOAIPMH () {
        com.lyncode.xoai.model.oaipmh.Set set = new com.lyncode.xoai.model.oaipmh.Set();
        set.withName(getName());
        set.withSpec(getSpec());
        for (XOAIMetadata description : descriptions)
            set.withDescription(description);
        return set;
    }
}
