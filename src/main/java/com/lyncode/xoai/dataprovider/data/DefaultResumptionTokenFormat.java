package com.lyncode.xoai.dataprovider.data;

import com.lyncode.xoai.dataprovider.core.ResumptionToken;
import com.lyncode.xoai.dataprovider.exceptions.BadResumptionToken;
import com.lyncode.xoai.util.Base64Utils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;


public class DefaultResumptionTokenFormat extends AbstractResumptionTokenFormat {
    private static Logger log = LogManager.getLogger(DefaultResumptionTokenFormat.class);

    @Override
    public ResumptionToken parse(String resumptionToken) throws BadResumptionToken {
        if (resumptionToken == null) return new ResumptionToken();
        int _offset = 0;
        String _set = null;
        Date _from = null;
        Date _until = null;
        String _metadataPrefix = null;
        if (resumptionToken == null || resumptionToken.trim().equals("")) {
            return new ResumptionToken();
        } else {
            String s = Base64Utils.decode(resumptionToken);
            String[] pieces = s.split(Pattern.quote("|"));
            try {
                if (pieces.length > 0) {
                    _offset = Integer.parseInt(pieces[0].substring(2));
                    if (pieces.length > 1) {
                        _set = pieces[1].substring(2);
                        if (_set != null && _set.equals(""))
                            _set = null;
                    }
                    if (pieces.length > 2) {
                        _from = stringToDate(pieces[2].substring(2));
                    }
                    if (pieces.length > 3) {
                        _until = stringToDate(pieces[3].substring(2));
                    }
                    if (pieces.length > 4) {
                        _metadataPrefix = pieces[4].substring(2);
                        if (_metadataPrefix != null && _metadataPrefix.equals(""))
                            _metadataPrefix = null;
                    }
                } else
                    throw new BadResumptionToken();
            } catch (Exception ex) {
                log.debug(ex.getMessage(), ex);
                throw new BadResumptionToken();
            }
        }
        return new ResumptionToken(_offset, _metadataPrefix, _set, _from, _until);
    }

    @Override
    public String format(ResumptionToken resumptionToken) {
        if (resumptionToken.isEmpty())
            return "";

        String s = "1:" + resumptionToken.getOffset();
        s += "|2:";
        if (resumptionToken.hasSet())
            s += resumptionToken.getSet();
        s += "|3:";
        if (resumptionToken.hasFrom())
            s += dateToString(resumptionToken.getFrom());
        s += "|4:";
        if (resumptionToken.hasUntil())
            s += dateToString(resumptionToken.getUntil());
        s += "|5:";
        if (resumptionToken.hasMetadataPrefix())
            s += resumptionToken.getMetadataPrefix();

        return Base64Utils.encode(s);
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

}
