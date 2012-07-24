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
 * @version 2.2.1
 */

package com.lyncode.xoai.serviceprovider.verbs;

import java.io.InputStream;

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
import com.lyncode.xoai.serviceprovider.data.Header;
import com.lyncode.xoai.serviceprovider.data.Metadata;
import com.lyncode.xoai.serviceprovider.exceptions.HarvestException;
import com.lyncode.xoai.serviceprovider.util.URLEncoder;
import com.lyncode.xoai.serviceprovider.util.XMLUtils;


/**
 * @author DSpace @ Lyncode
 * @version 2.2.1
 */
public class GetRecord extends AbstractVerb
{
    private static Logger log = LogManager.getLogger(GetRecord.class);
    private String identifier;
    private String metadataPrefix;
    
    private Header header;
    private Metadata metadata;
    
    public GetRecord(Configuration config, String baseUrl, String identifier, String metadataPrefix) throws HarvestException
    {
        super(config, baseUrl);
        this.identifier = identifier;
        this.metadataPrefix = metadataPrefix;
        harvest();
    }
    
    
    
    private String makeUrl () {
        return (super.getBaseUrl() + "?verb=GetRecord" + URLEncoder.SEPARATOR + "metadataPrefix="+metadataPrefix + URLEncoder.SEPARATOR + "identifier=" + identifier);
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
        catch (Exception e)
        {
            throw new HarvestException(e);
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
