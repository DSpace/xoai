package com.lyncode.xoai.dataprovider.services.api;

import com.lyncode.xoai.dataprovider.core.Granularity;

import java.text.ParseException;
import java.util.Date;

public interface DateProvider {
    String format(Date date, Granularity granularity);
    Date parse (String date, Granularity granularity) throws ParseException;
    Date parse(String date) throws ParseException;
    String format(Date date);
    Date now();
}
