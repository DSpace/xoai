package com.lyncode.xoai.common.serviceprovider.iterators;

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

import com.lyncode.xoai.common.serviceprovider.HarvesterManager;
import com.lyncode.xoai.common.serviceprovider.data.MetadataFormat;
import com.lyncode.xoai.common.serviceprovider.exceptions.HarvestException;
import com.lyncode.xoai.common.serviceprovider.util.URLEncoder;
import com.lyncode.xoai.common.serviceprovider.util.XMLUtils;
import com.lyncode.xoai.common.serviceprovider.verbs.ListMetadataFormats.ExtraParameters;
import com.lyncode.xoai.common.serviceprovider.xml.configuration.Configuration;

public class MetadataFormatIterator implements Iterator<MetadataFormat>
{
    private static Logger log = LogManager.getLogger(MetadataFormatIterator.class);
    
    private Configuration config;
    private String baseUrl;
    private ExtraParameters extra;

    public MetadataFormatIterator(Configuration configuration, String baseUrl, ExtraParameters extra)
    {
        super();
        this.config = configuration;
        this.baseUrl = baseUrl;
        this.extra = extra;
    }


    private Queue<MetadataFormat> _queue = null;
    
    private String makeUrl () {
        if (extra == null || extra.equals(""))
                return (baseUrl + "?verb=ListMetadataFormats");
        else
                return (baseUrl + "?verb=ListMetadataFormats" + URLEncoder.SEPARATOR + extra.toUrl());
        
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
            NodeList listRecords = doc.getElementsByTagName("metadataFormat");
            for (int i = 0;i<listRecords.getLength();i++)
                _queue.add(XMLUtils.getMetadataFormat(listRecords.item(i)));
            
        }
        catch (Exception e)
        {
            throw new HarvestException(e);
        }
        
    }
    
    @Override
    public boolean hasNext()
    {
        if (_queue == null) {
            if (_queue == null) _queue = new LinkedList<MetadataFormat>();
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
    public MetadataFormat next()
    {
        return _queue.poll();
    }

    @Override
    public void remove()
    {
        // No need to implement
    }

}
