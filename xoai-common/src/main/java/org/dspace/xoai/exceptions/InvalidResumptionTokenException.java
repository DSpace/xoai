/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.exceptions;

public class InvalidResumptionTokenException extends Exception {
    public InvalidResumptionTokenException() {
    }

    public InvalidResumptionTokenException(String message) {
        super(message);
    }

    public InvalidResumptionTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidResumptionTokenException(Throwable cause) {
        super(cause);
    }
}
