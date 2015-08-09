/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.model.xoai;

import com.lyncode.xml.XmlReader;
import com.lyncode.xml.XmlWritable;
import com.lyncode.xml.XmlWriter;
import com.lyncode.xml.exceptions.XmlReaderException;
import com.lyncode.xml.exceptions.XmlWriteException;

import javax.xml.stream.XMLStreamException;

import static com.lyncode.xml.matchers.AttributeMatchers.attributeName;
import static com.lyncode.xml.matchers.QNameMatchers.localPart;
import static com.lyncode.xml.matchers.XmlEventMatchers.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;

public class Field implements XmlWritable {
    public static Field parse (XmlReader reader) throws XmlReaderException {
        if (!reader.current(allOf(aStartElement(), elementName(localPart(equalTo("field"))))))
            throw new XmlReaderException("Invalid XML. Expecting entity 'field'");

        Field field = new Field();

        if (reader.hasAttribute(attributeName(localPart(equalTo("name")))))
            field.withName(reader.getAttributeValue(localPart(equalTo("name"))));

        if (reader.next(anElement(), text()).current(text())) {
            field.withValue(reader.getText());
            reader.next(anElement());
        }

        if (!reader.current(allOf(anEndElement(), elementName(localPart(equalTo("field"))))))
            throw new XmlReaderException("Invalid XML. Expecting end of entity 'field'");

        return field;
    }

    public Field() {
    }

    public Field(String value, String name) {
        this.value = value;
        this.name = name;
    }

    protected String value;
    protected String name;

    public String getValue() {
        return value;
    }

    public Field withValue(String value) {
        this.value = value;
        return this;
    }

    public String getName() {
        return name;
    }

    public Field withName(String value) {
        this.name = value;
        return this;
    }

    @Override
    public void write(XmlWriter writer) throws XmlWriteException {
        try {
            if (this.name != null)
                writer.writeAttribute("name", this.getName());

            if (this.value != null)
                writer.writeCharacters(value);

        } catch (XMLStreamException e) {
            throw new XmlWriteException(e);
        }
    }
}
