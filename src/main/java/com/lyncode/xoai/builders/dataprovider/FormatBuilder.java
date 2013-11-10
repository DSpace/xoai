package com.lyncode.xoai.builders.dataprovider;

import com.lyncode.xoai.builders.Builder;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration;

public class FormatBuilder implements Builder<Configuration.Formats.Format> {
    private String id;
    private String prefix;
    private String namespace;
    private String schemaLocation;
    private String xslt;

    public FormatBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public FormatBuilder withXslt(String xslt) {
        this.xslt = xslt;
        return this;
    }

    public FormatBuilder withPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public FormatBuilder withNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public FormatBuilder withSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
        return this;
    }

    public Configuration.Formats.Format build() {
        Configuration.Formats.Format format = new Configuration.Formats.Format();
        format.setId(id);
        format.setPrefix(prefix);
        format.setNamespace(namespace);
        format.setSchemaLocation(schemaLocation);
        format.setXSLT(xslt);
        return format;
    }
}
