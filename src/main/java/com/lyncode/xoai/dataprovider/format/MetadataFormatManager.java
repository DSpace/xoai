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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.xoai.dataprovider.data.AbstractItem;
import com.lyncode.xoai.dataprovider.data.MetadataFormat;
import com.lyncode.xoai.dataprovider.exceptions.BadArgumentException;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.filter.AbstractFilter;
import com.lyncode.xoai.dataprovider.filter.FilterManager;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.BundleReference;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration.Formats;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration.Formats.Format;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.2
 */
public class MetadataFormatManager {
	private static Logger log = LogManager
			.getLogger(MetadataFormatManager.class);
	private Map<String, MetadataFormat> _contexts;

	public MetadataFormatManager(String baseDir, Formats config,
			FilterManager fm) throws ConfigurationException {
		_contexts = new HashMap<String, MetadataFormat>();
		for (Format f : config.getFormat()) {
			String xsltFilepath = (baseDir.endsWith(File.separator) ? baseDir
					: baseDir + File.separator) + f.getXSLT();
			File xsltFile = new File(xsltFilepath);

			log.trace("[XOAI] XSLT File: " + xsltFilepath);

			MetadataFormat mdf = new MetadataFormat(f.getPrefix(), xsltFile,
					f.getNamespace(), f.getSchemaLocation());
			List<AbstractFilter> list = new ArrayList<AbstractFilter>();
			for (BundleReference refid : f.getFilter()) {
				if (!fm.filterExists(refid.getRefid()))
					throw new ConfigurationException("Filter referred as "
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
