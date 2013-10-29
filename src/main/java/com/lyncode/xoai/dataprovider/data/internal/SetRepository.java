package com.lyncode.xoai.dataprovider.data.internal;

import java.util.ArrayList;
import java.util.List;

import com.lyncode.xoai.dataprovider.core.ListSetsResult;
import com.lyncode.xoai.dataprovider.core.Set;
import com.lyncode.xoai.dataprovider.core.XOAIContext;
import com.lyncode.xoai.dataprovider.data.AbstractSetRepository;
import com.lyncode.xoai.dataprovider.sets.StaticSet;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class SetRepository {
	private static Logger log = LogManager.getLogger(SetRepository.class);
	private AbstractSetRepository setRepository;

	public SetRepository(AbstractSetRepository setRepository) {
		super();
		this.setRepository = setRepository;
	}
	
	public ListSetsResult getSets(XOAIContext context, int offset, int length) {
		List<Set> results = new ArrayList<Set>();
		List<StaticSet> statics = context.getStaticSets();
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
	
	public boolean exists(XOAIContext context, String set) {
		List<StaticSet> statics = context.getStaticSets();
		for (StaticSet s : statics)
			if (s.getSetSpec().equals(set))
				return true;

		return setRepository.exists(set);
	}

	public boolean supportSets() {
		return setRepository.supportSets();
	}
}
