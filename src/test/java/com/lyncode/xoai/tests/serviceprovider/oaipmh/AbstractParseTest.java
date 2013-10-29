package com.lyncode.xoai.tests.serviceprovider.oaipmh;

import com.lyncode.xoai.dataprovider.services.impl.BaseDateProvider;
import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import org.junit.Before;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractParseTest {


    private OAIServiceConfiguration config;

    @Before
    public void setUp () {
        config = mock(OAIServiceConfiguration.class);
        when(config.getFormatter()).thenReturn(new BaseDateProvider());
    }

    protected OAIServiceConfiguration theConfiguration () {
        return config;
    }

}
