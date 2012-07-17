package com.lyncode.xoai.common.serviceprovider.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils
{
    public static String fromDate (Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return format.format(date);
    }
}
