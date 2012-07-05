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

package com.lyncode.xoai.common.dataprovider.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lyncode.xoai.common.dataprovider.core.ListItemIdentifiersResult;
import com.lyncode.xoai.common.dataprovider.core.ListItemsResults;
import com.lyncode.xoai.common.dataprovider.core.XOAIContext;
import com.lyncode.xoai.common.dataprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.common.dataprovider.exceptions.NoMetadataFormatsException;
import com.lyncode.xoai.common.dataprovider.filter.AbstractFilter;
import com.lyncode.xoai.common.dataprovider.filter.Filter;
import com.lyncode.xoai.common.dataprovider.filter.FilterScope;

/**
 * @author DSpace @ Lyncode
 * @version 2.1.0
 */
public abstract class AbstractItemRepository {
	// private static Logger log =
	// LogManager.getLogger(AbstractItemRepository.class);
	public abstract AbstractItem getItem(String identifier)
			throws IdDoesNotExistException;

	public ListItemIdentifiersResult getItemIdentifiers(XOAIContext context,
			int offset, int length, String metadataPrefix)
			throws NoMetadataFormatsException {
		List<Filter> filters = new ArrayList<Filter>();
		for (AbstractFilter f : context.getFilters())
			filters.add(new Filter(f, FilterScope.Context));
		for (AbstractFilter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new Filter(f, FilterScope.MetadataFormat));
		return this.getItemIdentifiers(filters, offset, length);
	}

	public ListItemIdentifiersResult getItemIdentifiers(XOAIContext context,
			int offset, int length, String metadataPrefix, Date from)
			throws NoMetadataFormatsException {
		List<Filter> filters = new ArrayList<Filter>();
		for (AbstractFilter f : context.getFilters())
			filters.add(new Filter(f, FilterScope.Context));
		for (AbstractFilter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new Filter(f, FilterScope.MetadataFormat));
		return this.getItemIdentifiers(filters, offset, length, from);
	}

	public ListItemIdentifiersResult getItemIdentifiersUntil(
			XOAIContext context, int offset, int length, String metadataPrefix,
			Date until) throws NoMetadataFormatsException {
		List<Filter> filters = new ArrayList<Filter>();
		for (AbstractFilter f : context.getFilters())
			filters.add(new Filter(f, FilterScope.Context));
		for (AbstractFilter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new Filter(f, FilterScope.MetadataFormat));
		return this.getItemIdentifiersUntil(filters, offset, length, until);
	}

	public ListItemIdentifiersResult getItemIdentifiers(XOAIContext context,
			int offset, int length, String metadataPrefix, Date from, Date until)
			throws NoMetadataFormatsException {
		List<Filter> filters = new ArrayList<Filter>();
		for (AbstractFilter f : context.getFilters())
			filters.add(new Filter(f, FilterScope.Context));
		for (AbstractFilter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new Filter(f, FilterScope.MetadataFormat));
		return this.getItemIdentifiers(filters, offset, length, from, until);
	}

	public ListItemIdentifiersResult getItemIdentifiers(XOAIContext context,
			int offset, int length, String metadataPrefix, String setSpec)
			throws NoMetadataFormatsException {
		List<Filter> filters = new ArrayList<Filter>();
		for (AbstractFilter f : context.getFilters())
			filters.add(new Filter(f, FilterScope.Context));
		for (AbstractFilter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new Filter(f, FilterScope.MetadataFormat));
		if (context.isStaticSet(setSpec)) {
			for (AbstractFilter f : context.getSetFilters(setSpec))
				filters.add(new Filter(f, FilterScope.Set));
			return this.getItemIdentifiers(filters, offset, length);
		} else
			return this.getItemIdentifiers(filters, offset, length, setSpec);
	}

	public ListItemIdentifiersResult getItemIdentifiers(XOAIContext context,
			int offset, int length, String metadataPrefix, String setSpec,
			Date from) throws NoMetadataFormatsException {
		List<Filter> filters = new ArrayList<Filter>();
		for (AbstractFilter f : context.getFilters())
			filters.add(new Filter(f, FilterScope.Context));
		for (AbstractFilter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new Filter(f, FilterScope.MetadataFormat));
		if (context.isStaticSet(setSpec)) {
			for (AbstractFilter f : context.getSetFilters(setSpec))
				filters.add(new Filter(f, FilterScope.Set));
			return this.getItemIdentifiers(filters, offset, length, from);
		} else
			return this.getItemIdentifiers(filters, offset, length, setSpec,
					from);
	}

