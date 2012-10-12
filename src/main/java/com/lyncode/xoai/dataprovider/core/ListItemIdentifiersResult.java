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

import java.util.List;

import com.lyncode.xoai.dataprovider.data.AbstractItemIdentifier;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.7
 */
public class ListItemIdentifiersResult {
	private boolean hasMore;
	private List<AbstractItemIdentifier> results;
	private int totalResults = -1;

	public ListItemIdentifiersResult(boolean hasMoreResults,
			List<AbstractItemIdentifier> results) {
		this.hasMore = hasMoreResults;
		this.results = results;
	}

    public ListItemIdentifiersResult(boolean hasMoreResults,
            List<AbstractItemIdentifier> results, int totalResults) {
        this.hasMore = hasMoreResults;
        this.results = results;
        this.totalResults = totalResults;
    }

	public boolean hasMore() {
		return hasMore;
	}

	public List<AbstractItemIdentifier> getResults() {
		return results;
	}
	
	public boolean hasTotalResults () {
	    return this.totalResults > 0;
	}
	
	public int getTotal () {
	    return this.totalResults;
	}
}
