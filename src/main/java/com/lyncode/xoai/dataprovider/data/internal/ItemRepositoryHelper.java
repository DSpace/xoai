package com.lyncode.xoai.dataprovider.data.internal;

import com.lyncode.xoai.dataprovider.core.ListItemIdentifiersResult;
import com.lyncode.xoai.dataprovider.core.ListItemsResults;
import com.lyncode.xoai.dataprovider.core.XOAIContext;
import com.lyncode.xoai.dataprovider.data.Item;
import com.lyncode.xoai.dataprovider.exceptions.CannotDisseminateFormatException;
import com.lyncode.xoai.dataprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.filter.Scope;
import com.lyncode.xoai.dataprovider.filter.ScopedFilter;
import com.lyncode.xoai.dataprovider.services.api.ItemRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.lyncode.xoai.dataprovider.filter.Scope.Context;
import static com.lyncode.xoai.dataprovider.filter.Scope.MetadataFormat;

public class ItemRepositoryHelper {
    private ItemRepository itemRepository;

    public ItemRepositoryHelper(ItemRepository itemRepository) {
        super();
        this.itemRepository = itemRepository;
    }

    /**
     * Method used by XOAI internals.
     *

     * @param offset         Start offset
     * @param length         Max returned length
     * @param metadataPrefix Metadata Prefix parameter
     * @return List of Identifiers
     */
    public ListItemIdentifiersResult getItemIdentifiers(XOAIContext context,
                                                        int offset, int length, String metadataPrefix)
            throws CannotDisseminateFormatException, OAIException {
        return itemRepository.getItemIdentifiers(getScopedFilters(context, metadataPrefix), offset, length);
    }

    /**
     * Method used by XOAI internals.
     *

     * @param offset         Start offset
     * @param length         Max returned length
     * @param metadataPrefix Metadata Prefix parameter
     * @param from           From parameter
     * @return List of Identifiers
     */
    public ListItemIdentifiersResult getItemIdentifiers(XOAIContext context,
                                                        int offset, int length, String metadataPrefix, Date from)
            throws CannotDisseminateFormatException, OAIException {
        List<ScopedFilter> filters = getScopedFilters(context, metadataPrefix);
        return itemRepository.getItemIdentifiers(filters, offset, length, from);
    }

    private List<ScopedFilter> getScopedFilters(XOAIContext context, String metadataPrefix) throws CannotDisseminateFormatException {
        List<ScopedFilter> filters = new ArrayList<ScopedFilter>();
        if (context.hasCondition())
            filters.add(new ScopedFilter(context.getCondition(), Context));

        MetadataFormat metadataFormat = context.getFormatByPrefix(metadataPrefix);
        if (metadataFormat.hasCondition())
            filters.add(new ScopedFilter(metadataFormat.getCondition(), MetadataFormat));
        return filters;
    }

    /**
     * Method used by XOAI internals.
     *

     * @param offset         Start offset
     * @param length         Max returned length
     * @param metadataPrefix Metadata Prefix parameter
     * @param until          Date parameter
     * @return List of Identifiers
     */
    public ListItemIdentifiersResult getItemIdentifiersUntil(
            XOAIContext context, int offset, int length, String metadataPrefix,
            Date until) throws CannotDisseminateFormatException, OAIException {
        List<ScopedFilter> filters = getScopedFilters(context, metadataPrefix);
        return itemRepository.getItemIdentifiersUntil(filters, offset, length, until);
    }

    /**
     * Method used by XOAI internals.
     *

     * @param offset         Start offset
     * @param length         Max returned length
     * @param metadataPrefix Metadata Prefix parameter
     * @param from           Date parameter
     * @param until          Date parameter
     * @return List of Identifiers
     *
     */
    public ListItemIdentifiersResult getItemIdentifiers(XOAIContext context,
                                                        int offset, int length, String metadataPrefix, Date from, Date until)
            throws CannotDisseminateFormatException, OAIException {
        return itemRepository.getItemIdentifiers(getScopedFilters(context, metadataPrefix), offset, length, from, until);
    }

    /**
     * Method used by XOAI internals.
     *

     * @param offset         Start offset
     * @param length         Max returned length
     * @param metadataPrefix Metadata Prefix parameter
     * @param setSpec        Set spec
     * @return List of Identifiers
     *
     */
    public ListItemIdentifiersResult getItemIdentifiers(XOAIContext context,
                                                        int offset, int length, String metadataPrefix, String setSpec)
            throws CannotDisseminateFormatException, OAIException {
        List<ScopedFilter> filters = getScopedFilters(context, metadataPrefix);
        if (context.isStaticSet(setSpec)) {
            if (context.getSetFilter(setSpec) != null)
                filters.add(new ScopedFilter(context.getSetFilter(setSpec), Scope.Set));
            return itemRepository.getItemIdentifiers(filters, offset, length);
        } else
            return itemRepository.getItemIdentifiers(filters, offset, length, setSpec);
    }

    public ListItemIdentifiersResult getItemIdentifiers(XOAIContext context,
                                                        int offset, int length, String metadataPrefix, String setSpec,
                                                        Date from) throws CannotDisseminateFormatException, OAIException {
        List<ScopedFilter> filters = getScopedFilters(context, metadataPrefix);
        if (context.isStaticSet(setSpec)) {
            if (context.getSetFilter(setSpec) != null)
                filters.add(new ScopedFilter(context.getSetFilter(setSpec), Scope.Set));
            return itemRepository.getItemIdentifiers(filters, offset, length, from);
        } else
            return itemRepository.getItemIdentifiers(filters, offset, length, setSpec,
                    from);
    }

