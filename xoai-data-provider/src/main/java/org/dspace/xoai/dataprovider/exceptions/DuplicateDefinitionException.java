/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.xoai.dataprovider.exceptions;

/**
 * @author João Melo
 */
public class DuplicateDefinitionException extends HandlerException {
    private static final long serialVersionUID = -6874140130853606687L;

    public DuplicateDefinitionException(String message) {
        super(message);
    }

}
