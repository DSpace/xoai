package com.lyncode.xoai.dataprovider.core;

import com.lyncode.xoai.dataprovider.data.DefaultResumptionTokenFormat;
import com.lyncode.xoai.dataprovider.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ResumptionTokenTest {

    @Before
    public void setUp() throws Exception {

    }


    @Test
    public void test() throws IllegalVerbException, BadArgumentException, BadResumptionToken, UnknownParameterException, DuplicateDefinitionException {
        DefaultResumptionTokenFormat format = new DefaultResumptionTokenFormat();

        ResumptionToken token = new ResumptionToken(100, "teste", null, null, null);
        ResumptionToken read = format.parse(format.format(token));

        assertEquals(read.getMetadatePrefix(), "teste");

    }

}
