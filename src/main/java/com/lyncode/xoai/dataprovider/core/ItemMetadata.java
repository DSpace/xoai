package com.lyncode.xoai.dataprovider.core;

import java.io.ByteArrayInputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.xoai.dataprovider.exceptions.MetadataBindException;
import com.lyncode.xoai.dataprovider.xml.xoai.Metadata;
import com.lyncode.xoai.util.MarshallingUtils;

public class ItemMetadata
{
    private static Logger log = LogManager.getLogger(ItemMetadata.class);
    private Metadata metadata;
    private String compiled;
    
    public ItemMetadata (Metadata meta) {
        metadata = meta;
    }
    
    public ItemMetadata (String compiledItem) {
        compiled = compiledItem;
    }
    
    public Metadata getMetadata () {
        if (metadata == null) {
            try
            {
                metadata = MarshallingUtils.readMetadata(new ByteArrayInputStream(compiled.getBytes()));
            }
            catch (MetadataBindException e)
            {
                log.error(e.getMessage(), e);
            }
        }
        return metadata;
    }
    
    public boolean isCompiled () {
        return (compiled != null);
    }
    
    public String getCompiled () {
        return compiled;
    }
}
