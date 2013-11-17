package com.lyncode.xoai.dataprovider.services.api;


import com.lyncode.xoai.dataprovider.core.ResumptionToken;
import com.lyncode.xoai.dataprovider.exceptions.BadResumptionToken;

public interface ResumptionTokenFormatter {
    ResumptionToken parse(String resumptionToken) throws BadResumptionToken;

    String format(ResumptionToken resumptionToken);
}
