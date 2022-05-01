/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.handlers;

import io.gdcc.xoai.dataprovider.builder.OAIRequestParametersBuilder;
import io.gdcc.xoai.dataprovider.exceptions.HandlerException;
import io.gdcc.xoai.dataprovider.exceptions.OAIException;
import io.gdcc.xoai.dataprovider.model.Context;
import io.gdcc.xoai.dataprovider.parameters.OAICompiledRequest;
import io.gdcc.xoai.dataprovider.parameters.OAIRequest;
import io.gdcc.xoai.dataprovider.repository.Repository;
import io.gdcc.xoai.exceptions.InvalidResumptionTokenException;
import io.gdcc.xoai.types.Builder;
import io.gdcc.xoai.xml.XmlWritable;

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
