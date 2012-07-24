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

package com.lyncode.xoai.dataprovider.core;

/**
 * @author DSpace @ Lyncode
 * @version 2.2.1
 */
public class Set extends ReferenceSet {
	private String setName;
	private String description;

	public Set(String setSpec, String setName) {
		super(setSpec);
		this.setName = setName;
		this.description = null;
	}

	/**
	 * 
	 * @param setSpec
	 * @param setName
	 * @param description
	 *            Marshable object
	 */
	public Set(String setSpec, String setName, String xmldescription) {
		this(setSpec, setName);
		this.description = xmldescription;
	}

	public String getSetName() {
		return setName;
	}

	public String getDescription() {
		return description;
	}

	public boolean hasDescription() {
		return (this.description != null);
	}
}
