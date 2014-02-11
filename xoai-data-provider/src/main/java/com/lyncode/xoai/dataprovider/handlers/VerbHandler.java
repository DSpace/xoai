package com.lyncode.xoai.dataprovider.handlers;

import com.lyncode.builder.Builder;
import com.lyncode.xoai.dataprovider.builder.OAIRequestParametersBuilder;
import com.lyncode.xoai.dataprovider.exceptions.HandlerException;
import com.lyncode.xoai.exceptions.InvalidResumptionTokenException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.model.Context;
import com.lyncode.xoai.dataprovider.parameters.OAICompiledRequest;
import com.lyncode.xoai.dataprovider.parameters.OAIRequest;
import com.lyncode.xoai.dataprovider.repository.Repository;
import com.lyncode.xoai.xml.XmlWritable;


public abstract class VerbHandler<T extends XmlWritable> {
    private Context context;
    private Repository repository;

    public VerbHandler (Context context, Repository repository) {
        this.context = context;
        this.repository = repository;
    }

    public Context getContext() {
        return context;
    }

    public Repository getRepository() {
        return repository;
    }

    public T handle (OAIRequest parameters) throws HandlerException, InvalidResumptionTokenException, OAIException {
        return handle(parameters.compile());
    }

    public T handle (OAIRequestParametersBuilder parameters) throws OAIException, HandlerException, InvalidResumptionTokenException {
        return handle(parameters.build());
    }

    public T handle(Builder<OAICompiledRequest> parameters) throws OAIException, HandlerException {
        return handle(parameters.build());
    }

    public abstract T handle(OAICompiledRequest params) throws OAIException, HandlerException;
}
