/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider;

import io.gdcc.xoai.serviceprovider.model.Context;

import static org.dspace.xoai.dataprovider.model.MetadataFormat.identity;

public abstract class AbstractServiceProviderTest extends AbstractInMemoryDataProviderTest {

    private final Context context = new Context()
            .withOAIClient(oaiClient())
            .withBaseUrl(BASE_URL)
            .withMetadataTransformer(FORMAT, identity());

    protected Context theContext() {
        return context;
    }
}
