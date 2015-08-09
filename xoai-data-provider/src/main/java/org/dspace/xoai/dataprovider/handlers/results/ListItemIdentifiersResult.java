/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider.handlers.results;

import org.dspace.xoai.dataprovider.model.ItemIdentifier;

import java.util.List;

/**
 * @author Development @ Lyncode
 * @version 3.1.0
 */
public class ListItemIdentifiersResult {
    private boolean hasMore;
    private List<ItemIdentifier> results;
    private int totalResults = -1;

    public ListItemIdentifiersResult(boolean hasMoreResults, List<ItemIdentifier> results) {
        this.hasMore = hasMoreResults;
        this.results = results;
    }

    public ListItemIdentifiersResult(boolean hasMoreResults, List<ItemIdentifier> results, int totalResults) {
        this.hasMore = hasMoreResults;
        this.results = results;
        this.totalResults = totalResults;
    }

    public boolean hasMore() {
        return hasMore;
    }

    public List<ItemIdentifier> getResults() {
        return results;
    }

    public boolean hasTotalResults() {
        return this.totalResults > 0;
    }

    public int getTotal() {
        return this.totalResults;
    }
}
