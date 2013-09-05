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
 * @version 3.1.0
 */

package com.lyncode.xoai.serviceprovider.iterators;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.stream.XMLStreamException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.HarvesterManager;
import com.lyncode.xoai.serviceprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.serviceprovider.exceptions.InternalHarvestException;
import com.lyncode.xoai.serviceprovider.exceptions.NoMetadataFormatsException;
import com.lyncode.xoai.serviceprovider.oaipmh.OAIPMHParser;
import com.lyncode.xoai.serviceprovider.oaipmh.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.ListMetadataFormatsType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.MetadataFormatType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.OAIPMHtype;
import com.lyncode.xoai.serviceprovider.verbs.Parameters;
import com.lyncode.xoai.util.URLEncoder;


/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 3.1.0
 */
public class MetadataFormatIterator
{
    private Logger log;
    private String baseUrl;
    private Parameters extra;

    public MetadataFormatIterator(String baseUrl, Parameters extra, Logger logger)
    {
        super();
        this.baseUrl = baseUrl;
        this.extra = extra;
        this.log = logger;
    }


    private Queue<MetadataFormatType> _queue = null;
    
    private String makeUrl () {
        if (extra == null || extra.equals(""))
                return (baseUrl + "?verb=ListMetadataFormats");
        else
                return (baseUrl + "?verb=ListMetadataFormats" + URLEncoder.SEPARATOR + extra.toUrl());
        
    }
    
    public ListMetadataFormatsType harvest () throws InternalHarvestException {
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
                        try {
							Thread.sleep(Integer.parseInt(retry_time)*1000);
						} catch (NumberFormatException e) {
							log.warn("Cannot parse "+retry_time+" to Integer", e);
						} catch (InterruptedException e) {
							log.debug(e.getMessage(), e);
						}
                        httpclient.getConnectionManager().shutdown();
                        httpclient = new DefaultHttpClient();
                        response = httpclient.execute(httpget);
                    }
                }
            }
            
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            
            OAIPMHParser parser = OAIPMHParser.newInstance(instream, log);
            OAIPMHtype res = parser.parse();
            
            return res.getListMetadataFormats();
        }
        catch (IOException e)
        {
            throw new InternalHarvestException(e);
        } catch (XMLStreamException e) 
        {
            throw new InternalHarvestException(e);
        } 
        catch (ParseException e)
        {
            throw new InternalHarvestException(e);
        } 
        
    }
    
    public boolean hasNext() throws NoMetadataFormatsException, IdDoesNotExistException, InternalHarvestException
    {
        if (_queue == null) {
            if (_queue == null) _queue = new LinkedList<MetadataFormatType>();
            this.harvest();
        }
        
        return (_queue.size() > 0);
    }

    public MetadataFormatType next()
    {
        return _queue.poll();
    }


}
