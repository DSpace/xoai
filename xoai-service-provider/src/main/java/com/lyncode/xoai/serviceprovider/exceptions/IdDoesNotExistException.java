package com.lyncode.xoai.serviceprovider.exceptions;

public class IdDoesNotExistException extends HarvestException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8245395447554519167L;

	public IdDoesNotExistException() {
	}

	public IdDoesNotExistException(String arg0) {
		super(arg0);
	}

	public IdDoesNotExistException(Throwable arg0) {
		super(arg0);
	}

	public IdDoesNotExistException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
