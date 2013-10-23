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

package com.lyncode.xoai.dataprovider.format;

import com.lyncode.xoai.dataprovider.data.AbstractItem;
import com.lyncode.xoai.dataprovider.data.MetadataFormat;
import com.lyncode.xoai.dataprovider.exceptions.BadArgumentException;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.filter.Filter;
import com.lyncode.xoai.dataprovider.filter.FilterManager;
import com.lyncode.xoai.dataprovider.services.api.ResourceResolver;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.BundleReference;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration.Formats;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration.Formats.Format;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 3.1.0
 */
public class MetadataFormatManager {

    private static Logger log = LogManager.getLogger(MetadataFormatManager.class);
    private static TransformerFactory tFactory = TransformerFactory.newInstance();

    private Map<String, MetadataFormat> _contexts;

    public MetadataFormatManager(ResourceResolver resolver, Formats config,
                                 FilterManager fm) throws ConfigurationException {
        _contexts = new HashMap<String, MetadataFormat>();
        for (Format f : config.getFormat()) {
            Transformer transformer = null;

            try {
                transformer = tFactory.newTransformer(new StreamSource(resolver.getResource(f.getXSLT())));
            } catch (TransformerConfigurationException e) {
                throw new ConfigurationException(e.getMessage(), e);
            } catch (IOException e) {
                throw new ConfigurationException(e.getMessage(), e);
            }

            MetadataFormat mdf = new MetadataFormat(f.getPrefix(), transformer, f.getNamespace(), f.getSchemaLocation());
            List<Filter> list = new ArrayList<Filter>();
            for (BundleReference refid : f.getFilter()) {
                if (!fm.filterExists(refid.getRefid()))
                    throw new ConfigurationException("ScopedFilter referred as "
                            + refid.getRefid() + " does not exist");
                list.add(fm.getFilter(refid.getRefid()));
            }
            mdf.loadFilters(list);
            _contexts.put(f.getId(), mdf);
        }
    }

    public boolean formatExists(String id) {
        return this._contexts.containsKey(id);
    }

    public MetadataFormat getFormat(String id) {
        return _contexts.get(id);
    }

    public List<MetadataFormat> getFormats(AbstractItem item) {
        List<MetadataFormat> formats = new ArrayList<MetadataFormat>();
        for (MetadataFormat format : _contexts.values())
            if (format.isApplyable(item))
                formats.add(format);
        return formats;
    }

    public MetadataFormat getFormatByPrefix(String prefix)
            throws BadArgumentException {
        for (MetadataFormat f : _contexts.values())
            if (f.getPrefix().equals(prefix))
                return f;
        throw new BadArgumentException(
                "There is no metadata schema with the given metadataPrefix");
    }
}
