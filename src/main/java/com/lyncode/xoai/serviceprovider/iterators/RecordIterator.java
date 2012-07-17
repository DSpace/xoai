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
 * @author DSpace @ Lyncode
 * @version 2.2.0
 */

package com.lyncode.xoai.serviceprovider.iterators;

import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.lyncode.xoai.serviceprovider.HarvesterManager;
import com.lyncode.xoai.serviceprovider.configuration.Configuration;
import com.lyncode.xoai.serviceprovider.data.Record;
import com.lyncode.xoai.serviceprovider.exceptions.HarvestException;
import com.lyncode.xoai.serviceprovider.util.URLEncoder;
import com.lyncode.xoai.serviceprovider.util.XMLUtils;
import com.lyncode.xoai.serviceprovider.verbs.ListRecords.ExtraParameters;


/**
 * @author DSpace @ Lyncode
 * @version 2.2.0
 */
public class RecordIterator implements Iterator<Record>
{
    private static Logger log = LogManager.getLogger(RecordIterator.class);
    
    private Configuration config;
    private String baseUrl;
    private String metadataPrefix;
    private ExtraParameters extra;

    public RecordIterator(Configuration configuration, String baseUrl, String metadataPrefix,
            ExtraParameters extra)
    {
        super();
        this.config = configuration;
        this.baseUrl = baseUrl;
        this.metadataPrefix = metadataPrefix;
        this.extra = extra;
    }

    
    private String resumption = null;
    private Queue<Record> _queue = null;
    
    private String makeUrl () {
        if (resumption != null && !resumption.trim().equals("")) {
            try
            {
                int wait = this.config.getResumptionInterval();
                log.debug("Waiting "+wait+" miliseconds");
                Thread.sleep(wait);
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
            }
            return (baseUrl + "?verb=ListRecords"+ URLEncoder.SEPARATOR +"resumptionToken="+URLEncoder.encode(resumption));
        }
        else {
            if (extra == null || extra.equals(""))
                return (baseUrl + "?verb=ListRecords" + URLEncoder.SEPARATOR + "metadataPrefix="+metadataPrefix);
            else
                return (baseUrl + "?verb=ListRecords" + URLEncoder.SEPARATOR + "metadataPrefix="+metadataPrefix + URLEncoder.SEPARATOR + extra.toUrl());
        }
    }
    
    private void harvest () throws HarvestException {
        HttpClient httpclient = new DefaultHttpClient();
        String url = makeUrl();
        log.info("Harvesting: "+url);
        HttpGet httpget = new HttpGet(url);
        httpget.addHeader("User-Agent", HarvesterManager.USERAGENT);
        httpget.addHeader("From", HarvesterManager.FROM);
        
        HttpResponse response = null;
        
        try
        {
            response = httpclient.execute(httpget);
            StatusLine status = response.getStatusLine();
            
            log.debug(response.getStatusLine());
            
            if(status.getStatusCode() == 503) // 503 Status (must wait)
            {
                org.apache.http.Header[] headers = response.getAllHeaders();
                for (org.apache.http.Header h : headers) {
                    if (h.getName().equals("Retry-After")) {
                        String retry_time = h.getValue();
                        Thread.sleep(Integer.parseInt(retry_time)*1000);
                        httpclient.getConnectionManager().shutdown();
                        httpclient = new DefaultHttpClient();
                        response = httpclient.execute(httpget);
                    }
                }
            }
            
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            
            Document doc = XMLUtils.parseRecords(instream);
            NodeList listRecords = doc.getElementsByTagName("record");
            for (int i = 0;i<listRecords.getLength();i++)
                _queue.add(XMLUtils.getRecord(listRecords.item(i)));
            
            resumption = XMLUtils.getText(doc.getElementsByTagName("resumptionToken"));
            System.out.println("RESUMPTION: "+resumption);
        }
        catch (Exception e)
        {
            throw new HarvestException(e);
        }
        
    }
    
    @Override
    public boolean hasNext()
    {
        if (_queue == null || (_queue.size() == 0 && resumption != null && !resumption.trim().equals(""))) {
            if (_queue == null) _queue = new LinkedList<Record>();
            // First Query
            try
            {
                this.harvest();
            }
            catch (HarvestException e)
            {
                log.error(e.getMessage(), e);
            }
        }
        
        return (_queue.size() > 0);
    }

    @Override
    public Record next()
    {
        return _queue.poll();
    }

    @Override
    public void remove()
    {
        // No need to implement
    }

    
}
