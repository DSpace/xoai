package com.lyncode.xoai.common.serviceprovider.verbs;

import java.util.Iterator;

import com.lyncode.xoai.common.serviceprovider.data.Set;
import com.lyncode.xoai.common.serviceprovider.iterators.SetIterator;
import com.lyncode.xoai.common.serviceprovider.xml.configuration.Configuration;

public class ListSets extends AbstractVerb implements Iterable<Set>
{

    public ListSets(Configuration config, String baseUrl)
    {
        super(config, baseUrl);
    }

    @Override
    public Iterator<Set> iterator()
    {
        return new SetIterator(super.getConfiguration(), super.getBaseUrl());
    }

}
