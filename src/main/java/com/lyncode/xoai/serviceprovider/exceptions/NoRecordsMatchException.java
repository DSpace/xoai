package com.lyncode.xoai.serviceprovider.exceptions;

public class NoRecordsMatchException extends HarvestException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4301316258437125256L;

	public NoRecordsMatchException() {
	}

	public NoRecordsMatchException(String arg0) {
		super(arg0);
	}

	public NoRecordsMatchException(Throwable arg0) {
		super(arg0);
	}

	public NoRecordsMatchException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
