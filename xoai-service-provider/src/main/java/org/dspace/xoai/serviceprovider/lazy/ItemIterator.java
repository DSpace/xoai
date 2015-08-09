/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.lazy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemIterator<T> implements Iterator<T> {
    private List<T> items = new ArrayList<T>();
    private Source<T> source;
    private int position = 0;

    public ItemIterator(Source<T> source) {
        this.source = source;
        hasNext();
    }

    @Override
    public boolean hasNext() {
        if (items.size() > position)
            return true;
        else {
            if (source.endReached()) return false;
            else {
                items.addAll(source.nextIteration());
                return hasNext();
            }
        }
    }

    @Override
    public T next() {
        return items.get(position++);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("OAI-PMH is a read-only interface");
    }
}
