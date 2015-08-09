/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.xml;

import com.lyncode.xml.XmlReader;
import com.lyncode.xml.exceptions.XmlReaderException;
import org.dspace.xoai.services.api.DateProvider;
import org.dspace.xoai.services.impl.UTCDateProvider;

import java.text.ParseException;
import java.util.Date;

public class IslandParsers {
    private static DateProvider dateProvider = new UTCDateProvider();


    public static XmlReader.IslandParser<Date> dateParser() {
        return new XmlReader.IslandParser<Date>() {
            @Override
            public Date parse(XmlReader reader) throws XmlReaderException {
                try {
                    return dateProvider.parse(reader.getText());
                } catch (ParseException e) {
                    throw new XmlReaderException(e);
                }
            }
        };
    }
}
