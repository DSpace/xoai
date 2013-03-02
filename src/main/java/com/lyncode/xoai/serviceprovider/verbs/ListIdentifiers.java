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

package com.lyncode.xoai.serviceprovider.verbs;

import com.lyncode.xoai.serviceprovider.iterators.IdentifierIterator;


/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */
public class ListIdentifiers extends AbstractVerb
{
    private String metadataPrefix;
    private Parameters extra;
    private int interval;
    
    public ListIdentifiers(String baseUrl, String metadataPrefix, int interval)
    {
        super(baseUrl);
        this.metadataPrefix = metadataPrefix;
        this.extra = null;
        this.interval = interval;
    }
    

    public ListIdentifiers(String baseUrl, String metadataPrefix, Parameters extra, int interval)
    {
        super(baseUrl);
        this.metadataPrefix = metadataPrefix;
        this.extra = extra;
        this.interval = interval;
    }

    public IdentifierIterator iterator()
    {
        return new IdentifierIterator(this.interval, super.getBaseUrl(), metadataPrefix, extra);
    }
}
