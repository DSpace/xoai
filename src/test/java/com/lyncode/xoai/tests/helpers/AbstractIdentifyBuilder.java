package com.lyncode.xoai.tests.helpers;

import com.lyncode.xoai.dataprovider.core.DeleteMethod;
import com.lyncode.xoai.dataprovider.core.Granularity;
import com.lyncode.xoai.dataprovider.data.AbstractIdentify;

import java.util.Date;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractIdentifyBuilder {
    private AbstractIdentify identify = mock(AbstractIdentify.class);
    private Granularity granularity = Granularity.Second;

    public AbstractIdentifyBuilder resolveAllAdminEmailsTo(String... email) {
        when(identify.getAdminEmails()).thenReturn(asList(email));
        return this;
    }

    public AbstractIdentifyBuilder resolveTheRepositoryNameTo(String name) {
        when(identify.getRepositoryName()).thenReturn(name);
        return this;
    }

    public AbstractIdentifyBuilder resolveTheDeletedMethodTo(DeleteMethod method) {
        when(identify.getDeleteMethod()).thenReturn(method);
        return this;
    }

    public AbstractIdentifyBuilder resolveTheGranularityTo(Granularity granularity) {
        this.granularity = granularity;
        when(identify.getGranularity()).thenReturn(granularity);
        return this;
    }
    public AbstractIdentifyBuilder resolveTheEarliestDateTo(Date date) {
        when(identify.getEarliestDate()).thenReturn(date);
        return this;
    }
    public AbstractIdentifyBuilder resolveAllDescriptionsTo(String... desc) {
        when(identify.getDescription()).thenReturn(asList(desc));
        return this;
    }
    public AbstractIdentifyBuilder resolveBaseUrlTo(String base) {
        when(identify.getBaseUrl()).thenReturn(base);
        return this;
    }

    public AbstractIdentifyBuilder and () {
        return this;
    }

    public AbstractIdentify build () {
        return identify;
    }

    public Granularity getGranularity() {
        return granularity;
    }
}