	public ListItemIdentifiersResult getItemIdentifiersUntil(
			XOAIContext context, int offset, int length, String metadataPrefix,
			String setSpec, Date until) throws NoMetadataFormatsException {
		List<Filter> filters = new ArrayList<Filter>();
		for (AbstractFilter f : context.getFilters())
			filters.add(new Filter(f, FilterScope.Context));
		for (AbstractFilter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new Filter(f, FilterScope.MetadataFormat));
		if (context.isStaticSet(setSpec)) {
			for (AbstractFilter f : context.getSetFilters(setSpec))
				filters.add(new Filter(f, FilterScope.Set));
			return this.getItemIdentifiersUntil(filters, offset, length, until);
		} else
			return this.getItemIdentifiersUntil(filters, offset, length,
					setSpec, until);
	}

	public ListItemIdentifiersResult getItemIdentifiers(XOAIContext context,
			int offset, int length, String metadataPrefix, String setSpec,
			Date from, Date until) throws NoMetadataFormatsException {
		List<Filter> filters = new ArrayList<Filter>();
		for (AbstractFilter f : context.getFilters())
			filters.add(new Filter(f, FilterScope.Context));
		for (AbstractFilter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new Filter(f, FilterScope.MetadataFormat));
		if (context.isStaticSet(setSpec)) {
			for (AbstractFilter f : context.getSetFilters(setSpec))
				filters.add(new Filter(f, FilterScope.Set));
			return this
					.getItemIdentifiers(filters, offset, length, from, until);
		} else
			return this.getItemIdentifiers(filters, offset, length, setSpec,
					from, until);
	}

	public ListItemsResults getItems(XOAIContext context, int offset,
			int length, String metadataPrefix)
			throws NoMetadataFormatsException {
		List<Filter> filters = new ArrayList<Filter>();
		for (AbstractFilter f : context.getFilters())
			filters.add(new Filter(f, FilterScope.Context));
		for (AbstractFilter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new Filter(f, FilterScope.MetadataFormat));
		return this.getItems(filters, offset, length);
	}

	public ListItemsResults getItems(XOAIContext context, int offset,
			int length, String metadataPrefix, Date from)
			throws NoMetadataFormatsException {
		List<Filter> filters = new ArrayList<Filter>();
		for (AbstractFilter f : context.getFilters())
			filters.add(new Filter(f, FilterScope.Context));
		for (AbstractFilter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new Filter(f, FilterScope.MetadataFormat));
		return this.getItems(filters, offset, length, from);
	}

	public ListItemsResults getItemsUntil(XOAIContext context, int offset,
			int length, String metadataPrefix, Date until)
			throws NoMetadataFormatsException {
		List<Filter> filters = new ArrayList<Filter>();
		for (AbstractFilter f : context.getFilters())
			filters.add(new Filter(f, FilterScope.Context));
		for (AbstractFilter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new Filter(f, FilterScope.MetadataFormat));
		return this.getItemsUntil(filters, offset, length, until);
	}

	public ListItemsResults getItems(XOAIContext context, int offset,
			int length, String metadataPrefix, Date from, Date until)
			throws NoMetadataFormatsException {
		List<Filter> filters = new ArrayList<Filter>();
		for (AbstractFilter f : context.getFilters())
			filters.add(new Filter(f, FilterScope.Context));
		for (AbstractFilter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new Filter(f, FilterScope.MetadataFormat));
		return this.getItems(filters, offset, length, from, until);
	}

	public ListItemsResults getItems(XOAIContext context, int offset,
			int length, String metadataPrefix, String setSpec)
			throws NoMetadataFormatsException {

		List<Filter> filters = new ArrayList<Filter>();
		for (AbstractFilter f : context.getFilters())
			filters.add(new Filter(f, FilterScope.Context));
		for (AbstractFilter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new Filter(f, FilterScope.MetadataFormat));
		if (context.isStaticSet(setSpec)) {
			for (AbstractFilter f : context.getSetFilters(setSpec))
				filters.add(new Filter(f, FilterScope.Set));
			return this.getItems(filters, offset, length);
		} else
			return this.getItems(filters, offset, length, setSpec);
	}

