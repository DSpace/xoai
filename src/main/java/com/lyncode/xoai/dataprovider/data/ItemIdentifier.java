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
public interface ItemIdentifier {
    /**
     * Returns the OAI-PMH unique identifier.
     *
     * @return OAI-PMH unique identifier.
     * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#UniqueIdentifier">Unique identifier definition</a>
     */
    String getIdentifier();

    /**
     * Creation, modification or deletion date.
     *
     * @return OAI-PMH record datestamp
     * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#Record">Record definition</a>
     */
    Date getDatestamp();

    /**
     * Exposes the list of sets (using the set_spec) that contains the item (OAI-PMH record).
     *
     * @return List of sets
     * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#Set">Set definition</a>
     * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#Record">Record definition</a>
     */
    List<ReferenceSet> getSets();

    /**
     * Checks if the item is deleted or not.
     *
     * @return Checks if the item is deleted or not.
     * @see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#Record">Record definition</a>
     */
    boolean isDeleted();
}
