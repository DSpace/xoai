/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider.handlers.results;

import org.dspace.xoai.dataprovider.model.Item;

import java.util.List;

/**
 * @author Development @ Lyncode
 * @version 3.1.0
 */
public class ListItemsResults {
    private boolean hasMore;
    private List<Item> results;
    private int totalResults = -1;

    public ListItemsResults(boolean hasMoreResults, List<Item> results) {
        this.hasMore = hasMoreResults;
        this.results = results;
    }

    public ListItemsResults(boolean hasMoreResults, List<Item> results, int total) {
        this.hasMore = hasMoreResults;
        this.results = results;
        this.totalResults = total;
    }

    public boolean hasMore() {
        return hasMore;
    }

    public List<Item> getResults() {
        return results;
    }

    public boolean hasTotalResults() {
        return this.totalResults > 0;
    }

    public int getTotal() {
        return this.totalResults;
    }
}
