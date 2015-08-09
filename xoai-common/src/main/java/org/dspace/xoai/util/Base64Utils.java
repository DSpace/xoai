/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.util;

import org.apache.commons.codec.binary.Base64;

public class Base64Utils {
    public static String encode(String input) {
        return new String(Base64.encodeBase64(input.getBytes()));
    }

    public static String decode(String input) {
        return new String(Base64.decodeBase64(input.getBytes()));
    }
}
