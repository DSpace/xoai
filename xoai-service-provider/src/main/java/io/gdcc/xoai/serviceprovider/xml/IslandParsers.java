/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider.xml;

import io.gdcc.xoai.services.api.DateProvider;
import io.gdcc.xoai.xmlio.XmlReader;
import io.gdcc.xoai.xmlio.exceptions.XmlReaderException;

import java.time.DateTimeException;
import java.time.Instant;

public class IslandParsers {
    public static XmlReader.IslandParser<Instant> dateParser() {
        return reader -> {
            try {
                return DateProvider.parse(reader.getText());
            } catch (DateTimeException e) {
                throw new XmlReaderException(e);
            }
        };
    }
}
