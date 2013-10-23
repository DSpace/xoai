package com.lyncode.xoai.serviceprovider.verbs.runners;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.core.Parameters;
import com.lyncode.xoai.serviceprovider.exceptions.InternalHarvestException;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.OAIPMHParser;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.HeaderType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.OAIPMHtype;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;
import com.lyncode.xoai.util.ProcessingQueue;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

public class RetrieveListIdentifiers implements Runnable {
    private ProcessingQueue<HeaderType> queue;
    private Parameters parameters;
    private OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config;

    public RetrieveListIdentifiers(ProcessingQueue<HeaderType> list, OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config, Parameters params) {
        this.queue = list;
        this.config = config;
        this.parameters = params;
    }


    private String makeUrl(String resumption) {
        return parameters.toUrl(resumption);
    }

    @Override
    public void run() {
        try {
            long timeBefore, timeAfter;
            timeBefore = System.currentTimeMillis();
            String resumption = null;
            resumption = this.harvest(makeUrl(resumption));
            timeAfter = System.currentTimeMillis();
            if (timeAfter - timeBefore < config.getIntervalBetweenRequests())
                Thread.sleep(this.config.getIntervalBetweenRequests() - (timeAfter - timeBefore));
            while (resumption != null) {
                timeBefore = System.currentTimeMillis();
                resumption = this.harvest(makeUrl(resumption));
                timeAfter = System.currentTimeMillis();
                if (timeAfter - timeBefore < this.config.getIntervalBetweenRequests())
                    Thread.sleep(this.config.getIntervalBetweenRequests() - (timeAfter - timeBefore));
            }
            queue.finish();
        } catch (InternalHarvestException e) {
            config.getLogger().error("Internal error", e);
            queue.finish();
        } catch (InterruptedException e) {
            config.getLogger().error("Internal error", e);
            queue.finish();
        }
    }


    private String harvest(String url) throws InternalHarvestException {
        HttpClient httpclient = new DefaultHttpClient();
        config.getLogger().info("Harvesting: " + url);
        HttpGet httpget = new HttpGet(url);
        httpget.addHeader("User-Agent", config.getServiceName() + " : XOAI Service Provider");
        httpget.addHeader("From", config.getServiceName());

        HttpResponse response = null;

        try {
            response = httpclient.execute(httpget);
            StatusLine status = response.getStatusLine();

            config.getLogger().debug(response.getStatusLine());

            if (status.getStatusCode() == 503) // 503 Status (must wait)
            {
                org.apache.http.Header[] headers = response.getAllHeaders();
                for (org.apache.http.Header h : headers) {
                    if (h.getName().equals("Retry-After")) {
                        String retry_time = h.getValue();
                        try {
                            Thread.sleep(Integer.parseInt(retry_time) * 1000);
                        } catch (NumberFormatException e) {
                            config.getLogger().warn("Cannot parse " + retry_time + " to Integer", e);
                        } catch (InterruptedException e) {
                            config.getLogger().debug(e.getMessage(), e);
                        }
                        httpclient.getConnectionManager().shutdown();
                        httpclient = new DefaultHttpClient();
                        response = httpclient.execute(httpget);
                    }
                }
            }

            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();

            OAIPMHtype res = OAIPMHParser.parse(instream, config);

            if (res.getListIdentifiers() != null) {
                for (HeaderType h : res.getListIdentifiers().getHeader()) {
                    queue.enqueue(h);
                }

                if (res.getListIdentifiers().getResumptionToken() != null) {
                    String result = res.getListIdentifiers().getResumptionToken().getValue();
                    if (result != null && result.trim().equals("")) return null;
                    else return result;
                }
            }

            return null;
        } catch (IOException e) {
            throw new InternalHarvestException(e);
        } catch (ParseException e) {
            throw new InternalHarvestException(e);
        }

    }
}
