package com.lyncode.xoai.common.serviceprovider;

import com.lyncode.xoai.common.serviceprovider.exceptions.HarvestException;
import com.lyncode.xoai.common.serviceprovider.verbs.GetRecord;
import com.lyncode.xoai.common.serviceprovider.verbs.Identify;
import com.lyncode.xoai.common.serviceprovider.verbs.ListIdentifiers;
import com.lyncode.xoai.common.serviceprovider.verbs.ListMetadataFormats;
import com.lyncode.xoai.common.serviceprovider.verbs.ListRecords;
import com.lyncode.xoai.common.serviceprovider.verbs.ListSets;
import com.lyncode.xoai.common.serviceprovider.xml.configuration.Configuration;

public class HarvesterManager
{
    public static final String USERAGENT = "XOAI Service Provider by Lyncode.com";
    public static final String FROM = "general@lyncode.com";
    
    private Configuration config;
    private String baseUrl;
    
    public HarvesterManager (Configuration configure, String baseUrl) {
        config = configure;
        this.baseUrl = baseUrl;
    }
    
    private Configuration getConfiguration () {
        return config;
    }

    public ListRecords listRecords (String metadataPrefix) {
        return new ListRecords(getConfiguration(), baseUrl, metadataPrefix);
    }
    
    public ListRecords listRecords (String metadataPrefix, com.lyncode.xoai.common.serviceprovider.verbs.ListRecords.ExtraParameters extra) {
        return new ListRecords(config, baseUrl, metadataPrefix, extra);
    }

    public ListIdentifiers listIdentifiers (String metadataPrefix) {
        return new ListIdentifiers(getConfiguration(), baseUrl, metadataPrefix);
    }
    
    public ListIdentifiers listIdentifiers (String metadataPrefix, com.lyncode.xoai.common.serviceprovider.verbs.ListIdentifiers.ExtraParameters extra) {
        return new ListIdentifiers(getConfiguration(), baseUrl, metadataPrefix, extra);
    }
    
    public ListMetadataFormats listMetadataFormats () {
        return new ListMetadataFormats(config, baseUrl);
    }
    public ListMetadataFormats listMetadataFormats (com.lyncode.xoai.common.serviceprovider.verbs.ListMetadataFormats.ExtraParameters extra) {
        return new ListMetadataFormats(config, baseUrl, extra);
    }
    
    public ListSets listSets () {
        return new ListSets(config, baseUrl);
    }
    
    public GetRecord getRecord (String identifier, String metadataPrefix) throws HarvestException {
        return new GetRecord(config, baseUrl, identifier, metadataPrefix);
    }
    
    public Identify identify () throws HarvestException {
        return new Identify(config, baseUrl);
    }
}
