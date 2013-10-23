package com.lyncode.xoai.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;


public class DateUtilsTest {

    @Before
    public void setUp() throws Exception {

    }





    @Test
    public void test() throws ParseException {
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.MILLISECOND, 0);
        d = c.getTime();
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = format.format(d);
        
        assertEquals(d.getTime(), DateUtils.parse(date).getTime());
    }

}
