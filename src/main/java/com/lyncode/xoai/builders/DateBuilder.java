package com.lyncode.xoai.builders;

import java.util.Calendar;
import java.util.Date;

public class DateBuilder implements Builder<Date> {
    public static final int MIN_MILLISECONDS = 0;
    public static final int MAX_MILLISECONDS = 999;
    private Calendar calendar = Calendar.getInstance();

    public DateBuilder() {
    }

    public DateBuilder(Date time) {
        calendar.setTime(time);
    }

    public DateBuilder addDays(int days) {
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return this;
    }

    public DateBuilder subtractDays(int days) {
        calendar.add(Calendar.DAY_OF_YEAR, days * -1);
        return this;
    }

    public DateBuilder setMilliseconds(int milliseconds) {
        calendar.set(Calendar.MILLISECOND, milliseconds);
        return this;
    }

    public DateBuilder setMinMilliseconds() {
        calendar.set(Calendar.MILLISECOND, MIN_MILLISECONDS);
        return this;
    }

    public DateBuilder setMaxMilliseconds() {
        calendar.set(Calendar.MILLISECOND, MAX_MILLISECONDS);
        return this;
    }

    public Date build() {
        return calendar.getTime();
    }
}
