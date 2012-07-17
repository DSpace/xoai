package com.lyncode.xoai.common.serviceprovider.verbs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.lyncode.xoai.common.serviceprovider.data.MetadataFormat;
import com.lyncode.xoai.common.serviceprovider.iterators.MetadataFormatIterator;
import com.lyncode.xoai.common.serviceprovider.util.URLEncoder;
import com.lyncode.xoai.common.serviceprovider.xml.configuration.Configuration;

public class ListMetadataFormats extends AbstractVerb implements Iterable<MetadataFormat>
{
    private ExtraParameters parameters;

    public ListMetadataFormats(Configuration config, String baseUrl)
    {
        super(config, baseUrl);
        parameters = null;
    }
    public ListMetadataFormats(Configuration config, String baseUrl, ExtraParameters extra)
    {
        super(config, baseUrl);
        parameters = extra;
    }

    @Override
    public Iterator<MetadataFormat> iterator()
    {
        return new MetadataFormatIterator(super.getConfiguration(), super.getBaseUrl(), parameters);
    }


    public class ExtraParameters {
        private String identifier;
        
        public ExtraParameters()
        {
            super();
        }
        
        
        
        public String getIdentifier()
        {
            return identifier;
        }



        public void setIdentifier(String identifier)
        {
            this.identifier = identifier;
        }



        public String toUrl () {
            List<String> string = new ArrayList<String>();
            if (identifier != null) string.add("set="+URLEncoder.encode(identifier));
            return StringUtils.join(string, URLEncoder.SEPARATOR);
        }
    }
}
