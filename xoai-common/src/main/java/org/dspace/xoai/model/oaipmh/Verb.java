/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.model.oaipmh;

import org.dspace.xoai.xml.XmlWritable;

public interface Verb extends XmlWritable {
    public static enum Type {
        Identify("Identify"),
        ListMetadataFormats("ListMetadataFormats"),
        ListSets("ListSets"),
        GetRecord("GetRecord"),
        ListIdentifiers("ListIdentifiers"),
        ListRecords("ListRecords");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        public String displayName() {
            return value;
        }

        public static Type fromValue(String value) {
            for (Type c : Type.values()) {
                if (c.value.equals(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(value);
        }

    }

    Type getType ();

}
