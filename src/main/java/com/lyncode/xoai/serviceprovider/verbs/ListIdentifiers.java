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
 * @version 3.1.0
 */

package com.lyncode.xoai.serviceprovider.verbs;

import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.iterators.IdentifierIterator;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.HeaderType;
import com.lyncode.xoai.util.ProcessingQueue;


/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 3.1.0
 */
public class ListIdentifiers extends AbstractVerb
{
    private String metadataPrefix;
    private int interval;
    private Parameters extra;
    
    public ListIdentifiers(String baseUrl, String metadataPrefix, Parameters extra, int interval, Logger log)
    {
        super(baseUrl, log);
        this.metadataPrefix = metadataPrefix;
        this.interval = interval;
        this.extra = extra;
    }


	public ProcessingQueue<HeaderType> harvest()
    {
        return (new IdentifierIterator(this.interval, super.getBaseUrl(), metadataPrefix, this.extra, getLogger())).harvest();
    }
}
