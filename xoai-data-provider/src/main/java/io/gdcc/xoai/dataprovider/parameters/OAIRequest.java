/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package io.gdcc.xoai.dataprovider.parameters;

import io.gdcc.xoai.dataprovider.exceptions.BadArgumentException;
import io.gdcc.xoai.dataprovider.exceptions.DuplicateDefinitionException;
import io.gdcc.xoai.dataprovider.exceptions.IllegalVerbException;
import io.gdcc.xoai.dataprovider.exceptions.UnknownParameterException;
import io.gdcc.xoai.exceptions.InvalidResumptionTokenException;
import io.gdcc.xoai.services.api.DateProvider;

import java.time.DateTimeException;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static io.gdcc.xoai.dataprovider.parameters.OAIRequest.Parameter.Verb;
import static io.gdcc.xoai.model.oaipmh.Verb.Type;
import static io.gdcc.xoai.model.oaipmh.Verb.Type.fromValue;

/**
 * @author Development @ Lyncode
 * @version 3.1.0
 */
public class OAIRequest {
    
    public enum Parameter {
        From("from"),
        Until("until"),
        Identifier("identifier"),
        MetadataPrefix("metadataPrefix"),
        ResumptionToken("resumptionToken"),
        Set("set"),
        Verb("verb");

        private final String representation;

        Parameter (String rep) {
            this.representation = rep;
        }

        public String toString () {
            return representation;
        }

        public static Parameter fromRepresentation (String representation) {
            for (Parameter param : Parameter.values())
                if (param.representation.equals(representation))
                    return param;
            throw new IllegalArgumentException("Given representation '" + representation + "' is not a valid parameter.");
        }
    }

    private final Map<String, List<String>> map;

    public OAIRequest(Map<String, List<String>> map) {
        this.map = map;
    }

    private void validate(Parameter parameter) throws IllegalVerbException, DuplicateDefinitionException {
        List<String> values = this.map.get(parameter.toString());
        
        if (values != null && values.size() > 1) {
            if (parameter == Verb ) {
                throw new IllegalVerbException("Illegal verb");
            } else {
                throw new DuplicateDefinitionException("Duplicate definition of parameter '" + parameter + "'");
            }
        }
    }

    public boolean has(Parameter parameter) {
        return map.containsKey(parameter.toString());
    }

    public String get(Parameter parameter) {
        List<String> values = this.map.get(parameter.toString());
        
        if (values == null || values.isEmpty() || values.get(0) == null || values.get(0).isEmpty()) {
            return null;
        } else {
            return values.get(0);
        }
    }

    public Instant getDate(Parameter parameter) throws BadArgumentException {
        if (!has(parameter)) return null;
        try {
            return DateProvider.parse(get(parameter));
        } catch (DateTimeException e) {
            throw new BadArgumentException("The " + parameter + " parameter given is not valid");
        }
    }

    public String getString(Parameter parameter) throws DuplicateDefinitionException, IllegalVerbException {
        if (!has(parameter)) return null;
        validate(parameter);
        return get(parameter);
    }

    public Type getVerb() throws DuplicateDefinitionException, IllegalVerbException {
        validate(Verb);
        String verb = get(Verb);
        if (verb == null)
            throw new IllegalVerbException("The verb given by the request is null, assuming identify");
        try {
            return fromValue(verb);
        } catch (IllegalArgumentException e) {
            throw new IllegalVerbException("The verb given by the request is unknown, assuming identify");
        }
    }


    public Collection<String> getParameterNames () {
        return this.map.keySet();
    }

    public OAICompiledRequest compile () throws IllegalVerbException, InvalidResumptionTokenException, UnknownParameterException, BadArgumentException, DuplicateDefinitionException {
        return OAICompiledRequest.compile(this);
    }

}
