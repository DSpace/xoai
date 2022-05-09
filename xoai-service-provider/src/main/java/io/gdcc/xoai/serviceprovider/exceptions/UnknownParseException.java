/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider.exceptions;

public class UnknownParseException extends ParseException {

    private static final long serialVersionUID = -2944101752081064236L;

    public UnknownParseException() {}

    public UnknownParseException(Throwable cause) {
        super(cause);
    }
}
