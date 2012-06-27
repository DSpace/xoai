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

package com.lyncode.xoai.common.dataprovider.exceptions;

import java.sql.SQLException;


/**
 * @author DSpace @ Lyncode
 * @version 2.0.0
 */
public class IdDoesNotExistException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = -657866486396669641L;


	/**
     * Creates a new instance of <code>IdDoesNotExistException</code> without detail message.
     */
    public IdDoesNotExistException() {
    }


    /**
     * Constructs an instance of <code>IdDoesNotExistException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public IdDoesNotExistException(String msg) {
        super(msg);
    }


	public IdDoesNotExistException(Exception e) {
		super(e);
	}
}
