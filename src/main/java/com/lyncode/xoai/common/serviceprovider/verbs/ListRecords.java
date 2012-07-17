package com.lyncode.xoai.common.serviceprovider.verbs;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.lyncode.xoai.common.serviceprovider.data.Record;
import com.lyncode.xoai.common.serviceprovider.iterators.RecordIterator;
import com.lyncode.xoai.common.serviceprovider.util.DateUtils;
import com.lyncode.xoai.common.serviceprovider.util.URLEncoder;
import com.lyncode.xoai.common.serviceprovider.xml.configuration.Configuration;

public class ListRecords extends AbstractVerb implements Iterable<Record>
{
    private String metadataPrefix;
    private ExtraParameters extra;
    
    public ListRecords(Configuration config, String baseUrl, String metadataPrefix)
    {
        super(config, baseUrl);
        this.metadataPrefix = metadataPrefix;
        this.extra = null;
    }
    

    public ListRecords(Configuration config, String baseUrl, String metadataPrefix, ExtraParameters extra)
    {
        super(config, baseUrl);
        this.metadataPrefix = metadataPrefix;
        this.extra = extra;
    }

    @Override
    public Iterator<Record> iterator()
    {
        return new RecordIterator(super.getConfiguration(), super.getBaseUrl(), metadataPrefix, extra);
    }
    
    public class ExtraParameters {
        private String set;
        private Date from;
        private Date until;
        
        public ExtraParameters()
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
            return StringUtils.join(string, URLEncoder.SEPARATOR);
        }
    }

}
