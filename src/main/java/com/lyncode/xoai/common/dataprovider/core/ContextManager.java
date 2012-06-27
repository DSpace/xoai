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
 */

package com.lyncode.xoai.common.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lyncode.xoai.common.data.AbstractMetadataFormat;
import com.lyncode.xoai.common.exceptions.ConfigurationException;
import com.lyncode.xoai.common.filter.AbstractFilter;
import com.lyncode.xoai.common.filter.FilterManager;
import com.lyncode.xoai.common.format.MetadataFormatManager;
import com.lyncode.xoai.common.sets.StaticSet;
import com.lyncode.xoai.common.sets.StaticSetManager;
import com.lyncode.xoai.common.transform.AbstractTransformer;
import com.lyncode.xoai.common.transform.NullTransformer;
import com.lyncode.xoai.common.transform.TransformManager;
import com.lyncode.xoai.common.xml.xoaiconfig.BundleReference;
import com.lyncode.xoai.common.xml.xoaiconfig.Configuration.Contexts;
import com.lyncode.xoai.common.xml.xoaiconfig.Configuration.Contexts.Context;


/**
 * @author DSpace @ Lyncode
 * @version 1.0.1
 */
public class ContextManager {

    private Map<String, XOAIContext> _contexts;

    public ContextManager(Contexts contexts, FilterManager fm, TransformManager tm, MetadataFormatManager mfm, StaticSetManager sm) throws ConfigurationException {
        _contexts = new HashMap<String, XOAIContext>();
        
        for (Context ct : contexts.getContext()) {
            List<AbstractFilter> filters = new ArrayList<AbstractFilter>();
            for (BundleReference b : ct.getFilter()) {
                if (!fm.filterExists(b.getRefid()))
                    throw new ConfigurationException("Filter refered as "+b.getRefid()+" does not exists");
                filters.add(fm.getFilter(b.getRefid()));
            }
            AbstractTransformer transformer;
            if (ct.getTransformer() != null) transformer = tm.getTransformer(ct.getTransformer().getRefid());
            else transformer = new NullTransformer();
            
            List<StaticSet> sets = new ArrayList<StaticSet>();
            for (BundleReference b : ct.getSet()) {
                if (!sm.setExists(b.getRefid()))
                    throw new ConfigurationException("Set refered as "+b.getRefid()+" does not exists");
                sets.add(sm.getSet(b.getRefid()));
            }

            List<AbstractMetadataFormat> formats = new ArrayList<AbstractMetadataFormat>();
            for (BundleReference b : ct.getFormat()) {
                if (!mfm.formatExists(b.getRefid()))
                    throw new ConfigurationException("Metadata Format refered as "+b.getRefid()+" does not exists");
                formats.add(mfm.getFormat(b.getRefid()));
            }

            _contexts.put(ct.getBaseurl(), new XOAIContext(ct.getBaseurl(), transformer, filters, formats, sets));
        }
    }

    public boolean contextExists (String baseurl) {
        return this._contexts.containsKey(baseurl);
    }

    public XOAIContext getOAIContext (String baseurl) {
        return _contexts.get(baseurl);
    }
    
}
