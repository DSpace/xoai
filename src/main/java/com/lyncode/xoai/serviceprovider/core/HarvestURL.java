package com.lyncode.xoai.serviceprovider.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.lyncode.xoai.serviceprovider.util.DateUtils;
import com.lyncode.xoai.serviceprovider.util.URLEncoder;

public class HarvestURL {
	//private static Logger log = LogManager.getLogger(HarvestURL.class);
	
	public static HarvestURL identify () {
		return new HarvestURL(OAIVerb.Identify, null, null, null, null, null, null);
	}
	
	public static HarvestURL getRecord (String metadataPrefix, String identifier) {
		return new HarvestURL(OAIVerb.GetRecord, null, null, null, identifier, metadataPrefix, null);
	}

	public static HarvestURL listRecords (String metadataPrefix) {
		return new HarvestURL(OAIVerb.ListRecords, null, null, null, null, metadataPrefix, null);
	}
	
	public static HarvestURL listRecordsResumption (String resumption) {
		return new HarvestURL(OAIVerb.ListRecords, null, null, null, null, null, resumption);
	}
	public static HarvestURL listIdentifiers (String metadataPrefix) {
		return new HarvestURL(OAIVerb.ListIdentifiers, null, null, null, null, metadataPrefix, null);
	}
	
	public static HarvestURL listIdentifiersResumption (String resumption) {
		return new HarvestURL(OAIVerb.ListIdentifiers, null, null, null, null, null, resumption);
	}
	
	public static HarvestURL listSets () {
		return new HarvestURL(OAIVerb.ListSets, null, null, null, null, null, null);
	}
	
	public static HarvestURL listSetsResumption (String resumption) {
		return new HarvestURL(OAIVerb.ListSets, null, null, null, null, null, resumption);
	}
	
	public static HarvestURL listMetadataFormats () {
		return new HarvestURL(OAIVerb.ListMetadataFormats, null, null, null, null, null, null);
	}
	
	private OAIVerb verb;
	private String set;
	private Date from;
	private Date until;
	private String identifier;
	private String metadataPrefix;
	private String resumptionToken;
	
	private HarvestURL(OAIVerb verb, String set, Date from,
			Date until, String identifier, String metadataPrefix,
			String resumptionToken) {
		super();
		this.verb = verb;
		this.set = set;
		this.from = from;
		this.until = until;
		this.identifier = identifier;
		this.metadataPrefix = metadataPrefix;
		this.resumptionToken = resumptionToken;
	}

	
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void setSet(String set) {
		this.set = set;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public void setUntil(Date until) {
		this.until = until;
	}

	public OAIVerb getVerb() {
		return verb;
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

	public String getMetadataPrefix() {
		return metadataPrefix;
	}

	public String getResumptionToken() {
		return resumptionToken;
	}
	
	public boolean hasResumptionToken () {
		return (this.resumptionToken != null);
	}
	
	public boolean hasSet () {
		return (this.set != null);
	}
	
	public boolean hasFrom () {
		return (this.from != null);
	}
	
	public boolean hasUntil () {
		return (this.until != null);
	}
	
	public boolean hasIdentifier () {
		return (this.identifier != null);
	}
	
	public boolean hasMetadataPrefix () {
		return (this.metadataPrefix != null);
	}
	
	public String toURL (String baseURL) {
		List<String> params = new ArrayList<String>();
		switch (this.getVerb()) {
			case GetRecord:
				params.add("verb=GetRecord");
				params.add("metadataPrefix="+URLEncoder.encode(this.getMetadataPrefix()));
				params.add("identifier="+URLEncoder.encode(this.getIdentifier()));
				break;
			case Identify:
				params.add("verb=Identify");
				break;
			case ListSets:
				params.add("verb=ListSets");
				if (this.hasResumptionToken())
					params.add("resumptionToken="+URLEncoder.encode(this.getResumptionToken()));
				break;
			case ListMetadataFormats:
				params.add("verb=ListMetadataFormats");
				if (this.hasIdentifier())
					params.add("identifier="+URLEncoder.encode(this.getIdentifier()));
				break;
			case ListIdentifiers:
				params.add("verb=ListIdentifiers");
				if (this.hasResumptionToken())
					params.add("resumptionToken="+URLEncoder.encode(this.getResumptionToken()));
				else {
					params.add("metadataPrefix="+URLEncoder.encode(this.getMetadataPrefix()));
					if (this.hasSet())
						params.add("set="+URLEncoder.encode(this.getSet()));
					if (this.hasFrom())
						params.add("from="+URLEncoder.encode(DateUtils.fromDate(this.getFrom())));
					if (this.hasUntil())
						params.add("until="+URLEncoder.encode(DateUtils.fromDate(this.getUntil())));
				}
				break;
			case ListRecords:
				params.add("verb=ListRecords");
				if (this.hasResumptionToken())
					params.add("resumptionToken="+URLEncoder.encode(this.getResumptionToken()));
				else {
					params.add("metadataPrefix="+URLEncoder.encode(this.getMetadataPrefix()));
					if (this.hasSet())
						params.add("set="+URLEncoder.encode(this.getSet()));
					if (this.hasFrom())
						params.add("from="+URLEncoder.encode(DateUtils.fromDate(this.getFrom())));
					if (this.hasUntil())
						params.add("until="+URLEncoder.encode(DateUtils.fromDate(this.getUntil())));
				}
				break;
		}
		
		return baseURL + "?" + StringUtils.join(params, URLEncoder.SEPARATOR);
	}
}
