/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.filter;

import io.gdcc.xoai.dataprovider.model.ItemIdentifier;

public interface Filter {
    boolean isItemShown(ItemIdentifier item);
}
