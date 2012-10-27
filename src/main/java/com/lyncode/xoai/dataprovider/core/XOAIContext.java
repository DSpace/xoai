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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.xoai.dataprovider.data.AbstractItem;
import com.lyncode.xoai.dataprovider.data.AbstractItemIdentifier;
import com.lyncode.xoai.dataprovider.data.MetadataFormat;
import com.lyncode.xoai.dataprovider.data.MetadataTransformer;
import com.lyncode.xoai.dataprovider.exceptions.NoMetadataFormatsException;
import com.lyncode.xoai.dataprovider.filter.AbstractFilter;
import com.lyncode.xoai.dataprovider.sets.StaticSet;
import com.lyncode.xoai.serviceprovider.exceptions.CannotDisseminateFormatException;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */
public class XOAIContext extends ConfigurableBundle {
	private static Logger log = LogManager.getLogger(XOAIContext.class);
	private String _baseurl;
	private List<AbstractFilter> _filters;
	private Map<String, StaticSet> _sets;
	private MetadataTransformer _transformer;
	private Map<String, MetadataFormat> _formats;

	public XOAIContext(String baseurl, MetadataTransformer transformer,
			List<AbstractFilter> filters, List<MetadataFormat> formats,
			List<StaticSet> sets) {
		_baseurl = baseurl;
		_transformer = transformer;
		_filters = filters;
		_formats = new HashMap<String, MetadataFormat>();
		for (MetadataFormat mdf : formats)
			_formats.put(mdf.getPrefix(), mdf);
		_sets = new HashMap<String, StaticSet>();
		for (StaticSet s : sets)
			_sets.put(s.getSetSpec(), s);
	}

	public String getBaseUrl() {
		return this._baseurl;
	}

	public List<AbstractFilter> getFilters() {
		return _filters;
	}

	public MetadataTransformer getTransformer() {
		return _transformer;
	}

	private List<StaticSet> cachedSets = null;
	public List<StaticSet> getStaticSets() {
		if (cachedSets == null) {
			log.debug("{ XOAI } Static Sets for this Context: "
					+ _sets.values().size());
			cachedSets = new ArrayList<StaticSet>(_sets.values());
		}
		return cachedSets;
	}

	public List<AbstractFilter> getSetFilters(String setID) {
		log.debug("{ XOAI } Getting StaticSet filters");
		return _sets.get(setID).getFilters();
	}

	public MetadataFormat getFormatByPrefix(String prefix)
			throws CannotDisseminateFormatException {
		for (MetadataFormat format : this._formats.values())
			if (format.getPrefix().equals(prefix))
				return format;
		throw new CannotDisseminateFormatException(prefix);
	}

	public List<MetadataFormat> getFormats() {
		return new ArrayList<MetadataFormat>(_formats.values());
	}

	public List<MetadataFormat> getFormats(AbstractItem item) {
		List<MetadataFormat> formats = new ArrayList<MetadataFormat>();
		if (this.isItemShown(item)) {
			for (MetadataFormat format : _formats.values())
				if (item.isDeleted() || format.isApplyable(item))
					formats.add(format);
		}
		return formats;
	}

	public boolean isItemShown(AbstractItemIdentifier item) {
		boolean shown = true;
		for (AbstractFilter f : this.getFilters())
			if (!f.isItemShown(item))
				shown = false;
		return shown;
	}

	public boolean isStaticSet(String setSpec) {
		for (StaticSet s : this.getStaticSets())
			if (s.getSetSpec().equals(setSpec))
				return true;
		return false;
	}
}
