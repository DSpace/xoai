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

package com.lyncode.xoai.dataprovider.core;

import com.lyncode.xoai.dataprovider.data.MetadataFormat;
import com.lyncode.xoai.dataprovider.data.MetadataTransformer;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.filter.Filter;
import com.lyncode.xoai.dataprovider.filter.FilterManager;
import com.lyncode.xoai.dataprovider.format.MetadataFormatManager;
import com.lyncode.xoai.dataprovider.sets.StaticSet;
import com.lyncode.xoai.dataprovider.sets.StaticSetManager;
import com.lyncode.xoai.dataprovider.transform.TransformManager;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.BundleReference;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration.Contexts;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration.Contexts.Context;

import java.util.*;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 3.1.0
 */
public class ContextManager {

    private Map<String, XOAIContext> contexts;

    public ContextManager(Contexts contexts, FilterManager fm,
                          TransformManager tm, MetadataFormatManager mfm, StaticSetManager sm)
            throws ConfigurationException {
        this.contexts = new HashMap<String, XOAIContext>();

        for (Context ct : contexts.getContext()) {
            List<Filter> filters = new ArrayList<Filter>();
            for (BundleReference b : ct.getFilter()) {
                if (!fm.filterExists(b.getRefid()))
                    throw new ConfigurationException("ScopedFilter referred as "
                            + b.getRefid() + " does not exist");
                filters.add(fm.getFilter(b.getRefid()));
            }

            MetadataTransformer transformer = new MetadataTransformer();
            if (ct.getTransformer() != null)
                if (ct.getTransformer().getRefid() != null)
                    transformer = tm.getTransformer(ct.getTransformer()
                            .getRefid());

            List<StaticSet> sets = new ArrayList<StaticSet>();
            for (BundleReference b : ct.getSet()) {
                if (!sm.setExists(b.getRefid()))
                    throw new ConfigurationException("Set referred as "
                            + b.getRefid() + " does not exist");
                sets.add(sm.getSet(b.getRefid()));
            }

            List<MetadataFormat> formats = new ArrayList<MetadataFormat>();
            for (BundleReference b : ct.getFormat()) {
                if (!mfm.formatExists(b.getRefid()))
                    throw new ConfigurationException(
                            "Metadata Format referred as " + b.getRefid()
                                    + " does not exist");
                formats.add(mfm.getFormat(b.getRefid()));
            }

            this.contexts.put(ct.getBaseUrl(), new XOAIContext(ct.getBaseUrl(), ct.getName(), ct.getDescription(),
                    transformer, filters, formats, sets));
        }
    }

    public boolean contextExists(String baseUrl) {
        return this.contexts.containsKey(baseUrl);
    }

    public XOAIContext getOAIContext(String baseUrl) {
        return contexts.get(baseUrl);
    }

    public Collection<XOAIContext> getContexts() {
        return contexts.values();
    }
}
