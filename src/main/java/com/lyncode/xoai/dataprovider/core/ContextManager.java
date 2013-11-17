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

import com.lyncode.xoai.dataprovider.data.internal.MetadataFormat;
import com.lyncode.xoai.dataprovider.data.internal.MetadataTransformer;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.filter.FilterManager;
import com.lyncode.xoai.dataprovider.format.MetadataFormatManager;
import com.lyncode.xoai.dataprovider.sets.StaticSet;
import com.lyncode.xoai.dataprovider.sets.StaticSetManager;
import com.lyncode.xoai.dataprovider.transform.TransformManager;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.BundleReference;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.ContextConfiguration;

import java.util.*;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 3.1.0
 */
public class ContextManager {

    private Map<String, XOAIContext> contexts;

    public ContextManager(List<ContextConfiguration> contexts, FilterManager filterManager,
                          TransformManager tm, MetadataFormatManager mfm, StaticSetManager sm)
            throws ConfigurationException {
        this.contexts = new HashMap<String, XOAIContext>();

        for (ContextConfiguration contextConfiguration : contexts) {
            MetadataTransformer transformer = new MetadataTransformer();
            if (contextConfiguration.getTransformers() != null)
                if (contextConfiguration.getTransformers().getReference() != null)
                    transformer = tm.getTransformer(contextConfiguration.getTransformers()
                            .getReference());

            List<StaticSet> sets = new ArrayList<StaticSet>();
            for (BundleReference b : contextConfiguration.getSets()) {
                if (!sm.setExists(b.getReference()))
                    throw new ConfigurationException("Set referred as "
                            + b.getReference() + " does not exist");
                sets.add(sm.getSet(b.getReference()));
            }

            List<MetadataFormat> formats = new ArrayList<MetadataFormat>();
            for (BundleReference b : contextConfiguration.getFormats()) {
                if (!mfm.formatExists(b.getReference()))
                    throw new ConfigurationException(
                            "Metadata FormatConfiguration referred as " + b.getReference()
                                    + " does not exist");
                formats.add(mfm.getFormat(b.getReference()));
            }

            XOAIContext xoaiContext = new XOAIContext(contextConfiguration.getBaseUrl(),
                    contextConfiguration.getName(), contextConfiguration.getDescription(),
                    transformer, formats, sets);
            if (contextConfiguration.hasFilter())
                xoaiContext.setFilter(filterManager.getFilter(contextConfiguration.getFilters().getReference()));
            this.contexts.put(contextConfiguration.getBaseUrl(), xoaiContext);
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
