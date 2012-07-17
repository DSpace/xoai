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
 * @author DSpace @ Lyncode
 * @version 2.1.0
 */

package com.lyncode.xoai.common.serviceprovider.verbs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.lyncode.xoai.common.serviceprovider.configuration.Configuration;
import com.lyncode.xoai.common.serviceprovider.data.MetadataFormat;
import com.lyncode.xoai.common.serviceprovider.iterators.MetadataFormatIterator;
import com.lyncode.xoai.common.serviceprovider.util.URLEncoder;


/**
 * @author DSpace @ Lyncode
 * @version 2.1.0
 */
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
