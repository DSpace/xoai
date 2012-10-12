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

import java.util.Date;
import java.util.List;

import com.lyncode.xoai.dataprovider.core.ListItemIdentifiersResult;
import com.lyncode.xoai.dataprovider.core.ListItemsResults;
import com.lyncode.xoai.dataprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.dataprovider.filter.Filter;

/**
 * This class wraps the data source of items.
 * 
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.7
 */
public abstract class AbstractItemRepository {
	/**
	 * Gets an item from the data source.
	 * 
	 * @param identifier Unique identifier of the item
	 * 
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#UniqueIdentifier">Unique identifier definition</a>
	 * @return Item
	 * @throws IdDoesNotExistException
	 */
	public abstract AbstractItem getItem(String identifier)
			throws IdDoesNotExistException;

	/**
	 * Gets a paged list of identifiers. The metadata prefix parameter is internally converted to a list of filters.
	 * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
	 * 
	 * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
	 * @param offset Start offset
	 * @param length Max items returned
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#ListIdentifiers">List Identifiers definition</a>
	 * @return List of identifiers
	 */
	public abstract ListItemIdentifiersResult getItemIdentifiers(
			List<Filter> filters, int offset, int length);

	/**
	 * Gets a paged list of identifiers. The metadata prefix parameter is internally converted to a list of filters.
	 * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
	 * 
	 * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
	 * @param offset Start offset
	 * @param length Max items returned
	 * @param from Date parameter
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#ListIdentifiers">List Identifiers definition</a>
	 * @return List of identifiers
	 */
	public abstract ListItemIdentifiersResult getItemIdentifiers(
			List<Filter> filters, int offset, int length, Date from);

	/**
	 * Gets a paged list of identifiers. The metadata prefix parameter is internally converted to a list of filters.
	 * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
	 * 
	 * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
	 * @param offset Start offset
	 * @param length Max items returned
	 * @param until Date parameter
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#ListIdentifiers">List Identifiers definition</a>
	 * @return List of identifiers
	 */
	public abstract ListItemIdentifiersResult getItemIdentifiersUntil(
			List<Filter> filters, int offset, int length, Date until);

	/**
	 * Gets a paged list of identifiers. The metadata prefix parameter is internally converted to a list of filters.
	 * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
	 * 
	 * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
	 * @param offset Start offset
	 * @param length Max items returned
	 * @param from Date parameter
	 * @param until Date parameter
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#ListIdentifiers">List Identifiers definition</a>
	 * @return List of identifiers
	 */
	public abstract ListItemIdentifiersResult getItemIdentifiers(
			List<Filter> filters, int offset, int length, Date from, Date until);

	/**
	 * Gets a paged list of identifiers. The metadata prefix parameter is internally converted to a list of filters.
	 * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
	 * 
	 * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
	 * @param offset Start offset
	 * @param length Max items returned
	 * @param setSpec Set Spec
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#ListIdentifiers">List Identifiers definition</a>
	 * @return List of identifiers
	 */
	public abstract ListItemIdentifiersResult getItemIdentifiers(
			List<Filter> filters, int offset, int length, String setSpec);

	/**
	 * Gets a paged list of identifiers. The metadata prefix parameter is internally converted to a list of filters.
	 * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
	 * 
	 * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
	 * @param offset Start offset
	 * @param length Max items returned
	 * @param setSpec Set Spec
	 * @param from Date parameter
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#ListIdentifiers">List Identifiers definition</a>
	 * @return List of identifiers
	 */
	public abstract ListItemIdentifiersResult getItemIdentifiers(
			List<Filter> filters, int offset, int length, String setSpec,
			Date from);

	/**
	 * Gets a paged list of identifiers. The metadata prefix parameter is internally converted to a list of filters.
	 * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
	 * 
	 * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
	 * @param offset Start offset
	 * @param length Max items returned
	 * @param setSpec Set Spec
	 * @param until Date parameter
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#ListIdentifiers">List Identifiers definition</a>
	 * @return List of identifiers
	 */
	public abstract ListItemIdentifiersResult getItemIdentifiersUntil(
			List<Filter> filters, int offset, int length, String setSpec,
			Date until);

