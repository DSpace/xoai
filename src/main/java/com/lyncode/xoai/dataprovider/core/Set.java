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

package com.lyncode.xoai.dataprovider.core;

import com.lyncode.xoai.dataprovider.xml.xoai.Metadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 3.1.0
 */
public class Set extends ReferenceSet {
    private String setName;
    private List<Metadata> descriptions;

    public Set(String setSpec, String setName) {
        super(setSpec);
        this.setName = setName;
        this.descriptions = new ArrayList<Metadata>();
    }

    /**
     * @param setSpec
     * @param setName
     * @param xmldescription Marshable object
     */
    public Set(String setSpec, String setName, List<Metadata> descriptions) {
        this(setSpec, setName);
        this.descriptions = descriptions;
    }

    public String getSetName() {
        return setName;
    }

    public List<Metadata> getDescriptions() {
        return descriptions;
    }

    public void addDescription(Metadata desc) {
        descriptions.add(desc);
    }

    public boolean hasDescription() {
        return (!this.descriptions.isEmpty());
    }
}
