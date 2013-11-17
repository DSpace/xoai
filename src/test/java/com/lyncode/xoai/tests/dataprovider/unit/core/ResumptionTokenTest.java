package com.lyncode.xoai.tests.dataprovider.unit.core;

import com.lyncode.xoai.dataprovider.core.ResumptionToken;
import com.lyncode.xoai.dataprovider.exceptions.*;
import com.lyncode.xoai.dataprovider.services.impl.DefaultResumptionTokenFormatter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ResumptionTokenTest {

    @Test
    public void defaultImplementationShouldReturnAValidMetadataPrefix() throws IllegalVerbException, BadArgumentException, BadResumptionToken, UnknownParameterException, DuplicateDefinitionException {
        DefaultResumptionTokenFormatter format = new DefaultResumptionTokenFormatter();

        ResumptionToken token = new ResumptionToken(100, "teste", null, null, null);
        ResumptionToken read = format.parse(format.format(token));

        assertEquals(read.getMetadataPrefix(), "teste");

    }

}
