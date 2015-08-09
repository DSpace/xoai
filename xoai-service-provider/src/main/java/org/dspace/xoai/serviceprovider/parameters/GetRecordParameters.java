/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.parameters;

public class GetRecordParameters {
    public static GetRecordParameters request () {
        return new GetRecordParameters();
    }

    private String identifier;
    private String metadataPrefix;

    public String getIdentifier() {
        return identifier;
    }

    public GetRecordParameters withIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public String getMetadataPrefix() {
        return metadataPrefix;
    }

    public GetRecordParameters withMetadataFormatPrefix(String metadataFormatPrefix) {
        this.metadataPrefix = metadataFormatPrefix;
        return this;
    }

    public boolean areValid() {
        return identifier != null && metadataPrefix != null;
    }
}
