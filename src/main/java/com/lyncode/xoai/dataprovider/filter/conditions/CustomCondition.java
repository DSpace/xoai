package com.lyncode.xoai.dataprovider.filter.conditions;

import com.lyncode.xoai.dataprovider.data.Filter;
import com.lyncode.xoai.dataprovider.data.ItemIdentifier;
import com.lyncode.xoai.dataprovider.services.api.FilterResolver;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.ParameterMap;

public class CustomCondition implements Condition {
    private FilterResolver filterResolver;
    private Class<? extends Filter> filterClass;
    private ParameterMap configuration;

    public CustomCondition(FilterResolver filterResolver, Class<? extends Filter> filterClass, ParameterMap configuration) {
        this.filterResolver = filterResolver;
        this.filterClass = filterClass;
        this.configuration = configuration;
    }


    @Override
    public boolean isItemShown(ItemIdentifier item) {
        return filterResolver.getFilterInstance(filterClass, configuration).isItemShown(item);
    }
}
