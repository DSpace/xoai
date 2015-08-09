/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.exceptions;

public class CannotDisseminateFormatException extends HarvestException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4365928393912692884L;

	public CannotDisseminateFormatException() {
	}

	public CannotDisseminateFormatException(String arg0) {
		super(arg0);
	}

	public CannotDisseminateFormatException(Throwable arg0) {
		super(arg0);
	}

	public CannotDisseminateFormatException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
