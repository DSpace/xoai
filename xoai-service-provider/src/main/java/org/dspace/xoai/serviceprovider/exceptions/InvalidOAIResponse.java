/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.exceptions;

public class InvalidOAIResponse extends RuntimeException {
    public InvalidOAIResponse() {
    }

    public InvalidOAIResponse(String message) {
        super(message);
    }

    public InvalidOAIResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidOAIResponse(Throwable cause) {
        super(cause);
    }
}
