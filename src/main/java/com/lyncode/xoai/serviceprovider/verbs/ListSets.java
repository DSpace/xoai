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
 * @version 2.2.0
 */

package com.lyncode.xoai.serviceprovider.verbs;

import java.util.Iterator;

import com.lyncode.xoai.serviceprovider.configuration.Configuration;
import com.lyncode.xoai.serviceprovider.data.Set;
import com.lyncode.xoai.serviceprovider.iterators.SetIterator;

/**
 * @author DSpace @ Lyncode
 * @version 2.2.0
 */

public class ListSets extends AbstractVerb implements Iterable<Set>
{

    public ListSets(Configuration config, String baseUrl)
    {
        super(config, baseUrl);
    }

    @Override
    public Iterator<Set> iterator()
    {
        return new SetIterator(super.getConfiguration(), super.getBaseUrl());
    }

}
