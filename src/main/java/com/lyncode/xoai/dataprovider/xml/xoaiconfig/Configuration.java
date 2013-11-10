package com.lyncode.xoai.dataprovider.xml.xoaiconfig;

import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "contexts",
        "formats",
        "transformers",
        "filters",
        "sets"
})
@XmlRootElement(name = "Configuration")
public class Configuration {
    public static Configuration readConfiguration(InputStream input) throws ConfigurationException {
        try {
            JAXBContext context = JAXBContext.newInstance(Configuration.class
                    .getPackage().getName());
            Unmarshaller marshaller = context.createUnmarshaller();
            Object obj = marshaller.unmarshal(input);
            input.close();
            if (obj instanceof Configuration) {
                return (Configuration) obj;
            } else
                throw new ConfigurationException("Invalid configuration bundle");
        } catch (IOException ex) {
            throw new ConfigurationException(ex.getMessage(), ex);
        } catch (JAXBException ex) {
            throw new ConfigurationException(ex.getMessage(), ex);
        }
    }


    @XmlElement(name = "Contexts", required = true)
    private Configuration.Contexts contexts;
    @XmlElement(name = "Formats", required = true)
    private Configuration.Formats formats;
    @XmlElement(name = "Transformers")
    private Configuration.Transformers transformers;
    @XmlElement(name = "Filters")
    private Configuration.Filters filters;
    @XmlElement(name = "Sets")
    private Configuration.Sets sets;
    @XmlAttribute(name = "maxListRecordsSize")
    private Integer maxListRecordsSize;
    @XmlAttribute(name = "maxListSetsSize")
    private Integer maxListSetsSize;
    @XmlAttribute(name = "maxListIdentifiersSize")
    private Integer maxListIdentifiersSize;
    @XmlAttribute(name = "indentation")
    private Boolean indentation;
    @XmlAttribute(name = "stylesheet")
    private String stylesheet;
    @XmlAttribute(name = "descriptionFile")
    private String descriptionFile;

    public void write(OutputStream output) throws ConfigurationException {
        try {
            JAXBContext context = JAXBContext.newInstance(Configuration.class
                    .getPackage().getName());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(this, output);
            output.close();
        } catch (IOException ex) {
            throw new ConfigurationException(ex.getMessage(), ex);
        } catch (JAXBException ex) {
            throw new ConfigurationException(ex.getMessage(), ex);
        }
    }

    /**
     * Gets the value of the contexts property.
     *
     * @return possible object is
     *         {@link Configuration.Contexts }
     */
    public Configuration.Contexts getContexts() {
        return contexts;
    }

    /**
     * Sets the value of the contexts property.
     *
     * @param value allowed object is
     *              {@link Configuration.Contexts }
     */
    public void setContexts(Configuration.Contexts value) {
        this.contexts = value;
    }

    /**
     * Gets the value of the formats property.
     *
     * @return possible object is
     *         {@link Configuration.Formats }
     */
    public Configuration.Formats getFormats() {
        return formats;
    }

    /**
     * Sets the value of the formats property.
     *
     * @param value allowed object is
     *              {@link Configuration.Formats }
     */
    public void setFormats(Configuration.Formats value) {
        this.formats = value;
    }

    /**
     * Gets the value of the transformers property.
     *
     * @return possible object is
     *         {@link Configuration.Transformers }
     */
    public Configuration.Transformers getTransformers() {
        return transformers;
    }

    /**
     * Sets the value of the transformers property.
     *
     * @param value allowed object is
     *              {@link Configuration.Transformers }
     */
    public void setTransformers(Configuration.Transformers value) {
        this.transformers = value;
    }

    /**
     * Gets the value of the filters property.
     *
     * @return possible object is
     *         {@link Configuration.Filters }
     */
    public Configuration.Filters getFilters() {
        return filters;
    }

