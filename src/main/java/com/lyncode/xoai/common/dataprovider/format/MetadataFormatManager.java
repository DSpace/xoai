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

package com.lyncode.xoai.common.format;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.xoai.common.data.AbstractItem;
import com.lyncode.xoai.common.data.AbstractMetadataFormat;
import com.lyncode.xoai.common.exceptions.BadArgumentException;
import com.lyncode.xoai.common.exceptions.ConfigurationException;
import com.lyncode.xoai.common.filter.AbstractFilter;
import com.lyncode.xoai.common.filter.FilterManager;
import com.lyncode.xoai.common.xml.xoaiconfig.BundleReference;
import com.lyncode.xoai.common.xml.xoaiconfig.Configuration.Formats;
import com.lyncode.xoai.common.xml.xoaiconfig.Configuration.Formats.Format;


/**
 * @author DSpace @ Lyncode
 * @version 1.0.1
 */
public class MetadataFormatManager {
    private static Logger log = LogManager.getLogger(MetadataFormatManager.class);
    private Map<String, AbstractMetadataFormat> _contexts;


    public MetadataFormatManager (Formats config, FilterManager fm) throws ConfigurationException {
        _contexts = new HashMap<String, AbstractMetadataFormat>();
        for (Format f : config.getFormat()) {
            try {
                Class<?> c = Class.forName(f.getClazz());
                Object obj = c.newInstance();
                List<AbstractFilter> list = new ArrayList<AbstractFilter>();
                for (BundleReference refid : f.getFilter()) {
                    if (!fm.filterExists(refid.getRefid()))
                        throw new ConfigurationException("Filter refered as "+refid.getRefid()+" does not exists");
                    list.add(fm.getFilter(refid.getRefid()));
                }
                if (obj instanceof AbstractMetadataFormat) {
                    ((AbstractMetadataFormat) obj).load(f.getPrefix(), f.getParameter());
                    ((AbstractMetadataFormat) obj).loadFilters(list);
                    _contexts.put(f.getId(), (AbstractMetadataFormat) obj);
                }
            } catch (ClassNotFoundException ex) {
                log.error(ex.getMessage(), ex);
            } catch (InstantiationException ex) {
                log.error(ex.getMessage(), ex);
            } catch (IllegalAccessException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }

    public boolean formatExists (String id) {
        return this._contexts.containsKey(id);
    }

    public AbstractMetadataFormat getFormat (String id) {
        return _contexts.get(id);
    }


    public List<AbstractMetadataFormat> getFormats (AbstractItem item) {
        List<AbstractMetadataFormat> formats = new ArrayList<AbstractMetadataFormat>();
        for (AbstractMetadataFormat format : _contexts.values())
            if (format.isApplyable(item))
                formats.add(format);
        return formats;
    }
    
    public AbstractMetadataFormat getFormatByPrefix (String prefix) throws BadArgumentException {
        for (AbstractMetadataFormat f : _contexts.values())
            if (f.getPrefix().equals(prefix))
                return f;
        throw new BadArgumentException("There is no metadata schema with the given metadataPrefix");
    }
}
