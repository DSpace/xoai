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

import com.lyncode.xoai.dataprovider.core.ReferenceSet;

/**
 * Base class for identifying an OAI-PMH record.
 * 
 * @author Lyncode <development@lyncode.com>
 * @version 3.1.0
 */
public abstract class AbstractItemIdentifier {
	
	/**
	 * Returns the OAI-PMH unique identifier.
	 * 
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#UniqueIdentifier">Unique identifier definition</a>
	 * @return OAI-PMH unique identifier.
	 */
	public abstract String getIdentifier();

	/**
	 * Creation, modification or deletion date.
	 * 
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#Record">Record definition</a>
	 * @return OAI-PMH record datestamp
	 */
	public abstract Date getDatestamp();

	/**
	 * Exposes the list of sets (using the set_spec) that contains the item (OAI-PMH record).
	 * 
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#Set">Set definition</a>
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#Record">Record definition</a>
	 * @return List of sets
	 */
	public abstract List<ReferenceSet> getSets();
	
	/**
	 * Checks if the item is deleted or not.
	 * 
	 * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#Record">Record definition</a>
	 * @return Checks if the item is deleted or not.
	 */
	public abstract boolean isDeleted();
}
