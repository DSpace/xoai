package com.lyncode.xoai.tests.dataprovider.stubs;

import com.lyncode.xoai.dataprovider.core.ListSetsResult;
import com.lyncode.xoai.dataprovider.core.Set;
import com.lyncode.xoai.dataprovider.data.AbstractSetRepository;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class StubbedSetRepository extends AbstractSetRepository {
    private boolean supportSets = true;
    private List<Set> sets = new ArrayList<Set>();

    public StubbedSetRepository doesntSupportSets () {
        this.supportSets = false;
        return this;
    }

    public StubbedSetRepository withSet (String name, String spec) {
        this.sets.add(new Set(name, spec));
        return this;
    }

    public StubbedSetRepository withRandomSets (int number) {
        for (int i = 0;i<number;i++) {
            this.sets.add(new Set("Set"+(i+1), randomAlphabetic(number)));
        }
        return this;
    }

    @Override
    public boolean supportSets() {
        return supportSets;
    }

    @Override
    public ListSetsResult retrieveSets(int offset, int length) {
        return new ListSetsResult(offset+length < this.sets.size(), this.sets.subList(offset, Math.min(offset+length, sets.size())));
    }

    @Override
    public boolean exists(String setSpec) {
        for (Set s : this.sets)
            if (s.getSetSpec().equals(setSpec))
                return true;

        return false;
    }
}
