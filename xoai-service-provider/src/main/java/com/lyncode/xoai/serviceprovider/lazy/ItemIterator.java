/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     client://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.xoai.serviceprovider.lazy;

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
