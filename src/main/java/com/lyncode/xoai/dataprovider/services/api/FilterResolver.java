package com.lyncode.xoai.dataprovider.services.api;

import com.lyncode.xoai.dataprovider.data.Filter;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.ParameterMap;

public interface FilterResolver {
    Filter getFilterInstance(Class<? extends Filter> filterClass, ParameterMap configuration);
}
