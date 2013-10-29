package com.lyncode.xoai.tests.helpers;

import com.lyncode.xoai.dataprovider.core.ListSetsResult;
import com.lyncode.xoai.dataprovider.core.Set;
import com.lyncode.xoai.dataprovider.data.AbstractSetRepository;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractSetRepositoryBuilder {
    private AbstractSetRepository setRepository = mock(AbstractSetRepository.class);


    public AbstractSetRepositoryBuilder contains (Set... sets) {
        ListSetsResult result = new ListSetsResult(false, asList(sets));
        when(setRepository.supportSets()).thenReturn(true);
        when(setRepository.retrieveSets(anyInt(), anyInt())).thenReturn(result);
        return this;
    }

    public AbstractSetRepositoryBuilder containsPages (int pageSize, List<List<Set>> pages) {
        when(setRepository.supportSets()).thenReturn(true);
        for (int i=0;i<pages.size();i++) {
            ListSetsResult result = new ListSetsResult(i + 1 < pages.size(), pages.get(i));
            when(setRepository.retrieveSets(i*pageSize, i*pageSize+pageSize)).thenReturn(result);
        }
        return this;
    }

    public AbstractSetRepositoryBuilder supportsSets () {
        when(setRepository.supportSets()).thenReturn(true);
        return this;
    }


    public AbstractSetRepositoryBuilder and () {
        return this;
    }

    public AbstractSetRepository build () {
        return setRepository;
    }

    public AbstractSetRepositoryBuilder doesNotSupportSets() {
        when(setRepository.supportSets()).thenReturn(false);
        return this;
    }
}
