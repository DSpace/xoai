/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.parameters;

public class ListMetadataParameters {
    public static ListMetadataParameters request() {
        return new ListMetadataParameters();
    }

    private String identifier;

    public String getIdentifier() {
        return identifier;
    }

    public ListMetadataParameters withIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }
}
