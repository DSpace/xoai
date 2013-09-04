package com.lyncode.xoai.dataprovider.handlers;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.xoai.dataprovider.core.ListSetsResult;
import com.lyncode.xoai.dataprovider.core.OAIParameters;
import com.lyncode.xoai.dataprovider.core.ResumptionToken;
import com.lyncode.xoai.dataprovider.core.Set;
import com.lyncode.xoai.dataprovider.core.XOAIContext;
import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.data.internal.SetRepository;
import com.lyncode.xoai.dataprovider.exceptions.DoesNotSupportSetsException;
import com.lyncode.xoai.dataprovider.exceptions.HandlerException;
import com.lyncode.xoai.dataprovider.exceptions.NoMatchesException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.xml.oaipmh.DescriptionType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.ListSetsType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.ResumptionTokenType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.SetType;
import com.lyncode.xoai.dataprovider.xml.xoai.Metadata;


public class ListSetsHandler extends VerbHandler<ListSetsType> {
    private static Logger log = LogManager.getLogger(ListSetsHandler.class);
    private SetRepository listSets;
    private XOAIContext context;

    
    public ListSetsHandler(SetRepository listSets, XOAIContext context) {

        super();
        this.listSets = listSets;
        this.context = context;
    }


    @Override
    public ListSetsType handle(OAIParameters params) throws OAIException, HandlerException {
        ListSetsType result = new ListSetsType();
        if (!listSets.supportSets())
            throw new DoesNotSupportSetsException();

        ResumptionToken resumptionToken = params.getResumptionToken();
        int length = XOAIManager.getManager().getMaxListSetsSize();
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
            token.setValue(rtoken.toString());
            token.setCursor(resumptionToken.getOffset()/XOAIManager.getManager().getMaxListSetsSize());
            
            if (res.hasTotalResults()) {
                int total = res.getTotalResults();
                token.setCompleteListSize(total);
                log.debug("Total results: "+total);
            } else {
                log.debug("Has no total results shown");
            }
            result.setResumptionToken(token);
        }

        return result;
    }

}
