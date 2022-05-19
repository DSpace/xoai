/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.repository;

import io.gdcc.xoai.dataprovider.model.InMemoryItem;
import io.gdcc.xoai.dataprovider.model.Item;
import io.gdcc.xoai.dataprovider.exceptions.IdDoesNotExistException;
import io.gdcc.xoai.dataprovider.exceptions.OAIException;
import io.gdcc.xoai.dataprovider.filter.ScopedFilter;
import io.gdcc.xoai.dataprovider.handlers.results.ListItemIdentifiersResult;
import io.gdcc.xoai.dataprovider.handlers.results.ListItemsResults;
import io.gdcc.xoai.dataprovider.model.MetadataFormat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static io.gdcc.xoai.dataprovider.model.InMemoryItem.randomItem;
import static java.lang.Math.min;
import static java.util.Arrays.asList;

public class InMemoryItemRepository implements ItemRepository {
    private final List<InMemoryItem> list = new ArrayList<>();

    public InMemoryItemRepository withNoItems() {
        return this;
    }

    public InMemoryItemRepository withItem(InMemoryItem item) {
        list.add(item);
        return this;
    }

    public InMemoryItemRepository withItems(InMemoryItem... item) {
        list.addAll(asList(item));
        return this;
    }

    public InMemoryItemRepository withRandomItems(int number) {
        for (int i = 0; i < number; i++)
            list.add(randomItem());
        return this;
    }

    @Override
    public Item getItem(String identifier) throws IdDoesNotExistException, OAIException {
        for (InMemoryItem item : this.list) {
            if (item.getIdentifier().equals(identifier))
                return item;
        }
        throw new IdDoesNotExistException();
    }
    
    @Override
    public Item getItem(String identifier, MetadataFormat format) throws IdDoesNotExistException, OAIException {
        return getItem(identifier);
    }
    
    @Override
    public ListItemIdentifiersResult getItemIdentifiers(List<ScopedFilter> filters, int offset, int length) throws OAIException {
        return new ListItemIdentifiersResult(offset + length < list.size(), new ArrayList<>(list.subList(offset, min(offset + length, list.size()))));
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiers(List<ScopedFilter> filters, int offset, int length, Instant from) throws OAIException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiersUntil(List<ScopedFilter> filters, int offset, int length, Instant until) throws OAIException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiers(List<ScopedFilter> filters, int offset, int length, Instant from, Instant until) throws OAIException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiers(List<ScopedFilter> filters, int offset, int length, String setSpec) throws OAIException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiers(List<ScopedFilter> filters, int offset, int length, String setSpec, Instant from) throws OAIException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiersUntil(List<ScopedFilter> filters, int offset, int length, String setSpec, Instant until) throws OAIException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiers(List<ScopedFilter> filters, int offset, int length, String setSpec, Instant from, Instant until) throws OAIException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset, int length) throws OAIException {
        return new ListItemsResults(offset + length < list.size(), new ArrayList<>(list.subList(offset, min(offset + length, list.size()))));
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset, int length, Instant from) throws OAIException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ListItemsResults getItemsUntil(List<ScopedFilter> filters, int offset, int length, Instant until) throws OAIException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset, int length, Instant from, Instant until) throws OAIException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset, int length, String setSpec) throws OAIException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset, int length, String setSpec, Instant from) throws OAIException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ListItemsResults getItemsUntil(List<ScopedFilter> filters, int offset, int length, String setSpec, Instant until) throws OAIException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset, int length, String setSpec, Instant from, Instant until) throws OAIException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
