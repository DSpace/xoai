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

import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.filter.FilterManager;
import com.lyncode.xoai.dataprovider.format.MetadataFormatManager;
import com.lyncode.xoai.dataprovider.services.api.FilterResolver;
import com.lyncode.xoai.dataprovider.services.api.ResourceResolver;
import com.lyncode.xoai.dataprovider.sets.StaticSetManager;
import com.lyncode.xoai.dataprovider.transform.TransformManager;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration;

/**

 * @version 3.1.0
 */
public class XOAIManager {
    private FilterManager filter;
    private ContextManager context;
    private TransformManager transformer;
    private MetadataFormatManager format;
    private StaticSetManager set;
    private int listSetsSize;
    private int listRecordsSize;
    private int listIdentifiersSize;
    private boolean identation;
    private String styleSheet;
    private FilterResolver filterResolver;

    public XOAIManager(FilterResolver filterResolver, ResourceResolver resolver, Configuration config)
            throws ConfigurationException {
        this.filterResolver = filterResolver;
        filter = new FilterManager(filterResolver, config.getFilters(), config.getConditions());
        transformer = new TransformManager(resolver, config.getTransformers());
        format = new MetadataFormatManager(resolver, config.getFormats(), filter);
        set = new StaticSetManager(config.getSets(), filter);
        listSetsSize = config.getMaxListSetsSize();
        listIdentifiersSize = config.getMaxListRecordsSize();
        listRecordsSize = config.getMaxListRecordsSize();
        identation = config.getIndented();
        styleSheet = config.getStylesheet();
        context = new ContextManager(config.getContexts(), filter,
                transformer, format, set);
    }

    public boolean hasStyleSheet() {
        return (styleSheet != null);
    }

    public String getStyleSheet() {
        return styleSheet;
    }

    public ContextManager getContextManager() {
        return context;
    }

    public FilterManager getFilterManager() {
        return filter;
    }

    public MetadataFormatManager getFormatManager() {
        return format;
    }

    public StaticSetManager getSetManager() {
        return set;
    }

    public TransformManager getTransformerManager() {
        return transformer;
    }

    public int getMaxListIdentifiersSize() {
        return listIdentifiersSize;
    }

    public int getMaxListRecordsSize() {
        return listRecordsSize;
    }

    public int getMaxListSetsSize() {
        return listSetsSize;
    }

    public boolean isIndentated() {
        return identation;
    }

}
