/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider;

import io.gdcc.xoai.serviceprovider.exceptions.BadArgumentException;
import io.gdcc.xoai.serviceprovider.exceptions.CannotDisseminateFormatException;
import io.gdcc.xoai.serviceprovider.exceptions.EncapsulatedKnownException;
import io.gdcc.xoai.serviceprovider.exceptions.HarvestException;
import io.gdcc.xoai.serviceprovider.exceptions.IdDoesNotExistException;
import io.gdcc.xoai.serviceprovider.exceptions.NoSetHierarchyException;
import io.gdcc.xoai.serviceprovider.handler.GetRecordHandler;
import io.gdcc.xoai.serviceprovider.handler.IdentifyHandler;
import io.gdcc.xoai.serviceprovider.handler.ListIdentifierHandler;
import io.gdcc.xoai.serviceprovider.handler.ListMetadataFormatsHandler;
import io.gdcc.xoai.serviceprovider.handler.ListRecordHandler;
import io.gdcc.xoai.serviceprovider.handler.ListSetsHandler;
import io.gdcc.xoai.serviceprovider.parameters.GetRecordParameters;
import io.gdcc.xoai.serviceprovider.parameters.ListIdentifiersParameters;
import io.gdcc.xoai.serviceprovider.parameters.ListMetadataParameters;
import io.gdcc.xoai.serviceprovider.parameters.ListRecordsParameters;
import org.dspace.xoai.model.oaipmh.*;
import org.dspace.xoai.serviceprovider.exceptions.*;
import org.dspace.xoai.serviceprovider.handler.*;
import io.gdcc.xoai.serviceprovider.lazy.ItemIterator;
import io.gdcc.xoai.serviceprovider.model.Context;

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

    public Iterator<MetadataFormat> listMetadataFormats () throws IdDoesNotExistException {
        return listMetadataFormatsHandler.handle(ListMetadataParameters.request()).iterator();
    }

    public Iterator<MetadataFormat> listMetadataFormats (ListMetadataParameters parameters) throws IdDoesNotExistException {
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
