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
 * 
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.2
 */

package com.lyncode.xoai.serviceprovider.util;

import java.io.UnsupportedEncodingException;


/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.2
 */
public class URLEncoder
{
    public static final String SEPARATOR = "&";
    
    public static String encode (String value) {
        try
        {
            return java.net.URLEncoder.encode(value, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            return value;
        }
    }
}
