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
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.xoai.dataprovider.exceptions.BadArgumentException;
import com.lyncode.xoai.dataprovider.exceptions.BadResumptionToken;
import com.lyncode.xoai.util.Base64Utils;

/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */
public class ResumptionToken {
	private static Logger log = LogManager.getLogger(ResumptionToken.class);
	
	private int _offset;
	private String _set;
	private Date _from;
	private Date _until;
	private String _metadataPrefix;
	private boolean empty;

	public ResumptionToken(int offset, OAIParameters parameters)
			throws BadArgumentException {
		empty = false;
		_offset = offset;
		if (parameters.hasFrom())
			_from = parameters.getFrom();
		if (parameters.hasUntil())
			_until = parameters.getUntil();
		if (parameters.hasSet())
			_set = parameters.getSet();
		if (parameters.hasMetadataPrefix())
			_metadataPrefix = parameters.getMetadataPrefix();
	}

	public boolean isEmpty() {
		return empty;
	}

	public ResumptionToken(String encoded) throws BadResumptionToken {
		_offset = 0;
		_set = null;
		_from = null;
		_until = null;
		_metadataPrefix = null;
		empty = false;
		if (encoded == null || encoded.trim().equals("")) {
			empty = true;
		} else {
			String s = Base64Utils.decode(encoded);
			String[] pieces = s.split(Pattern.quote("|"));
			try {
				if (pieces.length > 0) {
					_offset = Integer.parseInt(pieces[0].substring(2));
					if (pieces.length > 1) {
						_set = pieces[1].substring(2);
						if (_set != null && _set.equals(""))
							_set = null;
					}
					if (pieces.length > 2) {
						_from = stringToDate(pieces[2].substring(2));
					}
					if (pieces.length > 3) {
						_until = stringToDate(pieces[3].substring(2));
					}
					if (pieces.length > 4) {
						_metadataPrefix = pieces[4].substring(2);
						if (_metadataPrefix != null && _metadataPrefix.equals(""))
							_metadataPrefix = null;
					}
				} else
					throw new BadResumptionToken();
			} catch (Exception ex) {
				log.debug(ex.getMessage(), ex);
				throw new BadResumptionToken();
			}
		}
	}

	public boolean hasFrom() {
		return (_from != null);
	}

	public boolean hasUntil() {
		return (_until != null);
	}

	public boolean hasSet() {
		return (_set != null);
	}

	public boolean hasMetadataPrefix() {
		return (_metadataPrefix != null);
	}

	public String getMetadatePrefix() {
		return _metadataPrefix;
	}

	public Date getFrom() {
		return _from;
	}

	public String getSet() {
		return _set;
	}

	public Date getUntil() {
		return _until;
	}

	public ResumptionToken(int offset) {
		empty = false;
		_set = null;
		_from = null;
		_until = null;
		_metadataPrefix = null;
		this._offset = offset;
	}

	public ResumptionToken() {
		this.empty = true;
		_offset = 0;
		_set = null;
		_from = null;
		_until = null;
		_metadataPrefix = null;
	}

	public int getOffset() {
		return _offset;
	}

	@Override
	public String toString() {
		String s = "1:" + _offset;
		s += "|2:";
		if (_set != null)
			s += _set;
		s += "|3:";
		if (_from != null)
			s += dateToString(_from);
		s += "|4:";
		if (_until != null)
			s += dateToString(_until);
		s += "|5:";
		if (_metadataPrefix != null)
			s += _metadataPrefix;

		if (this.empty)
			return "";
		else
			return Base64Utils.encode(s);
	}

	private String dateToString(Date date) {
		SimpleDateFormat formatDate = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss'Z'");
		return formatDate.format(date);
	}

	private Date stringToDate(String string) {
		SimpleDateFormat formatDate = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss'Z'");
		try {
			return formatDate.parse(string);
		} catch (ParseException ex) {
			formatDate = new SimpleDateFormat(
					"yyyy-MM-dd");
			try {
				return formatDate.parse(string);
			} catch (ParseException ex1) {
				return null;
			}
		}
	}
}
