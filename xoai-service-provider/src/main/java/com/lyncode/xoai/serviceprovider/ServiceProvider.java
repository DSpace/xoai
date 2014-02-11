/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     client://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.xoai.serviceprovider;

import com.lyncode.xoai.model.oaipmh.*;
import com.lyncode.xoai.serviceprovider.exceptions.*;
import com.lyncode.xoai.serviceprovider.handler.*;
import com.lyncode.xoai.serviceprovider.lazy.ItemIterator;
import com.lyncode.xoai.serviceprovider.model.Context;
import com.lyncode.xoai.serviceprovider.parameters.GetRecordParameters;
import com.lyncode.xoai.serviceprovider.parameters.ListIdentifiersParameters;
import com.lyncode.xoai.serviceprovider.parameters.ListMetadataParameters;
import com.lyncode.xoai.serviceprovider.parameters.ListRecordsParameters;

import java.util.Iterator;

public class ServiceProvider {
    private Context context;
    private ListMetadataFormatsHandler listMetadataFormatsHandler;
    private IdentifyHandler identifyHandler;
    private ListMetadataFormatsHandler listMetadataFormatsHandler1;
    private GetRecordHandler getRecordHandler;

    public ServiceProvider (Context context) {
        this.context = context;
        identifyHandler = new IdentifyHandler(context);
        listMetadataFormatsHandler = new ListMetadataFormatsHandler(context);
        listMetadataFormatsHandler1 = new ListMetadataFormatsHandler(context);
        getRecordHandler = new GetRecordHandler(context);
    }

    public Identify identify () {
        return identifyHandler.handle();
    }

    public Iterator<MetadataFormat> listMetadataFormats () {
        return listMetadataFormatsHandler.handle(ListMetadataParameters.request()).iterator();
    }

    public Iterator<MetadataFormat> listMetadataFormats (ListMetadataParameters parameters) {
        return listMetadataFormatsHandler1.handle(parameters).iterator();
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