	/**
	 * Gets a paged list of identifiers. The metadata prefix parameter is internally converted to a list of filters.
	 * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
	 * 
	 * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
	 * @param offset Start offset
	 * @param length Max items returned
	 * @param setSpec Set Spec
	 * @param from Date parameter
	 * @param until Date parameter
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#ListIdentifiers">List Identifiers definition</a>
	 * @return List of identifiers
	 */
	public abstract ListItemIdentifiersResult getItemIdentifiers(
			List<Filter> filters, int offset, int length, String setSpec,
			Date from, Date until);

	/**
	 * Gets a paged list of items. The metadata prefix parameter is internally converted to a list of filters.
	 * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
	 * 
	 * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
	 * @param offset Start offset
	 * @param length Max items returned
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#ListRecords">List Records Definition</a>
	 * @return List of Items
	 */
	public abstract ListItemsResults getItems(List<Filter> filters,
			int offset, int length);

	/**
	 * Gets a paged list of items. The metadata prefix parameter is internally converted to a list of filters.
	 * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
	 * 
	 * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
	 * @param offset Start offset
	 * @param length Max items returned
	 * @param from Date parameter
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#ListRecords">List Records Definition</a>
	 * @return List of Items
	 */
	public abstract ListItemsResults getItems(List<Filter> filters,
			int offset, int length, Date from);

	/**
	 * Gets a paged list of items. The metadata prefix parameter is internally converted to a list of filters.
	 * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
	 * 
	 * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
	 * @param offset Start offset
	 * @param length Max items returned
	 * @param until Date parameter
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#ListRecords">List Records Definition</a>
	 * @return List of Items
	 */
	public abstract ListItemsResults getItemsUntil(List<Filter> filters,
			int offset, int length, Date until);

	/**
	 * Gets a paged list of items. The metadata prefix parameter is internally converted to a list of filters.
	 * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
	 * 
	 * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
	 * @param offset Start offset
	 * @param length Max items returned
	 * @param from Date parameter
	 * @param until Date parameter
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#ListRecords">List Records Definition</a>
	 * @return List of Items
	 */
	public abstract ListItemsResults getItems(List<Filter> filters,
			int offset, int length, Date from, Date until);

	/**
	 * Gets a paged list of items. The metadata prefix parameter is internally converted to a list of filters.
	 * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
	 * 
	 * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
	 * @param offset Start offset
	 * @param length Max items returned
	 * @param setSpec Set spec
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#ListRecords">List Records Definition</a>
	 * @return List of Items
	 */
	public abstract ListItemsResults getItems(List<Filter> filters,
			int offset, int length, String setSpec);

	/**
	 * Gets a paged list of items. The metadata prefix parameter is internally converted to a list of filters.
	 * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
	 * 
	 * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
	 * @param offset Start offset
	 * @param length Max items returned
	 * @param from Date parameter
	 * @param setSpec Set spec
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#ListRecords">List Records Definition</a>
	 * @return List of Items
	 */
	public abstract ListItemsResults getItems(List<Filter> filters,
			int offset, int length, String setSpec, Date from);

	/**
	 * Gets a paged list of items. The metadata prefix parameter is internally converted to a list of filters.
	 * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
	 * 
	 * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
	 * @param offset Start offset
	 * @param length Max items returned
	 * @param until Date parameter
	 * @param setSpec Set spec
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#ListRecords">List Records Definition</a>
	 * @return List of Items
	 */
	public abstract ListItemsResults getItemsUntil(List<Filter> filters,
			int offset, int length, String setSpec, Date until);

	/**
	 * Gets a paged list of items. The metadata prefix parameter is internally converted to a list of filters.
	 * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
	 * 
	 * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
	 * @param offset Start offset
	 * @param length Max items returned
	 * @param from Date parameter
	 * @param until Date parameter
	 * @param setSpec Set spec
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#ListRecords">List Records Definition</a>
	 * @return List of Items
	 */
	public abstract ListItemsResults getItems(List<Filter> filters,
			int offset, int length, String setSpec, Date from, Date until);

}
