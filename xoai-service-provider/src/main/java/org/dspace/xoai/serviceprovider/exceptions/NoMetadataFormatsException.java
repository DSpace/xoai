/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.exceptions;

public class NoMetadataFormatsException extends HarvestException {
    public NoMetadataFormatsException() {
    }

    public NoMetadataFormatsException(String arg0) {
        super(arg0);
    }

    public NoMetadataFormatsException(Throwable arg0) {
        super(arg0);
    }

    public NoMetadataFormatsException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
