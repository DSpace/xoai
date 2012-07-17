package com.lyncode.xoai.common.serviceprovider.verbs;

import com.lyncode.xoai.common.serviceprovider.xml.configuration.Configuration;


public abstract class AbstractVerb
{
    private String baseUrl;
    private Configuration configuration;
    
    public AbstractVerb (Configuration config, String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    protected String getBaseUrl () {
        return this.baseUrl;
    }

    public Configuration getConfiguration()
    {
        return configuration;
    }
    
    
}
