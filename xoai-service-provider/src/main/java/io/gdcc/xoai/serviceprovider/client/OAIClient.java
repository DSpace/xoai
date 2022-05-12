/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider.client;

import io.gdcc.xoai.serviceprovider.exceptions.OAIRequestException;
import io.gdcc.xoai.serviceprovider.parameters.Parameters;

import java.io.InputStream;
import java.net.URL;
import java.time.Duration;

/**
 * An OAI-PMH client to interact with a target repository.
 */
public abstract class OAIClient {
    
    // Make default constructor protected, so now one avoids the builder pattern
    protected OAIClient() {}
    
    /**
     * Create a new OAIClient with this builder, allowing for precise configuration.
     * Individual builders may allow for extended configuration.
     *
     * A default implementation based on JDK 11+ HTTP Client is sent your way via {@link OAIClient#newBuilder()}.
     */
    public interface Builder {
        /**
         * The base HTTP(S) URL to reach the OAI-PMH endpoint of your data provider.
         * Often shaped like this: https://host.example.org/oai
         * Request parameters are handled by the implementation.
         *
         * @param baseUrl
         * @return A builder, ready for configuration and creating your {@link OAIClient}.
         * @throws IllegalArgumentException When not HTTP(S) URL
         */
        Builder withBaseUrl(URL baseUrl);
        /**
         * The base HTTP(S) URL to reach the OAI-PMH endpoint of your data provider.
         * Often shaped like this: https://host.example.org/oai
         * Request parameters are handled by the implementation.
         *
         * @param baseUrl
         * @return A builder, ready for configuration and creating your {@link OAIClient}.
         * @throws IllegalArgumentException When not a URL or not HTTP(S)
         */
        Builder withBaseUrl(String baseUrl);
    
        /**
         * Set the timeout for establishing new connections from a client (does not affect
         * existing, reused connections).
         *
         * @param timeout
         * @return A builder, ready for configuration and creating your {@link OAIClient}.
         */
        Builder withConnectTimeout(Duration timeout);
    
        /**
         * Set the timeout for each HTTP request of this client. Needs to be adapted to
         * the servers (data providers) capabilities, usually connected to the amount of
         * data you are trying to receive.
         *
         * Recommended setting to avoid blocking forever, waiting for a response.
         *
         * @param timeout The maximum amount of time to wait for a server response
         * @return A builder, ready for configuration and creating your {@link OAIClient}.
         */
        Builder withRequestTimeout(Duration timeout);
    
        /**
         * Set the user agent of your HTTP requests to data providers to sth. you enjoy.
         * Will be used for every request of this client.
         *
         * @param userAgent A string to be used as user agent indication. Should reflect this is a harvester.
         * @return A builder, ready for configuration and creating your {@link OAIClient}.
         */
        Builder withUserAgent(String userAgent);
    
        /**
         * Some OAI-PMH endpoints might redirect you to some other URL.  (In example HTTP redirects to HTTPS,
         * URL rewriting etc.) By enabling this, you tell the client to silently follow and redirects instead of
         * returning you with the 30x response you need to handle yourself.
         *
         * @return A builder, ready for configuration and creating your {@link OAIClient}.
         */
        Builder withFollowRedirects();
    
        /**
         * In test or other non-production scenarios you might want to use target OAI-PMH server
         * using self-signed certificates. To enable accessing these resources, enable this here.
         *
         * @return A builder, ready for configuration and creating your {@link OAIClient}.
         */
        Builder withInsecureSSL();
    
        /**
         * Build the {@link OAIClient} after configuring it.
         *
         * @return The {@link OAIClient} ready to use.
         */
        OAIClient build();
    }
    
    /**
     * Receive a builder to create a new OAIClient, allowing to configure
     * different aspects of the client and underlying technology.
     *
     * @return If not overridden, an instance of {@link JdkHttpOaiClient.JdkHttpBuilder}
     */
    public static Builder newBuilder() {
        return new JdkHttpOaiClient.JdkHttpBuilder();
    }
    
    /**
     * Execute a query against the OAI-PMH server given when building this client
     * via an {@link Builder}.
     *
     * Will throw an exception when the response is not successful (not HTTP 200), connection could not
     * be established or the response took longer then the timeout to arrive.
     *
     * @param parameters The query parameters like verb, filters, etc.
     * @return An {@link InputStream} ready to read the server response from.
     * @throws OAIRequestException in case anything goes wrong.
     */
    public abstract InputStream execute(Parameters parameters) throws OAIRequestException;
}
