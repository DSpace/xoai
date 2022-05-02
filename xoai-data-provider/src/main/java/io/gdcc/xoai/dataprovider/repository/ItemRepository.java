/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package io.gdcc.xoai.dataprovider.repository;

import io.gdcc.xoai.dataprovider.model.Item;
import io.gdcc.xoai.dataprovider.exceptions.IdDoesNotExistException;
import io.gdcc.xoai.dataprovider.exceptions.OAIException;
import io.gdcc.xoai.dataprovider.filter.ScopedFilter;
import io.gdcc.xoai.dataprovider.handlers.results.ListItemIdentifiersResult;
import io.gdcc.xoai.dataprovider.handlers.results.ListItemsResults;

import java.util.Date;
import java.util.List;

/**
 * This class wraps the data source of items.
 *
 * @author Development @ Lyncode
 * @version 3.1.0
 */
public interface ItemRepository {
    /**
     * Gets an item from the data source.
     *
     * @param identifier Unique identifier of the item
     * @return ItemHelper
     * @throws IdDoesNotExistException
     *
     * @throws OAIException
     *
     * @see <a href="client://www.openarchives.org/OAI/openarchivesprotocol.html#UniqueIdentifier">Unique identifier definition</a>
     */
    Item getItem(String identifier)
            throws IdDoesNotExistException, OAIException;

    /**
     * Gets a paged list of identifiers. The metadata prefix parameter is internally converted to a list of filters.
     * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
     *
     * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
     * @param offset  Start offset
     * @param length  Max items returned
     * @return List of identifiers
     * @throws OAIException
     * @see <a href="client://www.openarchives.org/OAI/openarchivesprotocol.html#ListIdentifiers">List Identifiers definition</a>
     */
    ListItemIdentifiersResult getItemIdentifiers(
        List<ScopedFilter> filters, int offset, int length) throws OAIException;

    /**
     * Gets a paged list of identifiers. The metadata prefix parameter is internally converted to a list of filters.
     * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
     *
     * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
     * @param offset  Start offset
     * @param length  Max items returned
     * @param from    Date parameter
     * @return List of identifiers
     * @throws OAIException
     * @see <a href="client://www.openarchives.org/OAI/openarchivesprotocol.html#ListIdentifiers">List Identifiers definition</a>
     */
    ListItemIdentifiersResult getItemIdentifiers(
        List<ScopedFilter> filters, int offset, int length, Date from) throws OAIException;

    /**
     * Gets a paged list of identifiers. The metadata prefix parameter is internally converted to a list of filters.
     * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
     *
     * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
     * @param offset  Start offset
     * @param length  Max items returned
     * @param until   Date parameter
     * @return List of identifiers
     * @throws OAIException
     * @see <a href="client://www.openarchives.org/OAI/openarchivesprotocol.html#ListIdentifiers">List Identifiers definition</a>
     */
    ListItemIdentifiersResult getItemIdentifiersUntil(
        List<ScopedFilter> filters, int offset, int length, Date until) throws OAIException;

    /**
     * Gets a paged list of identifiers. The metadata prefix parameter is internally converted to a list of filters.
     * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
     *
     * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
     * @param offset  Start offset
     * @param length  Max items returned
     * @param from    Date parameter
     * @param until   Date parameter
     * @return List of identifiers
     * @throws OAIException
     * @see <a href="client://www.openarchives.org/OAI/openarchivesprotocol.html#ListIdentifiers">List Identifiers definition</a>
     */
    ListItemIdentifiersResult getItemIdentifiers(
        List<ScopedFilter> filters, int offset, int length, Date from, Date until) throws OAIException;

    /**
     * Gets a paged list of identifiers. The metadata prefix parameter is internally converted to a list of filters.
     * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
     *
     * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
     * @param offset  Start offset
     * @param length  Max items returned
     * @param setSpec Set Spec
     * @return List of identifiers
     * @throws OAIException
     * @see <a href="client://www.openarchives.org/OAI/openarchivesprotocol.html#ListIdentifiers">List Identifiers definition</a>
     */
    ListItemIdentifiersResult getItemIdentifiers(
        List<ScopedFilter> filters, int offset, int length, String setSpec) throws OAIException;

    /**
     * Gets a paged list of identifiers. The metadata prefix parameter is internally converted to a list of filters.
     * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
     *
     * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
     * @param offset  Start offset
     * @param length  Max items returned
     * @param setSpec Set Spec
     * @param from    Date parameter
     * @return List of identifiers
     * @throws OAIException
     * @see <a href="client://www.openarchives.org/OAI/openarchivesprotocol.html#ListIdentifiers">List Identifiers definition</a>
     */
    ListItemIdentifiersResult getItemIdentifiers(
        List<ScopedFilter> filters, int offset, int length, String setSpec,
        Date from) throws OAIException;

    /**
     * Gets a paged list of identifiers. The metadata prefix parameter is internally converted to a list of filters.
     * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
     *
     * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
     * @param offset  Start offset
     * @param length  Max items returned
     * @param setSpec Set Spec
     * @param until   Date parameter
     * @return List of identifiers
     * @throws OAIException
     * @see <a href="client://www.openarchives.org/OAI/openarchivesprotocol.html#ListIdentifiers">List Identifiers definition</a>
     */
    ListItemIdentifiersResult getItemIdentifiersUntil(
        List<ScopedFilter> filters, int offset, int length, String setSpec,
        Date until) throws OAIException;

