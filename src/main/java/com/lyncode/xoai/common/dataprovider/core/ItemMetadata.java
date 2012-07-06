package com.lyncode.xoai.common.dataprovider.core;

import java.io.ByteArrayInputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.xoai.common.dataprovider.exceptions.MetadataBindException;
import com.lyncode.xoai.common.dataprovider.util.MarshallingUtils;
import com.lyncode.xoai.common.dataprovider.xml.xoai.Metadata;

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
