package com.lyncode.xoai.dataprovider.handlers;

import com.lyncode.xoai.dataprovider.core.*;
import com.lyncode.xoai.dataprovider.data.internal.SetRepositoryHelper;
import com.lyncode.xoai.dataprovider.exceptions.DoesNotSupportSetsException;
import com.lyncode.xoai.dataprovider.exceptions.HandlerException;
import com.lyncode.xoai.dataprovider.exceptions.NoMatchesException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.services.api.DateProvider;
import com.lyncode.xoai.dataprovider.services.api.ResumptionTokenFormatter;
import com.lyncode.xoai.dataprovider.xml.oaipmh.DescriptionType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.ListSetsType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.ResumptionTokenType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.SetType;
import com.lyncode.xoai.dataprovider.xml.xoai.Metadata;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.List;


public class ListSetsHandler extends VerbHandler<ListSetsType> {
    private static Logger log = LogManager.getLogger(ListSetsHandler.class);
    private final int maxListSize;
    private SetRepositoryHelper listSets;
    private XOAIContext context;
    private ResumptionTokenFormatter resumptionFormat;


    public ListSetsHandler(DateProvider formatter, int maxListSize, SetRepositoryHelper listSets, XOAIContext context, ResumptionTokenFormatter _format) {
        super(formatter);
        this.maxListSize = maxListSize;
        this.listSets = listSets;
        this.context = context;
        this.resumptionFormat = _format;
    }


    @Override
    public ListSetsType handle(OAIParameters params) throws OAIException, HandlerException {
        ListSetsType result = new ListSetsType();
        if (!listSets.supportSets())
            throw new DoesNotSupportSetsException();

        ResumptionToken resumptionToken = params.getResumptionToken();
        int length = maxListSize;
        log.debug("Length: " + length);
        ListSetsResult res = listSets.getSets(context, resumptionToken.getOffset(), length);
        List<Set> sets = res.getResults();

        if (sets.isEmpty() && resumptionToken.isEmpty())
            throw new NoMatchesException();

        if (sets.size() > length)
            sets = sets.subList(0, length);

        for (Set s : sets) {
            SetType set = new SetType();
            set.setSetName(s.getSetName());
            set.setSetSpec(s.getSetSpec());

            if (s.hasDescription()) {
                for (Metadata desc : s.getDescriptions())
                    set.getSetDescription().add(new DescriptionType(desc));
            }

            result.getSet().add(set);
        }
        ResumptionToken rtoken;
        if (res.hasMore()) {
            rtoken = new ResumptionToken(resumptionToken.getOffset() + length);
        } else {
            rtoken = new ResumptionToken();
        }


        if (params.hasResumptionToken() || !rtoken.isEmpty()) {
            ResumptionTokenType token = new ResumptionTokenType();
            token.setCursor(resumptionToken.getOffset() / maxListSize);

            if (res.hasTotalResults()) {
                int total = res.getTotalResults();
                token.setCompleteListSize(total);
                log.debug("Total results: " + total);
            } else {
                log.debug("Has no total results shown");
            }
            if (!rtoken.isEmpty())
                token.setValue(resumptionFormat.format(rtoken));
            result.setResumptionToken(token);
        }

        return result;
    }

}