    /**
     * Gets a paged list of identifiers. The metadata prefix parameter is internally converted to a list of filters.
     * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
     *
     * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
     * @param offset  Start offset
     * @param length  Max items returned
     * @param setSpec Set Spec
     * @param from    Date parameter
     * @param until   Date parameter
     * @return List of identifiers
     * @throws OAIException
     * @see <a href="client://www.openarchives.org/OAI/openarchivesprotocol.html#ListIdentifiers">List Identifiers definition</a>
     */
    ListItemIdentifiersResult getItemIdentifiers(
        List<ScopedFilter> filters, int offset, int length, String setSpec,
        Date from, Date until) throws OAIException;

    /**
     * Gets a paged list of items. The metadata prefix parameter is internally converted to a list of filters.
     * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
     *
     * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
     * @param offset  Start offset
     * @param length  Max items returned
     * @return List of Items
     * @throws OAIException
     * @see <a href="client://www.openarchives.org/OAI/openarchivesprotocol.html#ListRecords">List Records Definition</a>
     */
    ListItemsResults getItems(List<ScopedFilter> filters,
                              int offset, int length) throws OAIException;

    /**
     * Gets a paged list of items. The metadata prefix parameter is internally converted to a list of filters.
     * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
     *
     * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
     * @param offset  Start offset
     * @param length  Max items returned
     * @param from    Date parameter
     * @return List of Items
     * @throws OAIException
     * @see <a href="client://www.openarchives.org/OAI/openarchivesprotocol.html#ListRecords">List Records Definition</a>
     */
    ListItemsResults getItems(List<ScopedFilter> filters,
                              int offset, int length, Date from) throws OAIException;

    /**
     * Gets a paged list of items. The metadata prefix parameter is internally converted to a list of filters.
     * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
     *
     * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
     * @param offset  Start offset
     * @param length  Max items returned
     * @param until   Date parameter
     * @return List of Items
     * @throws OAIException
     * @see <a href="client://www.openarchives.org/OAI/openarchivesprotocol.html#ListRecords">List Records Definition</a>
     */
    ListItemsResults getItemsUntil(List<ScopedFilter> filters,
                                   int offset, int length, Date until) throws OAIException;

    /**
     * Gets a paged list of items. The metadata prefix parameter is internally converted to a list of filters.
     * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
     *
     * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
     * @param offset  Start offset
     * @param length  Max items returned
     * @param from    Date parameter
     * @param until   Date parameter
     * @return List of Items
     * @throws OAIException
     * @see <a href="client://www.openarchives.org/OAI/openarchivesprotocol.html#ListRecords">List Records Definition</a>
     */
    ListItemsResults getItems(List<ScopedFilter> filters,
                              int offset, int length, Date from, Date until) throws OAIException;

    /**
     * Gets a paged list of items. The metadata prefix parameter is internally converted to a list of filters.
     * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
     *
     * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
     * @param offset  Start offset
     * @param length  Max items returned
     * @param setSpec Set spec
     * @return List of Items
     * @throws OAIException
     * @see <a href="client://www.openarchives.org/OAI/openarchivesprotocol.html#ListRecords">List Records Definition</a>
     */
    ListItemsResults getItems(List<ScopedFilter> filters,
                              int offset, int length, String setSpec) throws OAIException;

    /**
     * Gets a paged list of items. The metadata prefix parameter is internally converted to a list of filters.
     * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
     *
     * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
     * @param offset  Start offset
     * @param length  Max items returned
     * @param from    Date parameter
     * @param setSpec Set spec
     * @return List of Items
     * @throws OAIException
     * @see <a href="client://www.openarchives.org/OAI/openarchivesprotocol.html#ListRecords">List Records Definition</a>
     */
    ListItemsResults getItems(List<ScopedFilter> filters,
                              int offset, int length, String setSpec, Date from) throws OAIException;

    /**
     * Gets a paged list of items. The metadata prefix parameter is internally converted to a list of filters.
     * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
     *
     * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
     * @param offset  Start offset
     * @param length  Max items returned
     * @param until   Date parameter
     * @param setSpec Set spec
     * @return List of Items
     * @throws OAIException
     * @see <a href="client://www.openarchives.org/OAI/openarchivesprotocol.html#ListRecords">List Records Definition</a>
     */
    ListItemsResults getItemsUntil(List<ScopedFilter> filters,
                                   int offset, int length, String setSpec, Date until) throws OAIException;

    /**
     * Gets a paged list of items. The metadata prefix parameter is internally converted to a list of filters.
     * That is, when configuring XOAI, it is possible to associate to each metadata format a list of filters.
     *
     * @param filters List of Filters <a href="https://github.com/lyncode/xoai/wiki/XOAI-Data-Provider-Architecture">details</a>
     * @param offset  Start offset
     * @param length  Max items returned
     * @param from    Date parameter
     * @param until   Date parameter
     * @param setSpec Set spec
     * @return List of Items
     * @throws OAIException
     * @see <a href="client://www.openarchives.org/OAI/openarchivesprotocol.html#ListRecords">List Records Definition</a>
     */
    ListItemsResults getItems(List<ScopedFilter> filters,
                              int offset, int length, String setSpec, Date from, Date until) throws OAIException;

}
