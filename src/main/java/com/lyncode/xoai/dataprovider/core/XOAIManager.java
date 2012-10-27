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

import java.io.File;

import com.lyncode.xoai.dataprovider.configuration.ConfigurationManager;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.filter.FilterManager;
import com.lyncode.xoai.dataprovider.format.MetadataFormatManager;
import com.lyncode.xoai.dataprovider.sets.StaticSetManager;
import com.lyncode.xoai.dataprovider.transform.TransformManager;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */
public class XOAIManager {
	private static final String XOAI_CONFIG = "xoai.xml";

	private static XOAIManager _manager = null;

	public static XOAIManager getManager() {
		return _manager;
	}

	public static void initialize(String baseDir) throws ConfigurationException {
		String configFile = (baseDir.endsWith(File.separator) ? baseDir
				: baseDir + File.separator) + XOAI_CONFIG;
		Configuration config = ConfigurationManager
				.readConfiguration(configFile);
		_manager = new XOAIManager(baseDir, config);
	}

	private FilterManager _filter;
	private ContextManager _context;
	private TransformManager _transformer;
	private MetadataFormatManager _format;
	private StaticSetManager _set;
	private int _listSetsSize;
	private int _listRecordsSize;
	private int _listIdentifiersSize;
	private boolean _identation;
	private String styleSheet;

	private XOAIManager(String baseDir, Configuration config)
			throws ConfigurationException {
		_filter = new FilterManager(config.getFilters());
		_transformer = new TransformManager(baseDir, config.getTransformers());
		_format = new MetadataFormatManager(baseDir, config.getFormats(),
				_filter);
		_set = new StaticSetManager(config.getSets(), _filter);
		_listSetsSize = config.getMaxListSetsSize();
		_listIdentifiersSize = config.getMaxListRecordsSize();
		_listRecordsSize = config.getMaxListRecordsSize();
		_identation = config.isIdentation();
		styleSheet = config.getStylesheet();
		_context = new ContextManager(config.getContexts(), _filter,
				_transformer, _format, _set);
	}

	public boolean hasStyleSheet () {
		return (styleSheet != null);
	}
	
	public String getStyleSheet () {
		return styleSheet;
	}
	
	public ContextManager getContextManager() {
		return _context;
	}

	public FilterManager getFilterManager() {
		return _filter;
	}

	public MetadataFormatManager getFormatManager() {
		return _format;
	}

	public StaticSetManager getSetManager() {
		return _set;
	}

	public TransformManager getTransformerManager() {
		return _transformer;
	}

	public int getMaxListIdentifiersSize() {
		return _listIdentifiersSize;
	}

	public int getMaxListRecordsSize() {
		return _listRecordsSize;
	}

	public int getMaxListSetsSize() {
		return _listSetsSize;
	}

	public boolean isIdentated() {
		return _identation;
	}

}
