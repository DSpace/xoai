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

package io.gdcc.xoai.xmlio.matchers.extractor;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class MatcherExtractor<V, T> extends TypeSafeMatcher<V> {
    public static <V, T> MatcherExtractor<V, T> valueOf (ExtractFunction<V, T> extractFunction, Matcher<T> matcher) {
        return new MatcherExtractor<V, T>(matcher, extractFunction);
    }

    private Matcher<T> matcher;
    private ExtractFunction<V, T> extractor;

    public MatcherExtractor(Matcher<T> matcher, ExtractFunction<V, T> extractor) {
        this.matcher = matcher;
        this.extractor = extractor;
    }

    @Override
    protected boolean matchesSafely(V item) {
        return matcher.matches(extractor.apply(item));
    }

    @Override
    public void describeTo(Description description) {
        description.appendDescriptionOf(extractor).appendText(" ").appendDescriptionOf(matcher);
    }

    @Override
    protected void describeMismatchSafely(V item, Description mismatchDescription) {
        mismatchDescription.appendText("was ").appendValue(extractor.apply(item));
    }
}
