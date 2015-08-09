/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.exceptions;

public class EncapsulatedKnownException extends RuntimeException {
    private HarvestException exception;

    public EncapsulatedKnownException(HarvestException cause) {
        super(cause);
    }
}
