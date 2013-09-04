package com.lyncode.xoai.serviceprovider.verbs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.lyncode.xoai.util.DateUtils;
import com.lyncode.xoai.util.URLEncoder;

public class Parameters {
	private String set;
    private Date from;
    private Date until;
    private String identifier;
    
    public Parameters()
    {
        super();
    }

    public String getSet()
    {
        return set;
    }

    public void setSet(String set)
    {
        this.set = set;
    }

    public Date getFrom()
    {
        return from;
    }

    public void setFrom(Date from)
    {
        this.from = from;
    }

    public Date getUntil()
    {
        return until;
    }

    public void setUntil(Date until)
    {
        this.until = until;
    }
    
    public String toUrl () {
        List<String> string = new ArrayList<String>();
        if (set != null) string.add("set="+URLEncoder.encode(set));
        if (from != null) string.add("from="+URLEncoder.encode(DateUtils.fromDate(from)));
        if (until != null) string.add("until="+URLEncoder.encode(DateUtils.fromDate(until)));
        if (identifier != null) string.add("identifier="+URLEncoder.encode(identifier));
        return StringUtils.join(string, URLEncoder.SEPARATOR);
    }

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}