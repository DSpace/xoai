package com.lyncode.xoai.dataprovider.data.internal;

import com.lyncode.xoai.dataprovider.data.AbstractItem;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.util.XSLPipeline;
import org.codehaus.stax2.XMLOutputFactory2;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class Item extends ItemIdentify {
    private static XMLOutputFactory outputFactory = XMLOutputFactory2.newFactory();
    private AbstractItem item;

    public Item(AbstractItem item) {
        super(item);
        this.item = item;
    }

    public AbstractItem getItem() {
        return item;
    }

    public InputStream toStream() throws XMLStreamException, WritingXmlException {
        if (item.getMetadata().isCompiled()) {
            return new ByteArrayInputStream(item.getMetadata().getCompiled().getBytes());
        } else {
            ByteArrayOutputStream mdOUT = new ByteArrayOutputStream();
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(mdOUT);
            item.getMetadata().getMetadata().write(writer);
            writer.flush();
            writer.close();
            return new ByteArrayInputStream(mdOUT.toByteArray());
        }
    }

    public XSLPipeline toPipeline(boolean omitXMLDeclaration) throws WritingXmlException, XMLStreamException {
        return new XSLPipeline(toStream(), omitXMLDeclaration);
    }
}
