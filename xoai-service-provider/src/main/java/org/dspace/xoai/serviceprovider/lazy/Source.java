/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.lazy;

import java.util.List;

public interface Source<T> {
    List<T> nextIteration ();
    boolean endReached ();
}
