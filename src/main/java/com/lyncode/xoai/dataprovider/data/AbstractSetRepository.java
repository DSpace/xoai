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

import com.lyncode.xoai.dataprovider.core.ListSetsResult;

/**
 * API for implementing a repository of sets.
 * It is possible to have a data provider without sets.
 * 
 * @author Development @ Lyncode
 * @version 2.2.6
 */
public abstract class AbstractSetRepository {

	/**
	 * Checks if the actual data source supports sets.
	 * 
	 * @return Supports sets?
	 */
	public abstract boolean supportSets();
	
	/**
	 * Returns a paged list of sets. 
	 * It is common to use a partial result of 100 sets however, in XOAI this is a configured parameter.
	 * 
	 * @param offset Starting offset
	 * @param length Max size of the returned list
	 * @return List of Sets
	 */
	public abstract ListSetsResult retrieveSets(int offset, int length);
	
	/**
	 * Checks if a specific set exists in the data source.
	 * 
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#Set">Set definition</a>
	 * @param setSpec Set spec
	 * @return Set exists
	 */
	public abstract boolean exists(String setSpec);
}
