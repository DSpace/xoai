package com.lyncode.xoai.tests.helpers;


import com.lyncode.xoai.builders.ListBuilder;
import com.lyncode.xoai.builders.dataprovider.ElementBuilder;
import com.lyncode.xoai.builders.dataprovider.ItemMetadataBuilder;
import com.lyncode.xoai.builders.dataprovider.MetadataBuilder;
import com.lyncode.xoai.dataprovider.data.Item;
import com.lyncode.xoai.dataprovider.xml.xoai.Element;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractItemBuilder {
    private Item abstractItem = mock(Item.class);
    private MetadataBuilder metadataBuilder = new MetadataBuilder();
    private ItemMetadataBuilder itemMetadataBuilder = new ItemMetadataBuilder();

    public Item build() {
        when(abstractItem.getMetadata()).thenReturn(itemMetadataBuilder.withMetadata(metadataBuilder).build());
        return abstractItem;
    }

    public AbstractItemBuilder withMetadata(ElementBuilder element) {
        metadataBuilder.withElement(element.build());
        return this;
    }

    public AbstractItemBuilder withMetadata(ElementBuilder... elements) {
        metadataBuilder.withElement(new ListBuilder<ElementBuilder>()
                .add(elements).build(new ListBuilder.Transformer<ElementBuilder, Element>() {
                    @Override
                    public Element transform(ElementBuilder elem) {
                        return elem.build();
                    }
                })
                .toArray(new Element[elements.length]));

        return this;
    }
}
