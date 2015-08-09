/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider.handlers.helpers;

import com.lyncode.xml.exceptions.XmlWriteException;
import org.dspace.xoai.dataprovider.filter.FilterResolver;
import org.dspace.xoai.dataprovider.model.Context;
import org.dspace.xoai.dataprovider.model.Item;
import org.dspace.xoai.dataprovider.model.Set;
import org.dspace.xoai.xml.XmlWriter;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ItemHelper extends ItemIdentifyHelper {
    private Item item;

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
        List<Set> result = new ArrayList<Set>();
        for (Set set : context.getSets())
            if (set.getCondition().getFilter(resolver).isItemShown(item))
                result.add(set);

        result.addAll(item.getSets());

        return result;
    }
}
