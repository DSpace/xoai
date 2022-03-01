/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.services.api;

import io.gdcc.xoai.model.oaipmh.Granularity;

import java.text.ParseException;
import java.util.Date;

public interface DateProvider {
    String format(Date date, Granularity granularity);
    Date parse (String date, Granularity granularity) throws ParseException;
    Date parse(String date) throws ParseException;
    String format(Date date);
    Date now();
}
