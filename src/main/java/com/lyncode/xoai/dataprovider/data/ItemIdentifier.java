package com.lyncode.xoai.dataprovider.data;


import com.lyncode.xoai.dataprovider.core.ReferenceSet;

import java.util.Date;
import java.util.List;

/**
 * Base class for identifying an OAI-PMH record.
 *

 * @version 3.1.0
 */
public interface ItemIdentifier {
    /**
     * Returns the OAI-PMH unique identifier.
     *
     * @return OAI-PMH unique identifier.

     */
    String getIdentifier();

    /**
     * Creation, modification or deletion date.
     *
     * @return OAI-PMH record datestamp

     */
    Date getDatestamp();

    /**
     * Exposes the list of sets (using the set_spec) that contains the item (OAI-PMH record).
     *
     * @return List of sets


     */
    List<ReferenceSet> getSets();

    /**
     * Checks if the item is deleted or not.
     *
     * @return Checks if the item is deleted or not.

     */
    boolean isDeleted();
}
