/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.parameters;

import java.util.Date;

public class ListIdentifiersParameters {
    public static ListIdentifiersParameters request() {
        return new ListIdentifiersParameters();
    }



    private String metadataPrefix;
    private String setSpec;
    private Date from;
    private Date until;
	private String granularity;

    public String getMetadataPrefix() {
        return metadataPrefix;
    }

    public ListIdentifiersParameters withMetadataPrefix(String metadataPrefix) {
        this.metadataPrefix = metadataPrefix;
        return this;
    }

    public String getSetSpec() {
        return setSpec;
    }

    public ListIdentifiersParameters withSetSpec(String setSpec) {
        this.setSpec = setSpec;
        return this;
    }

    public Date getFrom() {
        return from;
    }

    public ListIdentifiersParameters withFrom(Date from) {
        this.from = from;
        return this;
    }

    public Date getUntil() {
        return until;
    }

    public ListIdentifiersParameters withUntil(Date until) {
        this.until = until;
        return this;
    }

    public boolean areValid() {
        return metadataPrefix != null;
    }

	public void withGranularity(String granularity) {
		this.granularity = granularity;
		
	}

	public String getGranularity() {
		return granularity;
	}
}
