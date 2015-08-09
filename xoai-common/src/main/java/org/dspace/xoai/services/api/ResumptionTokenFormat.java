/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.services.api;

import org.dspace.xoai.exceptions.InvalidResumptionTokenException;
import org.dspace.xoai.model.oaipmh.ResumptionToken;

public interface ResumptionTokenFormat {
    String format (ResumptionToken.Value value);
    ResumptionToken.Value parse (String value) throws InvalidResumptionTokenException;
}
