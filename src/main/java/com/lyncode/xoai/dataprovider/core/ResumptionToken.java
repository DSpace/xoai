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

import com.lyncode.xoai.dataprovider.exceptions.BadArgumentException;

import java.util.Date;

/**

 * @version 3.1.0
 */
public class ResumptionToken {
    //private static Logger log = LogManager.getLogger(ResumptionToken.class);

    private int _offset;
    private String _set;
    private Date _from;
    private Date _until;
    private String _metadataPrefix;
    private boolean empty;


    public ResumptionToken(int offset, String metadataPrefix, String set, Date from, Date until) {
        empty = false;
        _set = set;
        _offset = offset;
        _from = from;
        _until = until;
        _metadataPrefix = metadataPrefix;
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

    public String getMetadataPrefix() {
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

    public int getOffset() {
        return _offset;
    }
}
