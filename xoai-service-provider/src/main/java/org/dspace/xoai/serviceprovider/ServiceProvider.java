/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider;

import org.dspace.xoai.model.oaipmh.*;
import org.dspace.xoai.serviceprovider.exceptions.*;
import org.dspace.xoai.serviceprovider.handler.*;
import org.dspace.xoai.serviceprovider.lazy.ItemIterator;
import org.dspace.xoai.serviceprovider.model.Context;
import org.dspace.xoai.serviceprovider.parameters.GetRecordParameters;
import org.dspace.xoai.serviceprovider.parameters.ListIdentifiersParameters;
import org.dspace.xoai.serviceprovider.parameters.ListMetadataParameters;
import org.dspace.xoai.serviceprovider.parameters.ListRecordsParameters;

import java.util.Iterator;

public class ServiceProvider {
    private Context context;
    private ListMetadataFormatsHandler listMetadataFormatsHandler;
    private IdentifyHandler identifyHandler;
    private GetRecordHandler getRecordHandler;

    public ServiceProvider (Context context) {
        this.context = context;
        identifyHandler = new IdentifyHandler(context);
        listMetadataFormatsHandler = new ListMetadataFormatsHandler(context);
        getRecordHandler = new GetRecordHandler(context);
    }

    public Identify identify () {
        return identifyHandler.handle();
    }

    public Iterator<MetadataFormat> listMetadataFormats () {
        return listMetadataFormatsHandler.handle(ListMetadataParameters.request()).iterator();
    }

    public Iterator<MetadataFormat> listMetadataFormats (ListMetadataParameters parameters) {
        return listMetadataFormatsHandler.handle(parameters).iterator();
    }

    public Record getRecord (GetRecordParameters parameters) throws BadArgumentException, IdDoesNotExistException, CannotDisseminateFormatException {
        if (!parameters.areValid())
            throw new BadArgumentException("GetRecord verb requires identifier and metadataPrefix parameters");
        return getRecordHandler.handle(parameters);
    }

    public Iterator<Record> listRecords (ListRecordsParameters parameters) throws BadArgumentException {
        if (!parameters.areValid())
            throw new BadArgumentException("ListRecords verb requires the metadataPrefix");
        return new ItemIterator<Record>(new ListRecordHandler(context, parameters));
    }

    public Iterator<Header> listIdentifiers (ListIdentifiersParameters parameters) throws BadArgumentException {
        if (!parameters.areValid())
            throw new BadArgumentException("ListIdentifiers verb requires the metadataPrefix");
        return new ItemIterator<Header>(new ListIdentifierHandler(context, parameters));
    }

    public Iterator<Set> listSets () throws NoSetHierarchyException {
        try {
            return new ItemIterator<Set>(new ListSetsHandler(context));
        } catch (EncapsulatedKnownException ex) {
            throw get(ex, NoSetHierarchyException.class);
        }
    }

    private <T extends HarvestException> boolean instanceOf (EncapsulatedKnownException exception, Class<T> exceptionClass) {
        return exceptionClass.isInstance(exception.getCause());
    }

    private <T extends HarvestException> T get (EncapsulatedKnownException ex, Class<T> exceptionClass) {
        if (instanceOf(ex, exceptionClass))
            return (T) ex.getCause();
        else
            return null;
    }
}