    /**
     * Sets the value of the filters property.
     *
     * @param value allowed object is
     *              {@link Configuration.Filters }
     */
    public void setFilters(Configuration.Filters value) {
        this.filters = value;
    }

    /**
     * Gets the value of the sets property.
     *
     * @return possible object is
     *         {@link Configuration.Sets }
     */
    public Configuration.Sets getSets() {
        return sets;
    }

    /**
     * Sets the value of the sets property.
     *
     * @param value allowed object is
     *              {@link Configuration.Sets }
     */
    public void setSets(Configuration.Sets value) {
        this.sets = value;
    }

    /**
     * Gets the value of the maxListRecordsSize property.
     *
     * @return possible object is
     *         {@link Integer }
     */
    public int getMaxListRecordsSize() {
        if (maxListRecordsSize == null) {
            return 100;
        } else {
            return maxListRecordsSize;
        }
    }

    /**
     * Sets the value of the maxListRecordsSize property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setMaxListRecordsSize(Integer value) {
        this.maxListRecordsSize = value;
    }

    /**
     * Gets the value of the maxListSetsSize property.
     *
     * @return possible object is
     *         {@link Integer }
     */
    public int getMaxListSetsSize() {
        if (maxListSetsSize == null) {
            return 100;
        } else {
            return maxListSetsSize;
        }
    }

    /**
     * Sets the value of the maxListSetsSize property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setMaxListSetsSize(Integer value) {
        this.maxListSetsSize = value;
    }

    /**
     * Gets the value of the maxListIdentifiersSize property.
     *
     * @return possible object is
     *         {@link Integer }
     */
    public int getMaxListIdentifiersSize() {
        if (maxListIdentifiersSize == null) {
            return 100;
        } else {
            return maxListIdentifiersSize;
        }
    }

    /**
     * Sets the value of the maxListIdentifiersSize property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setMaxListIdentifiersSize(Integer value) {
        this.maxListIdentifiersSize = value;
    }

    /**
     * Gets the value of the indentation property.
     *
     * @return possible object is
     *         {@link Boolean }
     */
    public boolean isIndentation() {
        if (indentation == null) {
            return false;
        } else {
            return indentation;
        }
    }

    /**
     * Sets the value of the indentation property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setIndentation(Boolean value) {
        this.indentation = value;
    }

    /**
     * Gets the value of the stylesheet property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getStylesheet() {
        return stylesheet;
    }

    /**
     * Sets the value of the stylesheet property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStylesheet(String value) {
        this.stylesheet = value;
    }

    /**
     * Gets the value of the descriptionFile property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getDescriptionFile() {
        return descriptionFile;
    }

    /**
     * Sets the value of the descriptionFile property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDescriptionFile(String value) {
        this.descriptionFile = value;
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "context"
    })
    public static class Contexts {

        @XmlElement(name = "Context", required = true)
        private List<Configuration.Contexts.Context> context;

        public List<Configuration.Contexts.Context> getContext() {
            if (context == null) {
                context = new ArrayList<Configuration.Contexts.Context>();
            }
            return this.context;
        }


        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "transformer",
                "filter",
                "set",
                "format",
                "description"
        })
        public static class Context {

            @XmlElement(name = "Transformer")
            private BundleReference transformer;
            @XmlElement(name = "Filter")
            private List<BundleReference> filter;
            @XmlElement(name = "Set")
            private List<BundleReference> set;
            @XmlElement(name = "Format", required = true)
            private List<BundleReference> format;
            @XmlElement(name = "Description", required = false)
            private String description;
            @XmlAttribute(name = "name", required = false)
            private String name;

            @XmlAttribute(name = "baseurl")
            private String baseurl;

            /**
             * Gets the value of the transformer property.
             *
             * @return possible object is
             *         {@link BundleReference }
             */
            public BundleReference getTransformer() {
                return transformer;
            }

            public String getDescription() {
                if (description != null)
                    return description.trim();
                return null;
            }

            public void setDescription(String description) {
                this.description = description;
            }


