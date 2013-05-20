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

import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.exceptions.InternalHarvestException;
import com.lyncode.xoai.serviceprovider.iterators.MetadataFormatIterator;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.ListMetadataFormatsType;


/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */
public class ListMetadataFormats extends AbstractVerb
{
    private Parameters parameters;

    public ListMetadataFormats(String baseUrl, Logger log)
    {
        super(baseUrl, log);
        parameters = null;
    }
    public ListMetadataFormats(String baseUrl, Parameters extra, Logger log)
    {
        super(baseUrl, log);
        parameters = extra;
    }

    public ListMetadataFormatsType harvest() throws InternalHarvestException
    {
        return (new MetadataFormatIterator(super.getBaseUrl(), parameters, getLogger())).harvest();
    }

}
