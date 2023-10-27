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

import com.lyncode.xoai.dataprovider.exceptions.DuplicateDefinitionException;
import com.lyncode.xoai.dataprovider.exceptions.IllegalVerbException;
import com.lyncode.xoai.dataprovider.exceptions.UnknownParameterException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**

 * @version 3.1.0
 */
public class OAIRequestParameters {
    private static Logger log = LogManager.getLogger(OAIRequestParameters.class);
    private Map<String, List<String>> map;
    private boolean checkedArgs;

    public OAIRequestParameters(Map<String, List<String>> map) {
        this.map = map;
        this.checkedArgs = false;
    }

    public String getFrom() throws DuplicateDefinitionException, UnknownParameterException, IllegalVerbException {
        return this.getParameter("from");
    }

    public String getIdentifier() throws DuplicateDefinitionException, UnknownParameterException, IllegalVerbException {
        return this.getParameter("identifier");
    }

    public String getMetadataPrefix() throws DuplicateDefinitionException, UnknownParameterException, IllegalVerbException {
        return this.getParameter("metadataPrefix");
    }

    public String getResumptionToken() throws DuplicateDefinitionException, UnknownParameterException, IllegalVerbException {
        return this.getParameter("resumptionToken");
    }

    public String getSet() throws DuplicateDefinitionException, UnknownParameterException, IllegalVerbException {
        return this.getParameter("set");
    }

    public String getUntil() throws DuplicateDefinitionException, UnknownParameterException, IllegalVerbException {
        return this.getParameter("until");
    }

    public String getVerb() throws IllegalVerbException, UnknownParameterException, IllegalVerbException {
        try {
            return this.getParameter("verb");
        } catch (DuplicateDefinitionException e) {
            throw new IllegalVerbException("Illegal verb");
        }
    }

    private String getParameter(String parameter) throws DuplicateDefinitionException, UnknownParameterException, IllegalVerbException {
        if (!checkedArgs) {
            onlyHasKnownParameters();
            this.checkedArgs = true;
        }
        List<String> params = this.map.get(parameter);
        if (params == null || params.size() == 0) {
            log.debug("RootParameterMap '" + parameter + "' undefined");
            return null;
        } else if (params.size() > 1) {
            throw new DuplicateDefinitionException("Duplicate definition of parameter '" + parameter + "'");
        } else {
            log.debug("RootParameterMap '" + parameter + "' = '" + params.get(0) + "'");
            return params.get(0);
        }
    }

    private String getParameterID(String parameter) {
        List<String> params = this.map.get(parameter);
        if (params == null || params.size() == 0) {
            log.debug("RootParameterMap '" + parameter + "' undefined");
            return null;
        } else if (params.size() > 1) {
            return params.get(0) + "#" + params.size();
        } else {
            log.debug("RootParameterMap '" + parameter + "' = '" + params.get(0) + "'");
            return params.get(0);
        }
    }

    public String requestID() {
        String pre = "";
        try {
            onlyHasKnownParameters();
        } catch (UnknownParameterException e) {
            pre = "extra##";
        } catch (IllegalVerbException e) {
            pre = "badverb##";
        }
        return pre + this.getParameterID("verb") + this.getParameterID("metadataPrefix")
                + this.getParameterID("identifier")
                + this.getParameterID("resumptionToken") + this.getParameterID("set")
                + this.getParameterID("from") + this.getParameterID("until");
    }


    public void onlyHasKnownParameters() throws UnknownParameterException, IllegalVerbException {
        List<String> possibilities = new ArrayList<String>();
        possibilities.add("verb");
        possibilities.add("from");
        possibilities.add("until");
        possibilities.add("set");
        possibilities.add("identifier");
        possibilities.add("metadataPrefix");
        possibilities.add("resumptionToken");

        if (!this.map.containsKey("verb"))
            throw new IllegalVerbException();

        for (String parameter : this.map.keySet())
            if (!possibilities.contains(parameter))
                throw new UnknownParameterException("Unknown parameter '" + parameter + "'");
    }
}
