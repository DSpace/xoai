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
 * @version 2.2.9
 */
public class IllegalVerbException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2748244610538429452L;

	/**
	 * Creates a new instance of <code>IllegalVerbException</code> without
	 * detail message.
	 */
	public IllegalVerbException() {
	}

	/**
	 * Constructs an instance of <code>IllegalVerbException</code> with the
	 * specified detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public IllegalVerbException(String msg) {
		super(msg);
	}
}
