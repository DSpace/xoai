/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider.model;

import com.google.common.base.Function;
import com.lyncode.builder.ListBuilder;
import org.dspace.xoai.model.oaipmh.About;
import org.dspace.xoai.model.oaipmh.Metadata;
import org.dspace.xoai.model.xoai.Element;
import org.dspace.xoai.model.xoai.XOAIMetadata;

import java.util.*;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

public class InMemoryItem implements Item {
    public static InMemoryItem item () {
        return new InMemoryItem();
    }

    private Map<String, Object> values = new HashMap<String, Object>();

    public static InMemoryItem randomItem() {
        return new InMemoryItem()
                .with("identifier", randomAlphabetic(10))
                .with("datestamp", new Date())
                .with("sets", new ListBuilder<String>().add(randomAlphabetic(3)).build())
                .with("deleted", Integer.parseInt(randomNumeric(1)) > 5);
    }

    public InMemoryItem with(String name, Object value) {
        values.put(name, value);
        return this;
    }

    public InMemoryItem withSet(String name) {
        ((List<String>) values.get("sets")).add(name);
        return this;
    }

    @Override
    public List<About> getAbout() {
        return new ArrayList<About>();
    }

    @Override
    public Metadata getMetadata() {
        return new Metadata(toMetadata());
    }

    private XOAIMetadata toMetadata() {
        XOAIMetadata builder = new XOAIMetadata();
        for (String key : values.keySet()) {
            Element elementBuilder = new Element(key);
            Object value = values.get(key);
            if (value instanceof String)
                elementBuilder.withField(key, (String) value);
            else if (value instanceof Date)
                elementBuilder.withField(key, ((Date) value).toString());
            else if (value instanceof List) {
                List<String> obj = (List<String>) value;
                int i = 1;
                for (String e : obj)
                    elementBuilder.withField(key + (i++), e);
            }
            builder.withElement(elementBuilder);
        }
        return builder;
    }

    @Override
    public String getIdentifier() {
        return (String) values.get("identifier");
    }

    @Override
    public Date getDatestamp() {
        return (Date) values.get("datestamp");
    }

    @Override
    public List<org.dspace.xoai.dataprovider.model.Set> getSets() {
        List<String> list = ((List<String>) values.get("sets"));
        return new ListBuilder<String>().add(list.toArray(new String[list.size()])).build(new Function<String, org.dspace.xoai.dataprovider.model.Set>() {
            @Override
            public org.dspace.xoai.dataprovider.model.Set apply(String elem) {
                return new org.dspace.xoai.dataprovider.model.Set(elem);
            }
        });
    }

    @Override
    public boolean isDeleted() {
        return (Boolean) values.get("deleted");
    }

    public InMemoryItem withDefaults() {
        this
                .with("identifier", randomAlphabetic(10))
                .with("datestamp", new Date())
                .with("sets", new ListBuilder<String>().add(randomAlphabetic(3)).build())
                .with("deleted", Integer.parseInt(randomNumeric(1)) > 5);
        return this;
    }

    public InMemoryItem withIdentifier(String identifier) {
        this.with("identifier", identifier);
        return this;
    }
}
