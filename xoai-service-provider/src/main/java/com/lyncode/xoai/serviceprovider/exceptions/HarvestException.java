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
 *
 * @author Development @ Lyncode
 * @version 3.1.0
 */

package com.lyncode.xoai.serviceprovider.exceptions;


/**
 * @author Development @ Lyncode
 * @version 3.1.0
 */
public class HarvestException extends Exception {
    private static final long serialVersionUID = -1824340625967423555L;
    private String url;


    public HarvestException() {
        url = "";
    }

    public HarvestException(String arg0) {
        super(arg0);
    }

    public HarvestException(Throwable arg0) {
        super(arg0);
    }

    public HarvestException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }


    public void setURL(String url) {
        this.url = url;
    }

    public String getURL() {
        return this.url;
    }
}
