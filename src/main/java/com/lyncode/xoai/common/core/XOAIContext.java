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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.xoai.common.data.AbstractItem;
import com.lyncode.xoai.common.data.AbstractItemIdentifier;
import com.lyncode.xoai.common.data.AbstractMetadataFormat;
import com.lyncode.xoai.common.exceptions.NoMetadataFormatsException;
import com.lyncode.xoai.common.filter.AbstractFilter;
import com.lyncode.xoai.common.sets.StaticSet;
import com.lyncode.xoai.common.transform.AbstractTransformer;


/**
 * @author DSpace @ Lyncode
 * @version 1.0.1
 */
public class XOAIContext extends ConfigurableBundle {
    private static Logger log = LogManager.getLogger(XOAIContext.class);
    private String _baseurl;
    private List<AbstractFilter> _filters;
    private Map<String, StaticSet> _sets;
    private AbstractTransformer _transformer;
    private Map<String, AbstractMetadataFormat> _formats;

    public XOAIContext(String baseurl, AbstractTransformer transformer, List<AbstractFilter> filters, List<AbstractMetadataFormat> formats, List<StaticSet> sets) {
        _baseurl = baseurl;
        _transformer = transformer;
        _filters = filters;
        _formats = new HashMap<String, AbstractMetadataFormat>();
        for (AbstractMetadataFormat mdf : formats)
            _formats.put(mdf.getPrefix(), mdf);
        _sets = new HashMap<String, StaticSet>();
        for (StaticSet s : sets)
            _sets.put(s.getSetSpec(), s);
    }
    
    public String getBaseUrl () {
    	return this._baseurl;
    }
    
    public List<AbstractFilter> getFilters() {
        return _filters;
    }

    public AbstractTransformer getTransformer() {
        return _transformer;
    }

    public List<StaticSet> getStaticSets() {
        log.debug("{ XOAI } Static Sets for this Context: "+_sets.values().size());
        return new ArrayList<StaticSet>(_sets.values());
    }

    public List<AbstractFilter> getSetFilters (String setID) {
        log.debug("{ XOAI } Getting StaticSet filters");
        return _sets.get(setID).getFilters();
    }

    public AbstractMetadataFormat getFormatByPrefix (String prefix) throws NoMetadataFormatsException {
        for (AbstractMetadataFormat format : this._formats.values())
            if (format.getPrefix().equals(prefix))
                return format;
        throw new NoMetadataFormatsException();
    }

    public List<AbstractMetadataFormat> getFormats () {
        return new ArrayList<AbstractMetadataFormat>(_formats.values());
    }

    public List<AbstractMetadataFormat> getFormats (AbstractItem item) {
        List<AbstractMetadataFormat> formats = new ArrayList<AbstractMetadataFormat>();
        if (this.isItemShown(item)) {
            for (AbstractMetadataFormat format : _formats.values())
                if (item.isDeleted() || format.isApplyable(item))
                    formats.add(format);
        }
        return formats;
    }

    public boolean isItemShown (AbstractItemIdentifier item) {
        boolean shown = true;
        for (AbstractFilter f : this.getFilters())
            if (!f.isItemShown(item))
                shown = false;
        return shown;
    }

    public boolean isStaticSet (String setSpec) {
        for (StaticSet s : this.getStaticSets())
            if (s.getSetSpec().equals(setSpec))
                return true;
        return false;
    }
}
