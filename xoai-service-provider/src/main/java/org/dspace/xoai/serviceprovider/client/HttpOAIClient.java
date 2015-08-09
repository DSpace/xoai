/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.client;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.dspace.xoai.serviceprovider.exceptions.HttpException;
import org.dspace.xoai.serviceprovider.parameters.Parameters;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

public class HttpOAIClient implements OAIClient {
	private String baseUrl;
	private HttpClient httpclient;
	private int timeout = 60000;
	private List<String> baseUrlsHttpsExclusion;
	
	public HttpOAIClient(String baseUrl) {
		this.baseUrl = baseUrl;
		httpclient = new DefaultHttpClient(createHttpParams());
	}


	/**
	 * Creates a HttpOAIClient 
	 * 
	 * @param baseUrl - the base URL for the OAI repository 
	 * @param baseUrlsHttpsExclusion - if provided, the base URLs for the OAI repositories will ignore problems related with HTTPS certificate verification
	 * @throws HttpException
	 */
	public HttpOAIClient(String baseUrl, List<String> baseUrlsHttpsExclusion) throws HttpException {
		this.baseUrl = baseUrl;
		this.baseUrlsHttpsExclusion = baseUrlsHttpsExclusion;
		initHttpClient();
	}

	
	/**
	 * Creates a HttpOAIClient 
	 * 
	 * @param baseUrl - the base URL for the OAI repository 
	 * @param baseUrlsHttpsExclusion - if provided, the base URLs for the OAI repositories will ignore problems related with HTTPS certificate verification
	 * @par
	 * @throws HttpException
	 */
	public HttpOAIClient(String baseUrl, List<String> baseUrlsHttpsExclusion, int timeout) throws HttpException {
		this.timeout = timeout;
		this.baseUrl = baseUrl;
		this.baseUrlsHttpsExclusion = baseUrlsHttpsExclusion;
		initHttpClient();
	}

	public InputStream execute(Parameters parameters) throws HttpException {
		try {
			HttpResponse response = httpclient
					.execute(createGetRequest(parameters));
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				return response.getEntity().getContent();
			else
				throw new HttpException(
						"Error querying service. Returned HTTP Status Code: "
								+ response.getStatusLine().getStatusCode());
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}

	private HttpUriRequest createGetRequest(Parameters parameters) {
		return new HttpGet(parameters.toUrl(baseUrl));
	}

	/**
	 * Initializes the HTTP client and if the base URL is in the
	 * baseUrlsHttpsExclusion then the certificate verification will not happen
	 * 
	 * @throws HttpException
	 */
	private void initHttpClient() throws HttpException {
		try {
			if (baseUrlsHttpsExclusion != null && baseUrlsHttpsExclusion.contains(baseUrl)) {
				SSLSocketFactory sslsf = new SSLSocketFactory(
						new TrustStrategy() {
							@Override
							public boolean isTrusted(X509Certificate[] chain,
									String authType)
									throws CertificateException {
								return true;
							}

						}, new AllowAllHostnameVerifier());

				Scheme httpsScheme = new Scheme("https", 443, sslsf);
				SchemeRegistry schemeRegistry = new SchemeRegistry();
				schemeRegistry.register(httpsScheme);

				ClientConnectionManager cm = new BasicClientConnectionManager(
						schemeRegistry);

				httpclient = new DefaultHttpClient(cm, createHttpParams());
			} else {
				httpclient = new DefaultHttpClient(createHttpParams());

			}
		} catch (KeyManagementException e) {
			throw new HttpException(e);
		} catch (UnrecoverableKeyException e) {
			throw new HttpException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new HttpException(e);
		} catch (KeyStoreException e) {
			throw new HttpException(e);
		}
	}

	/**
	 * Creates a HttpParams with the options connection and socket timeout (default timeont if none is defined: 1
	 * minute)
	 * 
	 * @return
	 */
	private HttpParams createHttpParams() {
		final HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
	    HttpConnectionParams.setSoTimeout(httpParams, timeout);
		return httpParams;
	}
}