            /**
             * Sets the value of the transformer property.
             *
             * @param value allowed object is
             *              {@link BundleReference }
             */
            public void setTransformer(BundleReference value) {
                this.transformer = value;
            }

            public List<BundleReference> getFilter() {
                if (filter == null) {
                    filter = new ArrayList<BundleReference>();
                }
                return this.filter;
            }

            public List<BundleReference> getSet() {
                if (set == null) {
                    set = new ArrayList<BundleReference>();
                }
                return this.set;
            }

            public List<BundleReference> getFormat() {
                if (format == null) {
                    format = new ArrayList<BundleReference>();
                }
                return this.format;
            }

            /**
             * Gets the value of the baseurl property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getBaseUrl() {
                return baseurl;
            }

            /**
             * Sets the value of the baseurl property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setBaseurl(String value) {
                this.baseurl = value;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "customFilter",
            "filter"
    })
    public static class Filters {

        @XmlElement(name = "CustomFilter")
        private List<Configuration.Filters.CustomFilter> customFilter;
        @XmlElement(name = "Filter")
        private List<Configuration.Filters.Filter> filter;

        public List<Configuration.Filters.CustomFilter> getCustomFilter() {
            if (customFilter == null) {
                customFilter = new ArrayList<Configuration.Filters.CustomFilter>();
            }
            return this.customFilter;
        }

        public List<Configuration.Filters.Filter> getFilter() {
            if (filter == null) {
                filter = new ArrayList<Configuration.Filters.Filter>();
            }
            return this.filter;
        }


        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "clazz",
                "parameter"
        })
        public static class CustomFilter implements Referable {

            @XmlElement(name = "Class", required = true)
            private String clazz;
            @XmlElement(name = "Parameter")
            private List<Parameter> parameter;
            @XmlAttribute(name = "id")
            private String id;

            /**
             * Gets the value of the clazz property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getClazz() {
                return clazz;
            }

            /**
             * Sets the value of the clazz property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setClazz(String value) {
                this.clazz = value;
            }

            public List<Parameter> getParameter() {
                if (parameter == null) {
                    parameter = new ArrayList<Parameter>();
                }
                return this.parameter;
            }

            /**
             * Gets the value of the id property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getId() {
                return id;
            }

            /**
             * Sets the value of the id property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setId(String value) {
                this.id = value;
            }

        }


        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "definition"
        })
        public static class Filter implements Referable {

            private ConditionDefinitionType definition;
            @XmlAttribute(name = "id")
            private String id;

            /**
             * Gets the value of the definition property.
             *
             * @return possible object is
             *         {@link ConditionDefinitionType }
             */
            public ConditionDefinitionType getDefinition() {
                return definition;
            }

            /**
             * Sets the value of the definition property.
             *
             * @param value allowed object is
             *              {@link ConditionDefinitionType }
             */
            public void setDefinition(ConditionDefinitionType value) {
                this.definition = value;
            }

