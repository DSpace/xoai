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

package com.lyncode.xoai.serviceprovider;

import com.lyncode.xoai.serviceprovider.model.Context;

import static com.lyncode.xoai.dataprovider.model.MetadataFormat.identity;

public abstract class AbstractServiceProviderTest extends AbstractInMemoryDataProviderTest {

    private final Context context = new Context()
            .withOAIClient(oaiClient())
            .withBaseUrl(BASE_URL)
            .withMetadataTransformer(FORMAT, identity());

    protected Context theContext() {
        return context;
    }
}
