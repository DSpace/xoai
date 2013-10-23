package com.lyncode.xoai.dataprovider.handlers;

import com.lyncode.xoai.dataprovider.core.OAIParameters;
import com.lyncode.xoai.dataprovider.exceptions.HandlerException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;


public abstract class VerbHandler<T> {
    public abstract T handle(OAIParameters params) throws OAIException, HandlerException;
}
