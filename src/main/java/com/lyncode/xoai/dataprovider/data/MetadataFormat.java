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

package com.lyncode.xoai.dataprovider.data;

import java.io.File;
import java.util.List;

import com.lyncode.xoai.dataprovider.core.ConfigurableBundle;
import com.lyncode.xoai.dataprovider.filter.AbstractFilter;

/**
 * @author DSpace @ Lyncode
 * @version 2.2.1
 */
public class MetadataFormat extends ConfigurableBundle {
	private String prefix;
	private File xsltFile;
	private String namespace;
	private String schemaLocation;
	private List<AbstractFilter> _list;

	public MetadataFormat(String prefix, File xsltFile, String namespace,
			String schemaLocation) {
		this.prefix = prefix;
		this.xsltFile = xsltFile;
		this.namespace = namespace;
		this.schemaLocation = schemaLocation;
	}

	public void loadFilters(List<AbstractFilter> filters) {
		this._list = filters;
	}

	public List<AbstractFilter> getFilters() {
		return _list;
	}

	public String getPrefix() {
		return prefix;
	}

	public File getXSLTFile() {
		return xsltFile;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getSchemaLocation() {
		return schemaLocation;
	}

	public boolean isApplyable(AbstractItemIdentifier item) {
		if (item.isDeleted())
			return true;
		for (AbstractFilter filter : this.getFilters())
			if (!filter.isItemShown(item))
				return false;
		return true;
	}
}
