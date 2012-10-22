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

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.8
 */
public class ListSetsResult {
	private boolean hasMore;
	private List<Set> results;
	private int total = -1;

    public ListSetsResult(boolean hasMoreResults, List<Set> results) {
        this.hasMore = hasMoreResults;
        this.results = results;
    }
    public ListSetsResult(boolean hasMoreResults, List<Set> results, int total) {
        this.hasMore = hasMoreResults;
        this.results = results;
        this.total = total;
    }

	public boolean hasMore() {
		return hasMore;
	}

	public List<Set> getResults() {
		return results;
	}
	
	public boolean hasTotalResults () {
	    return this.total > 0;
	}
	
	public int getTotalResults () {
	    return this.total;
	}
}
