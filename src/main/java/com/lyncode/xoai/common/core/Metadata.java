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

package com.lyncode.xoai.common.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author DSpace @ Lyncode
 * @version 1.0.1
 */
public class Metadata {
    private Map<String, List<String>> _metadata;

    public Metadata() {
        _metadata = new HashMap<String, List<String>>();
    }

    public void add (String path, String value) {
        if (_metadata.get(path) == null)
            _metadata.put(path, new ArrayList<String>());
        _metadata.get(path).add(value);
    }

    public List<String> get (String path) {
        return _metadata.get(path);
    }

    public boolean hasMetadataEntry (String path) {
        return _metadata.containsKey(path);
    }

    public String getFirst (String path) {
        if (_metadata.get(path) != null && _metadata.get(path).size() > 0)
            return _metadata.get(path).get(0);
        else
            return null;
    }

    public List<String> getPaths () {
        return new ArrayList<String>(_metadata.keySet());
    }
}
