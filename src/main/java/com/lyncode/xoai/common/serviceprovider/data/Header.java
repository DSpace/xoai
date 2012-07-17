package com.lyncode.xoai.common.serviceprovider.data;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Header implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 2040053560562673601L;

    private String identifier;

    private String datestamp;

    private String status;

    private ArrayList<String> setSpec;

    public Header()
    {
        setSpec = new ArrayList<String>();
    }

    public void addSpec(String spec)
    {
        setSpec.add(spec);
    }

    public List<String> getSpecList()
    {
        return setSpec;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    public String getDatestamp()
    {
        return datestamp;
    }

    public void setDatestamp(String datestamp)
    {
        this.datestamp = datestamp;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
