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

import com.lyncode.xoai.dataprovider.filter.Filter;
import com.lyncode.xoai.dataprovider.filter.FilterResolver;
import com.lyncode.xoai.dataprovider.model.ItemIdentifier;
import com.lyncode.xoai.dataprovider.model.conditions.Condition;
import com.lyncode.xoai.model.oaipmh.Identify;
import com.lyncode.xoai.model.oaipmh.MetadataFormat;
import com.lyncode.xoai.serviceprovider.exceptions.CannotDisseminateFormatException;
import com.lyncode.xoai.serviceprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.serviceprovider.exceptions.NoSetHierarchyException;
import com.lyncode.xoai.serviceprovider.parameters.GetRecordParameters;
import com.lyncode.xoai.serviceprovider.parameters.ListIdentifiersParameters;
import com.lyncode.xoai.serviceprovider.parameters.ListRecordsParameters;
import org.junit.Test;

import java.util.Iterator;

import static com.lyncode.test.check.Assert.assertThat;
import static com.lyncode.xoai.dataprovider.model.InMemoryItem.item;
import static com.lyncode.xoai.dataprovider.model.MetadataFormat.identity;
import static com.lyncode.xoai.model.oaipmh.DeletedRecord.PERSISTENT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class ServiceProviderTest extends AbstractServiceProviderTest {
    private final ServiceProvider underTest = new ServiceProvider(theContext());

    @Test
    public void validIdentifyResponse() throws Exception {
        theDataRepositoryConfiguration()
                .withRepositoryName("NAME")
                .withDeleteMethod(PERSISTENT);
        Identify identify = underTest.identify();
        assertThat(identify.getRepositoryName(), equalTo("NAME"));
        assertThat(identify.getDeletedRecord(), equalTo(PERSISTENT));
    }

    @Test
    public void validListMetadataFormatsResponse () throws Exception {
        Iterator<MetadataFormat> metadataFormatIterator = underTest.listMetadataFormats();

        assertThat(metadataFormatIterator.hasNext(), is(true));
        MetadataFormat metadataFormat = metadataFormatIterator.next();
        assertThat(metadataFormat.getMetadataPrefix(), equalTo(FORMAT));
    }

    @Test(expected = IdDoesNotExistException.class)
    public void recordNotFoundForGetRecord () throws Exception {
        underTest.getRecord(GetRecordParameters.request().withIdentifier("asd").withMetadataFormatPrefix(FORMAT));
    }

    @Test(expected = CannotDisseminateFormatException.class)
    public void recordDoesNotSupportFormatForGetRecord () throws Exception {
        theDataProviderContext().withMetadataFormat(FORMAT, identity(), alwaysFalseCondition());
        theDataItemRepository().withItem(item().withDefaults().withIdentifier("asd").withSet("one"));
        underTest.getRecord(GetRecordParameters.request().withIdentifier("asd").withMetadataFormatPrefix(FORMAT));
    }

    @Test(expected = NoSetHierarchyException.class)
    public void listSetsWithNoSupportForSets () throws Exception {
        theDataSetRepository().doesNotSupportSets();
        underTest.listSets();
    }

    @Test
    public void listSetsWithNoSets () throws Exception {
        assertThat(underTest.listSets().hasNext(), is(false));
    }

    @Test
    public void listSetsWithSetsOnePage() throws Exception {
        theDataSetRepository().withRandomSets(5);
        assertThat(count(underTest.listSets()), equalTo(5));
    }

    @Test
    public void listSetsWithSetsTwoPages() throws Exception {
        theDataRepositoryConfiguration().withMaxListSets(5);
        theDataSetRepository().withRandomSets(10);
        assertThat(count(underTest.listSets()), equalTo(10));
    }


    @Test
    public void listIdentifiersWithNoItems () throws Exception {
        assertThat(underTest.listIdentifiers(ListIdentifiersParameters.request().withMetadataPrefix(FORMAT)).hasNext(), is(false));
    }

    @Test
    public void listIdentifiersWithOnePage() throws Exception {
        theDataItemRepository().withRandomItems(5);
        assertThat(count(underTest.listIdentifiers(ListIdentifiersParameters.request().withMetadataPrefix(FORMAT))), equalTo(5));
    }

    @Test
    public void listIdentifiersWithTwoPages() throws Exception {
        theDataRepositoryConfiguration().withMaxListIdentifiers(5);
        theDataItemRepository().withRandomItems(10);
        assertThat(count(underTest.listIdentifiers(ListIdentifiersParameters.request().withMetadataPrefix(FORMAT))), equalTo(10));
    }


    @Test
    public void listRecordsWithNoItems () throws Exception {
        assertThat(underTest.listRecords(ListRecordsParameters.request().withMetadataPrefix(FORMAT)).hasNext(), is(false));
    }

    @Test
    public void listRecordsWithOnePage() throws Exception {
        theDataItemRepository().withRandomItems(5);
        assertThat(count(underTest.listRecords(ListRecordsParameters.request().withMetadataPrefix(FORMAT))), equalTo(5));
    }

    @Test
    public void listRecordsWithTwoPages() throws Exception {
        theDataRepositoryConfiguration().withMaxListRecords(5);
        theDataItemRepository().withRandomItems(10);
        assertThat(count(underTest.listRecords(ListRecordsParameters.request().withMetadataPrefix(FORMAT))), equalTo(10));
    }

    private int count(Iterator<?> iterator) {
        int i = 0;
        while (iterator.hasNext()) {
            iterator.next();
            i++;
        }
        return i;
    }

    private Condition alwaysFalseCondition() {
        return new Condition() {
            @Override
            public Filter getFilter(FilterResolver filterResolver) {
                return new Filter() {
                    @Override
                    public boolean isItemShown(ItemIdentifier item) {
                        return false;
                    }
                };
            }
        };
    }
}
