/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.services.api;

import io.gdcc.xoai.model.oaipmh.Granularity;

import java.time.Instant;

public interface DateProvider {
    String format(Instant date, Granularity granularity);
    Instant parse (String date, Granularity granularity);
    Instant parse(String date);
    String format(Instant date);
    Instant now();
}
