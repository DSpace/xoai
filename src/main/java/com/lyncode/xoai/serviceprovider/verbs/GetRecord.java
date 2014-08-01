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

 * @version 3.1.0
 */

package com.lyncode.xoai.serviceprovider.verbs;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.core.Parameters;
import com.lyncode.xoai.serviceprovider.exceptions.*;
import com.lyncode.xoai.serviceprovider.oaipmh.OAIPMHParser;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.GetRecordType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.OAIPMHtype;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;


/**

 * @version 3.1.0
 */
public class GetRecord extends AbstractVerb {

    public GetRecord(Parameters params, OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config) throws InternalHarvestException, CannotDisseminateFormatException, IdDoesNotExistException {
        super(params, config);
    }


    private String makeUrl() {
        return getParameters().toUrl();
    }

    public GetRecordType harvest() throws InternalHarvestException, CannotDisseminateFormatException, IdDoesNotExistException, BadArgumentException {
        HttpClient httpclient = new DefaultHttpClient();
        String url = makeUrl();
        super.getServiceProvider().getLogger().debug("Harvesting: " + url);
        HttpGet httpget = new HttpGet(url);
        httpget.addHeader("User-Agent", getServiceProvider().getServiceName() + " : XOAI Service Provider");
        httpget.addHeader("From", getServiceProvider().getServiceName());

        HttpResponse response = null;

        try {
            response = httpclient.execute(httpget);
            StatusLine status = response.getStatusLine();

            super.getServiceProvider().getLogger().debug(response.getStatusLine());

            if (status.getStatusCode() == 503) // 503 Status (must wait)
            {
                org.apache.http.Header[] headers = response.getAllHeaders();
                for (org.apache.http.Header h : headers) {
                    if (h.getName().equals("Retry-After")) {
                        String retry_time = h.getValue();
                        try {
                            Thread.sleep(Integer.parseInt(retry_time) * 1000);
                        } catch (NumberFormatException e) {
                            super.getServiceProvider().getLogger().warn("Cannot parse " + retry_time + " to Integer", e);
                        } catch (InterruptedException e) {
                            super.getServiceProvider().getLogger().debug(e.getMessage(), e);
                        }
                        httpclient.getConnectionManager().shutdown();
                        httpclient = new DefaultHttpClient();
                        response = httpclient.execute(httpget);
                    }
                }
            }

            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();

            OAIPMHtype pmh = OAIPMHParser.parse(instream, getServiceProvider());

            if (!pmh.getError().isEmpty()) {
                switch (pmh.getError().get(0).getCode()) {
                    case BAD_ARGUMENT:
                        throw new BadArgumentException(pmh.getError().get(0).getValue());

                    default:
                        throw new BadArgumentException(pmh.getError().get(0).getValue());
                }
            }

            return pmh.getGetRecord();
        } catch (IOException e) {
            throw new InternalHarvestException(e);
        } catch (ParseException e) {
            throw new InternalHarvestException(e);
        }

    }

}
