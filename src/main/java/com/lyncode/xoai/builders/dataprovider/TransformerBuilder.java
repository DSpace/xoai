package com.lyncode.xoai.builders.dataprovider;

import com.lyncode.xoai.builders.Builder;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration;

public class TransformerBuilder implements Builder<Configuration.Transformers.Transformer> {
    private String xslt;
    private String description;
    private String id;

    public TransformerBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public TransformerBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TransformerBuilder withXslt(String xslt) {
        this.xslt = xslt;
        return this;
    }

    @Override
    public Configuration.Transformers.Transformer build() {
        Configuration.Transformers.Transformer transformer = new Configuration.Transformers.Transformer();
        transformer.setId(id);
        transformer.setDescription(description);
        transformer.setXSLT(xslt);
        return transformer;
    }
}
