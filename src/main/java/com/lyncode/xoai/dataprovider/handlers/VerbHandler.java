package com.lyncode.xoai.dataprovider.handlers;

import com.lyncode.xoai.dataprovider.core.OAIParameters;
import com.lyncode.xoai.dataprovider.exceptions.HandlerException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.services.api.DateProvider;


public abstract class VerbHandler<T> {
    private final DateProvider formatter;

    public VerbHandler (DateProvider formatter) {
        this.formatter = formatter;
    }

    protected DateProvider getFormatter () {
        return this.formatter;
    }

    public abstract T handle(OAIParameters params) throws OAIException, HandlerException;
}
