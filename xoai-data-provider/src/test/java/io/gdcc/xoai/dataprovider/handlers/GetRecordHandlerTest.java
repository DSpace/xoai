/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.handlers;

import io.gdcc.xoai.dataprovider.exceptions.BadArgumentException;
import io.gdcc.xoai.dataprovider.exceptions.CannotDisseminateFormatException;
import io.gdcc.xoai.dataprovider.exceptions.IdDoesNotExistException;
import io.gdcc.xoai.dataprovider.model.InMemoryItem;
import io.gdcc.xoai.dataprovider.model.MetadataFormat;
import io.gdcc.xoai.model.oaipmh.GetRecord;
import org.junit.Before;
import org.junit.Test;

import static com.lyncode.test.matchers.xml.XPathMatchers.xPath;
import static io.gdcc.xoai.model.oaipmh.Verb.Type.GetRecord;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class GetRecordHandlerTest extends AbstractHandlerTest {
    private GetRecordHandler underTest;

    @Before
    public void setup () {
        underTest = new GetRecordHandler(aContext(), theRepository());
    }

    @Test(expected = BadArgumentException.class)
    public void getRecordRequiresMetadataPrefixParameter() throws Exception {
        underTest.handle(a(request().withVerb(GetRecord).withIdentifier("a")));
    }

    @Test(expected = BadArgumentException.class)
    public void getRecordRequiresIdentifierParameter() throws Exception {
        underTest.handle(a(request().withVerb(GetRecord).withMetadataPrefix("a")));
    }

    @Test(expected = IdDoesNotExistException.class)
    public void idDoesNotExists() throws Exception {
        underTest.handle(a(request().withVerb(GetRecord)
                .withMetadataPrefix("xoai")
                .withIdentifier("1")));
    }

    @Test(expected = CannotDisseminateFormatException.class)
    public void cannotDisseminateFormat() throws Exception {
        theItemRepository().withItem(InMemoryItem.item().withDefaults().withIdentifier("1"));
        aContext().withMetadataFormat("xoai", MetadataFormat.identity());
        underTest.handle(a(request().withVerb(GetRecord)
                .withMetadataPrefix("abcd")
                .withIdentifier("1")));
    }

    @Test
    public void validResponse() throws Exception {
        theItemRepository().withItem(InMemoryItem.item().withDefaults().withIdentifier("1"));
        aContext().withMetadataFormat("xoai", MetadataFormat.identity());
        GetRecord handle = underTest.handle(a(request().withVerb(GetRecord)
                .withMetadataPrefix("xoai")
                .withIdentifier("1")));

        String result = write(handle);

        assertThat(result, xPath("//header/identifier", is(equalTo("1"))));
    }
}
