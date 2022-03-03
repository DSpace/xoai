/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.gdcc.xoai.xmlio.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;

public class MatcherBuilder<T> extends BaseMatcher<T> {
    public static <T> MatcherBuilder<T> is (Matcher<T> matcher) {
        return new MatcherBuilder<T>(matcher);
    }

    private Matcher<T> matcher = null;

    public MatcherBuilder(Matcher<T> matcher) {
        this.matcher = matcher;
    }

    public MatcherBuilder<T> and (Matcher<T> matcher) {
        this.matcher = allOf(this.matcher, matcher);
        return this;
    }

    public MatcherBuilder<T> or (Matcher<T> matcher) {
        this.matcher = anyOf(this.matcher, matcher);
        return this;
    }

    @Override
    public boolean matches(Object item) {
        return matcher.matches(item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendDescriptionOf(matcher);
    }
}
