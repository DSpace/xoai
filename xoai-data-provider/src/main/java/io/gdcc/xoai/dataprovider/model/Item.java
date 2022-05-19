/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.model;

import io.gdcc.xoai.model.oaipmh.About;
import io.gdcc.xoai.model.oaipmh.Metadata;

import java.util.Collections;
import java.util.List;

/**
 * This is a required class to extend when implementing a specific OAI Data Provider.
 * It works as a wrapper for all OAI Items.
 *
 * @author Development @ Lyncode
 * @version 3.1.0
 */
public interface Item extends ItemIdentifier {
    /**
     * Most of the implementations would return an empty list.
     * Anyway, the OAI-PMH protocol establishes an abouts section for each item.
     *
     * @return (Maybe empty) {@link List} of information {@link About} the item (marshable information)
     * @see <a href="client://www.openarchives.org/OAI/openarchivesprotocol.html#Record">Record definition</a>
     */
    default List<About> getAbout() {
        return Collections.emptyList();
    }

    /**
     * Metadata associated to the OAI-PMH Record.
     *
     * @return {@link Metadata} associated to the OAI-PMH Record
     * @see <a href="client://www.openarchives.org/OAI/openarchivesprotocol.html#Record">Record definition</a>
     */
    Metadata getMetadata();
}
