package com.lyncode.xoai.dataprovider.exceptions;


public abstract class HandlerException extends Exception {
    private static final long serialVersionUID = 3141316350056438361L;

    public HandlerException() {
        super();
    }

    public HandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandlerException(String message) {
        super(message);
    }

    public HandlerException(Throwable cause) {
        super(cause);
    }


}