            /**
             * Gets the value of the id property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getId() {
                return id;
            }

            /**
             * Sets the value of the id property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setId(String value) {
                this.id = value;
            }

        }

    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "format"
    })
    public static class Formats {

        @XmlElement(name = "Format", required = true)
        private List<Configuration.Formats.Format> format;

        public List<Configuration.Formats.Format> getFormat() {
            if (format == null) {
                format = new ArrayList<Configuration.Formats.Format>();
            }
            return this.format;
        }


        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "prefix",
                "xslt",
                "namespace",
                "schemaLocation",
                "filter"
        })
        public static class Format implements Referable {

            @XmlElement(name = "Prefix", required = true)
            private String prefix;
            @XmlElement(name = "XSLT", required = true)
            private String xslt;
            @XmlElement(name = "Namespace", required = true)
            private String namespace;
            @XmlElement(name = "SchemaLocation", required = true)
            private String schemaLocation;
            @XmlElement(name = "ScopedFilter")
            private List<BundleReference> filter;
            @XmlAttribute(name = "id")
            private String id;

            /**
             * Gets the value of the prefix property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getPrefix() {
                return prefix;
            }

            /**
             * Sets the value of the prefix property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setPrefix(String value) {
                this.prefix = value;
            }

            /**
             * Gets the value of the xslt property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getXSLT() {
                return xslt;
            }

            /**
             * Sets the value of the xslt property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setXSLT(String value) {
                this.xslt = value;
            }

            /**
             * Gets the value of the namespace property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getNamespace() {
                return namespace;
            }

            /**
             * Sets the value of the namespace property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setNamespace(String value) {
                this.namespace = value;
            }

            /**
             * Gets the value of the schemaLocation property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getSchemaLocation() {
                return schemaLocation;
            }

            /**
             * Sets the value of the schemaLocation property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setSchemaLocation(String value) {
                this.schemaLocation = value;
            }

            public List<BundleReference> getFilter() {
                if (filter == null) {
                    filter = new ArrayList<BundleReference>();
                }
                return this.filter;
            }

            /**
             * Gets the value of the id property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getId() {
                return id;
            }

            /**
             * Sets the value of the id property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setId(String value) {
                this.id = value;
            }

        }

    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "set"
    })
    public static class Sets {

        @XmlElement(name = "Set", required = true)
        private List<Configuration.Sets.Set> set;

        public List<Configuration.Sets.Set> getSet() {
            if (set == null) {
                set = new ArrayList<Configuration.Sets.Set>();
            }
            return this.set;
        }


        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "pattern",
                "name",
                "filter",
                "parameter"
        })
        public static class Set implements Referable {

            @XmlElement(name = "Pattern", required = true)
            private String pattern;
            @XmlElement(name = "Name", required = true)
            private String name;
            @XmlElement(name = "ScopedFilter")
            private List<BundleReference> filter;
            @XmlElement(name = "Parameter")
            private List<Parameter> parameter;
            @XmlAttribute(name = "id")
            private String id;

            /**
             * Gets the value of the pattern property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getPattern() {
                return pattern;
            }

            /**
             * Sets the value of the pattern property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setPattern(String value) {
                this.pattern = value;
            }

            /**
             * Gets the value of the name property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setName(String value) {
                this.name = value;
            }

            public List<BundleReference> getFilter() {
                if (filter == null) {
                    filter = new ArrayList<BundleReference>();
                }
                return this.filter;
            }

            public List<Parameter> getParameter() {
                if (parameter == null) {
                    parameter = new ArrayList<Parameter>();
                }
                return this.parameter;
            }

            /**
             * Gets the value of the id property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getId() {
                return id;
            }

            /**
             * Sets the value of the id property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setId(String value) {
                this.id = value;
            }

        }

    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "transformer"
    })
    public static class Transformers {

        @XmlElement(name = "Transformer", required = true)
        private List<Configuration.Transformers.Transformer> transformer;

        public List<Configuration.Transformers.Transformer> getTransformer() {
            if (transformer == null) {
                transformer = new ArrayList<Configuration.Transformers.Transformer>();
            }
            return this.transformer;
        }


        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "xslt",
                "description"
        })
        public static class Transformer implements Referable {

            @XmlElement(name = "XSLT", required = true)
            private String xslt;
            @XmlElement(name = "Description", required = false)
            private String description;
            @XmlAttribute(name = "id")
            private String id;


            public String getDescription() {
                if (description != null)
                    return description.trim();
                return null;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            /**
             * Gets the value of the xslt property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getXSLT() {
                return xslt;
            }

            /**
             * Sets the value of the xslt property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setXSLT(String value) {
                this.xslt = value;
            }

            /**
             * Gets the value of the id property.
             *
             * @return possible object is
             *         {@link String }
             */
            public String getId() {
                return id;
            }

            /**
             * Sets the value of the id property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setId(String value) {
                this.id = value;
            }

        }

    }

}
