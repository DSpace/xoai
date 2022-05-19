/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.services;

import io.gdcc.xoai.model.oaipmh.Granularity;
import io.gdcc.xoai.services.api.DateProvider;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateProviderTest {
    private static final Instant DATE = Instant.now();
    private static final String SECOND_FORMAT = "([0-9]{4})-([0-9]{2})-([0-9]{2})T([0-9]{2}):([0-9]{2}):([0-9]{2})Z";

    @Test
    void shouldUseUTCDateFormat() {
        String result = DateProvider.format(DATE, Granularity.Second);

        assertThat(result, matchesPattern(SECOND_FORMAT));
    
        LocalDateTime dateTime = LocalDateTime.ofInstant(DATE, ZoneOffset.UTC);
        Map<Integer, Matcher<String>> groupMatchers = Map.of(
            1, toInt(is(dateTime.getYear())),
            2, toInt(is(dateTime.getMonthValue())),
            3, toInt(is(dateTime.getDayOfMonth())),
            4, toInt(is(dateTime.getHour())),
            5, toInt(is(dateTime.getMinute())),
            6, toInt(is(dateTime.getSecond()))
        );
        
        java.util.regex.Matcher match = Pattern.compile(SECOND_FORMAT).matcher(result);
        match.find();
        
        groupMatchers.forEach((key, value) -> assertThat(match.group(key), value));
    }
    
    @Test
    void parseDates() {
        String localDate = "2022-05-19";
        String timestamp = "2022-05-19T10:01:02Z";
        
        Instant expected = LocalDate.of(2022, 5, 19).atTime(0, 0).toInstant(ZoneOffset.UTC);
        Instant expected10 = LocalDate.of(2022, 5, 19).atTime(10, 1, 2).toInstant(ZoneOffset.UTC);
    
        assertEquals(expected, DateProvider.parse(localDate, Granularity.Day));
        assertEquals(expected10, DateProvider.parse(timestamp, Granularity.Second));
    }
    
    @Test
    void formatDayGranularity() {
        Instant subject = LocalDate.of(2022, 5, 19).atTime(10, 1, 2).toInstant(ZoneOffset.UTC);
        String expected = "2022-05-19";
        
        assertEquals(expected, DateProvider.format(subject, Granularity.Day));
    }
    
    @Test
    void formatSecondGranularity() {
        Instant subject = LocalDate.of(2022, 5, 19).atTime(10, 1, 2).toInstant(ZoneOffset.UTC);
        String expected = "2022-05-19T10:01:02Z";
        
        assertEquals(expected, DateProvider.format(subject, Granularity.Second));
    }
    
    /**
     * The OAI-PMH spec definies when given "from" and "until" dates, records shall be returned that happened
     * between both INCLUDING the dates. This becomes tricky when only a date has been given with Granularity.Day
     */
    @Test
    void checkEqualOrBetween() {
        Instant from = DateProvider.parse("2022-05-19", Granularity.Day);
        Instant until = DateProvider.parse("2022-05-19", Granularity.Day);
    
        assertEquals(from, until);
        assertFalse(from.isAfter(until));
        assertFalse(until.isBefore(from));
        
        from = DateProvider.parse("2022-05-19T10:00:00Z", Granularity.Second);
        until = DateProvider.parse("2022-05-19T10:01:00Z", Granularity.Second);
    
        assertNotEquals(from, until);
        assertTrue(until.isAfter(from));
        assertFalse(until.isBefore(from));
    }
    
    @Test
    void checkWrongFormatFails() {
        assertThrows(NullPointerException.class, () -> DateProvider.parse(null, Granularity.Second));
        assertThrows(DateTimeException.class, () -> DateProvider.parse("foobar", Granularity.Second));
        assertThrows(DateTimeException.class, () -> DateProvider.parse("2022-05-19", Granularity.Second));
        assertThrows(DateTimeException.class, () -> DateProvider.parse("2022-05-19T10:00:00Z", Granularity.Day));
    }

    private Matcher<String> toInt (final Matcher<Integer> integerMatcher) {
        return new TypeSafeMatcher<String>() {
            @Override
            protected boolean matchesSafely(String item) {
                return integerMatcher.matches(Integer.valueOf(item));
            }

            @Override
            public void describeTo(Description description) {
                description.appendDescriptionOf(integerMatcher);
            }
        };
    }
}
