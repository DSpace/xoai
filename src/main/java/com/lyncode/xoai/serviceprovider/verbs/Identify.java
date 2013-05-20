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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.HarvesterManager;
import com.lyncode.xoai.serviceprovider.core.HarvestURL;
import com.lyncode.xoai.serviceprovider.exceptions.BadArgumentException;
import com.lyncode.xoai.serviceprovider.exceptions.InternalHarvestException;
import com.lyncode.xoai.serviceprovider.oaipmh.GenericParser;
import com.lyncode.xoai.serviceprovider.oaipmh.OAIPMHParser;
import com.lyncode.xoai.serviceprovider.oaipmh.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.IdentifyType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.OAIPMHtype;


/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */
public class Identify extends AbstractVerb
{
	public Identify(String baseUrl, Logger log) throws InternalHarvestException, BadArgumentException
    {
        super(baseUrl, log);
    }
    

    private String makeUrl () {
        return HarvestURL.identify().toURL(getBaseUrl());
    }
    
    public IdentifyType harvest (GenericParser description) throws InternalHarvestException, BadArgumentException {
        HttpClient httpclient = new DefaultHttpClient();
        String url = makeUrl();
        getLogger().info("Harvesting: "+url);
        HttpGet httpget = new HttpGet(url);
        httpget.addHeader("User-Agent", HarvesterManager.USERAGENT);
        httpget.addHeader("From", HarvesterManager.FROM);
        
        HttpResponse response = null;
        
        try
        {
            response = httpclient.execute(httpget);
            StatusLine status = response.getStatusLine();
            
            getLogger().debug(response.getStatusLine());
            
            if(status.getStatusCode() == 503) // 503 Status (must wait)
            {
                org.apache.http.Header[] headers = response.getAllHeaders();
                for (org.apache.http.Header h : headers) {
                    if (h.getName().equals("Retry-After")) {
                        String retry_time = h.getValue();
                        try {
							Thread.sleep(Integer.parseInt(retry_time)*1000);
						} catch (NumberFormatException e) {
							getLogger().warn("Cannot parse "+retry_time+" to Integer", e);
						} catch (InterruptedException e) {
							getLogger().debug(e.getMessage(), e);
						}
                        httpclient.getConnectionManager().shutdown();
                        httpclient = new DefaultHttpClient();
                        response = httpclient.execute(httpget);
                    }
                }
            }
            
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            
            OAIPMHParser parser = OAIPMHParser.newInstance(instream, getLogger(), null, description, null);
            OAIPMHtype pmh = parser.parse();
            
            return pmh.getIdentify();
        }
        catch (IOException e)
        {
            throw new InternalHarvestException(e);
        } catch (XMLStreamException e) {
            throw new InternalHarvestException(e);
		} catch (ParseException e) {
            throw new InternalHarvestException(e);
		}
        
    }   
}
