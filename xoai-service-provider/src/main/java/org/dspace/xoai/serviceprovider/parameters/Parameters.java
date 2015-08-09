/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.parameters;

import org.apache.commons.lang3.StringUtils;
import org.dspace.xoai.model.oaipmh.Granularity;
import org.dspace.xoai.model.oaipmh.Verb;
import org.dspace.xoai.services.api.DateProvider;
import org.dspace.xoai.services.impl.UTCDateProvider;
import org.dspace.xoai.util.URLEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.dspace.xoai.util.URLEncoder.encode;

public class Parameters {
    private static DateProvider formatter = new UTCDateProvider();

    public static Parameters parameters () {
        return new Parameters();
    }

    private Verb.Type verb;
    private String metadataPrefix;
    private String set;
    private Date from;
    private Date until;
    private String identifier;
    private String resumptionToken;
	private String granularity;

    public Parameters withVerb(Verb.Type verb) {
        this.verb = verb;
        return this;
    }

    public Parameters withUntil(Date until) {
        this.until = until;
        return this;
    }


    public Parameters withFrom(Date from) {
        this.from = from;
        return this;
    }

    public Parameters withSet(String value) {
        this.set = value;
        return this;
    }


    public Parameters identifier(String value) {
        this.identifier = value;
        return this;
    }

    public Parameters withResumptionToken(String value) {
        this.resumptionToken = value;
        this.metadataPrefix = null;
        this.until = null;
        this.set = null;
        this.from = null;
        return this;
    }

    public Parameters withoutResumptionToken () {
        this.resumptionToken = null;
        return this;
    }

    public Parameters withMetadataPrefix(String value) {
        this.metadataPrefix = value;
        return this;
    }

    public String toUrl(String baseUrl) {
        List<String> string = new ArrayList<String>();
        string.add("verb=" + this.verb.name());
        Granularity granularity = granularity();
        if (set != null) string.add("set=" + encode(set));
        if (from != null) string.add("from=" + encode(formatter.format(from,granularity)));
        if (until != null) string.add("until=" + encode(formatter.format(until,granularity)));
        if (identifier != null) string.add("identifier=" + encode(identifier));
        if (metadataPrefix != null) string.add("metadataPrefix=" + encode(metadataPrefix));
        if (resumptionToken != null) string.add("resumptionToken=" + encode(resumptionToken));
        return baseUrl + "?" + StringUtils.join(string, URLEncoder.SEPARATOR);
    }

    /**
     * If a valid granularity field exists, return corresponding granularity.
     * Defaults to: Second
     * @return
     */
    private Granularity granularity() {
		if(granularity != null){
			for (int i = 0; i < Granularity.values().length; i++) {
				Granularity possibleGranularity = Granularity.values()[i];
				if(granularity.equals(possibleGranularity.toString())){
					return possibleGranularity;
				}
				
			}
			
		}
		return Granularity.Second;
	}

	public Parameters include(ListMetadataParameters parameters) {
        this.identifier = parameters.getIdentifier();
        return this;
    }

    public Parameters include(GetRecordParameters parameters) {
        this.identifier = parameters.getIdentifier();
        this.metadataPrefix = parameters.getMetadataPrefix();
        return this;
    }

    public Parameters include(ListRecordsParameters parameters) {
        this.metadataPrefix = parameters.getMetadataPrefix();
        this.set = parameters.getSetSpec();
        this.until = parameters.getUntil();
        this.from = parameters.getFrom();
        this.granularity = parameters.getGranularity();

        return this;
    }

    public Parameters include(ListIdentifiersParameters parameters) {
        this.metadataPrefix = parameters.getMetadataPrefix();
        this.set = parameters.getSetSpec();
        this.until = parameters.getUntil();
        this.from = parameters.getFrom();
        this.granularity = parameters.getGranularity();

        return this;
    }

    public Verb.Type getVerb() {
        return verb;
    }

    public String getMetadataPrefix() {
        return metadataPrefix;
    }

    public String getSet() {
        return set;
    }

    public Date getFrom() {
        return from;
    }

    public Date getUntil() {
        return until;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getResumptionToken() {
        return resumptionToken;
    }

	public void withGranularity(String granularity) {
		this.granularity = granularity;
		
	}

	public Object getGranularity() {
		return granularity;
	}
}