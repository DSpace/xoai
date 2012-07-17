
package com.lyncode.xoai.common.serviceprovider.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

public class Metadata implements Serializable {
    private static final long serialVersionUID = 638219179011570764L;
    private String value;
    
    public Metadata (String value) {
        this.value = value;
    }
    
    public InputStream getMetadata () {
        return new ByteArrayInputStream(this.value.getBytes());
    }
}
