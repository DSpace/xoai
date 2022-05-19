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
import io.gdcc.xoai.services.api.DateProvider;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.gdcc.xoai.util.Randoms.randomAlphabetic;
import static io.gdcc.xoai.util.Randoms.randomNumeric;

public class InMemoryItem implements Item {
    
    private final Map<String, Object> values = new HashMap<>();
    private Metadata metadata;
    
    @Override
    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public String getIdentifier() {
        return (String) values.get("identifier");
    }

    @Override
    public Instant getDatestamp() {
        return (Instant) values.get("datestamp");
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

    // BUILDERS
    
    private InMemoryItem with(String name, Object value) {
        values.put(name, value);
        return this;
    }
    
    public InMemoryItem withIdentifier(String identifier) {
        return this.with("identifier", identifier);
    }
    
    @SuppressWarnings("unchecked")
    public InMemoryItem withSet(String spec) {
        if (values.containsKey("sets")) {
            ((List<String>) values.get("sets")).add(spec);
        } else {
            values.put("sets", new ArrayList<>(List.of(spec)));
        }
        return this;
    }
    
    public InMemoryItem withMetadata(Metadata metadata) {
        Objects.requireNonNull(metadata, "Metadata may not be null");
        this.metadata = metadata;
        return this;
    }
    
    public InMemoryItem withDeleted(Boolean deleted) {
        return this.with("deleted", deleted);
    }
    
    // GENERATORS
    public static InMemoryItem randomItem() {
        InMemoryItem item = new InMemoryItem();
        return item
            .with("identifier", randomAlphabetic(10))
            .with("datestamp", DateProvider.now())
            .withSet(randomAlphabetic(3))
            .with("deleted", Integer.parseInt(randomNumeric(1)) > 5)
            .withMetadata(new Metadata(generateXoaiMetadata(item.values)));
    }
    
    @SuppressWarnings("unchecked")
    private static XOAIMetadata generateXoaiMetadata(Map<String, Object> values) {
        XOAIMetadata builder = new XOAIMetadata();
        for (String key : values.keySet()) {
            Element elementBuilder = new Element(key);
            Object value = values.get(key);
            if (value instanceof String)
                elementBuilder.withField(key, (String) value);
            else if (value instanceof Instant)
                elementBuilder.withField(key, value.toString());
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
}
