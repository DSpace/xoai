/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider.client;

import io.gdcc.xoai.serviceprovider.exceptions.OAIRequestException;
import io.gdcc.xoai.serviceprovider.parameters.Parameters;

import java.io.InputStream;

public interface OAIClient {
    InputStream execute (Parameters parameters) throws OAIRequestException;
}
