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

package com.lyncode.xoai.dataprovider.data.internal;

import com.lyncode.xoai.dataprovider.transform.XSLTemplates;

/**

 * @version 3.1.0
 */
public class MetadataTransformer {
    private XSLTemplates xslTemplates;

    public MetadataTransformer() {
        xslTemplates = null;
    }

    public MetadataTransformer(XSLTemplates xslTemplates) {
        this.xslTemplates = xslTemplates;
    }

    public boolean hasXslTemplates() {
        return (this.xslTemplates != null);
    }

    public XSLTemplates getXslTemplates() {
        return this.xslTemplates;
    }
}
