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

import com.lyncode.xoai.serviceprovider.oaipmh.GenericParser;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.RecordType;
import com.lyncode.xoai.serviceprovider.util.ProcessingQueue;
import com.lyncode.xoai.serviceprovider.verbs.Parameters;
import com.lyncode.xoai.serviceprovider.verbs.runners.RetrieveListRecords;


/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */
public class RecordIterator
{
    private Logger log;
    private int config;
    private String baseUrl;
    private String metadataPrefix;
    private Parameters extra;
    private GenericParser met;
    private GenericParser abt;

    public RecordIterator(int interval, String baseUrl, String metadataPrefix,
    		Parameters extra, Logger log, GenericParser metadata)
    {
        super();
        this.config = interval;
        this.baseUrl = baseUrl;
        this.metadataPrefix = metadataPrefix;
        this.extra = extra;
        this.log = log;
        this.met = metadata;
        this.abt = null;
    }
    public RecordIterator(int interval, String baseUrl, String metadataPrefix,
    		Parameters extra, Logger log, GenericParser metadata, GenericParser about)
    {
        super();
        this.config = interval;
        this.baseUrl = baseUrl;
        this.metadataPrefix = metadataPrefix;
        this.extra = extra;
        this.log = log;
        this.met = metadata;
        this.abt = about;
    }

    public ProcessingQueue<RecordType> harvest () {
    	ProcessingQueue<RecordType> list = new ProcessingQueue<RecordType>();
    	RetrieveListRecords l = new RetrieveListRecords(config, baseUrl, metadataPrefix, extra, list, log, this.met, this.abt);
    	Thread t = new Thread(l);
    	t.start();
    	return list;
    }
}
