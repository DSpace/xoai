package com.lyncode.xoai.tests.helpers;


import com.lyncode.xoai.builders.ItemMetadataBuilder;
import com.lyncode.xoai.builders.ListBuilder;
import com.lyncode.xoai.builders.MetadataBuilder;
import com.lyncode.xoai.builders.MetadataElementBuilder;
import com.lyncode.xoai.dataprovider.data.AbstractItem;
import com.lyncode.xoai.dataprovider.xml.xoai.Element;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractItemBuilder {
    private AbstractItem abstractItem = mock(AbstractItem.class);
    private MetadataBuilder metadataBuilder = new MetadataBuilder();
    private ItemMetadataBuilder itemMetadataBuilder = new ItemMetadataBuilder();

    public AbstractItem build () {
        when(abstractItem.getMetadata()).thenReturn(itemMetadataBuilder.withMetadata(metadataBuilder).build());
        return abstractItem;
    }

    public AbstractItemBuilder withMetadata(MetadataElementBuilder element) {
        metadataBuilder.withElement(element.build());
        return this;
    }
    public AbstractItemBuilder withMetadata(MetadataElementBuilder... elements) {
        metadataBuilder.withElement(new ListBuilder<MetadataElementBuilder>()
                .add(elements).build(new ListBuilder.Transformer<MetadataElementBuilder, Element>() {
            @Override
            public Element transform(MetadataElementBuilder elem) {
                return elem.build();
            }
        })
                .toArray(new Element[elements.length]));

        return this;
    }
}
