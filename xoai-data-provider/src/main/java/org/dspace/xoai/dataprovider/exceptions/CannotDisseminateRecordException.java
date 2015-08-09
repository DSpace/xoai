/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider.exceptions;

/**
 * @author Development @ Lyncode
 * @version 3.1.0
 */
public class CannotDisseminateRecordException extends HandlerException {

    /**
     *
     */
    private static final long serialVersionUID = -7121029109566433474L;

    /**
     * Creates a new instance of <code>CannotDisseminateRecordException</code>
     * without detail message.
     */
    public CannotDisseminateRecordException() {
    }

    /**
     * Constructs an instance of <code>CannotDisseminateRecordException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CannotDisseminateRecordException(String msg) {
        super(msg);
    }
}
