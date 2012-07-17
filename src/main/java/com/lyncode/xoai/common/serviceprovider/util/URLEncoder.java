package com.lyncode.xoai.common.serviceprovider.util;

import java.io.UnsupportedEncodingException;

public class URLEncoder
{
    public static final String SEPARATOR = "&";
    
    public static String encode (String value) {
        try
        {
            return java.net.URLEncoder.encode(value, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            return value;
        }
    }
}
