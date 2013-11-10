package com.lyncode.xoai.builders.dataprovider;

import com.lyncode.xoai.builders.Builder;
import com.lyncode.xoai.dataprovider.core.ItemMetadata;
import com.lyncode.xoai.dataprovider.xml.xoai.Metadata;

public class ItemMetadataBuilder implements Builder<ItemMetadata> {
    private String compiled;
    private Metadata meta;
    private MetadataBuilder builder;

    public ItemMetadataBuilder withCompiled(String compiled) {
        this.compiled = compiled;
        return this;
    }

    public ItemMetadataBuilder withMetadata(Metadata meta) {
        this.meta = meta;
        return this;
    }

    public ItemMetadataBuilder withMetadata(MetadataBuilder meta) {
        this.builder = meta;
        return this;
    }

    public ItemMetadata build() {
        if (meta != null)
            return new ItemMetadata(meta);
        else if (builder != null)
            return new ItemMetadata(builder.build());
        else
            return new ItemMetadata(compiled);
    }
}
