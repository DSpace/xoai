/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.exceptions;

public class NoSetHierarchyException extends HarvestException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1773759434098664641L;

	public NoSetHierarchyException() {
	}

	public NoSetHierarchyException(String arg0) {
		super(arg0);
	}

	public NoSetHierarchyException(Throwable arg0) {
		super(arg0);
	}

	public NoSetHierarchyException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
