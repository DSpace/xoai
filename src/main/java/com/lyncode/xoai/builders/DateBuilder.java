package com.lyncode.xoai.builders;

import java.util.Calendar;
import java.util.Date;

public class DateBuilder {
    private Calendar calendar = Calendar.getInstance();

    public DateBuilder addDays (int days) {
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return this;
    }

    public Date build () {
        return calendar.getTime();
    }

    public DateBuilder subtractDays(int days) {
        calendar.add(Calendar.DAY_OF_YEAR, days * -1);
        return this;
    }
}
