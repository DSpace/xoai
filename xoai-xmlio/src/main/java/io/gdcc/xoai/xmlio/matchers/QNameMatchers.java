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

public class QNameMatchers {
    public static Matcher<QName> localPart(final Matcher<String> matcher) {
        return new MatcherExtractor<QName, String>(matcher, extractLocalPart());
    }

    private static ExtractFunction<QName, String> extractLocalPart() {
        return new ExtractFunction<QName, String>() {
            @Override
            public String apply(QName input) {
                return input.getLocalPart();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("local part");
            }
        };
    }
}
