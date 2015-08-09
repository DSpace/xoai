/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider.filter;

import org.dspace.xoai.dataprovider.model.conditions.Condition;

public abstract class FilterResolver {
    public abstract Filter getFilter (Condition condition);
}
