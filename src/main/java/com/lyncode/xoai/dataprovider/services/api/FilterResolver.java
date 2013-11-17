package com.lyncode.xoai.dataprovider.services.api;

import com.lyncode.xoai.dataprovider.data.Filter;
import com.lyncode.xoai.dataprovider.filter.conditions.AndCondition;
import com.lyncode.xoai.dataprovider.filter.conditions.NotCondition;
import com.lyncode.xoai.dataprovider.filter.conditions.OrCondition;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.ParameterMap;

public interface FilterResolver {
    Filter getFilter(Class<? extends Filter> filterClass, ParameterMap configuration);

    Filter getFilter(AndCondition andCondition);

    Filter getFilter(OrCondition orCondition);

    Filter getFilter(NotCondition notCondition);
}
