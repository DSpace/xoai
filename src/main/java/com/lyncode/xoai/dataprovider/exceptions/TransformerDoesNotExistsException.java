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
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.7
 */
public class TransformerDoesNotExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8417020535730995337L;

	/**
	 * Creates a new instance of <code>TransformerDoesNotExistsException</code>
	 * without detail message.
	 */
	public TransformerDoesNotExistsException() {
	}

	/**
	 * Constructs an instance of <code>TransformerDoesNotExistsException</code>
	 * with the specified detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public TransformerDoesNotExistsException(String msg) {
		super(msg);
	}
}
