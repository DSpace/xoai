package com.lyncode.xoai.dataprovider.xml.oaipmh;

import java.util.Date;

import com.lyncode.xoai.util.DateUtils;


public class DateInfo {
    private Date date;
    private GranularityType granularity;
    
    public DateInfo (Date date, GranularityType granularity) {
        this.date = date;
        this.granularity = granularity;
    }
    
    public Date getDate() {

        return date;
    }
    public void setDate(Date date) {

        this.date = date;
    }
    public GranularityType getGranularity() {

        return granularity;
    }
    public void setGranularity(GranularityType granularity) {

        this.granularity = granularity;
    }

    public String toString () {
        switch (this.granularity) {
            case YYYY_MM_DD:
                return DateUtils.formatOnlyDate(this.date);
            default:
                return DateUtils.format(this.date);
        }
    }
}
