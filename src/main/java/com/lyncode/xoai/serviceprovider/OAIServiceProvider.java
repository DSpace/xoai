package com.lyncode.xoai.serviceprovider;

import com.lyncode.xoai.serviceprovider.core.Harvester;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;


public class OAIServiceProvider {

    private OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config;

    public OAIServiceProvider(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config) {
        this.config = config;
    }

    public Harvester build(String baseURL) {
        return new Harvester(config, baseURL);
    }
}
