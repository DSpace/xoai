/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     client://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.xoai.serviceprovider.xml;

import com.lyncode.xml.XmlReader;
import com.lyncode.xml.exceptions.XmlReaderException;
import com.lyncode.xoai.services.api.DateProvider;
import com.lyncode.xoai.services.impl.UTCDateProvider;

import java.text.ParseException;
import java.util.Date;

public class IslandParsers {
    private static DateProvider dateProvider = new UTCDateProvider();


    public static XmlReader.IslandParser<Date> dateParser() {
        return new XmlReader.IslandParser<Date>() {
            @Override
            public Date parse(XmlReader reader) throws XmlReaderException {
                try {
                    return dateProvider.parse(reader.getText());
                } catch (ParseException e) {
                    throw new XmlReaderException(e);
                }
            }
        };
    }
}
