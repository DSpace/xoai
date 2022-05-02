/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.model;

import io.gdcc.xoai.dataprovider.model.conditions.Condition;
import io.gdcc.xoai.model.xoai.XOAIMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Development @ Lyncode
 * @version 3.1.0
 */
public class Set {
    public static Set set(String spec) {
        return new Set(spec);
    }

    private final String spec;
    private final List<XOAIMetadata> descriptions = new ArrayList<>();
    private String name;
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

    public io.gdcc.xoai.model.oaipmh.Set toOAIPMH () {
        io.gdcc.xoai.model.oaipmh.Set set = new io.gdcc.xoai.model.oaipmh.Set();
        set.withName(getName());
        set.withSpec(getSpec());
        for (XOAIMetadata description : descriptions)
            set.withDescription(description);
        return set;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Set)) return false;
        Set set = (Set) o;
        return Objects.equals(getSpec(), set.getSpec());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
