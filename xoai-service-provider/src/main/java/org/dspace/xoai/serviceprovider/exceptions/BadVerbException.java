/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.exceptions;

public class BadVerbException extends HarvestException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7559609480305804607L;

	public BadVerbException() {
	}

	public BadVerbException(String arg0) {
		super(arg0);
	}

	public BadVerbException(Throwable arg0) {
		super(arg0);
	}

	public BadVerbException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
