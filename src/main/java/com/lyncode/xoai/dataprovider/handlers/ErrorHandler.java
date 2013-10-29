package com.lyncode.xoai.dataprovider.handlers;

import com.lyncode.xoai.dataprovider.exceptions.*;
import com.lyncode.xoai.dataprovider.xml.oaipmh.OAIPMHerrorcodeType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.OAIPMHerrorType;


public class ErrorHandler {

    public OAIPMHerrorType handle(HandlerException ex) throws OAIException {
        OAIPMHerrorType error = new OAIPMHerrorType();
        if (ex instanceof IllegalVerbException) {
            error.setValue("Illegal verb");
            error.setCode(OAIPMHerrorcodeType.BAD_VERB);
        } else if (ex instanceof DoesNotSupportSetsException) {
            error.setValue("This repository does not support sets");
            error.setCode(OAIPMHerrorcodeType.NO_SET_HIERARCHY);
        } else if (ex instanceof NoMatchesException) {
            error.setValue("No matches for the query");
            error.setCode(OAIPMHerrorcodeType.NO_RECORDS_MATCH);

        } else if (ex instanceof BadResumptionToken) {
            error.setValue("The resumption token is invalid");
            error.setCode(OAIPMHerrorcodeType.BAD_RESUMPTION_TOKEN);
        } else if (ex instanceof IdDoesNotExistException) {
            error.setValue("The given id does not exist");
            error.setCode(OAIPMHerrorcodeType.ID_DOES_NOT_EXIST);
        } else if (ex instanceof NoMetadataFormatsException) {
            error.setValue("The item does not have any metadata format available for dissemination");
            error.setCode(OAIPMHerrorcodeType.NO_METADATA_FORMATS);
        } else if (ex instanceof BadArgumentException) {
            error.setValue(ex.getMessage());
            error.setCode(OAIPMHerrorcodeType.BAD_ARGUMENT);
        } else if (ex instanceof CannotDisseminateRecordException) {
            error.setValue("Cannot disseminate item with the given format");
            error.setCode(OAIPMHerrorcodeType.CANNOT_DISSEMINATE_FORMAT);
        } else if (ex instanceof CannotDisseminateFormatException) {
            error.setValue("Unknown metadata format");
            error.setCode(OAIPMHerrorcodeType.CANNOT_DISSEMINATE_FORMAT);
        } else if (ex instanceof DuplicateDefinitionException) {
            error.setValue(ex.getMessage());
            error.setCode(OAIPMHerrorcodeType.BAD_ARGUMENT);
        } else if (ex instanceof UnknownParameterException) {
            error.setValue(ex.getMessage());
            error.setCode(OAIPMHerrorcodeType.BAD_ARGUMENT);
        } else {
            error.setValue(ex.getMessage());
            error.setCode(OAIPMHerrorcodeType.BAD_ARGUMENT);
        }
        return error;
    }

}
