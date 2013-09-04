package com.lyncode.xoai.dataprovider.data.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lyncode.xoai.dataprovider.core.ListItemIdentifiersResult;
import com.lyncode.xoai.dataprovider.core.ListItemsResults;
import com.lyncode.xoai.dataprovider.core.XOAIContext;
import com.lyncode.xoai.dataprovider.data.AbstractItem;
import com.lyncode.xoai.dataprovider.data.AbstractItemRepository;
import com.lyncode.xoai.dataprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.dataprovider.exceptions.NoMetadataFormatsException;
import com.lyncode.xoai.dataprovider.filter.Filter;
import com.lyncode.xoai.dataprovider.filter.ScopedFilter;
import com.lyncode.xoai.dataprovider.filter.FilterScope;
import com.lyncode.xoai.serviceprovider.exceptions.CannotDisseminateFormatException;

public class ItemRepository {
	private AbstractItemRepository itemRepository;

	public ItemRepository(AbstractItemRepository itemRepository) {
		super();
		this.itemRepository = itemRepository;
	}
	
	/**
	 * Method used by XOAI internals.
	 * 
	 * @param context Requested Context <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">More details</a>
	 * @param offset Start offset
	 * @param length Max returned length
	 * @param metadataPrefix Metadata Prefix parameter
	 * 
	 * @return List of Identifiers
	 * @throws NoMetadataFormatsException
	 * @throws CannotDisseminateFormatException 
	 */
	public ListItemIdentifiersResult getItemIdentifiers(XOAIContext context,
			int offset, int length, String metadataPrefix)
			throws CannotDisseminateFormatException {
		List<ScopedFilter> filters = new ArrayList<ScopedFilter>();
		for (Filter f : context.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.Context));
		for (Filter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.MetadataFormat));
		return itemRepository.getItemIdentifiers(filters, offset, length);
	}

	/**
	 * Method used by XOAI internals.
	 * 
	 * @param context Requested Context <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">More details</a>
	 * @param offset Start offset
	 * @param length Max returned length
	 * @param metadataPrefix Metadata Prefix parameter 
	 * @param from From parameter
	 * 
	 * @return List of Identifiers
	 * @throws NoMetadataFormatsException
	 * @throws  
	 */
	public ListItemIdentifiersResult getItemIdentifiers(XOAIContext context,
			int offset, int length, String metadataPrefix, Date from)
			throws CannotDisseminateFormatException  {
		List<ScopedFilter> filters = new ArrayList<ScopedFilter>();
		for (Filter f : context.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.Context));
		for (Filter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.MetadataFormat));
		return itemRepository.getItemIdentifiers(filters, offset, length, from);
	}

	/**
	 * Method used by XOAI internals.
	 * 
	 * @param context Requested Context <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">More details</a>
	 * @param offset Start offset
	 * @param length Max returned length
	 * @param metadataPrefix Metadata Prefix parameter 
	 * @param until Date parameter
	 * 
	 * @return List of Identifiers
	 * @throws NoMetadataFormatsException
	 */
	public ListItemIdentifiersResult getItemIdentifiersUntil(
			XOAIContext context, int offset, int length, String metadataPrefix,
			Date until) throws CannotDisseminateFormatException {
		List<ScopedFilter> filters = new ArrayList<ScopedFilter>();
		for (Filter f : context.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.Context));
		for (Filter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.MetadataFormat));
		return itemRepository.getItemIdentifiersUntil(filters, offset, length, until);
	}

	/**
	 * Method used by XOAI internals.
	 * 
	 * @param context Requested Context <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">More details</a>
	 * @param offset Start offset
	 * @param length Max returned length
	 * @param metadataPrefix Metadata Prefix parameter 
	 * @param from Date parameter
	 * @param until Date parameter
	 * 
	 * @return List of Identifiers
	 * @throws NoMetadataFormatsException
	 */
	public ListItemIdentifiersResult getItemIdentifiers(XOAIContext context,
			int offset, int length, String metadataPrefix, Date from, Date until)
			throws CannotDisseminateFormatException {
		List<ScopedFilter> filters = new ArrayList<ScopedFilter>();
		for (Filter f : context.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.Context));
		for (Filter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.MetadataFormat));
		return itemRepository.getItemIdentifiers(filters, offset, length, from, until);
	}

	/**
	 * Method used by XOAI internals.
	 * 
	 * @param context Requested Context <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">More details</a>
	 * @param offset Start offset
	 * @param length Max returned length
	 * @param metadataPrefix Metadata Prefix parameter
	 * @param setSpec Set spec
	 * 
	 * @return List of Identifiers
	 * @throws NoMetadataFormatsException
	 */
	public ListItemIdentifiersResult getItemIdentifiers(XOAIContext context,
			int offset, int length, String metadataPrefix, String setSpec)
			throws CannotDisseminateFormatException {
		List<ScopedFilter> filters = new ArrayList<ScopedFilter>();
		for (Filter f : context.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.Context));
		for (Filter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.MetadataFormat));
		if (context.isStaticSet(setSpec)) {
			for (Filter f : context.getSetFilters(setSpec))
				filters.add(new ScopedFilter(f, FilterScope.Set));
			return itemRepository.getItemIdentifiers(filters, offset, length);
		} else
			return itemRepository.getItemIdentifiers(filters, offset, length, setSpec);
	}

	public ListItemIdentifiersResult getItemIdentifiers(XOAIContext context,
			int offset, int length, String metadataPrefix, String setSpec,
			Date from) throws CannotDisseminateFormatException {
		List<ScopedFilter> filters = new ArrayList<ScopedFilter>();
		for (Filter f : context.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.Context));
		for (Filter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.MetadataFormat));
		if (context.isStaticSet(setSpec)) {
			for (Filter f : context.getSetFilters(setSpec))
				filters.add(new ScopedFilter(f, FilterScope.Set));
			return itemRepository.getItemIdentifiers(filters, offset, length, from);
		} else
			return itemRepository.getItemIdentifiers(filters, offset, length, setSpec,
					from);
	}

	public ListItemIdentifiersResult getItemIdentifiersUntil(
			XOAIContext context, int offset, int length, String metadataPrefix,
			String setSpec, Date until) throws CannotDisseminateFormatException {
		List<ScopedFilter> filters = new ArrayList<ScopedFilter>();
		for (Filter f : context.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.Context));
		for (Filter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.MetadataFormat));
		if (context.isStaticSet(setSpec)) {
			for (Filter f : context.getSetFilters(setSpec))
				filters.add(new ScopedFilter(f, FilterScope.Set));
			return itemRepository.getItemIdentifiersUntil(filters, offset, length, until);
		} else
			return itemRepository.getItemIdentifiersUntil(filters, offset, length,
					setSpec, until);
	}

	public ListItemIdentifiersResult getItemIdentifiers(XOAIContext context,
			int offset, int length, String metadataPrefix, String setSpec,
			Date from, Date until) throws CannotDisseminateFormatException {
		List<ScopedFilter> filters = new ArrayList<ScopedFilter>();
		for (Filter f : context.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.Context));
		for (Filter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.MetadataFormat));
		if (context.isStaticSet(setSpec)) {
			for (Filter f : context.getSetFilters(setSpec))
				filters.add(new ScopedFilter(f, FilterScope.Set));
			return itemRepository
					.getItemIdentifiers(filters, offset, length, from, until);
		} else
			return itemRepository.getItemIdentifiers(filters, offset, length, setSpec,
					from, until);
	}

	public ListItemsResults getItems(XOAIContext context, int offset,
			int length, String metadataPrefix)
			throws CannotDisseminateFormatException {
		List<ScopedFilter> filters = new ArrayList<ScopedFilter>();
		for (Filter f : context.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.Context));
		for (Filter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.MetadataFormat));
		return itemRepository.getItems(filters, offset, length);
	}

	public ListItemsResults getItems(XOAIContext context, int offset,
			int length, String metadataPrefix, Date from)
			throws CannotDisseminateFormatException {
		List<ScopedFilter> filters = new ArrayList<ScopedFilter>();
		for (Filter f : context.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.Context));
		for (Filter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.MetadataFormat));
		return itemRepository.getItems(filters, offset, length, from);
	}

	public ListItemsResults getItemsUntil(XOAIContext context, int offset,
			int length, String metadataPrefix, Date until)
			throws CannotDisseminateFormatException {
		List<ScopedFilter> filters = new ArrayList<ScopedFilter>();
		for (Filter f : context.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.Context));
		for (Filter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.MetadataFormat));
		return itemRepository.getItemsUntil(filters, offset, length, until);
	}

	public ListItemsResults getItems(XOAIContext context, int offset,
			int length, String metadataPrefix, Date from, Date until)
			throws CannotDisseminateFormatException {
		List<ScopedFilter> filters = new ArrayList<ScopedFilter>();
		for (Filter f : context.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.Context));
		for (Filter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.MetadataFormat));
		return itemRepository.getItems(filters, offset, length, from, until);
	}

	public ListItemsResults getItems(XOAIContext context, int offset,
			int length, String metadataPrefix, String setSpec)
			throws CannotDisseminateFormatException {

		List<ScopedFilter> filters = new ArrayList<ScopedFilter>();
		for (Filter f : context.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.Context));
		for (Filter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.MetadataFormat));
		if (context.isStaticSet(setSpec)) {
			for (Filter f : context.getSetFilters(setSpec))
				filters.add(new ScopedFilter(f, FilterScope.Set));
			return itemRepository.getItems(filters, offset, length);
		} else
			return itemRepository.getItems(filters, offset, length, setSpec);
	}

	public ListItemsResults getItems(XOAIContext context, int offset,
			int length, String metadataPrefix, String setSpec, Date from)
			throws CannotDisseminateFormatException {
		List<ScopedFilter> filters = new ArrayList<ScopedFilter>();
		for (Filter f : context.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.Context));
		for (Filter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.MetadataFormat));
		if (context.isStaticSet(setSpec)) {
			for (Filter f : context.getSetFilters(setSpec))
				filters.add(new ScopedFilter(f, FilterScope.Set));
			return itemRepository.getItems(filters, offset, length, from);
		} else
			return itemRepository.getItems(filters, offset, length, setSpec, from);
	}

	public ListItemsResults getItemsUntil(XOAIContext context, int offset,
			int length, String metadataPrefix, String setSpec, Date until)
			throws CannotDisseminateFormatException {
		List<ScopedFilter> filters = new ArrayList<ScopedFilter>();
		for (Filter f : context.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.Context));
		for (Filter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.MetadataFormat));
		if (context.isStaticSet(setSpec)) {
			for (Filter f : context.getSetFilters(setSpec))
				filters.add(new ScopedFilter(f, FilterScope.Set));
			return itemRepository.getItemsUntil(filters, offset, length, until);
		} else
			return itemRepository.getItemsUntil(filters, offset, length, setSpec, until);
	}

	public ListItemsResults getItems(XOAIContext context, int offset,
			int length, String metadataPrefix, String setSpec, Date from,
			Date until) throws CannotDisseminateFormatException {
		List<ScopedFilter> filters = new ArrayList<ScopedFilter>();
		for (Filter f : context.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.Context));
		for (Filter f : context.getFormatByPrefix(metadataPrefix)
				.getFilters())
			filters.add(new ScopedFilter(f, FilterScope.MetadataFormat));
		if (context.isStaticSet(setSpec)) {
			for (Filter f : context.getSetFilters(setSpec))
				filters.add(new ScopedFilter(f, FilterScope.Set));
			return itemRepository.getItems(filters, offset, length, from, until);
		} else
			return itemRepository.getItems(filters, offset, length, setSpec, from, until);
	}

	public AbstractItem getItem(String identifier) throws IdDoesNotExistException {
		return itemRepository.getItem(identifier);
	}
}
