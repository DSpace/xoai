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

import com.lyncode.xoai.dataprovider.core.ReferenceSet;

import java.util.Date;
import java.util.List;

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
     * @return OAI-PMH unique identifier.
     * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#UniqueIdentifier">Unique identifier definition</a>
     */
    public abstract String getIdentifier();

    /**
     * Creation, modification or deletion date.
     *
     * @return OAI-PMH record datestamp
     * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#Record">Record definition</a>
     */
    public abstract Date getDatestamp();

    /**
     * Exposes the list of sets (using the set_spec) that contains the item (OAI-PMH record).
     *
     * @return List of sets
     * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#Set">Set definition</a>
     * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#Record">Record definition</a>
     */
    public abstract List<ReferenceSet> getSets();

    /**
     * Checks if the item is deleted or not.
     *
     * @return Checks if the item is deleted or not.
     * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#Record">Record definition</a>
     */
    public abstract boolean isDeleted();
}
