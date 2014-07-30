/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     client://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.xoai.serviceprovider.client;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import com.lyncode.xoai.serviceprovider.exceptions.HttpException;
import com.lyncode.xoai.serviceprovider.parameters.Parameters;

public class HttpOAIClient implements OAIClient {
    private String baseUrl;
    private HttpClient httpclient = new DefaultHttpClient();

    public HttpOAIClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public InputStream execute (Parameters parameters) throws HttpException {
        try {
            HttpResponse response = httpclient.execute(createGetRequest(parameters));
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                return response.getEntity().getContent();
            else
                throw new HttpException("Error querying service. Returned HTTP Status Code: "+response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }

    private HttpUriRequest createGetRequest(Parameters parameters) {
        return new HttpGet(parameters.toUrl(baseUrl));
    }
}