    public ListItemIdentifiersResult getItemIdentifiersUntil(
            XOAIContext context, int offset, int length, String metadataPrefix,
            String setSpec, Date until) throws CannotDisseminateFormatException, OAIException {
        List<ScopedFilter> filters = getScopedFilters(context, metadataPrefix);
        if (context.isStaticSet(setSpec)) {
            if (context.getSetFilter(setSpec) != null)
                filters.add(new ScopedFilter(context.getSetFilter(setSpec), Scope.Set));
            return itemRepository.getItemIdentifiersUntil(filters, offset, length, until);
        } else
            return itemRepository.getItemIdentifiersUntil(filters, offset, length,
                    setSpec, until);
    }

    public ListItemIdentifiersResult getItemIdentifiers(XOAIContext context,
                                                        int offset, int length, String metadataPrefix, String setSpec,
                                                        Date from, Date until) throws CannotDisseminateFormatException, OAIException {
        List<ScopedFilter> filters = getScopedFilters(context, metadataPrefix);
        if (context.isStaticSet(setSpec)) {
            if (context.getSetFilter(setSpec) != null)
                filters.add(new ScopedFilter(context.getSetFilter(setSpec), Scope.Set));
            return itemRepository
                    .getItemIdentifiers(filters, offset, length, from, until);
        } else
            return itemRepository.getItemIdentifiers(filters, offset, length, setSpec,
                    from, until);
    }

    public ListItemsResults getItems(XOAIContext context, int offset,
                                     int length, String metadataPrefix)
            throws CannotDisseminateFormatException, OAIException {
        List<ScopedFilter> filters = getScopedFilters(context, metadataPrefix);
        return itemRepository.getItems(filters, offset, length);
    }

    public ListItemsResults getItems(XOAIContext context, int offset,
                                     int length, String metadataPrefix, Date from)
            throws CannotDisseminateFormatException, OAIException {
        List<ScopedFilter> filters = getScopedFilters(context, metadataPrefix);
        return itemRepository.getItems(filters, offset, length, from);
    }

    public ListItemsResults getItemsUntil(XOAIContext context, int offset,
                                          int length, String metadataPrefix, Date until)
            throws CannotDisseminateFormatException, OAIException {
        List<ScopedFilter> filters = getScopedFilters(context, metadataPrefix);
        return itemRepository.getItemsUntil(filters, offset, length, until);
    }

    public ListItemsResults getItems(XOAIContext context, int offset,
                                     int length, String metadataPrefix, Date from, Date until)
            throws CannotDisseminateFormatException, OAIException {
        List<ScopedFilter> filters = getScopedFilters(context, metadataPrefix);
        return itemRepository.getItems(filters, offset, length, from, until);
    }

    public ListItemsResults getItems(XOAIContext context, int offset,
                                     int length, String metadataPrefix, String setSpec)
            throws CannotDisseminateFormatException, OAIException {

        List<ScopedFilter> filters = getScopedFilters(context, metadataPrefix);
        if (context.isStaticSet(setSpec)) {
            if (context.getSetFilter(setSpec) != null)
                filters.add(new ScopedFilter(context.getSetFilter(setSpec), Scope.Set));
            return itemRepository.getItems(filters, offset, length);
        } else
            return itemRepository.getItems(filters, offset, length, setSpec);
    }

    public ListItemsResults getItems(XOAIContext context, int offset,
                                     int length, String metadataPrefix, String setSpec, Date from)
            throws CannotDisseminateFormatException, OAIException {
        List<ScopedFilter> filters = getScopedFilters(context, metadataPrefix);
        if (context.isStaticSet(setSpec)) {
            if (context.getSetFilter(setSpec) != null)
                filters.add(new ScopedFilter(context.getSetFilter(setSpec), Scope.Set));
            return itemRepository.getItems(filters, offset, length, from);
        } else
            return itemRepository.getItems(filters, offset, length, setSpec, from);
    }

    public ListItemsResults getItemsUntil(XOAIContext context, int offset,
                                          int length, String metadataPrefix, String setSpec, Date until)
            throws CannotDisseminateFormatException, OAIException {
        List<ScopedFilter> filters = getScopedFilters(context, metadataPrefix);
        if (context.isStaticSet(setSpec)) {
            if (context.getSetFilter(setSpec) != null)
                filters.add(new ScopedFilter(context.getSetFilter(setSpec), Scope.Set));
            return itemRepository.getItemsUntil(filters, offset, length, until);
        } else
            return itemRepository.getItemsUntil(filters, offset, length, setSpec, until);
    }

    public ListItemsResults getItems(XOAIContext context, int offset,
                                     int length, String metadataPrefix, String setSpec, Date from,
                                     Date until) throws CannotDisseminateFormatException, OAIException {
        List<ScopedFilter> filters = getScopedFilters(context, metadataPrefix);
        if (context.isStaticSet(setSpec)) {
            if (context.getSetFilter(setSpec) != null)
                filters.add(new ScopedFilter(context.getSetFilter(setSpec), Scope.Set));
            return itemRepository.getItems(filters, offset, length, from, until);
        } else
            return itemRepository.getItems(filters, offset, length, setSpec, from, until);
    }

    public Item getItem(String identifier) throws IdDoesNotExistException, OAIException {
        return itemRepository.getItem(identifier);
    }
}
