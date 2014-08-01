package com.lyncode.xoai.model.oaipmh;

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

    private String representation;

    Granularity (String representation) {
        this.representation = representation;
    }

    public String toString () {
        return representation;
    }
}
