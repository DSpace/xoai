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

import com.lyncode.test.matchers.extractor.ExtractFunction;
import com.lyncode.test.matchers.extractor.MatcherExtractor;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;

public class AttributeMatchers {
    public static Matcher<Attribute> attributeName (Matcher<QName> matcher) {
        return new MatcherExtractor<Attribute, QName>(matcher, extractName());
    }

    public static Matcher<Attribute> attributeValue (Matcher<String> valueMatcher) {
        return new MatcherExtractor<Attribute, String>(valueMatcher, extractValue());
    }

    private static ExtractFunction<Attribute, String> extractValue() {
        return new ExtractFunction<Attribute, String>() {
            @Override
            public String apply(Attribute input) {
                return input.getValue();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("value");
            }
        };
    }

    private static ExtractFunction<Attribute, QName> extractName() {
        return new ExtractFunction<Attribute, QName>() {
            @Override
            public QName apply(Attribute input) {
                return input.getName();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("name");
            }
        };
    }
}
