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

package com.lyncode.xoai.serviceprovider;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.exceptions.BadArgumentException;
import com.lyncode.xoai.serviceprovider.exceptions.CannotDisseminateFormatException;
import com.lyncode.xoai.serviceprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.serviceprovider.exceptions.InternalHarvestException;
import com.lyncode.xoai.serviceprovider.verbs.GetRecord;
import com.lyncode.xoai.serviceprovider.verbs.Identify;
import com.lyncode.xoai.serviceprovider.verbs.ListIdentifiers;
import com.lyncode.xoai.serviceprovider.verbs.ListMetadataFormats;
import com.lyncode.xoai.serviceprovider.verbs.ListRecords;
import com.lyncode.xoai.serviceprovider.verbs.ListSets;
import com.lyncode.xoai.serviceprovider.verbs.Parameters;

/**
 * This class works as a wrapper to provide an API with all OAI-PMH possible requests.
 * It is based upon the definition at: http://www.openarchives.org/OAI/openarchivesprotocol.html
 * 
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */
public class HarvesterManager
{
	private static Logger log = LogManager.getLogger(HarvesterManager.class);
    public static final String USERAGENT = "XOAI Service Provider by Lyncode.com";
    public static final String FROM = "general@lyncode.com";
    
    private static boolean configured = false;
    
    public void trustAllCertificates () {
    	if (!configured) {
	    	// Create a trust manager that does not validate certificate chains
	    	TrustManager[] trustAllCerts = new TrustManager[]{
	    	    new X509TrustManager() {
	    	        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	    	            return null;
	    	        }
	    	        public void checkClientTrusted(
	    	            java.security.cert.X509Certificate[] certs, String authType) {
	    	        }
	    	        public void checkServerTrusted(
	    	            java.security.cert.X509Certificate[] certs, String authType) {
	    	        }
	    	    }
	    	};
	
	    	// Install the all-trusting trust manager
	    	try {
	    	    SSLContext sc = SSLContext.getInstance("SSL");
	    	    sc.init(null, trustAllCerts, new java.security.SecureRandom());
	    	    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	    	} catch (Exception e) {
	    		log.debug(e.getMessage(), e);
	    	}
	    	
	    	configured = true;
    	}
    }
    
    private String baseUrl;
    private int intervalBetweenRequests;

    public HarvesterManager (String baseUrl) {
        this.baseUrl = baseUrl;
        this.intervalBetweenRequests = 1000;
    }
    public HarvesterManager (String baseUrl, int interval) {
        this.baseUrl = baseUrl;
        this.intervalBetweenRequests = interval;
    }
    
    public int getIntervalBetweenRequests () {
        return intervalBetweenRequests;
    }

    public ListRecords listRecords (String metadataPrefix) {
        return new ListRecords(baseUrl, metadataPrefix, this.getIntervalBetweenRequests());
    }
    
    public ListRecords listRecords (String metadataPrefix, Parameters extra) {
        return new ListRecords(baseUrl, metadataPrefix, extra, this.getIntervalBetweenRequests());
    }

    public ListIdentifiers listIdentifiers (String metadataPrefix) {
        return new ListIdentifiers(baseUrl, metadataPrefix, this.getIntervalBetweenRequests());
    }
    
    public ListIdentifiers listIdentifiers (String metadataPrefix, Parameters extra) {
        return new ListIdentifiers(baseUrl, metadataPrefix, extra, this.getIntervalBetweenRequests());
    }
    
    public ListMetadataFormats listMetadataFormats () {
        return new ListMetadataFormats(baseUrl);
    }
    public ListMetadataFormats listMetadataFormats (Parameters extra) {
        return new ListMetadataFormats(baseUrl, extra);
    }
    
    public ListSets listSets () {
        return new ListSets(baseUrl, this.getIntervalBetweenRequests());
    }
    
    public GetRecord getRecord (String identifier, String metadataPrefix) throws InternalHarvestException, BadArgumentException, CannotDisseminateFormatException, IdDoesNotExistException {
        return new GetRecord(baseUrl, identifier, metadataPrefix);
    }
    
    public Identify identify () throws InternalHarvestException, BadArgumentException {
        return new Identify(baseUrl);
    }
}
