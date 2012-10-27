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
import com.lyncode.xoai.serviceprovider.data.Header;
import com.lyncode.xoai.serviceprovider.data.Metadata;
import com.lyncode.xoai.serviceprovider.exceptions.CannotDisseminateFormatException;
import com.lyncode.xoai.serviceprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.serviceprovider.exceptions.InternalHarvestException;
import com.lyncode.xoai.serviceprovider.util.URLEncoder;
import com.lyncode.xoai.serviceprovider.util.XMLUtils;


/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */
public class GetRecord extends AbstractVerb
{
    private static Logger log = LogManager.getLogger(GetRecord.class);
    private String identifier;
    private String metadataPrefix;
    
    private Header header;
    private Metadata metadata;
    
    public GetRecord(Configuration config, String baseUrl, String identifier, String metadataPrefix) throws InternalHarvestException, CannotDisseminateFormatException, IdDoesNotExistException
    {
        super(config, baseUrl);
        this.identifier = identifier;
        this.metadataPrefix = metadataPrefix;
        harvest();
    }
    
    
    
    private String makeUrl () {
        return (super.getBaseUrl() + "?verb=GetRecord" + URLEncoder.SEPARATOR + "metadataPrefix="+metadataPrefix + URLEncoder.SEPARATOR + "identifier=" + identifier);
    }
    
    private void harvest () throws InternalHarvestException, CannotDisseminateFormatException, IdDoesNotExistException {
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
            
            XMLUtils.checkGetRecord(doc);
            
            NodeList listRecords = doc.getElementsByTagName("record");
            for (int j = 0;j<listRecords.getLength();j++) {
                NodeList list = listRecords.item(j).getChildNodes();
                for (int i=0;i<list.getLength();i++) {
                    if (list.item(i).getNodeName().toLowerCase().equals("header")) {
                        this.setHeader(XMLUtils.getHeader(list.item(i).getChildNodes()));
                    } else if (list.item(i).getNodeName().toLowerCase().equals("metadata")) {
                        this.setMetadata(XMLUtils.getMetadata(list.item(i).getChildNodes()));
                    }
                }
            }
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
    
    
    public Header getHeader() {
        return header;
    }
    private void setHeader(Header header) {
        this.header = header;
    }
    public Metadata getMetadata() {
        return metadata;
    }
    private void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
    
    public boolean hasMetadata () {
        return (this.metadata != null);
    }
}
