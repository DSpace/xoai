/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.client;

import org.dspace.xoai.serviceprovider.exceptions.OAIRequestException;
import org.dspace.xoai.serviceprovider.parameters.Parameters;

import java.io.InputStream;

public interface OAIClient {
    InputStream execute (Parameters parameters) throws OAIRequestException;
}
