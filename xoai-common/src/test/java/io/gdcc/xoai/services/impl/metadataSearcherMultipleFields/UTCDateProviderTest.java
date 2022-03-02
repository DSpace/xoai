/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.services.impl.metadataSearcherMultipleFields;

import io.gdcc.xoai.services.api.DateProvider;
import io.gdcc.xoai.services.impl.UTCDateProvider;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.MatchesPattern.matchesPattern;

public class UTCDateProviderTest {
    private static final Date DATE = new Date();
    private static final String SECOND_FORMAT = "([0-9]{4})-([0-9]{2})-([0-9]{2})T([0-9]{2}):([0-9]{2}):([0-9]{2})Z";
    DateProvider underTest = new UTCDateProvider();

    @Test
    public void shouldUseUTCDateFormat() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        String result = underTest.format(DATE);

        assertThat(result, matchesPattern(SECOND_FORMAT));
        
        Map<Integer, Matcher<String>> groupMatchers = Map.of(
            1, toInt(is(getCalendar(DATE).get(Calendar.YEAR))),
            2, toInt(is(getCalendar(DATE).get(Calendar.MONTH)+1)),
            3, toInt(is(getCalendar(DATE).get(Calendar.DAY_OF_MONTH))),
            4, toInt(is(getCalendar(DATE).get(Calendar.HOUR_OF_DAY))),
            5, toInt(is(getCalendar(DATE).get(Calendar.MINUTE))),
            6, toInt(is(getCalendar(DATE).get(Calendar.SECOND)))
        );
        
        java.util.regex.Matcher match = Pattern.compile(SECOND_FORMAT).matcher(result);
        match.find();
        
        groupMatchers.forEach((key, value) -> assertThat(match.group(key), value));
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

    private Calendar getCalendar (Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance;
    }
}
