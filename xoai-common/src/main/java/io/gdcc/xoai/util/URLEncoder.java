/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.util;

import java.nio.charset.StandardCharsets;


/**
 * @author Development @ Lyncode
 * @version 3.1.0
 */
public class URLEncoder {
    public static final String SEPARATOR = "&";

    public static String encode(String value) {
        return java.net.URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
