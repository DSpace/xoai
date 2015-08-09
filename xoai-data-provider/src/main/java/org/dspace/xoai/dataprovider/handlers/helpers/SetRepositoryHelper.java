/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider.handlers.helpers;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dspace.xoai.dataprovider.handlers.results.ListSetsResult;
import org.dspace.xoai.dataprovider.model.Context;
import org.dspace.xoai.dataprovider.model.Set;
import org.dspace.xoai.dataprovider.repository.SetRepository;

import java.util.ArrayList;
import java.util.List;

public class SetRepositoryHelper {
    private static Logger log = LogManager.getLogger(SetRepositoryHelper.class);
    private SetRepository setRepository;

    public SetRepositoryHelper(SetRepository setRepository) {
        super();
        this.setRepository = setRepository;
    }

    public ListSetsResult getSets(Context context, int offset, int length) {
        List<Set> results = new ArrayList<Set>();
        List<Set> statics = context.getSets();
        if (offset < statics.size()) {
            log.debug("Offset less than static sets size");
            if (length + offset < statics.size()) {
                log.debug("Offset + length less than static sets size");
                for (int i = offset; i < (offset + length); i++)
                    results.add(statics.get(i));
                return new ListSetsResult(true, results);
            } else {
                log.debug("Offset + length greater or equal than static sets size");
                for (int i = offset; i < statics.size(); i++)
                    results.add(statics.get(i));
                int newLength = length - (statics.size() - offset);
                ListSetsResult res = setRepository.retrieveSets(0, newLength);
                results.addAll(res.getResults());
                if (!res.hasTotalResults())
                    return new ListSetsResult(res.hasMore(), results);
                else
                    return new ListSetsResult(res.hasMore(), results, res.getTotalResults() + statics.size());
            }
        } else {
            log.debug("Offset greater or equal than static sets size");
            int newOffset = offset - statics.size();
            ListSetsResult res = setRepository.retrieveSets(newOffset, length);
            results.addAll(res.getResults());
            if (!res.hasTotalResults())
                return new ListSetsResult(res.hasMore(), results);
            else
                return new ListSetsResult(res.hasMore(), results, res.getTotalResults() + statics.size());
        }
    }

    public boolean exists(Context context, String setSpec) {
        List<Set> statics = context.getSets();
        for (Set set : statics)
            if (set.getSpec().equals(set))
                return true;

        return setRepository.exists(setSpec);
    }

}
