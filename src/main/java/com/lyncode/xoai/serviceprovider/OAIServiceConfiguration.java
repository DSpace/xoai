package com.lyncode.xoai.serviceprovider;

import com.lyncode.xoai.dataprovider.services.api.DateProvider;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;
import org.apache.log4j.Logger;


public abstract class OAIServiceConfiguration<M extends MetadataParser, A extends AboutItemParser, D extends DescriptionParser, S extends AboutSetParser> {
    private static Logger log = Logger.getLogger(OAIServiceConfiguration.class);
    private static final int INTERVAL = 1000;

    private DateProvider formatter;

    public OAIServiceConfiguration(DateProvider formatter) {
        this.formatter = formatter;
    }

    public int getIntervalBetweenRequests() {
        return INTERVAL;
    }

    public Logger getLogger() {
        return log;
    }

    public DateProvider getFormatter () {
        return this.formatter;
    }

    public abstract M getMetadataParser();

    public abstract A getAboutItemParser();

    public abstract D getDescriptionParser();

    public abstract S getAboutSetParser();

    public abstract String getServiceName();
}
