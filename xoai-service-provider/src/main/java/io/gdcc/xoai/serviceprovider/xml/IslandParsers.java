/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider.xml;

import io.gdcc.xoai.services.api.DateProvider;
import io.gdcc.xoai.services.impl.UTCDateProvider;
import io.gdcc.xoai.xmlio.XmlReader;
import io.gdcc.xoai.xmlio.exceptions.XmlReaderException;

import java.text.ParseException;
import java.util.Date;

public class IslandParsers {
    private static DateProvider dateProvider = new UTCDateProvider();


    public static XmlReader.IslandParser<Date> dateParser() {
        return reader -> {
            try {
                return dateProvider.parse(reader.getText());
            } catch (ParseException e) {
                throw new XmlReaderException(e);
            }
        };
    }
}
