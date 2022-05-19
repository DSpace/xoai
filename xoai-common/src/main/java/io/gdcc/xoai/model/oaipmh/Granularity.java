/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.model.oaipmh;

import java.time.format.DateTimeFormatter;

/**
 * @author Development @ Lyncode
 * @version 3.1.0
 */
public enum Granularity {
    Day("YYYY-MM-DD"),
    Second("YYYY-MM-DDThh:mm:ssZ");

    public static Granularity fromRepresentation (String representation) {
        for (Granularity granularity : Granularity.values())
            if (granularity.toString().equals(representation))
                return granularity;

        throw new IllegalArgumentException(representation);
    }

    private final String representation;

    Granularity (String representation) {
        this.representation = representation;
    }

    public String toString () {
        return representation;
    }
}
