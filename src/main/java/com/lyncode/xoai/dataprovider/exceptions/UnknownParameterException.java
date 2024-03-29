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
package com.lyncode.xoai.dataprovider.exceptions;

/**
 "
 */
public class UnknownParameterException extends HandlerException {
    private static final long serialVersionUID = -813886035789840394L;

    public UnknownParameterException() {
        super();
    }

    public UnknownParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownParameterException(String message) {
        super(message);
    }

    public UnknownParameterException(Throwable cause) {
        super(cause);
    }

}
