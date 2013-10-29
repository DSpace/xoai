package com.lyncode.xoai.tests.dataprovider.stubs;

import com.lyncode.xoai.builders.ListBuilder;
import com.lyncode.xoai.builders.MetadataBuilder;
import com.lyncode.xoai.builders.MetadataElementBuilder;
import com.lyncode.xoai.dataprovider.core.ItemMetadata;
import com.lyncode.xoai.dataprovider.core.ReferenceSet;
import com.lyncode.xoai.dataprovider.data.AbstractAbout;
import com.lyncode.xoai.dataprovider.data.AbstractItem;
import com.lyncode.xoai.dataprovider.xml.xoai.Metadata;

import java.util.*;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

public class StubbedItem extends AbstractItem {
    private Map<String, Object> values = new HashMap<String, Object>();

    public StubbedItem () {
        this.withDefauls();
    }

    public static StubbedItem randomItem() {
        return new StubbedItem()
                .with("identifier", randomAlphabetic(10))
                .with("datestamp", new Date())
                .with("sets", new ListBuilder<String>().add(randomAlphabetic(3)).build())
                .with("deleted", Integer.parseInt(randomNumeric(1)) > 5);
    }

    public StubbedItem with (String name, Object value) {
        values.put(name, value);
        return this;
    }

    public StubbedItem withSet (String name) {
        ((List<String>)values.get("sets")).add(name);
        return this;
    }

    @Override
    public List<AbstractAbout> getAbout() {
        return new ArrayList<AbstractAbout>();
    }

    @Override
    public ItemMetadata getMetadata() {
        return new ItemMetadata(toMetadata());
    }

    private Metadata toMetadata() {
        MetadataBuilder builder = new MetadataBuilder();
        for (String key : values.keySet()) {
            MetadataElementBuilder metadataElementBuilder = new MetadataElementBuilder().withName(key);
            Object value = values.get(key);
            if (value instanceof String)
                metadataElementBuilder.withField("value", (String) value);
            else if (value instanceof Date)
                metadataElementBuilder.withField("value", ((Date) value).toString());
            else if (value instanceof List) {
                List<String> obj = (List<String>) value;
                int i = 1;
                for (String e : obj)
                    metadataElementBuilder.withField("value-"+(i++), e);
            }
            builder.withElement(metadataElementBuilder.build());
        }
        return builder.build();
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
    public List<ReferenceSet> getSets() {
        List<String> list = ((List<String>) values.get("sets"));
        return new ListBuilder<String>().add(list.toArray(new String[list.size()])).build(new ListBuilder.Transformer<String, ReferenceSet>() {
            @Override
            public ReferenceSet transform(String elem) {
                return new ReferenceSet(elem);
            }
        });
    }

    @Override
    public boolean isDeleted() {
        return (Boolean) values.get("deleted");
    }

    private StubbedItem withDefauls() {
        this
                .with("identifier", randomAlphabetic(10))
                .with("datestamp", new Date())
                .with("sets", new ListBuilder<String>().add(randomAlphabetic(3)).build())
                .with("deleted", Integer.parseInt(randomNumeric(1)) > 5);
        return this;
    }
}
