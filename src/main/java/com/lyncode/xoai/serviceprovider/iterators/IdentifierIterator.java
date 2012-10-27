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

package com.lyncode.xoai.serviceprovider.iterators;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.parsers.ParserConfigurationException;

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
import org.xml.sax.SAXException;

import com.lyncode.xoai.serviceprovider.HarvesterManager;
import com.lyncode.xoai.serviceprovider.configuration.Configuration;
import com.lyncode.xoai.serviceprovider.data.Identifier;
import com.lyncode.xoai.serviceprovider.exceptions.BadResumptionTokenException;
import com.lyncode.xoai.serviceprovider.exceptions.CannotDisseminateFormatException;
import com.lyncode.xoai.serviceprovider.exceptions.InternalHarvestException;
import com.lyncode.xoai.serviceprovider.exceptions.NoRecordsMatchException;
import com.lyncode.xoai.serviceprovider.exceptions.NoSetHierarchyException;
import com.lyncode.xoai.serviceprovider.util.URLEncoder;
import com.lyncode.xoai.serviceprovider.util.XMLUtils;
import com.lyncode.xoai.serviceprovider.verbs.ListIdentifiers.ExtraParameters;


/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */
public class IdentifierIterator
{
    private static Logger log = LogManager.getLogger(IdentifierIterator.class);
    
    private Configuration config;
    private String baseUrl;
    private String metadataPrefix;
    private ExtraParameters extra;

    public IdentifierIterator(Configuration configuration, String baseUrl, String metadataPrefix,
            ExtraParameters extra)
    {
        super();
        this.config = configuration;
        this.baseUrl = baseUrl;
        this.metadataPrefix = metadataPrefix;
        this.extra = extra;
    }


    private String resumption = null;
    private Queue<Identifier> _queue = null;
    
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
            return (baseUrl + "?verb=ListIdentifiers"+ URLEncoder.SEPARATOR +"resumptionToken="+URLEncoder.encode(resumption));
        }
        else {
            if (extra == null || extra.equals(""))
                return (baseUrl + "?verb=ListIdentifiers" + URLEncoder.SEPARATOR + "metadataPrefix="+metadataPrefix);
            else
                return (baseUrl + "?verb=ListIdentifiers" + URLEncoder.SEPARATOR + "metadataPrefix="+metadataPrefix + URLEncoder.SEPARATOR + extra.toUrl());
        }
    }
    
    private void harvest () throws InternalHarvestException, NoRecordsMatchException, BadResumptionTokenException, CannotDisseminateFormatException, NoSetHierarchyException {
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
            
            Document doc = XMLUtils.parseDocument(instream);
            
            XMLUtils.checkListIdentifiers(doc);
            
            NodeList listRecords = doc.getElementsByTagName("header");
            for (int i = 0;i<listRecords.getLength();i++)
                _queue.add(XMLUtils.getIdentifier(listRecords.item(i)));
            
            resumption = XMLUtils.getText(doc.getElementsByTagName("resumptionToken"));
            log.debug("RESUMPTION: "+resumption);
        }
        catch (IOException e)
        {
            throw new InternalHarvestException(e);
        } catch (ParserConfigurationException e) {
            throw new InternalHarvestException(e);
		} catch (SAXException e) {
            throw new InternalHarvestException(e);
		}
        
    }
    
    public boolean hasNext() throws NoRecordsMatchException, BadResumptionTokenException, CannotDisseminateFormatException, NoSetHierarchyException, InternalHarvestException
    {
        if (_queue == null || (_queue.size() == 0 && resumption != null && !resumption.trim().equals(""))) {
            if (_queue == null) _queue = new LinkedList<Identifier>();
            this.harvest();
        }
        
        return (_queue.size() > 0);
    }

    public Identifier next()
    {
        return _queue.poll();
    }

}
