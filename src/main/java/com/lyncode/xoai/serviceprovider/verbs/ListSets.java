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
 * @version 2.2.8
 */

package com.lyncode.xoai.serviceprovider.verbs;

import com.lyncode.xoai.serviceprovider.configuration.Configuration;
import com.lyncode.xoai.serviceprovider.iterators.SetIterator;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.8
 */

public class ListSets extends AbstractVerb
{

    public ListSets(Configuration config, String baseUrl)
    {
        super(config, baseUrl);
    }

    public SetIterator iterator()
    {
        return new SetIterator(super.getConfiguration(), super.getBaseUrl());
    }

}
