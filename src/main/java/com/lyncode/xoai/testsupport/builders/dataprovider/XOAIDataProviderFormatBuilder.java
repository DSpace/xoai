package com.lyncode.xoai.testsupport.builders.dataprovider;

import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration;

public class XOAIDataProviderFormatBuilder {
    private String id;
    private String prefix;
    private String namespace;
    private String schemaLocation;
    private String xslt;

    public XOAIDataProviderFormatBuilder withId (String id) {
        this.id = id;
        return this;
    }
    public XOAIDataProviderFormatBuilder withXslt (String xslt) {
        this.xslt = xslt;
        return this;
    }
    public XOAIDataProviderFormatBuilder withPrefix (String prefix) {
        this.prefix = prefix;
        return this;
    }
    public XOAIDataProviderFormatBuilder withNamespace (String namespace) {
        this.namespace = namespace;
        return this;
    }
    public XOAIDataProviderFormatBuilder withSchemaLocation (String schemaLocation) {
        this.schemaLocation = schemaLocation;
        return this;
    }

    public Configuration.Formats.Format build () {
        Configuration.Formats.Format format = new Configuration.Formats.Format();
        format.setId(id);
        format.setPrefix(prefix);
        format.setNamespace(namespace);
        format.setSchemaLocation(schemaLocation);
        format.setXSLT(xslt);
        return format;
    }
}
