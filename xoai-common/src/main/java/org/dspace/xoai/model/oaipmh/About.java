/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.model.oaipmh;

import com.lyncode.xml.exceptions.XmlWriteException;
import org.dspace.xoai.xml.EchoElement;
import org.dspace.xoai.xml.XmlWritable;
import org.dspace.xoai.xml.XmlWriter;

public class About implements XmlWritable {
    private final String value;

    public About(String xmlValue) {
        this.value = xmlValue;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void write(XmlWriter writer) throws XmlWriteException {
        if (this.value != null) {
            EchoElement elem = new EchoElement(value);
            elem.write(writer);
        }
    }
}
