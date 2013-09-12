package com.lyncode.xoai.serviceprovider;

import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;

import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;


public abstract class OAIServiceConfiguration<M extends MetadataParser, A extends AboutItemParser, D extends DescriptionParser, S extends AboutSetParser> {
    private static Logger log = Logger.getLogger(OAIServiceConfiguration.class);
    private static final int INTERVAL = 1000;
    
    public int getIntervalBetweenRequests () {
        return INTERVAL;
    }
    
    public Logger getLogger () {
        return log;
    }
    
    public abstract M getMetadataParser ();
    public abstract A getAboutItemParser ();
    public abstract D getDescriptionParser();
    public abstract S getAboutSetParser();
    
    public abstract String getServiceName ();
}
