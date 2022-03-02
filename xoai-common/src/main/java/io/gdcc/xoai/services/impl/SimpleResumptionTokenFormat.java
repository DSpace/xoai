/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.services.impl;

import io.gdcc.xoai.exceptions.InvalidResumptionTokenException;
import io.gdcc.xoai.model.oaipmh.ResumptionToken;
import io.gdcc.xoai.services.api.ResumptionTokenFormat;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Pattern;

public class SimpleResumptionTokenFormat implements ResumptionTokenFormat {
    
    public ResumptionToken.Value parse(String resumptionToken) throws InvalidResumptionTokenException {
        if (resumptionToken == null) {
            return new ResumptionToken.Value();
        }
        
        int offset = 0;
        String set = null;
        Date from = null;
        Date until = null;
        String metadataPrefix = null;
        
        if (resumptionToken.trim().equals("")) {
            return new ResumptionToken.Value();
        } else {
            String s = base64Decode(resumptionToken);
            String[] pieces = s.split(Pattern.quote("|"));
            try {
                if (pieces.length > 0) {
                    offset = Integer.parseInt(pieces[0].substring(2));
                    if (pieces.length > 1) {
                        set = pieces[1].substring(2);
                        if (set.equals(""))
                            set = null;
                    }
                    if (pieces.length > 2) {
                        from = stringToDate(pieces[2].substring(2));
                    }
                    if (pieces.length > 3) {
                        until = stringToDate(pieces[3].substring(2));
                    }
                    if (pieces.length > 4) {
                        metadataPrefix = pieces[4].substring(2);
                        if (metadataPrefix.equals("")) {
                            metadataPrefix = null;
                        }
                    }
                } else
                    throw new InvalidResumptionTokenException();
            } catch (Exception ex) {
                throw new InvalidResumptionTokenException(ex);
            }
        }
        
        return new ResumptionToken.Value()
                .withUntil(until)
                .withFrom(from)
                .withMetadataPrefix(metadataPrefix)
                .withOffset(offset)
                .withSetSpec(set);
    }

    @Override
    public String format(ResumptionToken.Value resumptionToken) {
        String s = "1:" + resumptionToken.getOffset();
        s += "|2:";
        if (resumptionToken.hasSetSpec())
            s += resumptionToken.getSetSpec();
        s += "|3:";
        if (resumptionToken.hasFrom())
            s += dateToString(resumptionToken.getFrom());
        s += "|4:";
        if (resumptionToken.hasUntil())
            s += dateToString(resumptionToken.getUntil());
        s += "|5:";
        if (resumptionToken.hasMetadataPrefix())
            s += resumptionToken.getMetadataPrefix();

        return base64Encode(s);
    }


    private String dateToString(Date date) {
        SimpleDateFormat formatDate = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'");
        return formatDate.format(date);
    }

    private Date stringToDate(String string) {
        SimpleDateFormat formatDate = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            return formatDate.parse(string);
        } catch (ParseException ex) {
            formatDate = new SimpleDateFormat(
                    "yyyy-MM-dd");
            try {
                return formatDate.parse(string);
            } catch (ParseException ex1) {
                return null;
            }
        }
    }
    
    /**
     * Simple decoding of a Base64 encoded String assuming UTF-8 usage
     * @param value The Base64 encoded string
     * @return A decoded String (may be empty)
     */
    private static String base64Decode(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value to be Base64-decoded may not be null.");
        }
        
        byte[] decodedValue = Base64.getDecoder().decode(value);
        return new String(decodedValue, StandardCharsets.UTF_8);
    }
    
    private static String base64Encode(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value to be Base64-encoded may not be null.");
        }
        
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

}
