/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.handlers.helpers;

import io.gdcc.xoai.xml.XmlWriter;
import io.gdcc.xoai.xmlio.exceptions.XmlWriteException;

import io.gdcc.xoai.dataprovider.model.Context;
import io.gdcc.xoai.dataprovider.model.Item;
import io.gdcc.xoai.dataprovider.filter.FilterResolver;
import io.gdcc.xoai.dataprovider.model.Set;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ItemHelper extends ItemIdentifyHelper {
    private final Item item;

    public ItemHelper(Item item) {
        super(item);
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public InputStream toStream() throws XMLStreamException, XmlWriteException {
        if (item.getMetadata() != null) {
            return new ByteArrayInputStream(item.getMetadata().toString().getBytes());
        } else {
            ByteArrayOutputStream mdOUT = new ByteArrayOutputStream();
            XmlWriter writer = new XmlWriter(mdOUT);
            item.getMetadata().write(writer);
            writer.flush();
            writer.close();
            return new ByteArrayInputStream(mdOUT.toByteArray());
        }
    }

    public List<Set> getSets(Context context, FilterResolver resolver) {
        List<Set> result = new ArrayList<>();
        for (Set set : context.getSets())
            if (set.getCondition().getFilter(resolver).isItemShown(item))
                result.add(set);

        result.addAll(item.getSets());

        return result;
    }
}
