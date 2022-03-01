/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.xml;

import com.lyncode.xml.exceptions.XmlWriteException;

public interface XmlWritable {
    public void write (XmlWriter writer) throws XmlWriteException;
}
