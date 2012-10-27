/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */

package com.lyncode.xoai.serviceprovider.verbs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.lyncode.xoai.serviceprovider.configuration.Configuration;
import com.lyncode.xoai.serviceprovider.iterators.RecordIterator;
import com.lyncode.xoai.serviceprovider.util.DateUtils;
import com.lyncode.xoai.serviceprovider.util.URLEncoder;


/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */
public class ListRecords extends AbstractVerb
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

    public RecordIterator iterator()
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
