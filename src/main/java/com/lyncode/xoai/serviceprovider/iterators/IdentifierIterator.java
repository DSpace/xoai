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
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */

package com.lyncode.xoai.serviceprovider.iterators;

import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.oaipmh.spec.HeaderType;
import com.lyncode.xoai.serviceprovider.util.ProcessingQueue;
import com.lyncode.xoai.serviceprovider.verbs.Parameters;
import com.lyncode.xoai.serviceprovider.verbs.runners.RetrieveListIdentifiers;


/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */
public class IdentifierIterator
{
    private int config;
    private String baseUrl;
    private String metadataPrefix;
    private Logger log;
    private Parameters extra;

    public IdentifierIterator(int configuration, String baseUrl, String metadataPrefix, Parameters extra, Logger log)
    {
        super();
        this.config = configuration;
        this.baseUrl = baseUrl;
        this.metadataPrefix = metadataPrefix;
        this.log = log;
        this.extra = extra;
    }
    
    public ProcessingQueue<HeaderType> harvest () {
    	ProcessingQueue<HeaderType> list = new ProcessingQueue<HeaderType>();
    	RetrieveListIdentifiers l = new RetrieveListIdentifiers(config, baseUrl, metadataPrefix, extra, list, log);
    	Thread t = new Thread(l);
    	t.start();
    	return list;
    }
}
