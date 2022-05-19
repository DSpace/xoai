/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.services.impl;

import io.gdcc.xoai.model.oaipmh.Granularity;
import io.gdcc.xoai.services.api.DateProvider;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public final class UTCDateProvider implements DateProvider {
    
    private static final Map<Granularity, DateTimeFormatter> formatMap = Map.of(
        Granularity.Day, DateTimeFormatter.ISO_LOCAL_DATE,
        Granularity.Second, DateTimeFormatter.ofPattern("YYYY-MM-dd'T'HH:mm:ss'Z'")
    );
    
    /**
     * Format an {@link Instant} as UTC instant ({@link Granularity#Second}). Example: "2022-05-19T10:00:00Z"
     * @param date The instant to format. By definition, an {@link Instant} is not zoned and always in UTC!
     * @return
     */
    @Override
    public String format(Instant date) {
        return format(date, Granularity.Second);
    }

    @Override
    public Instant now() {
        return Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }

    @Override
    public String format(Instant date, Granularity granularity) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date, ZoneOffset.UTC);
        return formatMap.get(granularity).format(dateTime);
    }

    @Override
    public Instant parse(String date, Granularity granularity) {
        return Instant.from(formatMap.get(granularity).parse(date));
    }

    @Override
    public Instant parse(String string) {
        try {
            return Instant.parse(string);
        } catch (DateTimeException e) {
            return LocalDate.parse(string, formatMap.get(Granularity.Day))
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC);
        }
    }
}
