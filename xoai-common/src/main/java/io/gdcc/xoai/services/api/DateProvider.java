/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.services.api;

import io.gdcc.xoai.model.oaipmh.Granularity;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public interface DateProvider {
    Map<Granularity, DateTimeFormatter> formatMap = Map.of(
        Granularity.Day, DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        Granularity.Second, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    );
    
    /**
     * Create an instant of now, truncated to seconds (skip nanosecond precision)
     */
    static Instant now() {
        return Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }
    
    /**
     * Format an {@link Instant} as UTC instant ({@link Granularity#Second}). Example: "2022-05-19T10:00:00Z"
     * @param date The instant to format. By definition, an {@link Instant} is not zoned and always in UTC!
     * @throws DateTimeException when a necessary clock unit is not available from the timestamp
     * @deprecated As OAI-PMH is pretty explicit about using the same Granularity everywhere, you should be more explicit.
     */
    @Deprecated(since = "2022-05-19", forRemoval = true)
    static String format(Instant date) {
        return format(date, Granularity.Second);
    }
    
    /**
     * Format an {@link Instant} with a given {@link Granularity}.
     * Example: Granularity "Second": "2022-05-19T10:00:00Z", Granularity "Day": "2022-05-19"
     * @param date The instant to format.  By definition, an {@link Instant} is not zoned and always in UTC!
     * @param granularity The granularity you seek
     * @throws DateTimeException when a necessary clock unit is not available from the timestamp
     */
    static String format(Instant date, Granularity granularity) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date, ZoneOffset.UTC);
        return formatMap.get(granularity).format(dateTime);
    }
    
    /**
     * Parse an {@link Instant} from a string with the granularity you need.
     * @param date The string containing the date
     * @param granularity The granularity you seek
     * @return An Instant in UTC to work with
     * @throws DateTimeException in case the parsing fails for any reason
     */
    static Instant parse(String date, Granularity granularity) {
        if (granularity == Granularity.Day)
            return LocalDate.parse(date, formatMap.get(granularity)).atStartOfDay().toInstant(ZoneOffset.UTC);
        else
            return LocalDateTime.parse(date, formatMap.get(granularity)).toInstant(ZoneOffset.UTC);
    }
    
    /**
     * Parse an {@link Instant} from a string, autodetecting the Granularity.
     * @param string The string containing the date
     * @return An Instant in UTC to work with
     * @throws DateTimeException in case the parsing fails for any reason
     * @deprecated As OAI-PMH is pretty explicit about using the same Granularity everywhere, you should be more explicit.
     */
    @Deprecated(since = "2022-05-19", forRemoval = true)
    static Instant parse(String string) {
        try {
            return Instant.parse(string);
        } catch (DateTimeException e) {
            return LocalDate.parse(string, formatMap.get(Granularity.Day))
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC);
        }
    }
    
}
