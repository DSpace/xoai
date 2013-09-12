package com.lyncode.xoai.serviceprovider.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.lyncode.xoai.util.DateUtils;
import com.lyncode.xoai.util.URLEncoder;

public class Parameters {
    private String baseURL;
    private OAIVerb verb;
    private String metadataPrefix;
	private String set;
    private Date from;
    private Date until;
    private String identifier;
    private String resumptionToken;

    public Parameters()
    {
        super();
        this.verb = OAIVerb.Identify;
    }
    
    public Parameters(String baseURL)
    {
        super();
        this.verb = OAIVerb.Identify;
        this.baseURL = baseURL;
    }
    
    public boolean isExtra () {
        return this.baseURL == null;
    }
    
    private Parameters deepClone () {
        Parameters params = new Parameters(this.baseURL);
        params.from = this.from;
        params.identifier = this.identifier;
        params.until = this.until;
        params.metadataPrefix = this.metadataPrefix;
        params.verb = this.verb;
        params.resumptionToken = this.resumptionToken;
        params.set = this.set;
        
        return params;
    }
    
    public Parameters verb (OAIVerb verb) {
        Parameters p = this.deepClone();
        p.verb = verb;
        return p;
    }
    
    public Parameters until (Date until) {
        Parameters p = this.deepClone();
        p.until = until;
        return p;
    }

    
    public Parameters from (Date from) {
        Parameters p = this.deepClone();
        p.from = from;
        return p;
    }
    
    public Parameters set (String value) {
        Parameters p = this.deepClone();
        p.set = value;
        return p;
    }

    
    public Parameters identifier (String value) {
        Parameters p = this.deepClone();
        p.identifier = value;
        return p;
    }
    
    public Parameters resumptionToken (String value) {
        Parameters p = this.deepClone();
        p.resumptionToken = value;
        return p;
    }
    
    public Parameters metadataPrefix (String value) {
        Parameters p = this.deepClone();
        p.metadataPrefix = value;
        return p;
    }
    
    public String toUrl () {
        List<String> string = new ArrayList<String>();
        string.add("verb="+this.verb.name());
        if (set != null) string.add("set="+URLEncoder.encode(set));
        if (from != null) string.add("from="+URLEncoder.encode(DateUtils.fromDate(from)));
        if (until != null) string.add("until="+URLEncoder.encode(DateUtils.fromDate(until)));
        if (identifier != null) string.add("identifier="+URLEncoder.encode(identifier));
        if (metadataPrefix != null) string.add("metadataPrefix="+URLEncoder.encode(metadataPrefix));
        if (resumptionToken != null) string.add("resumptionToken="+URLEncoder.encode(resumptionToken));
        return this.baseURL + "?" + StringUtils.join(string, URLEncoder.SEPARATOR);
    }

    public String toUrl(String resumption) {
        List<String> string = new ArrayList<String>();
        string.add("verb="+this.verb.name());
        string.add("resumptionToken="+URLEncoder.encode(resumption));
        return this.baseURL + "?" + StringUtils.join(string, URLEncoder.SEPARATOR);
    }
    
    public Parameters merge (Parameters p) {
        this.set = p.set;
        this.metadataPrefix = p.metadataPrefix;
        this.identifier = p.identifier;
        this.from = p.from;
        this.until = p.until;
        this.resumptionToken = p.resumptionToken;
        
        return this;
    }
}