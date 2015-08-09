/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.exceptions;


/**
 * @author Development @ Lyncode
 * @version 3.1.0
 */
public class InternalHarvestException extends RuntimeException {
    public InternalHarvestException() {
    }

    public InternalHarvestException(String message) {
        super(message);
    }

    public InternalHarvestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalHarvestException(Throwable cause) {
        super(cause);
    }
}
