/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.services.impl.metadataSearcherMultipleFields;

import org.dspace.xoai.services.api.DateProvider;
import org.dspace.xoai.services.impl.UTCDateProvider;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.lyncode.test.matchers.string.PatternMatcher.pattern;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UTCDateProviderTest {
    private static final Date DATE = new Date();
    private static final String SECOND_FORMAT = "([0-9]{4})-([0-9]{2})-([0-9]{2})T([0-9]{2}):([0-9]{2}):([0-9]{2})Z";
    DateProvider underTest = new UTCDateProvider();

    @Test
    public void shouldUseUTCDateFormat() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        String result = underTest.format(DATE);

        assertThat(result, pattern(SECOND_FORMAT, 1, toInt(is(getCalendar(DATE).get(Calendar.YEAR)))));
        assertThat(result, pattern(SECOND_FORMAT, 2, toInt(is(getCalendar(DATE).get(Calendar.MONTH)+1))));
        assertThat(result, pattern(SECOND_FORMAT, 3, toInt(is(getCalendar(DATE).get(Calendar.DAY_OF_MONTH)))));

        assertThat(result, pattern(SECOND_FORMAT, 4, toInt(is(getCalendar(DATE).get(Calendar.HOUR_OF_DAY)))));
        assertThat(result, pattern(SECOND_FORMAT, 5, toInt(is(getCalendar(DATE).get(Calendar.MINUTE)))));
        assertThat(result, pattern(SECOND_FORMAT, 6, toInt(is(getCalendar(DATE).get(Calendar.SECOND)))));
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
