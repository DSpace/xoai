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

import java.util.List;

import com.lyncode.xoai.dataprovider.core.ItemMetadata;

/**
 * This is a required class to extend when implementing a specific OAI Data Provider.
 * It works as a wrapper for all OAI Items.
 * 
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 3.1.0
 */
public abstract class AbstractItem extends AbstractItemIdentifier {
	/**
	 * Most of the implementations would return an empty list.
	 * Anyway, the OAI-PMH protocol establishes an about section for each item.
	 * 
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#Record">Record definition</a>
	 * @return List of information about the item (marshable information)
	 */
	public abstract List<AbstractAbout> getAbout();

	/**
	 * Checks if the about section is empty or not.
	 * 
	 * @return Has any about information?
	 */
	public boolean hasAbout() {
		return (!this.getAbout().isEmpty());
	}
	
	/**
	 * Metadata associated to the OAI-PMH Record.
	 * 
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#Record">Record definition</a>
	 * @return Metadata associated to the OAI-PMH Record
	 */
	public abstract ItemMetadata getMetadata();
}
