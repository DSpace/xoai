package com.lyncode.xoai.dataprovider.data;

import com.lyncode.xoai.dataprovider.core.ResumptionToken;
import com.lyncode.xoai.dataprovider.exceptions.BadResumptionToken;


public abstract class AbstractResumptionTokenFormat {
    public abstract ResumptionToken parse(String resumptionToken) throws BadResumptionToken;

    public abstract String format(ResumptionToken resumptionToken);
}
