/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider.handlers;

import org.dspace.xoai.dataprovider.exceptions.*;
import org.dspace.xoai.model.oaipmh.Error;


public class ErrorHandler {

    public Error handle(HandlerException ex) throws OAIException {
        if (ex instanceof IllegalVerbException) {
            return new Error("Illegal verb")
                    .withCode(Error.Code.BAD_VERB);
        } else if (ex instanceof DoesNotSupportSetsException) {
            return new Error("This repository does not support sets")
                    .withCode(Error.Code.NO_SET_HIERARCHY);
        } else if (ex instanceof NoMatchesException) {
            return new Error("No matches for the query")
                    .withCode(Error.Code.NO_RECORDS_MATCH);

        } else if (ex instanceof BadResumptionToken) {
            return new Error("The resumption token is invalid")
                    .withCode(Error.Code.BAD_RESUMPTION_TOKEN);
        } else if (ex instanceof IdDoesNotExistException) {
            return new Error("The given id does not exist")
                    .withCode(Error.Code.ID_DOES_NOT_EXIST);
        } else if (ex instanceof NoMetadataFormatsException) {
            return new Error("The item does not have any metadata format available for dissemination")
                    .withCode(Error.Code.NO_METADATA_FORMATS);
        } else if (ex instanceof BadArgumentException) {
            return new Error(ex.getMessage())
                    .withCode(Error.Code.BAD_ARGUMENT);
        } else if (ex instanceof CannotDisseminateRecordException) {
            return new Error("Cannot disseminate item with the given format")
                    .withCode(Error.Code.CANNOT_DISSEMINATE_FORMAT);
        } else if (ex instanceof CannotDisseminateFormatException) {
            return new Error("Unknown metadata format")
                    .withCode(Error.Code.CANNOT_DISSEMINATE_FORMAT);
        } else if (ex instanceof DuplicateDefinitionException) {
            return new Error(ex.getMessage())
                .withCode(Error.Code.BAD_ARGUMENT);
        } else if (ex instanceof UnknownParameterException) {
            return new Error(ex.getMessage())
                .withCode(Error.Code.BAD_ARGUMENT);
        } else {
            return new Error(ex.getMessage())
                .withCode(Error.Code.BAD_ARGUMENT);
        }
    }

}
