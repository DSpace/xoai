/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider;

import io.gdcc.xoai.dataprovider.filter.Filter;
import io.gdcc.xoai.dataprovider.filter.FilterResolver;
import io.gdcc.xoai.dataprovider.model.ItemIdentifier;
import io.gdcc.xoai.dataprovider.model.conditions.Condition;
import io.gdcc.xoai.model.oaipmh.Identify;
import io.gdcc.xoai.model.oaipmh.MetadataFormat;
import io.gdcc.xoai.serviceprovider.exceptions.CannotDisseminateFormatException;
import io.gdcc.xoai.serviceprovider.exceptions.IdDoesNotExistException;
import io.gdcc.xoai.serviceprovider.exceptions.NoSetHierarchyException;
import io.gdcc.xoai.serviceprovider.parameters.GetRecordParameters;
import io.gdcc.xoai.serviceprovider.parameters.ListIdentifiersParameters;
import io.gdcc.xoai.serviceprovider.parameters.ListMetadataParameters;
import io.gdcc.xoai.serviceprovider.parameters.ListRecordsParameters;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static io.gdcc.xoai.dataprovider.model.InMemoryItem.item;
import static io.gdcc.xoai.dataprovider.model.MetadataFormat.identity;
import static io.gdcc.xoai.model.oaipmh.DeletedRecord.PERSISTENT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    public void recordNotFoundForListMetadataFormats () throws Exception {
        assertThrows(IdDoesNotExistException.class, () -> underTest.listMetadataFormats(ListMetadataParameters.request().withIdentifier("asd")));
    }

    @Test
    public void recordNotFoundForGetRecord () throws Exception {
        assertThrows(IdDoesNotExistException.class, () -> underTest.getRecord(GetRecordParameters.request().withIdentifier("asd").withMetadataFormatPrefix(FORMAT)));
    }

    @Test
    public void recordDoesNotSupportFormatForGetRecord () throws Exception {
        theDataProviderContext().withMetadataFormat(FORMAT, identity(), alwaysFalseCondition());
        theDataItemRepository().withItem(item().withDefaults().withIdentifier("asd").withSet("one"));
        assertThrows(CannotDisseminateFormatException.class, () -> underTest.getRecord(GetRecordParameters.request().withIdentifier("asd").withMetadataFormatPrefix(FORMAT)));
    }

    @Test
    public void listSetsWithNoSupportForSets () throws Exception {
        theDataSetRepository().doesNotSupportSets();
        assertThrows(NoSetHierarchyException.class, underTest::listSets);
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
