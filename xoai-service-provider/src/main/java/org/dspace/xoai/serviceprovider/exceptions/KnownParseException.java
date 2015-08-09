/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.exceptions;


public class KnownParseException extends ParseException {

    /**
     *
     */
    private static final long serialVersionUID = 3017062595324205745L;


    public KnownParseException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public KnownParseException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}