	public ListItemsResults getItems(XOAIContext context, int offset,
			int length, String metadataPrefix, String setSpec, Date from)
			throws NoMetadataFormatsException {
		List<Filter> filters = new ArrayList<Filter>();
		for (AbstractFilter f : context.getFilters())
			filters.add(new Filter(f, FilterScope.Context));
		for (AbstractFilter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new Filter(f, FilterScope.MetadataFormat));
		if (context.isStaticSet(setSpec)) {
			for (AbstractFilter f : context.getSetFilters(setSpec))
				filters.add(new Filter(f, FilterScope.Set));
			return this.getItems(filters, offset, length, from);
		} else
			return this.getItems(filters, offset, length, setSpec, from);
	}

	public ListItemsResults getItemsUntil(XOAIContext context, int offset,
			int length, String metadataPrefix, String setSpec, Date until)
			throws NoMetadataFormatsException {
		List<Filter> filters = new ArrayList<Filter>();
		for (AbstractFilter f : context.getFilters())
			filters.add(new Filter(f, FilterScope.Context));
		for (AbstractFilter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new Filter(f, FilterScope.MetadataFormat));
		if (context.isStaticSet(setSpec)) {
			for (AbstractFilter f : context.getSetFilters(setSpec))
				filters.add(new Filter(f, FilterScope.Set));
			return this.getItemsUntil(filters, offset, length, until);
		} else
			return this.getItemsUntil(filters, offset, length, setSpec, until);
	}

	public ListItemsResults getItems(XOAIContext context, int offset,
			int length, String metadataPrefix, String setSpec, Date from,
			Date until) throws NoMetadataFormatsException {
		List<Filter> filters = new ArrayList<Filter>();
		for (AbstractFilter f : context.getFilters())
			filters.add(new Filter(f, FilterScope.Context));
		for (AbstractFilter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new Filter(f, FilterScope.MetadataFormat));
		if (context.isStaticSet(setSpec)) {
			for (AbstractFilter f : context.getSetFilters(setSpec))
				filters.add(new Filter(f, FilterScope.Set));
			return this.getItems(filters, offset, length, from, until);
		} else
			return this.getItems(filters, offset, length, setSpec, from, until);
	}

	protected abstract ListItemIdentifiersResult getItemIdentifiers(
			List<Filter> filters, int offset, int length);

	protected abstract ListItemIdentifiersResult getItemIdentifiers(
			List<Filter> filters, int offset, int length, Date from);

	protected abstract ListItemIdentifiersResult getItemIdentifiersUntil(
			List<Filter> filters, int offset, int length, Date until);

	protected abstract ListItemIdentifiersResult getItemIdentifiers(
			List<Filter> filters, int offset, int length, Date from, Date until);

	protected abstract ListItemIdentifiersResult getItemIdentifiers(
			List<Filter> filters, int offset, int length, String setSpec);

	protected abstract ListItemIdentifiersResult getItemIdentifiers(
			List<Filter> filters, int offset, int length, String setSpec,
			Date from);

	protected abstract ListItemIdentifiersResult getItemIdentifiersUntil(
			List<Filter> filters, int offset, int length, String setSpec,
			Date until);

	protected abstract ListItemIdentifiersResult getItemIdentifiers(
			List<Filter> filters, int offset, int length, String setSpec,
			Date from, Date until);

	protected abstract ListItemsResults getItems(List<Filter> filters,
			int offset, int length);

	protected abstract ListItemsResults getItems(List<Filter> filters,
			int offset, int length, Date from);

	protected abstract ListItemsResults getItemsUntil(List<Filter> filters,
			int offset, int length, Date until);

	protected abstract ListItemsResults getItems(List<Filter> filters,
			int offset, int length, Date from, Date until);

	protected abstract ListItemsResults getItems(List<Filter> filters,
			int offset, int length, String setSpec);

	protected abstract ListItemsResults getItems(List<Filter> filters,
			int offset, int length, String setSpec, Date from);

	protected abstract ListItemsResults getItemsUntil(List<Filter> filters,
			int offset, int length, String setSpec, Date from);

	protected abstract ListItemsResults getItems(List<Filter> filters,
			int offset, int length, String setSpec, Date from, Date until);

}
