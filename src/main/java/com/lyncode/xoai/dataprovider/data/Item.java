package com.lyncode.xoai.dataprovider.data;

import com.lyncode.xoai.dataprovider.core.ItemMetadata;

import java.util.List;

/**
 * This is a required class to extend when implementing a specific OAI Data Provider.
 * It works as a wrapper for all OAI Items.
 *

 * @version 3.1.0
 */
public interface Item extends ItemIdentifier {
    /**
     * Most of the implementations would return an empty list.
     * Anyway, the OAI-PMH protocol establishes an about section for each item.
     *
     * @return List of information about the item (marshable information)

     */
    List<About> getAbout();

    /**
     * Metadata associated to the OAI-PMH Record.
     *
     * @return Metadata associated to the OAI-PMH Record

     */
    ItemMetadata getMetadata();
}
