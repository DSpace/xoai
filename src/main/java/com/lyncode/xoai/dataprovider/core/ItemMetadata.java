package com.lyncode.xoai.dataprovider.core;

import com.lyncode.xoai.dataprovider.xml.xoai.Metadata;
import com.lyncode.xoai.dataprovider.xml.xoai.XOAIParser;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;

public class ItemMetadata {
    private static Logger log = LogManager.getLogger(ItemMetadata.class);
    private Metadata metadata;
    private String compiled;

    public ItemMetadata(Metadata meta) {
        metadata = meta;
    }

    public ItemMetadata(String compiledItem) {
        compiled = compiledItem;
    }

    public Metadata getMetadata() {
        if (metadata == null) {
            try {
                metadata = XOAIParser.parse(new ByteArrayInputStream(compiled.getBytes()));
            } catch (XMLStreamException e) {
                metadata = null;
            }
        }
        return metadata;
    }

    public boolean isCompiled() {
        return (compiled != null);
    }

    public String getCompiled() {
        return compiled;
    }
}
