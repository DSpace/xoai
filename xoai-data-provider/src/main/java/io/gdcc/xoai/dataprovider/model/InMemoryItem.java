/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.model;

import io.gdcc.xoai.model.oaipmh.About;
import io.gdcc.xoai.model.oaipmh.Metadata;
import io.gdcc.xoai.model.xoai.Element;
import io.gdcc.xoai.model.xoai.XOAIMetadata;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.gdcc.xoai.util.Randoms.randomAlphabetic;
import static io.gdcc.xoai.util.Randoms.randomNumeric;

public class InMemoryItem implements Item {
    public static InMemoryItem item () {
        return new InMemoryItem();
    }

    private final Map<String, Object> values = new HashMap<>();

    public static InMemoryItem randomItem() {
        return new InMemoryItem()
                .with("identifier", randomAlphabetic(10))
                .with("datestamp", new Date())
                .with("sets", List.of(randomAlphabetic(3)))
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
    public List<Set> getSets() {
        return ((List<?>) values.get("sets")).stream()
            .filter(obj -> obj instanceof String)
            .map(String.class::cast)
            .map(Set::new)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean isDeleted() {
        return (Boolean) values.get("deleted");
    }

    public InMemoryItem withDefaults() {
        this
                .with("identifier", randomAlphabetic(10))
                .with("datestamp", new Date())
                .with("sets", List.of(randomAlphabetic(3)))
                .with("deleted", Integer.parseInt(randomNumeric(1)) > 5);
        return this;
    }

    public InMemoryItem withIdentifier(String identifier) {
        this.with("identifier", identifier);
        return this;
    }
}
