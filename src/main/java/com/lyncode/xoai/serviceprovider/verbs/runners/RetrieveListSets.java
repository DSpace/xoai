package com.lyncode.xoai.serviceprovider.verbs.runners;

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
import com.lyncode.xoai.serviceprovider.exceptions.InternalHarvestException;
import com.lyncode.xoai.serviceprovider.oaipmh.OAIPMHParser;
import com.lyncode.xoai.serviceprovider.oaipmh.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.OAIPMHtype;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.SetType;
import com.lyncode.xoai.serviceprovider.util.ProcessingQueue;
import com.lyncode.xoai.serviceprovider.util.URLEncoder;

public class RetrieveListSets implements Runnable {
	private ProcessingQueue<SetType> queue;
	private Logger log;
	private String baseUrl;
	private int interval;
	
	public RetrieveListSets(int interval, String baseUrl, ProcessingQueue<SetType> list, Logger log) {
		this.queue = list;
		this.log = log;
		this.baseUrl = baseUrl;
		this.interval = interval;
	}

    private String makeUrl (String resumption) {
        if (resumption != null && !resumption.trim().equals("")) {
            return (baseUrl + "?verb=ListSets"+ URLEncoder.SEPARATOR +"resumptionToken="+URLEncoder.encode(resumption));
        }
        else {
            
            return (baseUrl + "?verb=ListSets");
            
        }
    }

	@Override
	public void run() {
		try {
			long timeBefore, timeAfter;
			timeBefore = System.currentTimeMillis();
			String resumption = null;
			resumption = this.harvest(makeUrl(resumption));
			timeAfter = System.currentTimeMillis();
			if (timeAfter - timeBefore < this.interval) 
				Thread.sleep(this.interval - (timeAfter - timeBefore));
			while (resumption != null) {
				timeBefore = System.currentTimeMillis();
				resumption = this.harvest(makeUrl(resumption));
				timeAfter = System.currentTimeMillis();
				if (timeAfter - timeBefore < this.interval) 
					Thread.sleep(this.interval - (timeAfter - timeBefore));
			}
			queue.finish();
		} catch (InternalHarvestException | InterruptedException e) {
			log.error("Internal error", e);
			queue.finish();
		}
	}


    private String harvest (String url) throws InternalHarvestException {
        HttpClient httpclient = new DefaultHttpClient();
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
            
            OAIPMHParser parse = OAIPMHParser.newInstance(instream, log);
            OAIPMHtype res = parse.parse();
            
            if (res.getListSets() != null) {
            	for (SetType h : res.getListSets().getSet()) {
            		queue.enqueue(h);
            	}
            	
            	if (res.getListSets().getResumptionToken() != null) {
            		String result = res.getListSets().getResumptionToken().getValue();
            		if (result != null && result.trim().equals("")) return null;
            		else return result;
            	}
            } 
            
            return null;
        }
        catch (IOException | XMLStreamException | ParseException e)
        {
            throw new InternalHarvestException(e);
        }
        
    }
}
