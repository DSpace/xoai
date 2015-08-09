/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider.handlers;

import org.dspace.xoai.dataprovider.exceptions.DoesNotSupportSetsException;
import org.dspace.xoai.dataprovider.exceptions.HandlerException;
import org.dspace.xoai.dataprovider.exceptions.NoMatchesException;
import org.dspace.xoai.dataprovider.exceptions.OAIException;
import org.dspace.xoai.dataprovider.handlers.helpers.ResumptionTokenHelper;
import org.dspace.xoai.dataprovider.handlers.helpers.SetRepositoryHelper;
import org.dspace.xoai.dataprovider.handlers.results.ListSetsResult;
import org.dspace.xoai.dataprovider.model.Context;
import org.dspace.xoai.dataprovider.model.Set;
import org.dspace.xoai.dataprovider.parameters.OAICompiledRequest;
import org.dspace.xoai.dataprovider.repository.Repository;
import org.dspace.xoai.model.oaipmh.ListSets;
import org.dspace.xoai.model.oaipmh.ResumptionToken;

import java.util.List;


public class ListSetsHandler extends VerbHandler<ListSets> {
    private final SetRepositoryHelper setRepositoryHelper;

    public ListSetsHandler(Context context, Repository repository) {
        super(context, repository);
        this.setRepositoryHelper = new SetRepositoryHelper(getRepository().getSetRepository());
    }


    @Override
    public ListSets handle(OAICompiledRequest parameters) throws OAIException, HandlerException {
        ListSets result = new ListSets();
        if (!getRepository().getSetRepository().supportSets())
            throw new DoesNotSupportSetsException();

        int length = getRepository().getConfiguration().getMaxListSets();
        ListSetsResult listSetsResult = setRepositoryHelper.getSets(getContext(), getOffset(parameters), length);
        List<Set> sets = listSetsResult.getResults();

        if (sets.isEmpty() && parameters.getResumptionToken().isEmpty())
            throw new NoMatchesException();

        if (sets.size() > length)
            sets = sets.subList(0, length);

        for (Set set : sets) {
            result.getSets().add(set.toOAIPMH());
        }

        ResumptionToken.Value currentResumptionToken = new ResumptionToken.Value();
        if (parameters.hasResumptionToken()) {
            currentResumptionToken = parameters.getResumptionToken();
        } else if (listSetsResult.hasMore()) {
            currentResumptionToken = parameters.extractResumptionToken();
        }

        ResumptionTokenHelper resumptionTokenHelper = new ResumptionTokenHelper(currentResumptionToken,
                getRepository().getConfiguration().getMaxListSets());
        result.withResumptionToken(resumptionTokenHelper.resolve(listSetsResult.hasMore()));

        return result;
    }

    private int getOffset(OAICompiledRequest parameters) {
        if (!parameters.hasResumptionToken())
            return 0;
        if (parameters.getResumptionToken().getOffset() == null)
            return 0;
        return parameters.getResumptionToken().getOffset().intValue();
    }

}
