/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.exceptions;

public class OAIRequestException extends Exception {
    public OAIRequestException() {
    }

    public OAIRequestException(String message) {
        super(message);
    }

    public OAIRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public OAIRequestException(Throwable cause) {
        super(cause);
    }
}
