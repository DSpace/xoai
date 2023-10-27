package com.lyncode.test.matchers.string;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.regex.Pattern;

/**
 * Tests if the argument is a {@link CharSequence} that matches a regular expression.
 */
public class PatternMatcher extends TypeSafeMatcher<CharSequence> {

    /**
     * Creates a matcher that matches if the examined {@link CharSequence} matches the specified
     * regular expression.
     * <p/>
     * For example:
     * <pre>assertThat("myStringOfNote", pattern("[0-9]+"))</pre>
     *
     * @param regex the regular expression that the returned matcher will use to match any examined {@link CharSequence}
     */
    @Factory
    public static Matcher<CharSequence> pattern(String regex) {
        return pattern(Pattern.compile(regex));
    }

    /**
     * Creates a matcher that matches if the examined {@link CharSequence} matches the specified
     * {@link java.util.regex.Pattern}.
     * <p/>
     * For example:
     * <pre>assertThat("myStringOfNote", Pattern.compile("[0-9]+"))</pre>
     *
     * @param pattern the pattern that the returned matcher will use to match any examined {@link CharSequence}
     */
    @Factory
    public static Matcher<CharSequence> pattern(Pattern pattern) {
        return new PatternMatcher(pattern);
    }

    public static Matcher<CharSequence> pattern (final String pattern, final int group, final Matcher<? super String> matcher) {
        return new TypeSafeMatcher<CharSequence>() {
            @Override
            protected boolean matchesSafely(CharSequence item) {
                java.util.regex.Matcher match = Pattern.compile(pattern).matcher(item);
                match.find();
                return matcher.matches(match.group(group));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Group ")
                           .appendValue(group)
                           .appendText(" in pattern ")
                           .appendValue(pattern)
                           .appendDescriptionOf(matcher);
            }

            @Override
            protected void describeMismatchSafely(CharSequence item, Description mismatchDescription) {
                java.util.regex.Matcher match = Pattern.compile(pattern).matcher(item);
                match.find();
                mismatchDescription.appendText(match.group(group));
            }
        };
    }

    private final Pattern pattern;

    public PatternMatcher(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean matchesSafely(CharSequence item) {
        return pattern.matcher(item).matches();
    }

    @Override
    public void describeMismatchSafely(CharSequence item, org.hamcrest.Description mismatchDescription) {
        mismatchDescription.appendText("was \"").appendText(String.valueOf(item)).appendText("\"");
    }

    @Override
    public void describeTo(org.hamcrest.Description description) {
        description.appendText("a string with pattern \"").appendText(String.valueOf(pattern)).appendText("\"");
    }
}
