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
 * @version 2.1.0
 */

package com.lyncode.xoai.common.serviceprovider.verbs;

import java.io.InputStream;
import java.util.ArrayList;

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

import com.lyncode.xoai.common.serviceprovider.HarvesterManager;
import com.lyncode.xoai.common.serviceprovider.configuration.Configuration;
import com.lyncode.xoai.common.serviceprovider.exceptions.HarvestException;
import com.lyncode.xoai.common.serviceprovider.util.XMLUtils;


/**
 * @author DSpace @ Lyncode
 * @version 2.1.0
 */
public class Identify extends AbstractVerb
{
    private static Logger log = LogManager.getLogger(Identify.class);

    private String repositoryName;

    private String protocolVersion;

    private String earliestDateStamp;

    private String deletedRecord;

    private String granularity;

    private String description;

    private ArrayList<String> adminEmails;
    
    private ArrayList<String> compressions;
    
    public Identify(Configuration config, String baseUrl) throws HarvestException
    {
        super(config, baseUrl);
        harvest();
    }
    

    private String makeUrl () {
        return (super.getBaseUrl() + "?verb=Identify");
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
            NodeList res = doc.getElementsByTagName("Identify");
            if (res.getLength() > 0) {
                NodeList listRecords = res.item(0).getChildNodes();
                for (int j = 0;j<listRecords.getLength();j++) {
                    if (listRecords.item(j).getNodeName().equals("repositoryName"))
                        this.repositoryName = XMLUtils.getText(listRecords.item(j));
                    else if (listRecords.item(j).getNodeName().equals("protocolVersion"))
                        this.protocolVersion = XMLUtils.getText(listRecords.item(j));
                    else if (listRecords.item(j).getNodeName().equals("earliestDatestamp"))
                        this.earliestDateStamp = XMLUtils.getText(listRecords.item(j));
                    else if (listRecords.item(j).getNodeName().equals("deletedRecord"))
                        this.deletedRecord = XMLUtils.getText(listRecords.item(j));
                    else if (listRecords.item(j).getNodeName().equals("granularity"))
                        this.granularity = XMLUtils.getText(listRecords.item(j));
                    else if (listRecords.item(j).getNodeName().equals("description"))
                        this.description = XMLUtils.getXML(listRecords.item(j).getChildNodes());
                    else if (listRecords.item(j).getNodeName().equals("adminEmail")) {
                        if (this.adminEmails == null) this.adminEmails = new ArrayList<String>();
                        this.adminEmails.add(XMLUtils.getText(listRecords.item(j)));
                    }
                    else if (listRecords.item(j).getNodeName().equals("compression")) {
                        if (this.compressions == null) this.compressions = new ArrayList<String>();
                        this.compressions.add(XMLUtils.getText(listRecords.item(j)));
                    }
                }
            } else throw new HarvestException("Unable to get Identify from OAI Interface");
        }
        catch (Exception e)
        {
            throw new HarvestException(e);
        }
        
    }
    

    public String getRepositoryName()
    {
        return repositoryName;
    }

    public String getProtocolVersion()
    {
        return protocolVersion;
    }

    public String getEarliestDateStamp()
    {
        return earliestDateStamp;
    }

    public String getDeletedRecord()
    {
        return deletedRecord;
    }

    public String getGranularity()
    {
        return granularity;
    }

    public ArrayList<String> getAdminEmails()
    {
        return adminEmails;
    }


    public String getDescription()
    {
        return description;
    }


    public ArrayList<String> getCompressions()
    {
        return compressions;
    }

    
    
}
