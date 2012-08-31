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
package com.lyncode.xoai.dataprovider;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.2
 */
public class OAIRequestParameters {
	private static Logger log = LogManager
			.getLogger(OAIRequestParameters.class);
	private String verb;
	private String resumptionToken;
	private String identifier;
	private String metadataPrefix;
	private String set;
	private String until;
	private String from;

	public OAIRequestParameters() {

	}

	public void setVerb(String verb) {
		log.trace("Verb parameter given: " + verb);
		this.verb = verb;
	}

	public void setResumptionToken(String res) {
		log.trace("ResumptionToken parameter given: " + res);
		this.resumptionToken = res;
	}

	public void setIdentifier(String identifier) {
		log.trace("Identifier parameter given: " + identifier);
		this.identifier = identifier;
	}

	public void setMetadataPrefix(String metadataPrefix) {
		log.trace("MetadataPrefix parameter given: " + metadataPrefix);
		this.metadataPrefix = metadataPrefix;
	}

	public void setSet(String set) {
		log.trace("Set parameter given: " + set);
		this.set = set;
	}

	public void setFrom(String from) {
		log.trace("From parameter given: " + from);
		this.from = from;
	}

	public void setUntil(String until) {
		log.trace("Until parameter given: " + until);
		this.until = until;
	}

	public String getFrom() {
		return from;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getMetadataPrefix() {
		return metadataPrefix;
	}

	public String getResumptionToken() {
		return resumptionToken;
	}

	public String getSet() {
		return set;
	}

	public String getUntil() {
		return until;
	}

	public String getVerb() {
		return verb;
	}

}
