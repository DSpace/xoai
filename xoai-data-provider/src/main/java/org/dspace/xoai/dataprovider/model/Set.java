/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider.model;

import org.dspace.xoai.dataprovider.model.conditions.Condition;
import org.dspace.xoai.model.xoai.XOAIMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Development @ Lyncode
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

    public org.dspace.xoai.model.oaipmh.Set toOAIPMH () {
        org.dspace.xoai.model.oaipmh.Set set = new org.dspace.xoai.model.oaipmh.Set();
        set.withName(getName());
        set.withSpec(getSpec());
        for (XOAIMetadata description : descriptions)
            set.withDescription(description);
        return set;
    }
}
