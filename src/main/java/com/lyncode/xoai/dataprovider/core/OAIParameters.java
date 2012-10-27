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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.xoai.dataprovider.OAIRequestParameters;
import com.lyncode.xoai.dataprovider.exceptions.BadArgumentException;
import com.lyncode.xoai.dataprovider.exceptions.BadResumptionToken;
import com.lyncode.xoai.dataprovider.exceptions.DuplicateDefinitionException;
import com.lyncode.xoai.dataprovider.exceptions.IllegalVerbException;
import com.lyncode.xoai.dataprovider.exceptions.UnknownParameterException;
import com.lyncode.xoai.dataprovider.xml.oaipmh.VerbType;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */
public class OAIParameters {
	private static Logger log = LogManager.getLogger(OAIParameters.class);
	private VerbType verb;
	private ResumptionToken resumptionToken;
	private String identifier;
	private String metadataPrefix;
	private String set;
	private Date until;
	private Date from;

	public OAIParameters(OAIRequestParameters request)
			throws IllegalVerbException, BadArgumentException,
			BadResumptionToken, UnknownParameterException, DuplicateDefinitionException {
		
		if (request.getUntil() != null && !request.getUntil().equals("")
				&& request.getFrom() != null && !request.getFrom().equals("")
				&& request.getFrom().length() != request.getUntil().length())
			throw new BadArgumentException("Distinct granularities provided for until and from parameters");
			
		
		this.verb = this.getVerb(request.getVerb());
		this.from = this.getDate(request.getFrom(), "from");
		this.until = this.getDate(request.getUntil(), "until");
		this.metadataPrefix = request.getMetadataPrefix();
		this.set = request.getSet();
		this.identifier = request.getIdentifier();
		this.resumptionToken = new ResumptionToken(request.getResumptionToken());

		this.validate();
		this.loadResumptionToken(this.resumptionToken);
	}

	private VerbType getVerb(String verb) throws IllegalVerbException {
		if (verb == null) {
			log.trace("The verb given by the request is null, assuming identify");
			throw new IllegalVerbException(
					"The verb given by the request is null, assuming identify");
		}
		if (verb.equals("Identify"))
			return VerbType.IDENTIFY;
		else if (verb.equals("GetRecord"))
			return VerbType.GET_RECORD;
		else if (verb.equals("ListIdentifiers"))
			return VerbType.LIST_IDENTIFIERS;
		else if (verb.equals("ListMetadataFormats"))
			return VerbType.LIST_METADATA_FORMATS;
		else if (verb.equals("ListRecords"))
			return VerbType.LIST_RECORDS;
		else if (verb.equals("ListSets"))
			return VerbType.LIST_SETS;
		else {
			log.trace("The verb given by the request is unknown, assuming identify");
			throw new IllegalVerbException(
					"The verb given by the request is unknown, assuming identify");
		}
	}

	public boolean hasResumptionToken() {
		return (!this.resumptionToken.isEmpty());
	}

	public String getIdentifier() {
		return identifier;
	}

	public boolean hasIdentifier() {
		return (this.identifier != null);
	}

	public String getMetadataPrefix() {
		return metadataPrefix;
	}

	public boolean hasMetadataPrefix() {
		return (this.metadataPrefix != null);
	}

	public String getSet() {
		return set;
	}

	public boolean hasSet() {
		return (this.set != null);
	}

	public boolean hasFrom() {
		return (this.from != null);
	}

	public boolean hasUntil() {
		return (this.until != null);
	}

	private Date getDate(String date, String param) throws BadArgumentException {
		if (date == null)
			return null;
		SimpleDateFormat formatDate = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss'Z'");
		try {
			return formatDate.parse(date);
		} catch (ParseException ex) {
			formatDate = new SimpleDateFormat(
					"yyyy-MM-dd");
			try {
				return formatDate.parse(date);
			} catch (ParseException ex1) {
				throw new BadArgumentException("The " + param
						+ " parameter given is not valid");
			}
		}
	}

	public Date getFrom() {
		return from;
	}

	public ResumptionToken getResumptionToken() {
		return resumptionToken;
	}

	public Date getUntil() {
		return until;
	}

	public VerbType getVerb() {
		return verb;
	}

	private void validate() throws IllegalVerbException, BadArgumentException {
		if (this.hasResumptionToken()) {
			if (this.hasFrom() || this.hasSet() || this.hasUntil()
					|| this.hasMetadataPrefix())
				throw new BadArgumentException(
						"ResumptionToken cannot be sent together with from, until, metadataPrefix or set parameters");
		}

		switch (this.getVerb()) {
		case IDENTIFY:
			if (this.hasIdentifier() || this.hasResumptionToken()
					|| this.hasSet() || this.hasMetadataPrefix()
					|| this.hasFrom() || this.hasUntil())
				throw new BadArgumentException(
						"Identify verb does not accept any extra parameter");
			break;
		case LIST_METADATA_FORMATS:
			if (this.hasResumptionToken() || this.hasSet()
					|| this.hasMetadataPrefix() || this.hasFrom()
					|| this.hasUntil())
				throw new BadArgumentException(
						"ListMetadataFormats verb only accepts one optional parameter - identifier");
			break;
		case LIST_SETS:
			if (this.hasIdentifier() || this.hasSet()
					|| this.hasMetadataPrefix() || this.hasFrom()
					|| this.hasUntil())
				throw new BadArgumentException(
						"ListSets verb only accepts one optional parameter - resumptionToken");
			break;
		case GET_RECORD:
			if (!this.hasIdentifier() || !this.hasMetadataPrefix()
					|| this.hasSet() || this.hasFrom() || this.hasUntil())
				throw new BadArgumentException(
						"GetRecord verb requires the use of the parameters - identifier and metadataPrefix");
			if (this.hasResumptionToken())
				throw new BadArgumentException(
						"GetRecord verb does not accept the resumptionToken parameter. It requires the use of the parameters - identifier and metadataPrefix");
			break;
		case LIST_IDENTIFIERS:
			if (!this.hasResumptionToken() && !this.hasMetadataPrefix())
				throw new BadArgumentException(
						"ListIdentifiers verb must receive the metadataPrefix parameter");
			if (this.hasIdentifier())
				throw new BadArgumentException(
						"ListIdentifiers verb does not accept the identifier parameter");
			break;
		case LIST_RECORDS:
			if (!this.hasResumptionToken() && !this.hasMetadataPrefix())
				throw new BadArgumentException(
						"ListRecords verb must receive the metadataPrefix parameter");
			if (this.hasIdentifier())
				throw new BadArgumentException(
						"ListRecords verb does not accept the identifier parameter");
			break;
		}
	}

	private void loadResumptionToken(ResumptionToken resumptionToken) {
		if (resumptionToken.hasFrom())
			this.from = resumptionToken.getFrom();
		if (resumptionToken.hasMetadataPrefix())
			this.metadataPrefix = resumptionToken.getMetadatePrefix();
		if (resumptionToken.hasSet())
			this.set = resumptionToken.getSet();
		if (resumptionToken.hasUntil())
			this.until = resumptionToken.getUntil();
	}
}
