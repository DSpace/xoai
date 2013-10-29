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

package com.lyncode.xoai.dataprovider.data;

import com.lyncode.xoai.dataprovider.transform.XSLTransformer;
import com.lyncode.xoai.dataprovider.core.ConfigurableBundle;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 3.1.0
 */
public class MetadataTransformer extends ConfigurableBundle {
    private XSLTransformer xslTransformer;

    public MetadataTransformer() {
        xslTransformer = null;
    }

    public MetadataTransformer(XSLTransformer xsltTransformer) {
        this.xslTransformer = xsltTransformer;
    }

    public boolean hasTransformer() {
        return (this.xslTransformer != null);
    }

    public XSLTransformer getXslTransformer() {
        return this.xslTransformer;
    }
}
