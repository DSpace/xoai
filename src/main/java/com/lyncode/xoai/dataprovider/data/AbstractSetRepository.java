/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.xoai.dataprovider.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.xoai.dataprovider.core.ListSetsResult;
import com.lyncode.xoai.dataprovider.core.Set;
import com.lyncode.xoai.dataprovider.core.XOAIContext;
import com.lyncode.xoai.dataprovider.sets.StaticSet;

/**
 * @author DSpace @ Lyncode
 * @version 2.2.1
 */
public abstract class AbstractSetRepository {
	private static Logger log = LogManager
			.getLogger(AbstractSetRepository.class);

	public abstract boolean supportSets();

	protected abstract ListSetsResult retrieveSets(int offset, int length);
	protected abstract boolean exists(String setSpec);

	public boolean exists(XOAIContext context, String set) {
		List<StaticSet> statics = context.getStaticSets();
		for (StaticSet s : statics)
			if (s.getSetSpec().equals(set))
				return true;

		return exists(set);
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
				ListSetsResult res = this.retrieveSets(0, newLength);
				results.addAll(res.getResults());
				return new ListSetsResult(res.hasMore(), results);
			}
		} else {
			log.debug("Offset greater or equal than static sets size");
			int newOffset = offset - statics.size();
			return this.retrieveSets(newOffset, length);
		}
	}
}
